package me.barker.ignacio.sample.mission.control;

import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.ControlInterface;
import me.barker.ignacio.sample.mission.contract.MissionInterface;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
import me.barker.ignacio.sample.mission.contract.ReportingInterface;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MissionControlCenterTest {

    private static final ControlCommand TEST_CONTROL_COMMAND = ControlCommand.REPORT;

    private static final MissionStatus TEST_REPORT = new MissionStatusStub();

    @Mock
    private MissionInterface missionInterface;

    @Mock
    private ControlInterface controlInterface;

    @Mock
    private ReportingInterface reportInterface;

    private MissionControlCenter underTest;

    @Before
    public void setUp() {
        this.underTest = new MissionControlCenter(
            missionInterface,
            controlInterface,
            reportInterface
        );

        when(controlInterface.nextCommand()).thenReturn(Mono.just(ControlCommand.REPORT));
        when(missionInterface.operate(any())).thenReturn(Mono.just(TEST_REPORT));
    }

    @Test
    public void establishCommunications() {
        this.underTest.establishCommunications();
        verify(controlInterface).nextCommand();
        verify(missionInterface).operate(TEST_CONTROL_COMMAND);
        verify(reportInterface).report(TEST_REPORT);
    }
}