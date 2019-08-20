package me.barker.ignacio.sample.aba.mission.control;

import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

public class AbstractControlInterfaceTest {

    private static final ControlCommand TEST_CONTROL_COMMAND = ControlCommand.MissionCommand.REPORT;

    private AbstractControlInterface underTest;

    @Before
    public void setUp() {
        underTest = new ControlInterfaceStub();
    }

    @Test
    public void testCommandQueueing() {
        StepVerifier.create(underTest.nextCommand()
                .doFirst(() -> Mono.fromRunnable(() -> underTest
                    .queueCommand(TEST_CONTROL_COMMAND))
                    .subscribeOn(Schedulers.parallel())
                    .subscribe()))
            .expectNextMatches(command -> TEST_CONTROL_COMMAND.equals(command)
                && command.equals(underTest.lastCommand()))
            .verifyComplete();
    }

    private static class ControlInterfaceStub extends AbstractControlInterface {

    }

}