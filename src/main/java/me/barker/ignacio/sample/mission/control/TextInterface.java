package me.barker.ignacio.sample.mission.control;

import java.util.Optional;
import java.util.Scanner;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.ControlInterface;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
import me.barker.ignacio.sample.mission.contract.ReportingInterface;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Log4j2
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(TextInterfaceConfigurationProperties.class)
public class TextInterface extends AbstractControlInterface implements ApplicationRunner, ReportingInterface {

    private static final Scheduler COMMAND_PROMPT_SCHEDULER = Schedulers
        .newSingle(ControlInterface.class.getSimpleName());

    private final TextInterfaceConfigurationProperties configurationProperties;

    /**
     * Text Interface runner from application arguments.
     * @param args if first argument is {@literal text} then Text Interface will run
     */
    @Override
    public void run(final ApplicationArguments args) {
        log.info("Application Runner for Text Interface is ready");
        Optional.ofNullable(configurationProperties.getAppArgument())
            .filter(args.getNonOptionArgs()::contains)
            .ifPresent(text -> startPrompt());
        log.info("Finished Application Runner for Text Interface");
    }

    @Override
    public void report(final MissionStatus report) {
        System.out.println("\nTerrain square surface: " + report.terrain().getDimensions());
        System.out.println("Border wrapping: " + report.terrain().getWrapping());
        System.out.println("Last command processed was " + report.lastCommand());
        System.out.println(String.format("Rover is now at %s facing: %s",
            report.rover().getPosition(), report.rover().getFacing()));
    }

    /**
     * Text Interface runner
     */
    private void startPrompt() {
        log.debug("Running Text Interface");
        Mono.just(new Scanner(System.in))
            .doFirst(() -> log.debug("Text command scanner ready"))
            .flatMapMany(scanner -> promptCommand(scanner)
                .doFirst(() -> System.out.println("\nCOMMAND YOUR ROVER " + configurationProperties.getCommandMap()))
                .doOnError(throwable -> log.error("Oops! Something went wrong...", throwable))
                .doAfterTerminate(scanner::close))
            .doAfterTerminate(() -> System.out.println("\nControl-C to close the app."))
            .subscribeOn(COMMAND_PROMPT_SCHEDULER)
            .subscribe(this::queueCommand);
    }

    private Flux<ControlCommand> promptCommand(final Scanner commandReader) {
        return Flux.create(emitter -> promptLoop(commandReader)
            .flatMap(controlCommand -> Mono.just(controlCommand)
                .map(emitter::next)
                .filter(fluxSink -> ControlCommand.QUIT.equals(controlCommand))
                .doOnNext(x -> System.out.println("Quitting prompt.")))
            .repeatWhenEmpty(Flux::repeat)
            .subscribe(FluxSink::complete));
    }

    private Mono<ControlCommand> promptLoop(final Scanner scanner) {
        return Mono.fromCallable(scanner::next)
            .doFirst(() -> System.out.print("prompt> "))
            .repeatWhenEmpty(configurationProperties.getPromptRetries(), Flux::repeat)
            .map(configurationProperties::getControlCommand)
            .flatMap(Mono::justOrEmpty)
            .switchIfEmpty(Mono.fromRunnable(() ->
                System.err.println("Wrong command. Try " + configurationProperties.getCommandMap())))
            .repeatWhenEmpty(configurationProperties.getPromptRetries(), Flux::repeat);
    }
}
