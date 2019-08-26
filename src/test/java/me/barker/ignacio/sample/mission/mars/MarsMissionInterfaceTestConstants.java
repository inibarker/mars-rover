package me.barker.ignacio.sample.mission.mars;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

public interface MarsMissionInterfaceTestConstants {

    int TERRAIN_HEIGHT = 10;

    int TERRAIN_MIDDLE_HEIGHT = 5;

    int TERRAIN_WIDTH = 10;

    int TERRAIN_MIDDLE_WIDTH = 5;

    Pair<Integer, Integer> TEST_TERRAIN_DIMENSIONS = Pair.of(TERRAIN_HEIGHT, TERRAIN_WIDTH);

    Map<Integer, Set<Integer>> TEST_TERRAIN_OBSTACLES = Optional
        .of(new HashMap<Integer, Set<Integer>>())
        .flatMap(obstacleMap -> Optional.of(IntStream
            .range(0, TERRAIN_HEIGHT)
            .map(i -> RandomUtils.nextInt(0, TERRAIN_HEIGHT))
            .peek(randomLatitude -> IntStream.of(RandomUtils.nextInt(0, TERRAIN_WIDTH))
                .forEach(randomLongitude ->
                    Optional.of(obstacleMap.getOrDefault(randomLatitude, new HashSet<>()))
                        .flatMap(meridian -> Optional.of(meridian.add(randomLongitude)).map(add -> meridian))
                        .ifPresent(meridian -> obstacleMap.putIfAbsent(randomLatitude, meridian)))))
            .map(meridianStream -> obstacleMap)).get();

    MarsTerrain TEST_TERRAIN_NO_WRAP_NO_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS).build();

    MarsTerrain TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS)
        .wrapping(Pair.of(true, true))
        .build();

    MarsTerrain TEST_TERRAIN_WITH_WRAP_AND_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS)
        .wrapping(Pair.of(true, true))
        .obstacles(TEST_TERRAIN_OBSTACLES)
        .build();

    MarsTerrain TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS)
        .obstacles(TEST_TERRAIN_OBSTACLES)
        .build();
}
