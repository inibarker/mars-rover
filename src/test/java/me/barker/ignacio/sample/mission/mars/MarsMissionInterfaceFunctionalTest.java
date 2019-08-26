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

import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TERRAIN_HEIGHT;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TERRAIN_MIDDLE_HEIGHT;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TERRAIN_WIDTH;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES;
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
            .operate(ControlCommand.REPORT))
            .assertNext(missionStatus -> {
                assertEquals(marsRoverTest, missionStatus.rover());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, missionStatus.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceBorderNoWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.MOVE_FORWARDS)
            .repeat(TERRAIN_HEIGHT).last())
            .assertNext(report -> {
                assertEquals(ControlCommand.MOVE_FORWARDS, report.lastCommand());
                assertEquals(Pair.of(TERRAIN_HEIGHT - 1, 0), report.rover().getPosition());
                assertEquals(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceBorderWithWrap() {
        marsRoverTest.setPosition(Pair.of(TERRAIN_MIDDLE_HEIGHT, TERRAIN_WIDTH - 1));
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.MOVE_FORWARDS)
            .repeat(TERRAIN_MIDDLE_HEIGHT).last())
            .assertNext(report -> {
                assertEquals(ControlCommand.MOVE_FORWARDS, report.lastCommand());
                assertEquals(Pair.of(1, TERRAIN_WIDTH - 1), report.rover().getPosition());
                assertEquals(TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

    @Test
    public void testAdvanceCornerNoWrap() {
        marsRoverTest.setPosition(Pair.of(0, 0));
        marsRoverTest.setFacing(CardinalDirection.SOUTH);
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(underTest
            .operate(ControlCommand.MOVE_FORWARDS)
            .then(underTest.operate(ControlCommand.TURN_RIGHT)
            .then(underTest.operate(ControlCommand.MOVE_FORWARDS))))
            .assertNext(report -> {
                assertEquals(ControlCommand.MOVE_FORWARDS, report.lastCommand());
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
            .operate(ControlCommand.MOVE_FORWARDS)
            .then(underTest.operate(ControlCommand.TURN_RIGHT)
                .then(underTest.operate(ControlCommand.MOVE_FORWARDS))))
            .assertNext(report -> {
                assertEquals(ControlCommand.MOVE_FORWARDS, report.lastCommand());
                assertEquals(CardinalDirection.WEST, report.rover().getFacing());
                assertEquals(Pair.of(TERRAIN_HEIGHT - 1, TERRAIN_WIDTH - 1), report.rover().getPosition());
                assertEquals(TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES, report.terrain());
            }).verifyComplete();
    }

}