package me.barker.ignacio.sample.aba.mission.mars;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Before;
import org.junit.Test;

public class MarsRoverTest {

    private MarsRover underTest;

    @Before
    public void setUp() {
        underTest = new MarsRover();
    }

    @Test(expected = NotImplementedException.class)
    public void move() {
        underTest.move(false);
    }

    @Test(expected = NotImplementedException.class)
    public void turn() {
        underTest.turn(false);
    }
}