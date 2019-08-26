package me.barker.ignacio.sample.mission.contract;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Mission terrain recon interface
 */
public interface MissionTerrain {

    /**
     * Terrain dimensions.
     * @return A pair containing latitudinal size (parallels) at left and longitudinal at right (meridians)
     */
    Pair<Integer, Integer> getDimensions();

    /**
     * Terrain dimension wrapping.
     * @return Pair indicating if terrain has latitudinal wrap (poles) at left and longitudinal wrap (equator) at right.
     */
    Pair<Boolean, Boolean> getWrapping();

    /**
     * Obstacles locations.
     * @return Latitude key based map containing collections of obstacles' longitudes
     */
    Map<Integer, Set<Integer>> getObstacles();

}
