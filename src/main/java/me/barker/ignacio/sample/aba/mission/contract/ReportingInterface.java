package me.barker.ignacio.sample.aba.mission.contract;

/**
 * Interface consuming Mission Status update reports
 */
public interface ReportingInterface {

    /**
     * Report consumer
     * @param report mission status update
     */
    void report(final MissionStatus report);
}
