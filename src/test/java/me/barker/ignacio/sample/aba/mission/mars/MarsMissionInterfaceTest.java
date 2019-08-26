package me.barker.ignacio.sample.aba.mission.mars;

import java.util.ArrayList;
import java.util.Arrays;

import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static me.barker.ignacio.sample.aba.mission.mars.MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarsMissionInterfaceTest {

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
    public void testBuildReport() {
        StepVerifier.create(Flux.fromStream(Arrays.stream(ControlCommand.MissionCommand.values())
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
    }

    @Test
    public void testNoObstaclePresent() {
    }

    @Test
    public void testInferNewPosition() {
    }

    @Test
    public void testWrapDimension() {
    }
}