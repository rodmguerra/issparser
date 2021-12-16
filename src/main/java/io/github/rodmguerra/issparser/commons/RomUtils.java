package io.github.rodmguerra.issparser.commons;

import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RomUtils {


    //public static final byte[] TEAM_NAME_TILES_REGION_SETTER = new byte[]{(byte) 0x82, (byte) 0xE0, (byte) 0x50, (byte) 0xDA, (byte) 0x84, (byte) 0x82, (byte) 0xFF};
    //public static final int TEAM_NAME_TILES_REGION_SETTER_OFFSET = 0x93c6;
    public static final int[] TEAM_NAME_TILES_DUMP_OFFSETS = {
            0x93C6,
            0x93CB,
            0x3A7EB,
            0x3A7F0,
            0x3A7F5,
            0x3A7FA,
            0x3A7FF,
            0x3A804,
            0x3A809,
            0x3A80E
    };
    public static final byte P17000_INDEX_BYTE = (byte)0x82;
    //93c6 93CB
    // 3a7eb 3a7f0

    public enum PointerFormat {
        P48000,
        P40000,
        P17000
    }

    public static int readPointer48(File rom, long pointerOffset) throws IOException {
        return readPointer(rom, pointerOffset, PointerFormat.P48000);
    }

    public static int readPointer17(File rom, long pointerOffset) throws IOException {
        return readPointer(rom, pointerOffset, PointerFormat.P17000);
    }

    public static int readPointer40(File rom, long pointerOffset) throws IOException {
        return readPointer(rom, pointerOffset, PointerFormat.P40000);
    }

    public static int readPointer(File rom, long pointerOffset, PointerFormat format) throws IOException {
        byte[] bytes = Files.asByteSource(rom).slice(pointerOffset, 2).read();
        return addressFromSnes(bytes[0], bytes[1], format);
    }


    public static int addressFromSnes48(byte pointerByte1, byte pointerByte2) {
        return ByteBuffer.wrap(new byte[]{0, 0x4, pointerByte2, pointerByte1}).getInt();
    }

    public static int addressFromSnes40(byte pointerByte1, byte pointerByte2) {
        return ByteBuffer.wrap(new byte[]{0, 0x4, (byte)(pointerByte2 - 0x80), pointerByte1}).getInt();
    }

    public static int addressFromSnes17(byte pointerByte1, byte pointerByte2) {
        return ByteBuffer.wrap(new byte[]{0, 0x1, (byte)(pointerByte2 - 0x80), pointerByte1}).getInt();
    }

    public static int addressFromSnes(byte pointerByte1, byte pointerByte2, PointerFormat format) {
        switch (format) {
            case P48000: return addressFromSnes48(pointerByte1, pointerByte2);
            case P40000: return addressFromSnes40(pointerByte1, pointerByte2);
            case P17000: return addressFromSnes17(pointerByte1, pointerByte2);
        }
        throw new IllegalArgumentException("Pointer format" + format);
    }

    public static byte[] addressToSnes(int address) {
        if(address >= 0x48000 && address < 0x50000) {
            address = address % 0x40000;
            return new byte[]{(byte) (address % 0x100), (byte) (address / 0x100)};
        }

        if(address >= 0x40000 && address < 0x48000){
            address -= 0x38000;
            return new byte[]{(byte) (address % 0x100), (byte) (address / 0x100)};
        }

        if(address >= 0x17000 && address < 0x18000){
            address -= 0x8000; //17yyyyy600 => F600
            return new byte[]{(byte) (address % 0x100), (byte) (address / 0x100)};
        }

        throw new IllegalArgumentException("Invalid address: " + Integer.toHexString(address));

    }

    public static void writeAdressToPointer(File rom, long pointerOffset, int newAdress) throws IOException {
        FileUtils.writeToPosition(rom, addressToSnes(newAdress), pointerOffset);
    }

    public static PointerFormat readTeamNameTilesPointerFormat(File rom) throws IOException {
        return readByteAt(rom, TEAM_NAME_TILES_DUMP_OFFSETS[0]) == P17000_INDEX_BYTE ? PointerFormat.P17000 : PointerFormat.P48000;
    }

    private static byte readByteAt(File rom, int offset) throws IOException {
        return Files.asByteSource(rom).slice(offset,1).read()[0];
    }

    public static void displaceTeamNameTilesIfNecessary(File rom) throws IOException {
        System.out.println("Displace Team Name Tiles If Necessary...");
        int newAddress = 0x17680;
        if(readTeamNameTilesPointerFormat(rom) != PointerFormat.P17000) {
            int pointerOffset = 0x93CD;
            for (int i = 0; i < RomHandler.Team.values().length; i++) {
                int originalAddress = readPointer48(rom, pointerOffset);
                int size = Files.asByteSource(rom).slice(originalAddress, 1).read()[0];
                byte[] data = Files.asByteSource(rom).slice(originalAddress, size).read();
                FileUtils.writeToPosition(rom, new byte[size], originalAddress);
                FileUtils.writeToPosition(rom, data, newAddress);
                FileUtils.writeToPosition(rom, addressToSnes(newAddress), pointerOffset);
                pointerOffset += 2;
                newAddress += size;
            }
            System.out.println("Displace team name tiles done");
        } else System.out.println("Displace team name tiles is not necessary");

        for (int offset : TEAM_NAME_TILES_DUMP_OFFSETS) {
            if(readByteAt(rom,offset) != P17000_INDEX_BYTE) {
                System.out.println("Displace pointer of team name tiles is necessary for offset " + offset);
                FileUtils.writeToPosition(rom, new byte[]{P17000_INDEX_BYTE}, offset);
            }
        }
    }

    public static void fixSharedPointersIfNecessary(File rom) throws IOException {
        System.out.println("Fix Shared Pointers If Necessary...");
        int offset1 = 0x39D78;
        byte[] data = Resources.asByteSource(Resources.getResource("39D78")).read();
        byte[] currentData = Files.asByteSource(rom).slice(offset1, data.length).read();
        if (!Arrays.equals(currentData, data)) {
            System.out.println("Fixing shared pointes at " + Integer.toHexString(offset1));
            FileUtils.writeToPosition(rom, data, offset1);
        } else System.out.println("Not necessary to fix shared pointer at " + Integer.toHexString(offset1));

        int offset2 = 0x444EB;
        data = Resources.asByteSource(Resources.getResource("444EB")).read();
        currentData = Files.asByteSource(rom).slice(offset2, data.length).read();
        if (!Arrays.equals(currentData, data)) {
            System.out.println("Fixing shared pointes at " + Integer.toHexString(offset1));
            FileUtils.writeToPosition(rom, data, offset2);
        } else System.out.println("Not necessary to fix shared pointer at " + Integer.toHexString(offset2));
    }

}
