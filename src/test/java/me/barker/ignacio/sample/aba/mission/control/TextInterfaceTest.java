package me.barker.ignacio.sample.aba.mission.control;

import java.util.HashMap;

import lombok.Value;
import lombok.experimental.Accessors;
import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionRover;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import me.barker.ignacio.sample.aba.mission.contract.MissionTerrain;
import me.barker.ignacio.sample.aba.mission.mars.MarsRover;
import me.barker.ignacio.sample.aba.mission.mars.MarsTerrain;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

public class TextInterfaceTest {

    private TextInterface underTest = new TextInterface();

    @Before
    public void setUp() {
        underTest = new TextInterface();
    }

    @Test
    public void testReport() {
        underTest.report(new MissionStatusStub());
    }

    @Value
    @Accessors(fluent = true)
    private class MissionStatusStub implements MissionStatus {

        ControlCommand lastCommand = ControlCommand.MissionCommand.REPORT;

        boolean commandProcessed = true;

        MissionTerrain terrain = new MarsTerrain(
            Pair.of(1, 1), new HashMap<>(), Pair.of(false, false));

        MissionRover rover = new MarsRover();

    }
}