package me.barker.ignacio.sample.mission.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

@Log4j2
@Data
@ConfigurationProperties("mission.control.text")
public class TextInterfaceConfigurationProperties {

    private String appArgument;

    private Integer promptRetries = 1;

    private TextCommand commandMap;

    @ToString.Exclude
    private final Map<String, ControlCommand> commandByMapping = new HashMap<>();

    @PostConstruct
    public void setUp() {
        mapCommand(ControlCommand.RoverCommand.MOVE_FORWARDS, getCommandMap().getMoveForwards().toLowerCase());
        mapCommand(ControlCommand.RoverCommand.MOVE_BACKWARDS, getCommandMap().getMoveBackwards().toLowerCase());
        mapCommand(ControlCommand.RoverCommand.TURN_LEFT, getCommandMap().getTurnLeft());
        mapCommand(ControlCommand.RoverCommand.TURN_RIGHT, getCommandMap().getTurnRight());
        mapCommand(ControlCommand.MissionCommand.QUIT, getCommandMap().getQuit());
        log.info("Text command configuration: " + this);
    }

    private void mapCommand(final ControlCommand command, final String mapping) {
        Optional.of(command)
            .map(mappingCommand -> getCommandByMapping().putIfAbsent(mapping, mappingCommand))
            .ifPresent(mappedCommand -> log.error("Can not map {} to command {} since command {} is previously mapped",
                mapping, getCommandByMapping(), mappedCommand));
    }

    Optional<ControlCommand> getControlCommand(final String textCommand) {
        return Optional.ofNullable(textCommand)
            .map(String::toLowerCase)
            .map(getCommandByMapping()::get);
    }

    @Data
    @NoArgsConstructor
    private static class TextCommand {

        @NonNull
        private String moveForwards, moveBackwards, turnLeft, turnRight, quit;

    }
}
