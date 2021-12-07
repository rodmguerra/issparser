package io.github.rodmguerra.issparser.handlers.tiles;


import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.FlagDesign;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.stream.Stream;

import static io.github.rodmguerra.issparser.commons.ParsingUtils.bytesString;

public class FlagDesignRomHandlerNew implements RomHandler<FlagDesign> {

    private final File rom;
    private static final long POINTER_OFFSET = 0x941a;
    private static final long POINTER_STEP = 4;

    public FlagDesignRomHandlerNew(File rom) {
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
        byte[] topPointerBytes = Files.asByteSource(rom).slice(POINTER_OFFSET + POINTER_STEP * team.ordinal(), 2).read();
        System.out.println("Team:   " + team);

        byte[] bottomPointerBytes = Files.asByteSource(rom).slice(POINTER_OFFSET + 2 + POINTER_STEP * team.ordinal(), 2).read();

        System.out.println("");

        int topOffset = offsetFromPointer(topPointerBytes[0], topPointerBytes[1]);
        int bottomOffset = offsetFromPointer(bottomPointerBytes[0], bottomPointerBytes[1]);

        System.out.println("Top:    " + Integer.toHexString(topOffset));
        System.out.println("Bottom: " + Integer.toHexString(bottomOffset));
        System.out.println("Top Size: " + Integer.toHexString(bottomOffset-topOffset));
        FlagDesign.Color[][] flagTop = readFlagPart(topOffset);
        FlagDesign.Color[][] flagBottom = readFlagPart(bottomOffset);
        //return new FlagDesign(joinFlagParts(flagTop, flagBottom));
        return null;

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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private static int offsetFromPointer(byte pointerByte1, byte pointerByte2) {
        return ByteBuffer.wrap(new byte[]{0, 0x4, pointerByte2, pointerByte1}).getInt();
    }


    private FlagDesign.Color[][] readFlagPart(int offset) throws IOException {
        ByteSource source = Files.asByteSource(rom);
        byte size = source.slice(offset, 1).read()[0];
        System.out.println(size);
        byte[] bytes = source.slice(offset, size).read();
        System.out.println("Flag part: " + bytesString(bytes));
        return new FlagDesign.Color[][]{}; //bytesToMatrix(bytes);
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
        //System.out.println(new FlagDesignRomHandlerNew(new File("iss.sfc")).readPointer(Team.SCOTLAND));
        for (Team team : Team.values()) {
            //System.out.println(new FlagDesignRomHandlerNew(new File("b95.sfc")).readPointer(team));
            System.out.println(new FlagDesignRomHandlerNew(new File("iss.sfc")).readFromRomAt(team));
        }

    }


}
