package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;
import ch.epfl.rigel.math.RightOpenInterval;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Le temps sidéral
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */

public final class SiderealTime {

    public static final double MILLIS_PER_HOUR=1.0/ Duration.ofHours(1).toMillis();

    /**
     * Retourne le temps sidéral de Greenwich pour un couple date/heure.
     *
     * @param when
     *         un couple date/heure
     *
     * @return le temps sidéral de Greenwich de when
     */
    public static double greenwich(ZonedDateTime when){
        when = when.withZoneSameInstant(ZoneOffset.UTC);

        double T = Epoch.J2000.julianCenturiesUntil((when).truncatedTo(ChronoUnit.DAYS));
        double t = (when.truncatedTo(ChronoUnit.DAYS).until(when,  ChronoUnit.MILLIS))*MILLIS_PER_HOUR;

        double S1 = Polynomial.of(0.000025862,2400.051336, 6.697374558).at(T);
        double S0 = 1.002737909*t;

        double GST = S0 + S1;
        return Angle.normalizePositive(Angle.ofHr(GST));
    }

    /**
     * Retourne le temps sidéral local pour un couple date/heure et une position.
     *
     * @param when
     *         un couple date/heure
     * @param where
     *         une position
     *
     * @return le temps sidéral local pour en fonction de when et where
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where){
        double LST= SiderealTime.greenwich(when) + where.lon();

        return Angle.normalizePositive(LST);
    }
}
