package io.github.rodmguerra.issparser.handlers.texts;


/* Não está funcionando ainda rom apresenta várias idiossincrasias */
/*
public class TeamNameRomHandler implements RomHandler<String> {

    private static final int A = 0xc0;
    private static final int Q = 0xe0;
    private static final int PONTO = 0xea;
    private static final long[] TEAM_OFFSETS = {
            0x4402A,
            0x44063,
            0x4408C,
            0x440C5,
            0x441B6,
            0x43FE9,
            0x43F4E,
            0x441EF,
            0x43F77,
            0x44158,
            0x4440D,
            0x4417D,
            0x440EE,
            0x43FB0,
            0x443E4,
            0x442DB,
            0x43ED8,
            0x44127,
            0x44292,
            0x44261,
            0x44220,
            0x44314,
            0x443BF,
            0x44345,
            0x4437E,
            0x43F17,
    };
    public static final int BYTE_LENGH = 64;
    public static final int MAX_CHARS = 16;

    private final String rom;

    public TeamNameRomHandler(String rom) {
        this.rom = rom;
    }

    @Override
    public Iterable<String> readFromRom() throws IOException {
        File file = new File(rom);
        List<String> teamNames = new ArrayList<>();
        for (long teamOffset : TEAM_OFFSETS) {
            teamNames.add(teamName(file, teamOffset));
        }

        System.out.println(teamNames);
        return teamNames;
    }

    private String teamName(File file, long offset) throws IOException {
        ByteSource source = Files.asByteSource(file).slice(offset, BYTE_LENGH);
        byte[] allBytes = source.read();
        byte[] relevantBytes = new byte[allBytes.length/4];
        for (int i=0; i<allBytes.length; i++) {
            if(i%4 == 0) relevantBytes[relevantBytes.length - (i/4) -1] = allBytes[i];
        }
        System.out.println(ParsingUtils.bytesString(relevantBytes));
        String teamName = teamNameText(relevantBytes);
        //System.out.println(teamName);
        return teamName;
    }

    @Override
    public void writeToRom(Iterable<String> input) throws IOException {
        String teamName = "AAAAAAAA";

        for (long teamOffset : TEAM_OFFSETS) {
            byte[] relevantBytes = teamNameRelevantBytes(teamName);
            System.out.println(ParsingUtils.bytesString(relevantBytes));
            for (int i=0; i<relevantBytes.length; i++) {
                //io.github.rodmguerra.issparser.commons.FileUtils.writeToPosition(rom, new byte[]{relevantBytes[relevantBytes.length-1-i]}, teamOffset + (4 * i));
            }
        }

    }

    @Override
    public String readFromRomAt(int index) throws IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void writeToRomAt(int index, String input) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private byte[] teamNameRelevantBytes(String teamName) {
        byte[] bytes = new byte[MAX_CHARS];
        for (int i=0; i<teamName.length(); i++) {
            char c = teamName.charAt(i);
            bytes[i*2] = charToLowerByte(c);
            bytes[i*2+1] = charToUpperByte(c);

        }
        return bytes;
    }




    private static byte charToLowerByte(char c) {
        if (c == '.') return (byte) PONTO;
        else if (c >= 'Q') return (byte) (c - 'Q' + Q);
        return (byte) (c - 'A' + A);
    }

    private byte charToUpperByte(char c) {
        if (c == '.') return (byte) (PONTO + 0x10);
        else if (c >= 'Q') return (byte) (c - 'Q' + Q + 0x10);
        return (byte) (c - 'A' + A + 0x10);
    }

     //C6 D6 C4 D4 D1 E1 CC DC C0 D0 CD DD D8 E8 00 00 00 00
    private static char lowerByteToChar(byte b) {
        if(ParsingUtils.unsigned(b) == PONTO) return '.';
        if(ParsingUtils.unsigned(b) >= Q) return (char) (ParsingUtils.unsigned(b) - Q + 'Q');
        return (char) (ParsingUtils.unsigned(b) - A + 'A');
    }

    public static String teamNameText(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<bytes.length; i++) {

            if(i%2 == 0) {
                //System.out.println(io.github.rodmguerra.issparser.commons.ParsingUtils.bytesString(new byte[]{bytes[i]}));
                sb.append(lowerByteToChar(bytes[i]));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        TeamNameRomHandler h = new TeamNameRomHandler("isse.sfc");
        h.readFromRom();

        h.writeToRom(null);
    }
}
   */