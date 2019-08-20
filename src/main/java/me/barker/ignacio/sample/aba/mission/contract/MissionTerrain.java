package me.barker.ignacio.sample.aba.mission.contract;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Mission terrain recon interface
 */
public interface MissionTerrain {

    /**
     * Terrain dimensions.
     * @return A pair containing latitudinal size at left and longitudinal at right
     */
    Pair<Integer, Integer> getDimensions();

    /**
     * Obstacles position.
     * @return Latitude key based map containing collections of obstacles' longitudes
     */
    Map<Integer, Set<Integer>> getObstacles();

    /**
     * Terrain dimension wrapping
     * @return Pair indicating if terrain is latitudinal wrapped at left and longitudinal wrapped at right.
     */
    Pair<Boolean, Boolean> getWrapping();

}
