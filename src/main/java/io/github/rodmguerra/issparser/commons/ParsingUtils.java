package io.github.rodmguerra.issparser.commons;

import com.google.common.base.Strings;

import java.text.Normalizer;

public class ParsingUtils {

    public static void printPlayerNames(String[] playerNames) {
        for(int i=0; i<playerNames.length; i++) {
            if(i%15 == 0) System.out.println("\n" + i/15);
            System.out.println(playerNames[i]);
        }
    }

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public static byte[] issBytes(CharSequence string) {
        byte[] bytes = new byte[string.length()];
        for (int i =0; i<string.length(); i++) {
            bytes[i] = charToIss(string.charAt(i));
        }
        return bytes;
    }

    public static String bytesString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    public static String issText(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(issChar(b));
        }
        return sb.toString();
    }

    public static int unsigned(byte b) {
        return b & 0xFF;
    }

    public static char issChar(byte b) {
        if(unsigned(b) >= 0x6c && unsigned(b) <= 0x85) return (char) (unsigned(b) - 0x6c + 'A');
        if(unsigned(b) >= 0x86 && unsigned(b) <= 0x9f) return (char) (unsigned(b) - 0x86 + 'a');
        if(unsigned(b) >= 0x62 && unsigned(b) <= 0x6B) return (char) (unsigned(b) - 0x62 + '0');
        if(unsigned(b) == 0x00) return ' ';
        if(unsigned(b) == 0x54) return '.';
        if(unsigned(b) == 0x56) return '\"';
        if(unsigned(b) == 0x5C) return '\'';
        if(unsigned(b) == 0x5F) return '/';
        return  '#';
    }

    public static byte charToIss(char c) {
        if(c >= 'A' && c <= 'Z') return (byte) (c - 'A' + 0x6c);
        if(c >= 'a' && c <= 'z') return (byte) (c - 'a' + 0x86);
        if(c >= '0' && c <= '9') return (byte) (c - '0' + 0x62);
        if(c == '.') return (byte) 0x54;
        if(c == '\"') return (byte) 0x56;
        if(c == '-') return (byte) 0x53;
        if(c == '\'') return (byte) 0x5C;
        if(c == '/') return (byte) 0x5F;
        return (byte) 0;
    }

    public static CharSequence cutAndCenter(String teamPlayer, int size) {
        StringBuilder sb = new StringBuilder();
        String name = teamPlayer.trim();
        if(name.length() > size) name = name.substring(0, 8);
        int dummyCount = (size - name.length()) / 2;
        int endDummyCount = size - name.length() - dummyCount;
        sb.append(Strings.repeat(" ", dummyCount));
        sb.append(name);
        sb.append(Strings.repeat(" ", endDummyCount));
        return sb;
    }
}
