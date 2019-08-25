package me.barker.ignacio.sample.aba.mission.mars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Value;
import lombok.val;
import me.barker.ignacio.sample.aba.mission.contract.CardinalDirection;
import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarsMissionInterfaceTest {

    private static final int TERRAIN_HEIGHT = 10;

    private static final int TERRAIN_MIDDLE_HEIGHT = 5;

    private static final int TERRAIN_WIDTH = 10;

    private static final int TERRAIN_MIDDLE_WIDTH = 5;

    private static final Pair<Integer, Integer> TEST_TERRAIN_DIMENSIONS = Pair.of(TERRAIN_HEIGHT, TERRAIN_WIDTH);

    private static final Map<Integer, Set<Integer>> TEST_TERRAIN_OBSTACLES = Optional
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

    @Mock
    private MarsMissionContextProperties marsMissionContextProperties;

    private MarsMissionInterface underTest;

    private MarsRover marsRoverTest;

    @Before
    public void setUp() {
        marsRoverTest = MarsRover.builder().build();
        underTest = new MarsMissionInterface(marsMissionContextProperties);

        when(marsMissionContextProperties.extractRoverData())
            .thenReturn(marsRoverTest);
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);

        underTest.setUp();
    }

    @Test
    public void testReport() {
        StepVerifier.create(underTest.report())
            .assertNext(missionStatus -> {
                assertEquals(marsRoverTest, missionStatus.rover());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, missionStatus.terrain());
            }).verifyComplete();
    }

    @Test
    public void testOperateReport() {
        StepVerifier.create(underTest
            .operate(ControlCommand.MissionCommand.REPORT))
            .assertNext(missionStatus -> {
                assertEquals(marsRoverTest, missionStatus.rover());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, missionStatus.terrain());
            })
            .verifyComplete();
    }

    @Test
    public void testAdvanceRandomlyWithWrapNoObstacles() {
        marsRoverTest.setPosition(Pair.of(TERRAIN_MIDDLE_HEIGHT, TERRAIN_MIDDLE_WIDTH));
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES);
        underTest.setUp();

        val commandAmount = 10;
        StepVerifier.create(Flux.fromStream(IntStream.range(0, commandAmount)
            .mapToObj(i -> ControlCommand.RoverCommand.values())
            .map(roverCommands -> roverCommands[RandomUtils.nextInt(0, roverCommands.length)]))
            .flatMap(randomCommand -> underTest.report()
            .map(MissionStatus::rover).cast(MarsRover.class)
            .map(MarsRover::toBuilder).map(MarsRover.MarsRoverBuilder::build)
            .flatMapMany(previousRover -> underTest.operate(randomCommand)
            .map(report -> OperationReport.builder()
                .commandExecuted(randomCommand)
                .previousRover(previousRover)
                .missionReport(report).build()))))
            .recordWith(ArrayList::new)
            .consumeRecordedWith(reportList -> {
                assertEquals(commandAmount, reportList.size());
                reportList.forEach(report -> {
                    assertEquals(report.getCommandExecuted(), report.getMissionReport().lastCommand());
                    assertFreeMovement(report);
                    assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.getMissionReport().terrain());
                });
            }).verifyComplete();
    }

    @Value
    @Builder
    private static class OperationReport {
        ControlCommand commandExecuted;
        MarsRover previousRover;
        MissionStatus missionReport;
    }

    private void assertFreeMovement(final OperationReport operationReport)
        throws AssertionError {

        if (ControlCommand.RoverCommand.LEFT.equals(operationReport.getCommandExecuted())) {
            assertEquals(operationReport.getPreviousRover().getPosition(),
                operationReport.getMissionReport().rover().getPosition());
            assertEquals(operationReport.getPreviousRover().getFacing().rotateAnticlockwise(),
                operationReport.getMissionReport().rover().getFacing());
        } else if (ControlCommand.RoverCommand.RIGHT.equals(operationReport.getCommandExecuted())) {
            assertEquals(operationReport.getPreviousRover().getPosition(),
                operationReport.getMissionReport().rover().getPosition());
            assertEquals(operationReport.getPreviousRover().getFacing().rotateClockwise(),
                operationReport.getMissionReport().rover().getFacing());
        } else {
            assertEquals(operationReport.getPreviousRover().getFacing(),
                operationReport.getMissionReport().rover().getFacing());
            val testRover = operationReport.getPreviousRover().toBuilder().build();
            testRover.move(ControlCommand.RoverCommand
                .BACKWARD.equals(operationReport.getCommandExecuted()));
            assertEquals(testRover.getPosition(),
                operationReport.getMissionReport().rover().getPosition());
        }
    }

    @Test
    public void testAdvanceBorderNoWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.FORWARD)
            .repeat(TERRAIN_HEIGHT).last())
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.FORWARD, report.lastCommand());
                assertEquals(Pair.of(TERRAIN_HEIGHT, 0), report.rover().getPosition());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceBorderWithWrap() {
        marsRoverTest.setPosition(Pair.of(1, TERRAIN_WIDTH));
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.FORWARD)
            .repeat(TERRAIN_MIDDLE_HEIGHT + 1).last())
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.FORWARD, report.lastCommand());
                assertEquals(Pair.of(2, TERRAIN_WIDTH), report.rover().getPosition());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.terrain());
            });
    }

    @Test
    public void testAdvanceCornerNoWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        marsRoverTest.setFacing(CardinalDirection.SOUTH);
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.FORWARD)
            .then(underTest.operate(ControlCommand.RoverCommand.RIGHT)
            .then(underTest.operate(ControlCommand.RoverCommand.FORWARD))))
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.FORWARD, report.lastCommand());
                assertEquals(CardinalDirection.WEST, report.rover().getFacing());
                assertEquals(Pair.of(0, 0), report.rover().getPosition());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceCornerWithWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        marsRoverTest.setFacing(CardinalDirection.SOUTH);
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.FORWARD)
            .then(underTest.operate(ControlCommand.RoverCommand.RIGHT)
                .then(underTest.operate(ControlCommand.RoverCommand.FORWARD))))
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.FORWARD, report.lastCommand());
                assertEquals(CardinalDirection.WEST, report.rover().getFacing());
                assertEquals(Pair.of(TERRAIN_HEIGHT, TERRAIN_WIDTH), report.rover().getPosition());
                assertEquals(TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceOverObstacle() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES);
        underTest.setUp();
    }

    @Test
    public void testAdvanceOverObstacleAfterWrap() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_WITH_WRAP_AND_OBSTACLES);
        underTest.setUp();
    }

    @Test
    public void testAdvanceAroundObstacle() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES);
        underTest.setUp();
    }

    @Test
    public void testAdvanceAroundObstacleCrossingWrap() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES);
        underTest.setUp();
    }

}