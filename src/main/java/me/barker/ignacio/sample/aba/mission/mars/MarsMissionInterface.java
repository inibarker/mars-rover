package me.barker.ignacio.sample.aba.mission.mars;

import lombok.RequiredArgsConstructor;
import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionInterface;
import me.barker.ignacio.sample.aba.mission.contract.MissionRover;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import me.barker.ignacio.sample.aba.mission.contract.MissionTerrain;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MarsMissionInterface implements MissionInterface {

    private final MissionTerrain missionTerrain;

    private final MissionRover missionRover;

    @Override
    public Mono<MissionStatus> operate(final ControlCommand command) {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }

    @Override
    public Mono<MissionStatus> report() {
        return Mono.just(MissionStatus.of(missionTerrain, missionRover, ControlCommand.MissionCommand.REPORT));
    }

   /*

        Code took from other mars rover implementation in order to advance and wrapping

    private void advance(final int step) {
        val newPosition = Optional.of(getFacing())
            .filter(facing -> Direction.EAST.equals(facing) || Direction.WEST.equals(facing))
            .map(x -> Coordinates.of(
                wrapCoordinate(position.getHorizontal() + step, map.getDimensions().getHorizontal()),
                position.getVertical()))
            .orElse(Coordinates.of(
                position.getHorizontal(),
                wrapCoordinate(position.getVertical() + step, map.getDimensions().getVertical())));

        if (Optional.ofNullable(map
            .getObstacles()
            .get(newPosition.getHorizontal()))
            .map(obstacles -> obstacles
                .contains(newPosition.getVertical()))
            .isPresent()) {
            System.out.println("Can not advance, obstacle detected in targeted position.");
        } else {
            position = newPosition;
        }
    }

    private Integer wrapCoordinate(final int newCoordinate, final Integer maxValue) {
        return newCoordinate < 0 ? maxValue : newCoordinate > maxValue ? 0 : newCoordinate;
    }
    */
}
