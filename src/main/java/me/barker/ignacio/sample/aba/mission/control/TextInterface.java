package me.barker.ignacio.sample.aba.mission.control;

import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import me.barker.ignacio.sample.aba.mission.contract.ReportingInterface;

public class TextInterface extends AbstractControlInterface implements ReportingInterface {

    @Override
    public void report(final MissionStatus report) {
        System.out.println("Last command" +
            (report.commandProcessed() ? " ":" NOT ") +
            "processed was " + report.lastCommand());
        System.out.println(String.format("Rover is now at %s facing: %s",
            report.rover().getPosition(), report.rover().getFacing()));
    }
}
