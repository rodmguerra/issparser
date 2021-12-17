package io.github.rodmguerra.issparser.handlers.texts;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.TeamNameText;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static io.github.rodmguerra.issparser.commons.RomUtils.*;

public class TeamNameTextRomHandler implements RomHandler<TeamNameText> {

    private final File rom;
    private static final long POINTER_OFFSET = 0x39DAE;
    private static final long POINTER_STEP = 2;
    private int maximumAddress = 0x44486;

    public TeamNameTextRomHandler(File rom) {
        this.rom = rom;
    }

    @Override
    public Map<Team, TeamNameText> readFromRom() throws IOException {
        Map<Team, TeamNameText> map = new HashMap<>();
        for (Team team : Team.values()) {
            map.put(team, readFromRomAt(team));
        }
        return map;
    }

    @Override
    public void writeToRom(Map<Team, ? extends TeamNameText> input) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TeamNameText readFromRomAt(Team team) throws IOException {
        int offset = readPointerAt(team);
        System.out.println(Integer.toHexString(offset));
        ByteSource source = Files.asByteSource(rom);
        int length = ((int)source.slice(offset, 1).read()[0]) * 4;
        byte[] data = source.slice(offset, length + 1).read();
        System.out.println(team + " name text on address " + Integer.toHexString(offset));
        System.out.println(ParsingUtils.bytesString(data));
        return TeamNameText.deserialize(data);
    }


    private PointerFormat pointerFormat() throws IOException {
        return PointerFormat.P40000;
    }

    private int readPointerAt(Team team) throws IOException {
        return readPointer(rom, pointerOffset(team), pointerFormat());
    }


    private void move(Map<Team, SizedAddress> addressMap, Map<SizedAddress, Integer> toMove) throws IOException {
        moveData(addressMap, toMove);
        movePointers(addressMap, toMove);
        updateAddressMapWithMovement(addressMap, toMove);
    }

    private void updateAddressMapWithMovement(Map<Team, SizedAddress> addressMap, Map<SizedAddress, Integer> moved) {
        for (Team team : addressMap.keySet()) {
            SizedAddress address = addressMap.get(team);
            Integer newAddress = moved.get(address);
            if (newAddress == null) newAddress = address.getAddress();
            addressMap.put(team, new SizedAddress(newAddress, address.getSize()));
        }
    }


    private long pointerOffset(Team team) {
        return POINTER_OFFSET + POINTER_STEP * team.ordinal();
    }

    @Override
    public void writeToRomAt(Team team, TeamNameText input) throws IOException {
        System.out.println("Write team name in menu to rom at " + team);
        byte[] data = input.serialize();
        int newSize = data.length;
        Map<Team, SizedAddress> addressMap = readAddressMap();
        SizedAddress oldAddress = addressMap.get(team);
        SizedAddress newAddress = new SizedAddress(oldAddress.getAddress(), newSize);
        Map<SizedAddress, Integer> toMove = addressesAfter(addressMap, oldAddress.getAddress(), newAddress.getSize() - oldAddress.getSize());
        move(addressMap, toMove);
        writeToPositionChecked(newAddress.getAddress(), data);
        addressMap.put(team, newAddress);
        System.out.println("Write team name in menu at " + team + "success!");
    }

    private void writeToPositionChecked(int address, byte[] data) throws IOException {
        checkMaximumAddress(address, data.length);
        FileUtils.writeToPosition(rom, data,
                address);
    }


    private void checkMaximumAddress(int address, int length) {
        if (address + length > maximumAddress) {
            throw new RuntimeException("Not enough space to write name in menu. Can't write to position: " + Integer.toHexString(address + length));
        }
    }

    private void checkMaximumAddress(SizedAddress address) {
        checkMaximumAddress(address.getAddress(), address.getSize());
    }

    private void moveData(Map<Team, SizedAddress> addressMap, Map<SizedAddress, Integer> addressesToMove) throws IOException {
        Map<Integer, byte[]> movingData = readMovingData(addressMap, addressesToMove);
        moveData(movingData);
    }

    private void movePointers(Map<Team, SizedAddress> addressMap, Map<SizedAddress, Integer> addressesToMove) throws IOException {
        for (Team team : Team.values()) {
            long pointerOffset = pointerOffset(team);
            SizedAddress topAddress = addressMap.get(team);
            if (addressesToMove.containsKey(topAddress)) {
                System.out.println("moving pointer of: " + team);
                writeAdressToPointer(rom, pointerOffset, addressesToMove.get(topAddress));
            }
        }
    }

    private void moveData(Map<Integer, byte[]> movingData) throws IOException {
        if (movingData.size() > 0) {
            int lastAddress = movingData.keySet().stream().sorted(Comparator.reverseOrder()).findFirst().get();
            checkMaximumAddress(lastAddress, movingData.get(lastAddress).length);
            for (Integer address : movingData.keySet()) {
                writeToPositionChecked(address, movingData.get(address));
            }
        }
    }

    private Map<Integer, byte[]> readMovingData(Map<Team, SizedAddress> addressMap, Map<SizedAddress, Integer> addressesToMove) throws IOException {
        System.out.println(addressesToMove.size());
        Map<Integer, byte[]> contentToWrite = new HashMap<>();
        for (Team currentTeam : Team.values()) {

            SizedAddress address1 = addressMap.get(currentTeam);
            Integer newAddress1 = addressesToMove.get(address1);
            if (newAddress1 != null) {
                System.out.println("will be moved team name in menu of:" + currentTeam);
                contentToWrite.put(newAddress1, Files.asByteSource(rom).slice(address1.getAddress(), address1.getSize()).read());
            }
        }
        return contentToWrite;
    }

    private Map<SizedAddress, Integer> addressesAfter(Map<Team, SizedAddress> addressMap, int afterThis, int delta) {
        Map<SizedAddress, Integer> addresses = new HashMap<>();
        for (Map.Entry<Team, SizedAddress> entry : addressMap.entrySet()) {
            SizedAddress address = entry.getValue();
            if (address.getAddress() > afterThis) {
                addresses.put(address, address.getAddress() + delta);
            }
        }
        return addresses;
    }


    public Multimap<SizedAddress, Team> invert(Map<Team, SizedAddress> addressMap) {
        Multimap<SizedAddress, Team> map = MultimapBuilder.treeKeys().linkedListValues().build();
        for (Team team : addressMap.keySet()) {
            map.put(addressMap.get(team), team);
        }
        return map;
    }

    private Process compressDataToFile(byte[] bytes, File file) throws IOException {
        String comp = file.getAbsolutePath();
        String decomp = file.getAbsolutePath() + ".decomp";
        Files.write(bytes, new File(decomp));

        String command = "konami\\konami_c \"" + decomp + "\" \"" + comp + "\" 1";
        System.out.println(command);
        return Runtime.getRuntime().exec(command, null);
    }



    private Map<Team, SizedAddress> readAddressMap() throws IOException {
        Map<Team, SizedAddress> map = new HashMap<>();
        for (Team team : Team.values()) {
            long offset = pointerOffset(team);
            int address = readPointer(rom, offset, pointerFormat());
            ByteSource source = Files.asByteSource(rom);
            int size = source.slice(address, 1).read()[0]*4 + 1;
            map.put(team, new SizedAddress(address, size));
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
        TeamNameTextRomHandler handler = new TeamNameTextRomHandler(new File("iss.sfc"));
        //System.out.println(handler.invert(handler.readAddressMap()));
        /*for (Team team : Team.values()) {
            TeamNameInMenu name = handler.readFromRomAt(team);
            for (int i = 0; i < name.getNumberOfPositions(); i++) {
                System.out.println("" + name.getLetterAt(i) +  name.getLetterSize(i));
            }
        }  */
        System.out.println(handler.invert(handler.readAddressMap()));



    }

    private class SizedAddress implements Comparable<SizedAddress> {
        private final int address;
        private final int size;

        private SizedAddress(int address, int size) {
            this.address = address;
            this.size = size;
        }

        private int getAddress() {
            return address;
        }

        private int getSize() {
            return size;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SizedAddress that = (SizedAddress) o;

            if (address != that.address) return false;
            if (size != that.size) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = address;
            result = 31 * result + size;
            return result;
        }

        @Override
        public String toString() {
            return "SizedAddress{" +
                    "address=" + Integer.toHexString(address) +
                    ", size=" + Integer.toHexString(size) +
                    '}';
        }

        @Override
        public int compareTo(SizedAddress o) {
            int compare = Integer.valueOf(this.getAddress()).compareTo(o.getAddress());
            return (compare == 0) ? Integer.valueOf(this.getSize()).compareTo(o.getSize()) : compare;
        }
    }
}
