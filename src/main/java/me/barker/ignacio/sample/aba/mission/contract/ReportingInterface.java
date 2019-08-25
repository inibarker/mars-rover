package me.barker.ignacio.sample.aba.mission.contract;

/**
 * Interface consuming Mission Status update reports
 */
public interface ReportingInterface {

    /**
     * Report consumer
     * @param report mission status update
     */
    <T extends MissionStatus> void report(final T report);
}
