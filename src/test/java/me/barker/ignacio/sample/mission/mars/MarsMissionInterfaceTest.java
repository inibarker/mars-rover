package me.barker.ignacio.sample.mission.mars;

import java.util.ArrayList;
import java.util.Arrays;

import me.barker.ignacio.sample.mission.contract.CardinalDirection;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static junit.framework.TestCase.assertTrue;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TERRAIN_HEIGHT;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TERRAIN_MIDDLE_HEIGHT;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TERRAIN_MIDDLE_WIDTH;
import static me.barker.ignacio.sample.mission.mars.MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarsMissionInterfaceTest {

    @Mock
    private MarsMissionContextProperties marsMissionContextProperties;

    private MarsMissionInterface underTest;

    private MarsRover marsRoverTest;

    @Before
    public void setUp() {
        marsRoverTest = MarsRover.builder()
            .position(Pair.of(TERRAIN_MIDDLE_HEIGHT, TERRAIN_MIDDLE_WIDTH))
            .build();
        underTest = new MarsMissionInterface(marsMissionContextProperties);

        when(marsMissionContextProperties.extractRoverData())
            .thenReturn(marsRoverTest);
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);

        underTest.setUp();
    }

    @Test
    public void testBuildReport() {
        StepVerifier.create(Flux.fromStream(Arrays
            .stream(ControlCommand.MissionCommand.values())
            .map(expectedCommand -> MissionStatus.of(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, marsRoverTest, expectedCommand)))
            .flatMap(expectedReport -> underTest
                .buildReport(expectedReport.lastCommand())
                .doOnNext(System.out::println)
                .map(actualReport -> Pair.of(expectedReport, actualReport))))
            .recordWith(ArrayList::new)
            .expectRecordedMatches(reportPairList -> reportPairList.stream()
                .allMatch(reportPair -> reportPair.getLeft().equals(reportPair.getRight())))
            .expectNextCount(ControlCommand.MissionCommand.values().length)
            .verifyComplete();
    }

    @Test
    public void testMoveRover() {
        underTest.moveRover(false);

    }

    @Test
    public void testObstaclePresent() {

        underTest.noObstaclePresent(null);
    }

    @Test
    public void testObstacleAbsent() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);

        underTest.setUp();
        assertTrue(underTest.noObstaclePresent(Pair.of(TERRAIN_MIDDLE_HEIGHT, TERRAIN_MIDDLE_WIDTH)));
    }

    @Test
    public void testInferNewPosition() {
        marsRoverTest.setFacing(CardinalDirection.NORTH);
        underTest.setUp();
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft() + 1, marsRoverTest.getPosition().getRight()),
            underTest.inferNewPosition(false));
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft() - 1, marsRoverTest.getPosition().getRight()),
            underTest.inferNewPosition(true));

        marsRoverTest.setFacing(CardinalDirection.SOUTH);
        underTest.setUp();
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft() - 1, marsRoverTest.getPosition().getRight()),
            underTest.inferNewPosition(false));
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft() + 1, marsRoverTest.getPosition().getRight()),
            underTest.inferNewPosition(true));

        marsRoverTest.setFacing(CardinalDirection.EAST);
        underTest.setUp();
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft(), marsRoverTest.getPosition().getRight() + 1),
            underTest.inferNewPosition(false));
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft(), marsRoverTest.getPosition().getRight() - 1),
            underTest.inferNewPosition(true));

        marsRoverTest.setFacing(CardinalDirection.WEST);
        underTest.setUp();
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft(), marsRoverTest.getPosition().getRight() - 1),
            underTest.inferNewPosition(false));
        assertEquals(Pair.of(marsRoverTest.getPosition().getLeft(), marsRoverTest.getPosition().getRight() + 1),
            underTest.inferNewPosition(true));
    }

    @Test
    public void testWrapIntoDimension() {
        assertEquals(TERRAIN_MIDDLE_HEIGHT, MarsMissionInterface.
            wrapIntoDimension(TERRAIN_MIDDLE_HEIGHT, false, TERRAIN_HEIGHT));
        assertEquals(TERRAIN_MIDDLE_HEIGHT, MarsMissionInterface.
            wrapIntoDimension(TERRAIN_MIDDLE_HEIGHT, true, TERRAIN_HEIGHT));
        assertEquals(TERRAIN_HEIGHT, MarsMissionInterface.
            wrapIntoDimension(Integer.MAX_VALUE, false, TERRAIN_HEIGHT));
        assertEquals(0, MarsMissionInterface.
            wrapIntoDimension(Integer.MAX_VALUE, true, TERRAIN_HEIGHT));
        assertEquals(0, MarsMissionInterface.
            wrapIntoDimension(Integer.MIN_VALUE, false, TERRAIN_HEIGHT));
        assertEquals(TERRAIN_HEIGHT, MarsMissionInterface.
            wrapIntoDimension(Integer.MIN_VALUE, true, TERRAIN_HEIGHT));
    }
}