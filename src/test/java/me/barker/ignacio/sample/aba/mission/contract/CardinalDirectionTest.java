package me.barker.ignacio.sample.aba.mission.contract;

import junit.framework.TestCase;

public class CardinalDirectionTest extends TestCase {

    public void testRotateFromNorth() {
        assertEquals(CardinalDirection.EAST, CardinalDirection.NORTH.rotateClockwise());
        assertEquals(CardinalDirection.WEST, CardinalDirection.NORTH.rotateAnticlockwise());

        assertEquals(CardinalDirection.WEST, CardinalDirection.SOUTH.rotateClockwise());
        assertEquals(CardinalDirection.EAST, CardinalDirection.SOUTH.rotateAnticlockwise());

        assertEquals(CardinalDirection.SOUTH, CardinalDirection.EAST.rotateClockwise());
        assertEquals(CardinalDirection.NORTH, CardinalDirection.EAST.rotateAnticlockwise());

        assertEquals(CardinalDirection.NORTH, CardinalDirection.WEST.rotateClockwise());
        assertEquals(CardinalDirection.SOUTH, CardinalDirection.WEST.rotateAnticlockwise());
    }

}