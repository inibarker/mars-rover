package me.barker.ignacio.sample.mission.control;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.ControlInterface;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Log4j2
public abstract class AbstractControlInterface implements ControlInterface {

    private static final Scheduler COMMAND_POLLING_SCHEDULER = Schedulers
        .newSingle(ControlInterface.class.getSimpleName());

    private final BlockingQueue<ControlCommand> commandQueue = new SynchronousQueue<>();

    private ControlCommand lastCommand;

    @Override
    public Mono<ControlCommand> nextCommand() {
        return Mono.fromCallable(commandQueue::take)
            .doFirst(() -> log.debug("{} waiting for command...", this.getClass().getSimpleName()))
            .doOnNext(this::rememberCommand)
            .subscribeOn(COMMAND_POLLING_SCHEDULER);
    }

    @Override
    public ControlCommand lastCommand() {
        return this.lastCommand;
    }

    private void rememberCommand(final ControlCommand controlCommand) {
        this.lastCommand = controlCommand;
        log.debug("Command dequeue {}", controlCommand);
    }

    @SneakyThrows
    void queueCommand(final ControlCommand controlCommand) {
        commandQueue.put(controlCommand);
        log.debug("Command queued {}", controlCommand);
    }
}
