package io.github.rodmguerra.issparser.handlers.tiles;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.commons.RomUtils;
import io.github.rodmguerra.issparser.model.tiles.TeamNameTiles;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.github.rodmguerra.issparser.commons.RomUtils.*;

public class TeamNameInGameRomHandler implements RomHandler<TeamNameTiles> {

    private final File rom;
    private static final long POINTER_OFFSET = 0x93CD;
    private static final long POINTER_STEP = 2;
    private int maximumAddress48 = 0x48A7F;//0x489CD;
    private int maximumAddress17 = 0x17FFF;//0x483FD;
    private PointerFormat pointerFormat;

    public TeamNameInGameRomHandler(File rom) {
        this.rom = rom;
    }

    @Override
    public Map<Team, TeamNameTiles> readFromRom() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToRom(Map<Team, ? extends TeamNameTiles> input) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TeamNameTiles readFromRomAt(Team team) throws IOException {
        int offset = readPointerAt(team);
        TeamNameTiles.Color[][] matrix = readMatrix(offset);
        return new TeamNameTiles(matrix);
    }

    private PointerFormat pointerFormat() throws IOException {
        if (pointerFormat == null) {
            pointerFormat = readTeamNameTilesPointerFormat(rom);
        }
        return pointerFormat;
    }

    private int readPointerAt(Team team) throws IOException {
        return readPointer(rom, pointerOffset(team), pointerFormat());
    }


    private void move(Map<Team, SizedAddress> addressMap, SortedMap<SizedAddress, Integer> toMove) throws IOException {
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
    public void writeToRomAt(Team team, TeamNameTiles input) throws IOException {
        RomUtils.displaceTeamNameTilesIfNecessary(rom);
        pointerFormat = PointerFormat.P17000;
        System.out.println("Write team name design to rom at " + team);
        byte[] data = serialize(input);
        File folder = new File("temp");
        folder.mkdir();
        File compressed = new File("temp" + File.separator + "teamname.bin");
        Process compression = compressDataToFile(data, compressed);
        Map<Team, SizedAddress> addressMap = readAddressMap();
        int newSize = 0;
        try {
            if (compression.waitFor(5, TimeUnit.SECONDS) && compression.exitValue() == 0) {
                newSize = (int) Files.asByteSource(compressed).size();
            } else throw new RuntimeException("Konami compressor could not compress team name: " + team);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Map<Team, TopAndBottomAddress> originalMap = ImmutableMap.copyOf(addressMap);
        SizedAddress oldAddress = addressMap.get(team);
        SizedAddress newAddress = new SizedAddress(oldAddress.getAddress(), newSize);
        Map<SizedAddress, Integer> toMove = addressesAfter(addressMap, oldAddress.getAddress(), newAddress.getSize() - oldAddress.getSize());
        moveData(addressMap, toMove);
        movePointers(addressMap, toMove);
        updateAddressMapWithMovement(addressMap, toMove);
        //write new content
        byte[] compressedData = Files.asByteSource(compressed).read();
        //System.out.println("writing treated data to " + team);
        writeToPositionChecked(newAddress.getAddress(), compressedData);
        addressMap.put(team, newAddress);
        System.out.println("Write team name tiles to rom at " + team + "success!");
    }

    private void writeToPositionChecked(int address, byte[] data) throws IOException {
        checkMaximumAddress(address, data.length);
        FileUtils.writeToPosition(rom, data,
                address);
    }


    private void checkMaximumAddress(int address, int length) throws IOException {
        if (pointerFormat() == PointerFormat.P48000) {
            if (address + length > maximumAddress48) {
                throw new RuntimeException("Not enough space. Can't write team name in game tiles to position: " + Integer.toHexString(address + length));
            }
        } else if(pointerFormat() == PointerFormat.P17000) {
            if (address + length > maximumAddress17) {
                throw new RuntimeException("Not enough space. Can't write team name in game tiles to position: " + Integer.toHexString(address + length));
            }
        }
    }

    private void checkMaximumAddress(SizedAddress address) throws IOException {
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
                System.out.println("will be moved team name design of:" + currentTeam);
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

    private TeamNameTiles.Color[][] readMatrix(int offset) throws IOException {
        String command = "konami\\konami_d \"" + rom.getAbsolutePath() + "\" 0x" + Integer.toHexString(offset) + " 1";
        System.out.println(command);

        Process process = Runtime.getRuntime().exec(command, null);
        try {
            if (process.waitFor(10, TimeUnit.SECONDS) && process.exitValue() == 0) {
                File decomp = new File("decomp.bin");
                byte[] bytes = Files.asByteSource(decomp).read();
                //System.out.println("Flag part: " + bytesString(bytes));
                TeamNameTiles.Color[][] colors = bytesToMatrix(bytes);
                try {
                    decomp.delete();
                } catch (Exception e) {
                    System.out.println("Warning: could not delete " + decomp.getAbsolutePath());
                }
                return colors;

            } else throw new RuntimeException("Failed to run konami decompressor");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static TeamNameTiles.Color[][] bytesToMatrix(byte[] bytes) {
        // System.out.println(bytes.length);
        TeamNameTiles.Color[][] matrix = new TeamNameTiles.Color[8][8 * 4];
        for (int b = 0; b < bytes.length / 2; b++) {
            int i = 16 * (b / 8) + (b % 8) * 2;

            boolean[] b1 = bitArray(bytes[i]);
            boolean[] b2 = bitArray(bytes[i + 1]);

            for (int j = 0; j < 8; j++) {
                //System.out.println(b/2);
                int row = b % 8;
                int col = b / 8 * 8 + j;
                matrix[row][col] = TeamNameTiles.Color.forCode(byteOf(new boolean[]{b1[j], b2[j]}));
            }
        }
        return matrix;

    }

    private static boolean[] bitArray(byte b) {
        BitSet bits = BitSet.valueOf(new byte[]{b});
        boolean[] bitArray = new boolean[8];
        for (int i = 0; i < 8; i++) {
            bitArray[i] = bits.get(7 - i);
        }
        return bitArray;
    }

    static byte byteOf(boolean[] bools) {

        BitSet bits = new BitSet(bools.length);
        for (int i = 0; i < bools.length; i++) {
            if (bools[i]) {
                bits.set(i);
            }
        }

        byte[] bytes = bits.toByteArray();
        if (bytes.length * 8 >= bools.length) {
            return bytes[0];

        } else {
            byte b = Arrays.copyOf(bytes, bools.length / 8 + (bools.length % 8 == 0 ? 0 : 1))[0];
            return b;
        }
    }


    public static void main(String[] args) throws IOException {
        TeamNameInGameRomHandler handler = new TeamNameInGameRomHandler(new File("iss.sfc"));
        /*
        Multimap<SizedAddress, Team> invert = handler.invert(handler.readAddressMap());
        Stream<SizedAddress> stream = invert.keys().<SizedAddress>stream();
        SizedAddress address = (SizedAddress) stream.collect(toList()).get(invert.keys().size() - 1);
        System.out.println("Last position" + Integer.toHexString(address.getAddress() + address.getSize() - 1));
        System.out.println(invert);
        */

        for (Team team : Team.values()) {
            System.out.println(handler.readFromRomAt(team));
        }
    }


    private static byte[] serialize(TeamNameTiles teamNameTiles) {
        TeamNameTiles.Color[][] matrix = teamNameTiles.getMatrix();
        byte[] data = new byte[64];
        for (int b = 0; b < 32; b++) {
            int i = 16 * (b / 8) + (b % 8) * 2;
            for (int j = 0; j < 8; j++) {
                int row = b % 8;
                int col = b / 8 * 8 + j;
                TeamNameTiles.Color color = matrix[row][col];
                byte colorCode = color.getCode();
                //System.out.println("row: " + row + ", col: " + col);
                data[i] |= getBit(colorCode, 0) << (7 - j);
                data[i + 1] |= getBit(colorCode, 1) << (7 - j);
            }
        }
        return data;
    }

    public static byte getBit(byte b, int position) {
        return (byte) ((b >> position) & 1);
    }

    private Map<Team, SizedAddress> readAddressMap() throws IOException {
        Map<Team, SizedAddress> map = new HashMap<>();
        for (Team team : Team.values()) {
            long offset = pointerOffset(team);
            int address = readPointer(rom, offset, pointerFormat());
            ByteSource source = Files.asByteSource(rom);
            int size = source.slice(address, 1).read()[0];
            map.put(team, new SizedAddress(address, size));
        }
        return map;
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
