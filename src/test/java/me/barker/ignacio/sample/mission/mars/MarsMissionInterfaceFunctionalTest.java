package me.barker.ignacio.sample.mission.mars;

import me.barker.ignacio.sample.mission.contract.CardinalDirection;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarsMissionInterfaceFunctionalTest {

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
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);

        underTest.setUp();
    }

    @Test
    public void testReport() {
        StepVerifier.create(underTest.report())
            .assertNext(missionStatus -> {
                assertEquals(marsRoverTest, missionStatus.rover());
                assertEquals(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, missionStatus.terrain());
            }).verifyComplete();
    }

    @Test
    public void testOperateReport() {
        StepVerifier.create(underTest
            .operate(ControlCommand.MissionCommand.REPORT))
            .assertNext(missionStatus -> {
                assertEquals(marsRoverTest, missionStatus.rover());
                assertEquals(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, missionStatus.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceBorderNoWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.MOVE_FORWARDS)
            .repeat(MarsMissionInterfaceTestConstants.TERRAIN_HEIGHT).last())
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.MOVE_FORWARDS, report.lastCommand());
                assertEquals(Pair.of(MarsMissionInterfaceTestConstants.TERRAIN_HEIGHT, 0), report.rover().getPosition());
                assertEquals(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceBorderWithWrap() {
        marsRoverTest.setPosition(Pair.of(1, MarsMissionInterfaceTestConstants.TERRAIN_WIDTH));
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.MOVE_FORWARDS)
            .repeat(MarsMissionInterfaceTestConstants.TERRAIN_MIDDLE_HEIGHT + 1).last())
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.MOVE_FORWARDS, report.lastCommand());
                assertEquals(Pair.of(2, MarsMissionInterfaceTestConstants.TERRAIN_WIDTH), report.rover().getPosition());
                assertEquals(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceCornerNoWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        marsRoverTest.setFacing(CardinalDirection.SOUTH);
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.MOVE_FORWARDS)
            .then(underTest.operate(ControlCommand.RoverCommand.TURN_RIGHT)
            .then(underTest.operate(ControlCommand.RoverCommand.MOVE_FORWARDS))))
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.MOVE_FORWARDS, report.lastCommand());
                assertEquals(CardinalDirection.WEST, report.rover().getFacing());
                assertEquals(Pair.of(0, 0), report.rover().getPosition());
                assertEquals(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceCornerWithWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        marsRoverTest.setFacing(CardinalDirection.SOUTH);
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.RoverCommand.MOVE_FORWARDS)
            .then(underTest.operate(ControlCommand.RoverCommand.TURN_RIGHT)
                .then(underTest.operate(ControlCommand.RoverCommand.MOVE_FORWARDS))))
            .assertNext(report -> {
                assertEquals(ControlCommand.RoverCommand.MOVE_FORWARDS, report.lastCommand());
                assertEquals(CardinalDirection.WEST, report.rover().getFacing());
                assertEquals(Pair.of(MarsMissionInterfaceTestConstants.TERRAIN_HEIGHT, MarsMissionInterfaceTestConstants.TERRAIN_WIDTH), report.rover().getPosition());
                assertEquals(MarsMissionInterfaceTestConstants.TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceOverObstacle() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES);
        underTest.setUp();
        assert false;
    }

    @Test
    public void testAdvanceOverObstacleAfterWrap() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_WITH_WRAP_AND_OBSTACLES);
        underTest.setUp();
        assert false;
    }

    @Test
    public void testAdvanceAroundObstacle() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES);
        underTest.setUp();
        assert false;
    }

    @Test
    public void testAdvanceAroundObstacleCrossingWrap() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_WITH_OBSTACLES);
        underTest.setUp();
        assert false;
    }

}