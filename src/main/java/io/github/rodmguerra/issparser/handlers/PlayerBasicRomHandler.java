package io.github.rodmguerra.issparser.handlers;

import io.github.rodmguerra.issparser.commons.PlayerRomHandler;
import io.github.rodmguerra.issparser.model.HairStyle;
import io.github.rodmguerra.issparser.model.PlayerBasic;
import io.github.rodmguerra.issparser.model.PlayerColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class PlayerBasicRomHandler implements PlayerRomHandler<PlayerBasic> {
    private final PlayerRomHandler<String> nameHandler;
    private final PlayerRomHandler<Integer> numberHandler;
    private final PlayerRomHandler<PlayerColor> colorHandler;
    private final PlayerRomHandler<HairStyle> hairStyleHandler;

    public PlayerBasicRomHandler(PlayerRomHandler<String> nameHandler, PlayerRomHandler<Integer> numberHandler, PlayerRomHandler<PlayerColor> colorHandler, PlayerRomHandler<HairStyle> hairStyleHandler) {
        this.numberHandler = numberHandler;
        this.nameHandler = nameHandler;
        this.colorHandler = colorHandler;
        this.hairStyleHandler = hairStyleHandler;
    }

    @Override
    public PlayerBasic readFromRomAt(Team team, int playerIndex) throws IOException {
        return new PlayerBasic(
                nameHandler.readFromRomAt(team, playerIndex),
                numberHandler.readFromRomAt(team, playerIndex),
                colorHandler.readFromRomAt(team, playerIndex),
                hairStyleHandler.readFromRomAt(team, playerIndex));
    }

    @Override
    public Map<Team, Iterable<PlayerBasic>> readFromRom() throws IOException {
        Map<Team, Iterable<String>> names = nameHandler.readFromRom();
        Map<Team, Iterable<Integer>> numbers = numberHandler.readFromRom();
        Map<Team, Iterable<PlayerColor>> colors = colorHandler.readFromRom();
        Map<Team, Iterable<HairStyle>> hairStyles = hairStyleHandler.readFromRom();
        Map<Team, Iterable<PlayerBasic>> output = new LinkedHashMap<>();
        for (Team team : Team.values()) {
            List<PlayerBasic> players = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                players.add(new PlayerBasic(
                        get(names.get(team), i),
                        get(numbers.get(team), i),
                        get(colors.get(team), i),
                        get(hairStyles.get(team), i)
                )
                );
            }
            output.put(team, players);
        }
        return output;
    }

    @Override
    public void writeToRom(Map<Team, ? extends Iterable<PlayerBasic>> input) throws IOException {
        Map<Team, Iterable<String>> names = input.entrySet().stream().collect(
                toMap(
                        e -> e.getKey(),
                        e -> (Iterable<String>) newArrayList(e.getValue()).stream().map(PlayerBasic::getName).collect(toList())
                ));

        Map<Team, Iterable<Integer>> numbers = input.entrySet().stream().collect(
                toMap(
                        e -> e.getKey(),
                        e -> (Iterable<Integer>) newArrayList(e.getValue()).stream().map(PlayerBasic::getNumber).collect(toList())
                ));

        Map<Team, Iterable<PlayerColor>> colors = input.entrySet().stream().collect(
                toMap(
                        e -> e.getKey(),
                        e -> (Iterable<PlayerColor>) newArrayList(e.getValue()).stream().map(PlayerBasic::getColor).collect(toList())
                ));

        Map<Team, Iterable<HairStyle>> hairStyles = input.entrySet().stream().collect(
                toMap(
                        e -> e.getKey(),
                        e -> (Iterable<HairStyle>) newArrayList(e.getValue()).stream().map(PlayerBasic::getHairStyle).collect(toList())
                ));

        nameHandler.writeToRom(names);
        numberHandler.writeToRom(numbers);
        colorHandler.writeToRom(colors);
        hairStyleHandler.writeToRom(hairStyles);
    }

    @Override
    public Iterable<PlayerBasic> readFromRomAt(Team team) throws IOException {
        List<PlayerBasic> players = new ArrayList<>(15);
        Iterable<String> names = nameHandler.readFromRomAt(team);
        Iterable<Integer> numbers = numberHandler.readFromRomAt(team);
        Iterable<HairStyle> hairStyles = hairStyleHandler.readFromRomAt(team);
        Iterable<PlayerColor> colors = colorHandler.readFromRomAt(team);
        for (int i = 0; i < 15; i++) {
            players.add(new PlayerBasic(get(names, i), get(numbers, i), get(colors, i), get(hairStyles, i)));
        }
        return players;
    }

    @Override
    public void writeToRomAt(Team team, Iterable<PlayerBasic> list) throws IOException {
        List<String> names = newArrayList(list).stream().map(PlayerBasic::getName).collect(toList());
        List<Integer> numbers = newArrayList(list).stream().map(PlayerBasic::getNumber).collect(toList());
        List<PlayerColor> colors = newArrayList(list).stream().map(PlayerBasic::getColor).collect(toList());
        List<HairStyle> hairStyles = newArrayList(list).stream().map(PlayerBasic::getHairStyle).collect(toList());
        nameHandler.writeToRomAt(team, names);
        numberHandler.writeToRomAt(team, numbers);
        colorHandler.writeToRomAt(team, colors);
        hairStyleHandler.writeToRomAt(team, hairStyles);
    }

    @Override
    public void writeToRomAt(Team team, int playerIndex, PlayerBasic player) throws IOException {
        nameHandler.writeToRomAt(team, playerIndex, player.getName());
        numberHandler.writeToRomAt(team, playerIndex, player.getNumber());
        colorHandler.writeToRomAt(team, playerIndex, player.getColor());
        hairStyleHandler.writeToRomAt(team, playerIndex, player.getHairStyle());
    }


}
