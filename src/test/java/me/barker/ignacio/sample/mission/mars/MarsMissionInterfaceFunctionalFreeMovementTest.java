package me.barker.ignacio.sample.mission.mars;

import java.util.ArrayList;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Value;
import lombok.val;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
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
public class MarsMissionInterfaceFunctionalFreeMovementTest {

    private static final int COMMAND_AMOUNT = 10;

    @Value
    @Builder
    private static final class OperationReport {
        ControlCommand commandExecuted;
        MarsRover previousRover;
        MissionStatus missionReport;
    }

    @Mock
    private MarsMissionContextProperties marsMissionContextProperties;

    private MarsMissionInterface underTest;

    @Before
    public void setUp() {
        underTest = new MarsMissionInterface(marsMissionContextProperties);

        when(marsMissionContextProperties.extractRoverData())
            .thenReturn(MarsRover.builder()
                .position(Pair.of(MarsMissionInterfaceTestConstants.TERRAIN_MIDDLE_HEIGHT, MarsMissionInterfaceTestConstants.TERRAIN_MIDDLE_WIDTH))
                .build());
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES);

        underTest.setUp();
    }

    @Test
    public void testAdvanceRandomlyWithWrapNoObstacles() {
        when(marsMissionContextProperties.extractTerrainData())
            .thenReturn(MarsMissionInterfaceTestConstants.TEST_TERRAIN_WITH_WRAP_NO_OBSTACLES);
        underTest.setUp();

        StepVerifier.create(Flux.fromStream(IntStream.range(0, COMMAND_AMOUNT)
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
                assertEquals(COMMAND_AMOUNT, reportList.size());
                reportList.forEach(report -> {
                    assertEquals(report.getCommandExecuted(), report.getMissionReport().lastCommand());
                    assertFacing(report);
                    assertFreeMovement(report);
                    assertEquals(MarsMissionInterfaceTestConstants.TEST_TERRAIN_NO_WRAP_NO_OBSTACLES, report.getMissionReport().terrain());
                });
            }).expectNextCount(COMMAND_AMOUNT)
            .verifyComplete();
    }

    private static void assertFreeMovement(final OperationReport operationReport)
        throws AssertionError {

        val moverBackwards = ControlCommand.RoverCommand.MOVE_BACKWARDS.equals(operationReport.getCommandExecuted());
        if (ControlCommand.RoverCommand.MOVE_FORWARDS.equals(operationReport.getCommandExecuted())
                || moverBackwards) {
            val testRover = operationReport.getPreviousRover().toBuilder().build();
            testRover.move(moverBackwards);
            assertEquals(testRover.getPosition(),
                operationReport.getMissionReport().rover().getPosition());
        }
    }

    private static void assertFacing(final OperationReport operationReport) {
        if (ControlCommand.RoverCommand.TURN_LEFT.equals(operationReport.getCommandExecuted())) {
            assertEquals(operationReport.getPreviousRover().getFacing().rotateAnticlockwise(),
                operationReport.getMissionReport().rover().getFacing());
        } else if (ControlCommand.RoverCommand.TURN_RIGHT.equals(operationReport.getCommandExecuted())) {
            assertEquals(operationReport.getPreviousRover().getFacing().rotateClockwise(),
                operationReport.getMissionReport().rover().getFacing());
        } else {
            assertEquals(operationReport.getPreviousRover().getFacing(),
                operationReport.getMissionReport().rover().getFacing());
            return;
        }
        assertEquals(operationReport.getPreviousRover().getPosition(),
            operationReport.getMissionReport().rover().getPosition());
    }

}