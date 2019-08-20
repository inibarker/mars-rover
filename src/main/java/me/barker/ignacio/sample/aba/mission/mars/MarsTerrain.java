package me.barker.ignacio.sample.aba.mission.mars;

import java.util.Map;
import java.util.Set;

import lombok.Value;
import me.barker.ignacio.sample.aba.mission.contract.MissionTerrain;
import org.apache.commons.lang3.tuple.Pair;

@Value
public class MarsTerrain implements MissionTerrain {

    Pair<Integer, Integer> dimensions;

    Map<Integer, Set<Integer>> obstacles;

    Pair<Boolean, Boolean> wrapping;

}
