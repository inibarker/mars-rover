package me.barker.ignacio.sample.mission.mars;

import me.barker.ignacio.sample.mission.contract.CardinalDirection;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarsRoverTest {

    private static final Pair<Integer, Integer> INITIAL_POSITION = Pair.of(10, 10);

    private MarsRover underTest;

    @Before
    public void setUp() {
        underTest = MarsRover.builder()
            .position(INITIAL_POSITION)
            .facing(CardinalDirection.NORTH)
            .build();
    }

    @Test
    public void testMoveForwardsOnceNorth() {
        underTest.move(false);
        assertEquals(CardinalDirection.NORTH, underTest.getFacing());
        assertEquals(Pair.of(INITIAL_POSITION.getLeft() + 1, INITIAL_POSITION.getRight()), underTest.getPosition());
    }

    @Test
    public void testMoveForwardsOnceEast() {
        underTest = underTest.toBuilder()
            .facing(CardinalDirection.EAST)
            .build();
        underTest.move(false);
        assertEquals(CardinalDirection.EAST, underTest.getFacing());
        assertEquals(Pair.of(INITIAL_POSITION.getLeft(), INITIAL_POSITION.getRight() + 1), underTest.getPosition());
    }

    @Test
    public void testGetFactorNorth() {
        underTest = underTest.toBuilder().facing(CardinalDirection.NORTH).build();
        assertEquals(1, underTest.getFactor(false));
        assertEquals(-1, underTest.getFactor(true));
    }

    @Test
    public void testGetFactorSouth() {
        underTest = underTest.toBuilder().facing(CardinalDirection.SOUTH).build();
        assertEquals(-1, underTest.getFactor(false));
        assertEquals(1, underTest.getFactor(true));
    }

    @Test
    public void testGetFactorEast() {
        underTest = underTest.toBuilder().facing(CardinalDirection.EAST).build();
        assertEquals(1, underTest.getFactor(false));
        assertEquals(-1, underTest.getFactor(true));
    }

    @Test
    public void testGetFactorWest() {
        underTest = underTest.toBuilder().facing(CardinalDirection.WEST).build();
        assertEquals(-1, underTest.getFactor(false));
        assertEquals(1, underTest.getFactor(true));
    }

    @Test
    public void testMoveForwardsOnceWest() {
        underTest = underTest.toBuilder()
            .facing(CardinalDirection.WEST)
            .build();
        underTest.move(false);
        assertEquals(CardinalDirection.WEST, underTest.getFacing());
        assertEquals(Pair.of(INITIAL_POSITION.getLeft(), INITIAL_POSITION.getRight() - 1), underTest.getPosition());
    }

    @Test
    public void testMoveForwardsOnceSouth() {
        underTest = underTest.toBuilder()
            .facing(CardinalDirection.SOUTH)
            .build();
        underTest.move(false);
        assertEquals(CardinalDirection.SOUTH, underTest.getFacing());
        assertEquals(Pair.of(INITIAL_POSITION.getLeft() - 1, INITIAL_POSITION.getRight()), underTest.getPosition());
    }

    @Test
    public void testMoveBackwardsOnce() {
        underTest.move(true);
        assertEquals(CardinalDirection.NORTH, underTest.getFacing());
        assertEquals(Pair.of(INITIAL_POSITION.getLeft() - 1, INITIAL_POSITION.getRight()), underTest.getPosition());
    }

    @Test
    public void testMoveForwardsOnceTurnTwiceMoveBackwardsOnce() {
        underTest.move(false);
        underTest.turn(false);
        underTest.turn(false);
        underTest.move(false);
        assertEquals(CardinalDirection.SOUTH, underTest.getFacing());
        assertEquals(INITIAL_POSITION, underTest.getPosition());
    }

    @Test
    public void testTurnOnceMoveTwiceTurnOnceMoveTrice() {
        underTest.turn(false);
        underTest.move(false);
        underTest.move(false);
        underTest.turn(false);
        underTest.move(false);
        underTest.move(false);
        underTest.move(false);
        assertEquals(CardinalDirection.SOUTH, underTest.getFacing());
        assertEquals(Pair.of(INITIAL_POSITION.getLeft() - 3, INITIAL_POSITION.getRight() - 2), underTest.getPosition());
    }

    @Test
    public void testTurnClockwiseOnce() {
        underTest.turn(true);
        assertEquals(CardinalDirection.EAST, underTest.getFacing());
        assertEquals(INITIAL_POSITION, underTest.getPosition());
    }

    @Test
    public void testTurnAnticlockwiseOnce() {
        underTest.turn(false);
        assertEquals(CardinalDirection.WEST, underTest.getFacing());
        assertEquals(INITIAL_POSITION, underTest.getPosition());
    }

    @Test
    public void testTurnClockwiseOnceAnticlockwiseOnce() {
        underTest.turn(true);
        underTest.turn(false);
        assertEquals(INITIAL_POSITION, underTest.getPosition());
        assertEquals(CardinalDirection.NORTH, underTest.getFacing());
    }

    @Test
    public void testTurnClockwiseTwice() {
        underTest.turn(true);
        underTest.turn(true);
        assertEquals(INITIAL_POSITION, underTest.getPosition());
        assertEquals(CardinalDirection.SOUTH, underTest.getFacing());
    }

    @Test
    public void testTurnClockwiseTrice() {
        underTest.turn(true);
        underTest.turn(true);
        underTest.turn(true);
        assertEquals(INITIAL_POSITION, underTest.getPosition());
        assertEquals(CardinalDirection.WEST, underTest.getFacing());
    }

    @Test
    public void testTurnFull() {
        underTest.turn(true);
        underTest.turn(true);
        underTest.turn(true);
        underTest.turn(true);
        assertEquals(INITIAL_POSITION, underTest.getPosition());
        assertEquals(CardinalDirection.NORTH, underTest.getFacing());
    }

}