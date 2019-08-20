package me.barker.ignacio.sample.aba.mission.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

@Log4j2
@Data
@NoArgsConstructor
@ConfigurationProperties("mission.control.text")
public class TextInterfaceConfigurationProperties {

    private String appArgument;

    private Integer promptRetries;

    private TextCommand commandMap;

    @ToString.Exclude
    private final Map<String, ControlCommand> commandByMapping = new HashMap<>();

    @PostConstruct
    public void setUp() {
        mapCommand(ControlCommand.RoverCommand.FORWARD, getCommandMap().getForward().toLowerCase());
        mapCommand(ControlCommand.RoverCommand.BACKWARD, getCommandMap().getBackward().toLowerCase());
        mapCommand(ControlCommand.RoverCommand.LEFT, getCommandMap().getLeft());
        mapCommand(ControlCommand.RoverCommand.RIGHT, getCommandMap().getRight());
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
        return Optional.ofNullable(getCommandByMapping().get(textCommand));
    }

    @Data
    @NoArgsConstructor
    private static class TextCommand {

        @NonNull
        private String forward, backward, left, right, quit;

    }
}
