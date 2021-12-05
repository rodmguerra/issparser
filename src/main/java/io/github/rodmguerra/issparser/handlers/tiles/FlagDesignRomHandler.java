package io.github.rodmguerra.issparser.handlers.tiles;


import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.FileUtils;
import io.github.rodmguerra.issparser.commons.ParsingUtils;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.FlagDesign;
import io.github.rodmguerra.issparser.model.FlagSnes4bpp;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import static io.github.rodmguerra.issparser.commons.ParsingUtils.bytesString;

public class FlagDesignRomHandler implements RomHandler<FlagDesign> {

    private final File rom;
    private static final long POINTER_OFFSET = 0x941a;
    private static final long POINTER_STEP = 4;

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

    public int readPointer(long pointerOffset) throws IOException {
        byte[] bytes = Files.asByteSource(rom).slice(pointerOffset, 2).read();
        return addressFromSnes(bytes[0], bytes[1]);
    }

    @Override
    public FlagDesign readFromRomAt(Team team) throws IOException {
        int topOffset = readPointer(POINTER_OFFSET + POINTER_STEP * team.ordinal());
        int bottomOffset = readPointer(2 + POINTER_OFFSET + POINTER_STEP * team.ordinal());
        FlagDesign.Color[][] flagTop = readFlagPart(topOffset);
        FlagDesign.Color[][] flagBottom = readFlagPart(bottomOffset);
        return new FlagDesign(joinFlagParts(flagTop, flagBottom));
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

    @Override
    public void writeToRomAt(Team team, FlagDesign input) throws IOException {
        FlagSnes4bpp flagSnes = serialize(input);
        File compressedTop = new File("top.bin");
        File compressedBottom = new File("bottom.bin");
        Process topCompression = compressDataToFile(flagSnes.getTop(), compressedTop);
        Process bottomCompression = compressDataToFile(flagSnes.getBottom(), compressedBottom);
        Map<Team, TopAndBottomAddress> addressMap = readAddressMap();
        int newTopSize = 0;
        int newBottomSize = 0;
        try {
            if (topCompression.waitFor() == 0) {
                newTopSize = (int) Files.asByteSource(compressedTop).size();
            } else throw new RuntimeException("Failed to compress flag design bytes of top: " + team);
            if (bottomCompression.waitFor() == 0) {
                newBottomSize = (int) Files.asByteSource(compressedBottom).size();
            } else throw new RuntimeException("Failed to compress flag design bytes of bottom: " + team);
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
            newAddressToMovePointer = new SizedAddress(topAddress + newBottomSize - bottomSize, newTopSize);
            newAddresses = new TopAndBottomAddress(newAddressToMovePointer, new SizedAddress(bottomAddress, newBottomSize));
        } else {
            oldAddressToMovePointer = teamAddress.getBottomAddress();
            int deltaGermany = newTopSize - topSize;
            System.out.println("deltagermany t = " + deltaGermany + " = " + newTopSize + " - " + topSize);
            System.out.println("deltagermany b = " + (newBottomSize-bottomSize) + " = " + newBottomSize + " - " + bottomSize);

            newAddressToMovePointer = new SizedAddress(bottomAddress + deltaGermany, newTopSize);
            newAddresses = new TopAndBottomAddress(new SizedAddress(topAddress, newTopSize), newAddressToMovePointer);
        }

        Map<SizedAddress, Integer> addressesToMove = addressesAfter(addressMap, teamAddress, newAddresses);
        Map<Integer, byte[]> movingData = readMovingData(addressMap, addressesToMove);
        moveData(movingData);
        //move also current team pointer
        addressesToMove.put(oldAddressToMovePointer, newAddressToMovePointer.getAddress());
        movePointers(addressMap, addressesToMove);

        //write new content
        byte[] top = Files.asByteSource(compressedTop).read();
        System.out.println("writing treated data to " + team);
        FileUtils.writeToPosition(rom, top,
                newAddresses.getTopAddress().getAddress());
        byte[] bottom = Files.asByteSource(compressedBottom).read();
        System.out.println(bottom.length);
        FileUtils.writeToPosition(rom, bottom,
                newAddresses.getBottomAddress().getAddress());

        //move new content pointers

    }

    private void movePointers(Map<Team, TopAndBottomAddress> addressMap, Map<SizedAddress, Integer> addressesToMove) throws IOException {
        for (Team team : Team.values()) {
            long pointerOffset = POINTER_OFFSET + POINTER_STEP * team.ordinal();
            SizedAddress topAddress = addressMap.get(team).getTopAddress();
            if (addressesToMove.containsKey(topAddress)) {
                System.out.println("moving top pointer of: " + team);
                writeAdressToPointer(pointerOffset, addressesToMove.get(topAddress));
            }
            SizedAddress bottomAddress = addressMap.get(team).getBottomAddress();
            if (addressesToMove.containsKey(bottomAddress)) {
                System.out.println("moving bottom pointer of: " + team);
                writeAdressToPointer(2 + pointerOffset, addressesToMove.get(bottomAddress));
            }
        }
    }

    private void moveData(Map<Integer, byte[]> movingData) throws IOException {
        for (Integer address : movingData.keySet()) {
            FileUtils.writeToPosition(rom, movingData.get(address), address);
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
                    System.out.println("displacement greater then zero for bottom of " + entry.getKey() + " displacement = " + displacement + " from " + Integer.toHexString(bottomAddress.getAddress()) + " to " + Integer.toHexString(newAddress)) ;

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
                    System.out.println("displacement greater then zero for top of " + entry.getKey() + " displacement = " + displacement + " from " + Integer.toHexString(topAddress.getAddress()) + " to " + Integer.toHexString(newAddress)) ;

                }
            }
        }
        return addresses;
    }

      /*
    private void writeDisplacementsToRom(Map<Team, TopAndBottomAddress> beforeMap, Map<Team, TopAndBottomAddress> displacements) throws IOException {
        Map<Team, byte[]> topContents = new HashMap<>();
        Map<Team, byte[]> bottomContents = new HashMap<>();

        for (Team team : displacements.keySet()) {
            long topOffset = POINTER_OFFSET + POINTER_STEP * team.ordinal();
            long bottomOffset = topOffset + 2;
            TopAndBottomAddress addresses = displacements.get(team);
            writeAdressToPointer(topOffset, addresses.getTopAddress().getAddress());
            writeAdressToPointer(bottomOffset, addresses.getBottomAddress().getAddress());
        }

        for (Team team : displacements.keySet()) {
            TopAndBottomAddress beforeAddress = beforeMap.get(team);
            SizedAddress topAddress = beforeAddress.getTopAddress();
            topContents.put(team,
                    Files.asByteSource(rom).slice(topAddress.getAddress(), topAddress.getSize()).read()
            );
            SizedAddress bottomAddress = beforeAddress.getBottomAddress();
            bottomContents.put(team,
                    Files.asByteSource(rom).slice(bottomAddress.getAddress(), bottomAddress.getSize()).read()
            );
        }

        for (Team team : displacements.keySet()) {
            TopAndBottomAddress displacedAddress = displacements.get(team);
            byte[] data = topContents.get(team);

            int address = displacedAddress.getTopAddress().getAddress();
            //System.out.println("writing " + team + " top 0x" + Integer.toHexString(address) + ": " + ParsingUtils.bytesString(data));
            FileUtils.writeToPosition(rom, data, address);
            FileUtils.writeToPosition(rom, bottomContents.get(team), displacedAddress.getBottomAddress().getAddress());
        }
    }
        */
    /*
private Map<Team, TopAndBottomAddress> displaceAddressMap(Map<Team, TopAndBottomAddress> addressMap, Team movingTeam, int newTopSize, int newBottomSize) throws IOException {
   Map<Team, TopAndBottomAddress> displaced = new HashMap<>();
   TopAndBottomAddress oldAddress = addressMap.get(movingTeam);
   int topDelta = newTopSize - oldAddress.getTopAddress().getSize();
   int bottomDelta = newBottomSize - oldAddress.getBottomAddress().getSize();
   for (Team currentTeam : Team.values()) {
       TopAndBottomAddress teamAddresses = addressMap.get(currentTeam);
       System.out.println("\n" + currentTeam);
       System.out.println("before: " + teamAddresses);
       int topAddress = teamAddresses.getTopAddress().getAddress();
       int bottomAddress = teamAddresses.getBottomAddress().getAddress();
       int topSize = teamAddresses.getTopAddress().getSize();
       int bottomSize = teamAddresses.getBottomAddress().getSize();

       //same address - change all pointers sizes
       if (teamAddresses.getTopAddress().getAddress() == oldAddress.getTopAddress().getAddress()) {
           topSize = newTopSize;
       }
       if (teamAddresses.getBottomAddress().getAddress() == oldAddress.getBottomAddress().getAddress()) {
           bottomSize = newBottomSize;
       }

       //greater or same address - displace
       if (teamAddresses.getTopAddress().getAddress() > oldAddress.getTopAddress().getAddress()) {
           topAddress += topDelta;
       }
       if (teamAddresses.getTopAddress().getAddress() > oldAddress.getBottomAddress().getAddress()) {
           topAddress += bottomDelta;
       }
       if (teamAddresses.getBottomAddress().getAddress() > oldAddress.getTopAddress().getAddress()) {
           bottomAddress += topDelta;
       }
       if (teamAddresses.getBottomAddress().getAddress() > oldAddress.getBottomAddress().getAddress()) {
           bottomAddress += bottomDelta;
       }

       TopAndBottomAddress after = new TopAndBottomAddress(
               new SizedAddress(topAddress, topSize),
               new SizedAddress(bottomAddress, bottomSize));


       if (after.getBottomAddress().getAddress() != (teamAddresses.getBottomAddress().getAddress()) &&
               after.getTopAddress().getAddress() != teamAddresses.getTopAddress().getAddress()) {
           displaced.put(currentTeam, after);
       }


       addressMap.put(currentTeam, after);
       System.out.println("after:" + after);
   }
   writeDisplacementsToRom(addressMap, displaced);
   //moving team mudar o ponteiro
}
 */
    private void writeAdressToPointer(long pointerOffset, int newAdress) throws IOException {
        FileUtils.writeToPosition(rom, addressToSnes(newAdress), pointerOffset);
    }

    private Process compressDataToFile(byte[] bytes, File file) throws IOException {
        String comp = file.getAbsolutePath();
        String decomp = file.getAbsolutePath() + ".decomp";
        Files.write(bytes, new File(decomp));

        String command = "konami\\konami_c \"" + decomp + "\" \"" + comp + "\" 1";
        return Runtime.getRuntime().exec(command, null);
    }

    private static int addressFromSnes(byte pointerByte1, byte pointerByte2) {
        return ByteBuffer.wrap(new byte[]{0, 0x4, pointerByte2, pointerByte1}).getInt();
    }

    private static byte[] addressToSnes(int address) {
        address = address % 0x40000;
        return new byte[]{(byte) (address % 0x100), (byte) (address / 0x100)};
    }


    private FlagDesign.Color[][] readFlagPart(int offset) throws IOException {
        String command = "konami\\konami_d \"" + rom.getAbsolutePath() + "\" 0x" + Integer.toHexString(offset) + " 1";
        System.out.println(command);

        Process process = Runtime.getRuntime().exec(command, null);
        try {
            if (process.waitFor() == 0) {
                byte[] bytes = Files.asByteSource(new File("decomp.bin")).read();
                System.out.println("Flag part: " + bytesString(bytes));
                return bytesToMatrix(bytes);
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
        FlagDesignRomHandler handler = new FlagDesignRomHandler(new File("iss.sfc"));
        handler.writeToRomAt(Team.GERMANY, handler.readFromRomAt(Team.WALES));
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
            long offset = POINTER_OFFSET + POINTER_STEP * team.ordinal();
            int topAddress = readPointer(offset);
            ByteSource source = Files.asByteSource(rom);
            int topSize = source.slice(topAddress, 1).read()[0];
            SizedAddress top = new SizedAddress(topAddress, topSize);
            int bottomAddress = readPointer(offset + 2);
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

    private class SizedAddress {
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
    }


}
