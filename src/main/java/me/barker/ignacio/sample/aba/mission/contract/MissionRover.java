package me.barker.ignacio.sample.aba.mission.contract;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Mission exploration vehicle
 */
public interface MissionRover {

    /**
     * Last reported position.
     * @return A pair containing latitudinal position at left and longitudinal at right
     */
    Pair<Integer, Integer> getPosition();

    /**
     * Last reported facing.
     */
    CardinalDirection getFacing();

    /**
     * Send move command to rover step-by-step.
     * @param backwards send <code>true</code> if the instruction is to move backwards.
     */
    void move(final boolean backwards);

    /**
     * Send turn command to rover step-by-step.
     * @param clockwise send <code>true</code> if the instruction is to turn clockwise.
     */
    void turn(final boolean clockwise);
}
