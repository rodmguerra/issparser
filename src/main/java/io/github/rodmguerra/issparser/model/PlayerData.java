package io.github.rodmguerra.issparser.model;

import java.nio.ByteBuffer;
 /*
import static io.github.rodmguerra.issparser.commons.ParsingUtils.bytes;

public class PlayerData {

    private final byte[] data;
    private ShirtNumber shirtNumber;
    private SkillData skills;
    private HairStyle hairStyle;
    private Type type;

    public PlayerData(byte[] innerData) {
        this.data = innerData;
        this.shirtNumber = new ShirtNumber(data[3]);
        this.skills = SkillData.fromBytes(bytes(data, 0, 3));
        this.hairStyle = HairStyle.fromByte(data[5]);
        this.type = Type.fromByte(data[5]);
    }


    private static void assignBytes(byte[] output, byte[] input, int offset, int size) {
        ByteBuffer.wrap(input).get(output, 0, 3);
    }

    private ShirtNumber getShirtNumber() {
        return shirtNumber;
    }


    public SkillData getSkills() {
        return skills;
    }

    public HairStyle getHairStyle() {
        return hairStyle;
    }

    public Type getType() {
        return type;
    }

    public void setShirtNumber(ShirtNumber shirtNumber) {
        this.shirtNumber = shirtNumber;
        //todo set data
    }

    public void setSkills(SkillData skills) {
        this.skills = skills;
        //todo set data

    }

    public void setHairStyle(HairStyle hairStyle) {
        this.hairStyle = hairStyle;
        //todo set data

    }

    public void setType(Type type) {
        this.type = type;
        //todo set data

    }



    public static enum Type {
        NORMAL,
        SPECIAL;

        public static Type fromByte(byte code) {
            return code / 0x40 == 1 ? SPECIAL : NORMAL;
        }
    }
}
 */