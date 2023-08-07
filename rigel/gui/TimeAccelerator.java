package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


/**
 * Interface TimeAccelerator
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
@FunctionalInterface
public interface TimeAccelerator {

    static final double SEC_TO_NANOS = Math.pow(10,9);
    ZonedDateTime adjust(ZonedDateTime initialTime, long realElapsedTime);

    /**
     * méthode static en package private qui retourne un accélerateur continu selon le facteur entré en argument
     * @param alpha
     * @return
     */
    static TimeAccelerator continuous(int alpha){
        return (initialTime, realElapsedTime) -> initialTime.plus(alpha*realElapsedTime, ChronoUnit.NANOS);
    }

    /**
     * retourne un accélerateur discret en fct de la fréquence et de la durée donnés en arguments
     * @param frequence
     * @param step
     * @return
     */
    static TimeAccelerator discrete(int frequence, Duration step){
        Preconditions.checkArgument(frequence > 0);
        return (initialTime, realElapsedTime) -> initialTime.plusNanos((long)(Math.floor(frequence*realElapsedTime/SEC_TO_NANOS))*step.toNanos());
    }
}

