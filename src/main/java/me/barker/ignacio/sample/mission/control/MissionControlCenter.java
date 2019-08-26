package me.barker.ignacio.sample.mission.control;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.barker.ignacio.sample.mission.contract.ControlInterface;
import me.barker.ignacio.sample.mission.contract.MissionInterface;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
import me.barker.ignacio.sample.mission.contract.ReportingInterface;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class MissionControlCenter {

    private final MissionInterface missionInterface;

    private final ControlInterface controlInterface;

    private final ReportingInterface reportInterface;

    /**
     * Scheduled task subscribing for commands to operate mission.
     */
    @Scheduled(
        fixedDelayString = "${mission.control.communications.fixed-delay.millis:5000}",
        initialDelayString = "${mission.control.communications.fixed-delay.millis:5000}")
    public void establishCommunications() {
        log.debug("Establishing communication");
        Optional.ofNullable(operateCommand())
            .map(this::publishReport)
            .ifPresent(this::waitDisposable);
        log.debug("Finishing communication");
    }

    private Mono<MissionStatus> operateCommand() {
        return controlInterface.nextCommand()
            .doOnSuccess(receivedCommand -> log.debug("Command received from control {}", receivedCommand))
            .flatMap(missionInterface::operate)
            .doOnSuccess(operationReport -> log.debug("Mission operation report {}", operationReport))
            .onErrorResume(throwable -> Mono.<MissionStatus>empty()
                .doAfterTerminate(() -> log.error("Mission Control Center failed operating", throwable)));
    }

    private Disposable publishReport(Mono<MissionStatus> missionStatus) {
        return missionStatus.subscribe(reportInterface::report);
    }

    private void waitDisposable(final Disposable command) {
        while (!command.isDisposed()) {
            log.trace("Looking for command disposition");
        }
        log.debug("Command disposed");
    }

}
