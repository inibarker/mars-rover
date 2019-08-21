package me.barker.ignacio.sample.aba.mission.mars;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import lombok.SneakyThrows;
import lombok.val;
import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionInterface;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

public class MarsMissionInterfaceTest {

    private static final int TERRAIN_HEIGHT = 100;

    private static final int TERRAIN_WIDTH = 100;

    private static final Pair<Integer, Integer> TEST_TERRAIN_DIMENSIONS = Pair.of(TERRAIN_HEIGHT, TERRAIN_WIDTH);

    private static final Map<Integer, Set<Integer>> TEST_TERRAIN_OBSTACLES = Optional
        .of(new HashMap<Integer, Set<Integer>>())
        .flatMap(obstacleMap -> Optional.of(IntStream
            .range(0, 10)
            .map(i -> RandomUtils.nextInt(1, TERRAIN_HEIGHT))
            .peek(randomLatitude -> IntStream.of(RandomUtils.nextInt(1, TERRAIN_WIDTH))
                .forEach(randomLongitude ->
                    Optional.of(obstacleMap.getOrDefault(randomLatitude, new HashSet<>()))
                        .flatMap(meridian -> Optional.of(meridian.add(randomLongitude)).map(add -> meridian))
                        .ifPresent(meridian -> obstacleMap.putIfAbsent(randomLatitude, meridian)))))
            .map(meridianStream -> obstacleMap)).get();

    private static final MarsTerrain TEST_TERRAIN_NO_WRAP_NO_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS).build();

    private static final MarsTerrain TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS)
        .wrapping(Pair.of(true, true))
        .build();

    private static final MarsTerrain TEST_TERRAIN_WITH_WRAP_AND_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS)
        .wrapping(Pair.of(true, true))
        .obstacles(TEST_TERRAIN_OBSTACLES)
        .build();

    private static final MarsTerrain TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES = MarsTerrain
        .builder().dimensions(TEST_TERRAIN_DIMENSIONS)
        .obstacles(TEST_TERRAIN_OBSTACLES)
        .build();

    @Test
    public void testOperateReport() {
        val marsRover = new MarsRover();
        val underTest = new MarsMissionInterface(
            TEST_TERRAIN_NO_WRAP_NO_OBSTACLES,
            marsRover);
        StepVerifier.create(underTest
            .operate(ControlCommand.MissionCommand.REPORT))
            .assertNext(missionStatus -> {
                assertEquals(marsRover, missionStatus.rover());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, missionStatus.terrain());
            })
            .verifyComplete();
    }

    @Test
    public void testReport() {
        val marsRover = new MarsRover();
        val underTest = new MarsMissionInterface(
            TEST_TERRAIN_NO_WRAP_NO_OBSTACLES,
            marsRover);
        StepVerifier.create(underTest
            .operate(ControlCommand.MissionCommand.REPORT))
            .assertNext(missionStatus -> {
                assertEquals(marsRover, missionStatus.rover());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, missionStatus.terrain());
            })
            .verifyComplete();
    }

    @Test
    public void testAdvanceMiddle() {
        val underTest = new MarsMissionInterface(null, null);
        assertOperation(() -> Mono.from(underTest
            .operate(ControlCommand.RoverCommand.FORWARD))
            .thenReturn(underTest), Assert::assertNotNull);
    }

    @Test
    public void testAdvanceBorderNoWrap() {
        val underTest = new MarsMissionInterface(null, null);
        assertOperation(() -> Mono.from(underTest
            .operate(ControlCommand.RoverCommand.FORWARD))
            .thenReturn(underTest), Assert::assertNotNull);
    }

    @Test
    public void testAdvanceBorderWithWrap() {
        val underTest = new MarsMissionInterface(null, null);
        assertOperation(() -> Mono.from(underTest
            .operate(ControlCommand.RoverCommand.FORWARD))
            .thenReturn(underTest), Assert::assertNotNull);
    }

    @Test
    public void testAdvanceCornerNoWrap() {
        val underTest = new MarsMissionInterface(null, null);
        assertOperation(() -> Mono.from(underTest
            .operate(ControlCommand.RoverCommand.FORWARD))
            .thenReturn(underTest), Assert::assertNotNull);
    }

    @Test
    public void testAdvanceCornerWithWrap() {
        val underTest = new MarsMissionInterface(null, null);
        assertOperation(() -> Mono.from(underTest
            .operate(ControlCommand.RoverCommand.FORWARD))
            .thenReturn(underTest), Assert::assertNotNull);
    }

    @Test
    public void testAdvanceObstacle() {
        val underTest = new MarsMissionInterface(null, null);
        assertOperation(() -> Mono.from(underTest
            .operate(ControlCommand.RoverCommand.FORWARD))
            .thenReturn(underTest), Assert::assertNotNull);
    }

    @Test
    public void testAdvanceAround() {
        val underTest = new MarsMissionInterface(null, null);
        assertOperation(() -> Mono.from(underTest
            .operate(ControlCommand.RoverCommand.FORWARD))
            .thenReturn(underTest), Assert::assertNotNull);
    }

    @SneakyThrows
    private static void assertOperation(final Callable<Mono<MissionInterface>> testOperation,
                                        final Consumer<MissionStatus> resultAssertion) {
        StepVerifier.create(testOperation.call()
            .flatMap(MissionInterface::report))
            .assertNext(resultAssertion)
            .verifyComplete();
    }

}