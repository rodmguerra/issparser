/*package io.github.rodmguerra.issparser.model;


public class SkillData {
    private int speed;
    private int shoot;
    private int technique;
    private int stamina;

    public SkillData(int speed, int shoot, int technique, int stamina) {
        this.speed = speed;
        this.shoot = shoot;
        this.technique = technique;
        this.stamina = stamina;
    }

    public static SkillData fromBytes(byte[] data) {
        int speed = speedFromByte(data[0]);
        int shoot = shootFromByte(data[1]);
        int technique = techniqueFromByte(data[2]);
        int stamina = staminaFromByte(data[2]);
    }

    private static int staminaFromByte(byte b) {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    private static int techniqueFromByte(byte b) {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    private static int shootFromByte(byte b) {
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }

    private static int speedFromByte(byte b) {
        if (b % 0x20 == 0) return b / 0x20 + 1;
        return ((int) b + 1) / 0x20 + 8 ;
    }

    public byte[] serialize() {
        return new byte[0];  //To change body of created methods use File | Settings | File Templates.
    }
}
  */