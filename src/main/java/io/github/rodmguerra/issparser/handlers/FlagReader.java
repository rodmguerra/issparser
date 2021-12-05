package io.github.rodmguerra.issparser.handlers;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.github.rodmguerra.issparser.commons.ParsingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

/**
 * Created with IntelliJ IDEA.
 * User: rodmg
 * Date: 02/12/21
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class FlagReader {


    public static void main(String[] args) throws IOException, InterruptedException {
        int offset = 0x48000;
        System.out.println(Integer.toHexString(offset));


        for (int i = 0; i < 27; i++) {
            //if(i== 19)  {

            System.out.println("");
            System.out.println(i);
            byte[] bytes = Files.asByteSource(new File("iss.sfc")).slice(0x941a + 4 * i, 2).read();
            //System.out.println(ParsingUtils.bytesString(bytes));
            ByteBuffer buffer = ByteBuffer.wrap(new byte[]{0, 0x4, bytes[1], bytes[0]});
            flagPart(buffer.getInt());


                byte[] bytes2 = Files.asByteSource(new File("iss.sfc")).slice(0x941a + 4 * i + 2, 2).read();
                ByteBuffer buffer2 = ByteBuffer.wrap(new byte[]{0, 0x4, bytes2[1], bytes2[0]});
            flagPart(buffer2.getInt());

            //}
        }


    }

    private static void flagPart(int offset) throws IOException, InterruptedException {
        String command = "konami\\konami_d \"C:\\rodmguerra\\Projetos\\issparser\\iss.sfc\" 0x" + Integer.toHexString(offset) + " 4";

        //System.out.println(command);
        Process process = Runtime.getRuntime().exec(command, null);
        if (process.waitFor() == 0) {
            //for(int p=0; p<3; p++) {
            byte[] bytes = Files.asByteSource(new File("decomp.bin")).read();
            //System.out.println(ParsingUtils.bytesString(bytes));
            byte[][] matrix = bytesToMatrix(bytes);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    String s = Integer.toHexString(matrix[i][j]);
                    String output = "";
                    if (s.equals("0")) output = " ";
                    else if (s.equals("1")) output = "*";
                    else if (s.equals("2")) output = "@";
                    else if (s.equals("3")) output = "#";
                    System.out.print(output + " ");
                }
                System.out.println("");
            }
            // }

        }
    }

    /*
    private static byte[][] bytesToMatrix(byte[] bytes) {
        byte[][] matrix = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            boolean[] b1 = bitArray(bytes[2 * i]);
            boolean[] b2 = bitArray(bytes[2 * i + 1]);
            boolean[] b3 = bitArray(bytes[2 * i + 16]);
            boolean[] b4 = bitArray(bytes[2 * i + 16 + 1]);
            for (int j = 0; j < 8; j++) {
                System.out.println();
                matrix[i][j] = byteOf(new boolean[]{b1[j], b2[j]});
            }
        }
        return matrix;

    } */

    private static byte[][] bytesToMatrix(byte[] bytes) {
        // System.out.println(bytes.length);
        byte[][] matrix = new byte[8][bytes.length / 4];
        for (int b = 0; b < bytes.length / 4; b++) {
            int i = 32 * (b / 8) + (b % 8) * 2;
            //System.out.println("" + b + ") " + Integer.toHexString(i));

            boolean[] b1 = bitArray(bytes[i]);
            boolean[] b2 = bitArray(bytes[i + 1]);
            boolean[] b3 = bitArray(bytes[i + 16]);
            boolean[] b4 = bitArray(bytes[i + 16 + 1]);

            for (int j = 0; j < 8; j++) {
                //System.out.println(b/2);
                int row = b % 8;
                int col = b / 8 * 8 + j;
                //System.out.println(row);
                //System.out.println(col);
                //System.out.println("");
                matrix[row][col] = byteOf(new boolean[]{b1[j], b2[j]});
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
}
