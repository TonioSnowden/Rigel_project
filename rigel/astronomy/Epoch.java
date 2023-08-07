package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Une époque astronomique.
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */

public enum Epoch {
    J2000(ZonedDateTime.of(LocalDate.of(2000,1,1)
            , LocalTime.NOON
            , ZoneOffset.UTC)),
    J2010((ZonedDateTime.of(LocalDate.of(2010,1,1)
            , LocalTime.MIDNIGHT
            , ZoneOffset.UTC)).minusDays(1));

    private final ZonedDateTime zonedDateTime;
    private static final double MILLIS_PER_DAYS = 1.0/Duration.ofDays(1).toMillis();
    private static final double MILLIS_PER_JULIAN_CENTURIES = 1.0/Duration.ofDays(36525).toMillis();

    /**
     * Construit une époque astronomique à l'aide d' un couple date/heure avec fuseau horaire.
     *
     * @param zonedDateTime
     *       un couple date/heure avec fuseau horaire
     */
    private Epoch(ZonedDateTime zonedDateTime){
        this.zonedDateTime=zonedDateTime;
    }

    /**
     * Retourne le nombre de jours entre l'époque à laquelle on l'applique et l'instant en paramètre.
     *
     * @param when
     *        un instant
     * @return le nombre de jours entre l'époque à laquelle on l'applique et when
     */
    public double daysUntil(ZonedDateTime when){
        return zonedDateTime.until(when, ChronoUnit.MILLIS)*MILLIS_PER_DAYS;
    }

    /**
     * Retourne le nombre de siècles juliens entre l'époque à laquelle on l'applique et l'instant en paramètre
     *
     * @param when
     *         un instant
     * @return le nombre de siècles juliens entre l'époque à laquelle on l'applique et when
     */
    public double julianCenturiesUntil(ZonedDateTime when){
        return zonedDateTime.until(when, ChronoUnit.MILLIS)*MILLIS_PER_JULIAN_CENTURIES;
    }
}
