package ch.epfl.rigel.gui;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeAcceleratorTest {
    private final static double secToNano = 1E9;

    @Test
    void adjust(){
    }

    @Test
    void continuous() {
        ZonedDateTime when = ZonedDateTime.of(2010, 1,
                1, 21, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime simulation = TimeAccelerator.continuous(300).adjust(when, (long) (2.34*secToNano));
        ZonedDateTime expected = ZonedDateTime.of(2010, 1,
                1, 21, 11, 42, 0, ZoneOffset.UTC);
        assertEquals(expected, simulation);
    }

    @Test
    void discrete() {
        ZonedDateTime when = ZonedDateTime.of(2010, 1,
                1, 21, 0, 0, 0, ZoneOffset.UTC);
        Duration step = Duration.ofHours(23).plusMinutes(56).plusSeconds(4);
        ZonedDateTime simulation = TimeAccelerator.discrete(10, step).adjust(when, (long) (2.34 * secToNano));
        ZonedDateTime expected = ZonedDateTime.of(2010, 1,
                24, 19, 29, 32, 0, ZoneOffset.UTC);
        assertEquals(expected, simulation);
    }
}