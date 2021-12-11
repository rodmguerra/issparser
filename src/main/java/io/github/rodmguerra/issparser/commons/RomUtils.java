package io.github.rodmguerra.issparser.commons;

import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: rodmg
 * Date: 11/12/21
 * Time: 03:15
 * To change this template use File | Settings | File Templates.
 */
public class RomUtils {

    public static int readPointer(File rom, long pointerOffset) throws IOException {
        byte[] bytes = Files.asByteSource(rom).slice(pointerOffset, 2).read();
        return addressFromSnes(bytes[0], bytes[1]);
    }

    public static int addressFromSnes(byte pointerByte1, byte pointerByte2) {
        return ByteBuffer.wrap(new byte[]{0, 0x4, pointerByte2, pointerByte1}).getInt();
    }

    public static byte[] addressToSnes(int address) {
        if(address >= 0x40000 && address < 0x50000) {
            address = address % 0x40000;
            return new byte[]{(byte) (address % 0x100), (byte) (address / 0x100)};
        }

        if(address >= 0x17000 && address < 0x18000){
            address -= 0x8000; //17600 => F600
            return new byte[]{(byte) (address % 0x100), (byte) (address / 0x100)};
        }

        throw new IllegalArgumentException("Invalid address: " + Integer.toHexString(address));

    }

    public static void writeAdressToPointer(File rom, long pointerOffset, int newAdress) throws IOException {
        FileUtils.writeToPosition(rom, addressToSnes(newAdress), pointerOffset);
    }


    public static void displaceTeamNameTilesIfNecessary(File rom) throws IOException {
        System.out.println("Displace Team Name Tiles If Necessary...");
        byte[] regionSetter = new byte[] {(byte)0x82, (byte)0xE0, (byte)0x50, (byte)0xDA, (byte)0x84, (byte)0x82, (byte) 0xFF};
        int regionSetterOffset = 0x93c6;
        byte[] currentSetter = Files.asByteSource(rom).slice(regionSetterOffset, regionSetter.length).read();
        int newAddress = 0x17680;
        if(!Arrays.equals(currentSetter, regionSetter)) {
            System.out.println("Displacing team name tiles is necessary");
            FileUtils.writeToPosition(rom,regionSetter,regionSetterOffset);
            int pointerOffset = 0x93CD;
            for (int i = 0; i < RomHandler.Team.values().length; i++) {
                int originalAddress = readPointer(rom, pointerOffset);
                int size = Files.asByteSource(rom).slice(originalAddress, 1).read()[0];
                byte[] data = Files.asByteSource(rom).slice(originalAddress, size).read();
                FileUtils.writeToPosition(rom, new byte[size], originalAddress);
                FileUtils.writeToPosition(rom, data, newAddress);
                FileUtils.writeToPosition(rom, addressToSnes(newAddress), pointerOffset);
                pointerOffset += 2;
                newAddress += size;
            }
            System.out.println("Displace team name tiles done");
        } else System.out.println("Setting tiles is not necessary");

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
