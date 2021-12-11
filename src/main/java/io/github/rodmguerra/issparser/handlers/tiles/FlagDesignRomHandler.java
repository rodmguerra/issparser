package io.github.rodmguerra.issparser.handlers.tiles;


import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.FlagDesign;
import io.github.rodmguerra.issparser.model.FlagSnes4bpp;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static io.github.rodmguerra.issparser.commons.RomUtils.*;
import static java.util.stream.Stream.concat;

public class FlagDesignRomHandler implements RomHandler<FlagDesign> {

    private final File rom;
    private static final long POINTER_OFFSET = 0x941a;
    private static final long POINTER_STEP = 4;
    private int maximumAddress = 0x489CD;//0x483FD;

    public FlagDesignRomHandler(File rom) {
        this.rom = rom;
    }

    @Override
    public Map<Team, FlagDesign> readFromRom() throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToRom(Map<Team, ? extends FlagDesign> input) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FlagDesign readFromRomAt(Team team) throws IOException {
        int topOffset = readPointerAt(team);
        int bottomOffset = readPointer(rom, pointerOffset(team) + 2);
        FlagDesign.Color[][] flagTop = readFlagPart(topOffset);
        FlagDesign.Color[][] flagBottom = readFlagPart(bottomOffset);
        return new FlagDesign(joinFlagParts(flagTop, flagBottom));
    }

    private int readPointerAt(Team team) throws IOException {
        return readPointer(rom, pointerOffset(team));
    }

    public List<Team> teamsSharing(Team team) throws IOException {
        Map<Team, TopAndBottomAddress> addressMap = readAddressMap();
        return teamsSharing(team, addressMap);

    }

    private static List<Team> teamsSharing(Team team, Map<Team, TopAndBottomAddress> addressMap) {
        List<Team> teamsSharing = new ArrayList<>();
        TopAndBottomAddress addresses = addressMap.get(team);
        for (Team currentTeam : Team.values()) {
            TopAndBottomAddress currentTeamAdresses = addressMap.get(currentTeam);
            if (currentTeamAdresses.getBottomAddress().equals(addresses.getBottomAddress()) ||
                    currentTeamAdresses.getBottomAddress().equals(addresses.getTopAddress()) ||
                    currentTeamAdresses.getTopAddress().equals(addresses.getTopAddress()) ||
                    currentTeamAdresses.getTopAddress().equals(addresses.getBottomAddress())) {
                teamsSharing.add(currentTeam);

            }
        }
        return teamsSharing;
    }


    private FlagDesign.Color[][] joinFlagParts(FlagDesign.Color[][] flagTop, FlagDesign.Color[][] flagBottom) {
        FlagDesign.Color[][] flag = FlagDesign.newColorArray();
        for (int i = 0; i < flag.length; i++) {
            for (int j = 0; j < flag[i].length; j++) {
                flag[i][j] = i < flagBottom.length ? flagTop[i][j] : flagBottom[i - flagBottom.length][j];
            }
        }
        return flag;
    }



    public void unlinkFlag(Team team) throws IOException {
        System.out.println("Unlink flag " + team);
        Map<Team, TopAndBottomAddress> addressMap = readAddressMap();
        List<Team> teams = teamsSharing(team, addressMap);
        if (teams.size() <= 1) {
            System.out.println("Unlink flag " + team + " is not necessary");
            return;
        }
        displaceTeamNameTilesIfNecessary(rom);
        slim(addressMap);

        //read current data
        TopAndBottomAddress teamAddresses = addressMap.get(team);
        SizedAddress topAddress = teamAddresses.getTopAddress();
        SizedAddress bottomAddress = teamAddresses.getBottomAddress();
        byte[] topData = Files.asByteSource(rom).slice(topAddress.getAddress(), topAddress.getSize()).read();
        byte[] bottomData = Files.asByteSource(rom).slice(bottomAddress.getAddress(), bottomAddress.getSize()).read();

        //discover nextAddress
        SizedAddress address = maximumAddress(addressMap);
        int newTopAddress = address.getAddress() + address.getSize();
        int newBottomAddress = newTopAddress + topData.length;

        //write bottom address first so it will throw an exception if size overflows available space
        writeToPositionChecked(newBottomAddress, bottomData);
        writeToPositionChecked(newTopAddress, topData);
        writeTopPointerAt(team, newTopAddress);
        writeBottomPointerAt(team, newBottomAddress);

        System.out.println("Unlink flag " + team + " - success");
    }

    private void writeTopPointerAt(Team team, int address) throws IOException {
        writeAdressToPointer(rom, pointerOffset(team), address);
    }

    private void writeBottomPointerAt(Team team, int address) throws IOException {
        writeAdressToPointer(rom, pointerOffset(team) + 2, address);
    }

    private void writePointersAt(Team team, TopAndBottomAddress adresses) throws IOException {
        writeTopPointerAt(team, adresses.getTopAddress().getAddress());
        writeBottomPointerAt(team, adresses.getBottomAddress().getAddress());
    }


    public void linkTeams(Team[] fromTeams, Team toTeam) throws IOException {
        System.out.println("Linking teams " + Joiner.on(", ").join(fromTeams) + " to " + toTeam);
        Map<Team, TopAndBottomAddress> addressMap = readAddressMap();
        int firstAddress = minimumAddress(addressMap).getAddress();
        for (Team fromTeam : fromTeams) {
            TopAndBottomAddress toTeamAddresses = addressMap.get(toTeam);
            writePointersAt(fromTeam, toTeamAddresses);
            addressMap.put(fromTeam, toTeamAddresses);
        }
        slim(addressMap, firstAddress);
        System.out.println("Linking teams " + " to " + toTeam + " - success");

    }

    /**
     * Warning: for performance, object addressMap is not updated
     *
     * @param addressMap
     * @param firstAddress
     */
    private void slim(Map<Team, TopAndBottomAddress> addressMap, int firstAddress) throws IOException {
        System.out.println("Slim: " + invert(addressMap));
        SortedSet<SizedAddress> sorted = new TreeSet<>();
        SortedMap<SizedAddress, Integer> toMove = new TreeMap<>();
        for (TopAndBottomAddress addresses : addressMap.values()) {
            sorted.add(addresses.getTopAddress());
            sorted.add(addresses.getBottomAddress());
        }
        int nextAddress = firstAddress;
        for (SizedAddress address : sorted) {
            if (address.getAddress() > nextAddress) {
                toMove.put(address, nextAddress);
            }
            nextAddress += address.getSize();
        }
        if (toMove.size() > 0) {
            move(addressMap, toMove);
            System.out.println("Slim - success: " + invert(addressMap));
        } else System.out.println("Slim not needed");

    }

    private void move(Map<Team, TopAndBottomAddress> addressMap, SortedMap<SizedAddress, Integer> toMove) throws IOException {
        moveData(addressMap, toMove);
        movePointers(addressMap, toMove);
        updateAddressMapWithMovement(addressMap, toMove);
    }

    private void updateAddressMapWithMovement(Map<Team, TopAndBottomAddress> addressMap, Map<SizedAddress, Integer> moved) {
        for (Team team : addressMap.keySet()) {
            SizedAddress topAddress = addressMap.get(team).getTopAddress();
            SizedAddress bottomAddress = addressMap.get(team).getBottomAddress();
            Integer movedTop = moved.get(topAddress);
            Integer movedBottom = moved.get(bottomAddress);
            if (movedTop == null) movedTop = topAddress.getAddress();
            if (movedBottom == null) movedBottom = bottomAddress.getAddress();
            addressMap.put(team, new TopAndBottomAddress(
                    new SizedAddress(movedTop, topAddress.getSize()),
                    new SizedAddress(movedBottom, bottomAddress.getSize())));
        }
    }


    public void slim(Map<Team, TopAndBottomAddress> addressMap) throws IOException {
        slim(addressMap, minimumAddress(addressMap).getAddress());
    }


    private long pointerOffset(Team team) {
        return POINTER_OFFSET + POINTER_STEP * team.ordinal();
    }

    private SizedAddress maximumAddress(Map<Team, TopAndBottomAddress> addressMap) {
        return maximumAddress(concat(
                addressMap.values().stream().map(TopAndBottomAddress::getTopAddress),
                addressMap.values().stream().map(TopAndBottomAddress::getBottomAddress)));
    }

    private SizedAddress minimumAddress(Map<Team, TopAndBottomAddress> addressMap) {
        return minimumAddress(concat(
                addressMap.values().stream().map(TopAndBottomAddress::getTopAddress),
                addressMap.values().stream().map(TopAndBottomAddress::getBottomAddress))
        );
    }


    private SizedAddress maximumAddress(Stream<SizedAddress> addresses) {
        SizedAddress[] sizedAddresses = addresses.toArray(SizedAddress[]::new);
        int maximumAddress = 0;
        SizedAddress maximumSizedAddress = null;
        for (SizedAddress address : sizedAddresses) {
            if (address.getAddress() > maximumAddress) {
                maximumAddress = address.getAddress();
                maximumSizedAddress = address;
            }
        }
        return maximumSizedAddress;
    }

    private SizedAddress minimumAddress(Stream<SizedAddress> addresses) {
        SizedAddress[] sizedAddresses = addresses.toArray(SizedAddress[]::new);
        int minumumAddress = Integer.MAX_VALUE;
        SizedAddress minumumSizedAddress = null;
        for (SizedAddress address : sizedAddresses) {
            if (address.getAddress() < minumumAddress) {
                minumumAddress = address.getAddress();
                minumumSizedAddress = address;
            }
        }
        return minumumSizedAddress;
    }


    public void writeToRomAt(Team team, Iterable<Team> teams, FlagDesign input) throws IOException {
        System.out.println("Write flag design to rom at " + teams);
        displaceTeamNameTilesIfNecessary(rom);
        linkTeams(Iterables.toArray(teams, Team.class), team); //liga com os novos que estão juntando, liberando o espaço deles
        unlinkFlag(team); //Separa o time dos atuais que estão juntos
        linkTeams(Iterables.toArray(teams, Team.class), team);  //liga novamente com os novos que estão juntando, separando dos demais;
        writeToRomAt(team, input); //escreve no novo (com todos juntos)
    }

    @Override
    public void writeToRomAt(Team team, FlagDesign input) throws IOException {
        System.out.println("Write flag design to rom at " + team);
        displaceTeamNameTilesIfNecessary(rom);
        FlagSnes4bpp flagSnes = serialize(input);
        File folder = new File("temp");
        folder.mkdir();
        File compressedTop = new File("temp" + File.separator + "top.bin");
        File compressedBottom = new File("temp" + File.separator + "bottom.bin");
        Process topCompression = compressDataToFile(flagSnes.getTop(), compressedTop);
        Process bottomCompression = compressDataToFile(flagSnes.getBottom(), compressedBottom);
        Map<Team, TopAndBottomAddress> addressMap = readAddressMap();
        int newTopSize = 0;
        int newBottomSize = 0;
        try {
            if (topCompression.waitFor(5, TimeUnit.SECONDS) && topCompression.exitValue() == 0) {
                newTopSize = (int) Files.asByteSource(compressedTop).size();
            } else throw new RuntimeException("Konami compressor could not compress flag top: " + team);
            if (bottomCompression.waitFor(5, TimeUnit.SECONDS) && bottomCompression.exitValue() == 0) {
                newBottomSize = (int) Files.asByteSource(compressedBottom).size();
            } else throw new RuntimeException("Konami compressor could not compress flag bottom: " + team);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Map<Team, TopAndBottomAddress> originalMap = ImmutableMap.copyOf(addressMap);
        TopAndBottomAddress teamAddress = addressMap.get(team);
        int topAddress = teamAddress.getTopAddress().getAddress();
        int topSize = teamAddress.getTopAddress().getSize();
        int bottomAddress = teamAddress.getBottomAddress().getAddress();
        int bottomSize = teamAddress.getBottomAddress().getSize();
        TopAndBottomAddress newAddresses = null;
        SizedAddress newAddressToMovePointer = null;
        SizedAddress oldAddressToMovePointer = null;
        if (topAddress >= bottomAddress) {
            oldAddressToMovePointer = teamAddress.getTopAddress();
            SizedAddress newTopAddress = new SizedAddress(topAddress + newBottomSize - bottomSize, newTopSize);
            SizedAddress newBottomAddress = new SizedAddress(bottomAddress, newBottomSize);
            newAddressToMovePointer = newTopAddress;
            newAddresses = new TopAndBottomAddress(newTopAddress, newBottomAddress);
        } else {
            oldAddressToMovePointer = teamAddress.getBottomAddress();
            SizedAddress newTopAddress = new SizedAddress(topAddress, newTopSize);
            SizedAddress newBottomAddress = new SizedAddress(bottomAddress + newTopSize - topSize, newBottomSize);
            newAddressToMovePointer = newBottomAddress;
            newAddresses = new TopAndBottomAddress(newTopAddress, newBottomAddress);
        }

        checkMaximumAddress(newAddressToMovePointer);


        Map<SizedAddress, Integer> toMove = addressesAfter(addressMap, teamAddress, newAddresses);
        moveData(addressMap, toMove);
        //move also current team pointer
        toMove.put(oldAddressToMovePointer, newAddressToMovePointer.getAddress());
        movePointers(addressMap, toMove);
        updateAddressMapWithMovement(addressMap, toMove);

        //write new content
        byte[] top = Files.asByteSource(compressedTop).read();
        //System.out.println("writing treated data to " + team);
        writeToPositionChecked(newAddresses.getTopAddress().getAddress(), top);
        byte[] bottom = Files.asByteSource(compressedBottom).read();
        System.out.println(bottom.length);
        int address = newAddresses.getBottomAddress().getAddress();
        writeToPositionChecked(address, bottom);

        List<Team> teams = teamsSharing(team, addressMap);
        for (Team teamSharing : teams) {
            addressMap.put(teamSharing, newAddresses);
        }
        slim(addressMap);
        fixSharedPointersIfNecessary(rom);
        System.out.println("Write flag design to rom at " + team + "success!");
    }

    private void writeToPositionChecked(int address, byte[] data) throws IOException {
        checkMaximumAddress(address, data.length);
        FileUtils.writeToPosition(rom, data,
                address);
    }


    private void checkMaximumAddress(int address, int length) {
        if (address + length > maximumAddress) {
            throw new RuntimeException("Not enough space. Can't write to position: " + Integer.toHexString(address + length));
        }
    }

    private void checkMaximumAddress(SizedAddress address) {
        checkMaximumAddress(address.getAddress(), address.getSize());
    }

    private void moveData(Map<Team, TopAndBottomAddress> addressMap, Map<SizedAddress, Integer> addressesToMove) throws IOException {
        Map<Integer, byte[]> movingData = readMovingData(addressMap, addressesToMove);
        moveData(movingData);
    }

    private void movePointers(Map<Team, TopAndBottomAddress> addressMap, Map<SizedAddress, Integer> addressesToMove) throws IOException {
        for (Team team : Team.values()) {
            long pointerOffset = pointerOffset(team);
            SizedAddress topAddress = addressMap.get(team).getTopAddress();
            if (addressesToMove.containsKey(topAddress)) {
                System.out.println("moving top pointer of: " + team);
                writeAdressToPointer(rom, pointerOffset, addressesToMove.get(topAddress));
            }
            SizedAddress bottomAddress = addressMap.get(team).getBottomAddress();
            if (addressesToMove.containsKey(bottomAddress)) {
                System.out.println("moving bottom pointer of: " + team);
                writeAdressToPointer(rom, 2 + pointerOffset, addressesToMove.get(bottomAddress));
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

    private Map<Integer, byte[]> readMovingData(Map<Team, TopAndBottomAddress> addressMap, Map<SizedAddress, Integer> addressesToMove) throws IOException {
        System.out.println(addressesToMove.size());
        Map<Integer, byte[]> contentToWrite = new HashMap<>();
        for (Team currentTeam : Team.values()) {

            SizedAddress address1 = addressMap.get(currentTeam).getBottomAddress();
            Integer newAddress1 = addressesToMove.get(address1);
            if (newAddress1 != null) {
                System.out.println("will be moved bottom of:" + currentTeam);
                contentToWrite.put(newAddress1, Files.asByteSource(rom).slice(address1.getAddress(), address1.getSize()).read());
            }


            SizedAddress address2 = addressMap.get(currentTeam).getTopAddress();
            Integer newAddress2 = addressesToMove.get(address2);
            if (newAddress2 != null) {
                System.out.println("will be moved top of:" + currentTeam);
                contentToWrite.put(newAddress2, Files.asByteSource(rom).slice(address2.getAddress(), address2.getSize()).read());
            }
        }
        return contentToWrite;
    }

    private Map<SizedAddress, Integer> addressesAfter(Map<Team, TopAndBottomAddress> addressMap, TopAndBottomAddress old, TopAndBottomAddress aNew) {
        Map<SizedAddress, Integer> addresses = new HashMap<>();
        int bottomDelta = aNew.getBottomAddress().getSize() - old.getBottomAddress().getSize();
        int topDelta = aNew.getTopAddress().getSize() - old.getTopAddress().getSize();

        for (Map.Entry<Team, TopAndBottomAddress> entry : addressMap.entrySet()) {
            TopAndBottomAddress address = entry.getValue();
            int displacement = 0;
            SizedAddress bottomAddress = address.getBottomAddress();
            if (!bottomAddress.equals(old.getBottomAddress())) {
                if (bottomAddress.getAddress() > old.getTopAddress().getAddress()) {
                    displacement += topDelta;
                }
                if (bottomAddress.getAddress() > old.getBottomAddress().getAddress()) {
                    displacement += bottomDelta;
                }
                if (displacement > 0) {
                    int newAddress = bottomAddress.getAddress() + displacement;
                    addresses.put(bottomAddress, newAddress);
                    System.out.println("displacement greater then zero for bottom of " + entry.getKey() + " displacement = " + displacement + " from " + Integer.toHexString(bottomAddress.getAddress()) + " to " + Integer.toHexString(newAddress));

                }
            }

            displacement = 0;
            SizedAddress topAddress = address.getTopAddress();
            if (!topAddress.equals(old.getTopAddress())) {
                if (topAddress.getAddress() > old.getTopAddress().getAddress()) {
                    displacement += topDelta;
                }
                if (topAddress.getAddress() > old.getBottomAddress().getAddress()) {
                    displacement += bottomDelta;
                }
                if (displacement > 0) {
                    int newAddress = topAddress.getAddress() + displacement;
                    addresses.put(topAddress, newAddress);
                    //System.out.println("displacement greater then zero for top of " + entry.getKey() + " displacement = " + displacement + " from " + Integer.toHexString(topAddress.getAddress()) + " to " + Integer.toHexString(newAddress));
                }
            }
        }
        return addresses;
    }

    public Multimap<SizedAddress, Team> invert(Map<Team, TopAndBottomAddress> addressMap) {
        Multimap<SizedAddress, Team> map = MultimapBuilder.treeKeys().linkedListValues().build();
        for (Team team : addressMap.keySet()) {
            TopAndBottomAddress addresses = addressMap.get(team);
            map.put(addresses.getTopAddress(), team);
            map.put(addresses.getBottomAddress(), team);
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

    private FlagDesign.Color[][] readFlagPart(int offset) throws IOException {
        String command = "konami\\konami_d \"" + rom.getAbsolutePath() + "\" 0x" + Integer.toHexString(offset) + " 1";
        System.out.println(command);

        Process process = Runtime.getRuntime().exec(command, null);
        try {
            if (process.waitFor(10, TimeUnit.SECONDS) && process.exitValue() == 0) {
                File decomp = new File("decomp.bin");
                byte[] bytes = Files.asByteSource(decomp).read();
                //System.out.println("Flag part: " + bytesString(bytes));
                FlagDesign.Color[][] colors = bytesToMatrix(bytes);
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

    private static FlagDesign.Color[][] bytesToMatrix(byte[] bytes) {
        // System.out.println(bytes.length);
        FlagDesign.Color[][] matrix = new FlagDesign.Color[8][bytes.length / 4];
        for (int b = 0; b < bytes.length / 4; b++) {
            int i = 32 * (b / 8) + (b % 8) * 2;

            boolean[] b1 = bitArray(bytes[i]);
            boolean[] b2 = bitArray(bytes[i + 1]);
            boolean[] b3 = bitArray(bytes[i + 16]);
            boolean[] b4 = bitArray(bytes[i + 16 + 1]);

            for (int j = 0; j < 8; j++) {
                //System.out.println(b/2);
                int row = b % 8;
                int col = b / 8 * 8 + j;
                matrix[row][col] = FlagDesign.Color.forCode(byteOf(new boolean[]{b1[j], b2[j], b3[j], b4[j]}));
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
       /* FlagDesignRomHandler handler = new FlagDesignRomHandler(new File("iss.sfc"));
        handler.unlinkFlag(Team.GERMANY);     */
        FlagDesignRomHandler handler = new FlagDesignRomHandler(new File("iss.sfc"));
        handler.slim(handler.readAddressMap());
        System.out.println(handler.invert(handler.readAddressMap()));
    }

    private static FlagSnes4bpp serialize(FlagDesign flagDesign) {
        FlagDesign.Color[][] matrix = flagDesign.getMatrix();
        byte[] top = new byte[96];
        for (int b = 0; b < 24; b++) {
            int i = 32 * (b / 8) + (b % 8) * 2;
            for (int j = 0; j < 8; j++) {
                int row = b % 8;
                int col = b / 8 * 8 + j;
                FlagDesign.Color color = matrix[row][col];
                byte colorCode = color.getCode();
                //System.out.println("row: " + row + ", col: " + col);
                top[i] |= getBit(colorCode, 0) << (7 - j);
                top[i + 1] |= getBit(colorCode, 1) << (7 - j);
                top[i + 16] |= getBit(colorCode, 2) << (7 - j);
                top[i + 16 + 1] |= getBit(colorCode, 3) << (7 - j);
            }
        }

        byte[] bottom = new byte[96];
        for (int b = 0; b < 24; b++) {
            int i = 32 * (b / 8) + (b % 8) * 2;
            for (int j = 0; j < 8; j++) {
                int row = b % 8;
                int col = b / 8 * 8 + j;
                FlagDesign.Color color = matrix[row + 8][col];
                byte colorCode = color.getCode();
                bottom[i] |= getBit(colorCode, 0) << (7 - j);
                bottom[i + 1] |= getBit(colorCode, 1) << (7 - j);
                bottom[i + 16] |= getBit(colorCode, 2) << (7 - j);
                bottom[i + 16 + 1] |= getBit(colorCode, 3) << (7 - j);
            }
        }

        return new FlagSnes4bpp(top, bottom);
    }

    public static byte getBit(byte b, int position) {
        return (byte) ((b >> position) & 1);
    }

    private Map<Team, TopAndBottomAddress> readAddressMap() throws IOException {
        Map<Team, TopAndBottomAddress> map = new HashMap<>();
        for (Team team : Team.values()) {
            long offset = pointerOffset(team);
            int topAddress = readPointer(rom, offset);
            ByteSource source = Files.asByteSource(rom);
            int topSize = source.slice(topAddress, 1).read()[0];
            SizedAddress top = new SizedAddress(topAddress, topSize);
            int bottomAddress = readPointer(rom, offset + 2);
            int bottomSize = source.slice(bottomAddress, 1).read()[0];
            SizedAddress bottom = new SizedAddress(bottomAddress, bottomSize);
            map.put(team, new TopAndBottomAddress(top, bottom));
            //System.out.println(team + " top start=" + topAddress + ", top end" + topAddress + topSize);
        }
        return map;
    }

    private class TopAndBottomAddress {
        private final SizedAddress topAddress;
        private final SizedAddress bottomAddress;

        private TopAndBottomAddress(SizedAddress topAddress, SizedAddress bottomAddress) {
            this.topAddress = topAddress;
            this.bottomAddress = bottomAddress;
        }

        public SizedAddress getTopAddress() {
            return topAddress;
        }

        public SizedAddress getBottomAddress() {
            return bottomAddress;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TopAndBottomAddress that = (TopAndBottomAddress) o;

            if (bottomAddress != null ? !bottomAddress.equals(that.bottomAddress) : that.bottomAddress != null)
                return false;
            if (topAddress != null ? !topAddress.equals(that.topAddress) : that.topAddress != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = topAddress != null ? topAddress.hashCode() : 0;
            result = 31 * result + (bottomAddress != null ? bottomAddress.hashCode() : 0);
            return result;
        }

        public String toString() {
            return "TopAndBottomAddress{" +
                    "topAddress=" + topAddress +
                    ", bottomAddress=" + bottomAddress +
                    '}';
        }
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
