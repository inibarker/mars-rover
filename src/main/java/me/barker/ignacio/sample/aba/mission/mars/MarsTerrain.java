package me.barker.ignacio.sample.aba.mission.mars;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import me.barker.ignacio.sample.aba.mission.contract.MissionTerrain;
import org.apache.commons.lang3.tuple.Pair;

@Value
@Builder
public class MarsTerrain implements MissionTerrain {

    @NonNull
    @Builder.Default
    Pair<Integer, Integer> dimensions = Pair.of(1, 1);

    @NonNull
    @Builder.Default
    Pair<Boolean, Boolean> wrapping = Pair.of(false, false);

    @NonNull
    @Builder.Default
    Map<Integer, Set<Integer>> obstacles = new HashMap<>();

}
