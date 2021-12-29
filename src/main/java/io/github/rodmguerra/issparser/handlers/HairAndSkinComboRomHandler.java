package io.github.rodmguerra.issparser.handlers;

import io.github.rodmguerra.issparser.commons.PlayerRomHandler;
import io.github.rodmguerra.issparser.commons.RomHandler;
import io.github.rodmguerra.issparser.model.HairAndSkinCombo;
import io.github.rodmguerra.issparser.model.SpecialHair;
import io.github.rodmguerra.issparser.model.SpecialSkin;
import io.github.rodmguerra.issparser.model.colors.hairandskin.NormalHairAndSkin;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class HairAndSkinComboRomHandler implements RomHandler<HairAndSkinCombo> {
    private final RomHandler<NormalHairAndSkin> normalHandler;
    private final RomHandler<SpecialHair> specialHairHandler;
    private final RomHandler<SpecialSkin> specialSkinHandler;


    public HairAndSkinComboRomHandler(RomHandler<NormalHairAndSkin> normalHandler, RomHandler<SpecialHair> specialHairHandler, RomHandler<SpecialSkin> specialSkinHandler) {
        this.normalHandler = normalHandler;
        this.specialHairHandler = specialHairHandler;
        this.specialSkinHandler = specialSkinHandler;
    }

    @Override
    public Map<Team, HairAndSkinCombo> readFromRom() throws IOException {
        Map<Team, NormalHairAndSkin> normals = normalHandler.readFromRom();
        Map<Team, SpecialHair> specialHairs = specialHairHandler.readFromRom();
        Map<Team, SpecialSkin> specialSkins = specialSkinHandler.readFromRom();
        Map<Team, HairAndSkinCombo> output = new LinkedHashMap<>();
        for (Team team : Team.values()) {
            output.put(team, new HairAndSkinCombo(normals.get(team), specialHairs.get(team), specialSkins.get(team)));
        }
        return output;
    }

    @Override
    public void writeToRom(Map<Team, ? extends HairAndSkinCombo> input) throws IOException {
        Map<Team, SpecialSkin> specialSkins = input.entrySet().stream().collect(
                toMap(
                        e -> e.getKey(),
                        e -> e.getValue().getSpecialSkin()
                )
        );

        Map<Team, SpecialHair> specialHairs = input.entrySet().stream().collect(
                toMap(
                        e -> e.getKey(),
                        e -> e.getValue().getSpecialHair()
                )
        );

        Map<Team, NormalHairAndSkin> normals = input.entrySet().stream().collect(
                toMap(
                        e -> e.getKey(),
                        e -> e.getValue().getNormal()
                )
        );

        normalHandler.writeToRom(normals);
        specialHairHandler.writeToRom(specialHairs);
        specialSkinHandler.writeToRom(specialSkins);
    }

    @Override
    public HairAndSkinCombo readFromRomAt(Team team) throws IOException {
        SpecialHair specialHair = specialHairHandler.readFromRomAt(team);
        SpecialSkin specialSkin = specialSkinHandler.readFromRomAt(team);
        NormalHairAndSkin normal = normalHandler.readFromRomAt(team);
        return new HairAndSkinCombo(normal, specialHair, specialSkin);
    }

    @Override
    public void writeToRomAt(Team team, HairAndSkinCombo input) throws IOException {
        normalHandler.writeToRomAt(team, input.getNormal());
        specialHairHandler.writeToRomAt(team, input.getSpecialHair());
        specialSkinHandler.writeToRomAt(team, input.getSpecialSkin());
    }
}
