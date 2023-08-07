package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Coordonnées équatoriales
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 *
 */

public final class EquatorialCoordinates extends SphericalCoordinates{

    /**
     * Constructeur
     *
     * @param latitude
     * @param longitude
     */
    private EquatorialCoordinates(double latitude, double longitude) {super(latitude, longitude);}

    /**
     * Donne les coordonnées équatoriales en retournant l'ascencion droite "ra" et la declinaison "Dec"
     *
     * @param ra
     * ascension droite
     * @param dec
     * déclinaison
     * @return les coordonnées équatoriales "equatorialCoordinates"
     */
    public static EquatorialCoordinates of(double ra, double dec){
        Preconditions.checkInInterval(RightOpenInterval.of(0,2*Math.PI), ra);
        Preconditions.checkInInterval(ClosedInterval.of(-Math.PI/2,Math.PI/2), dec);
        EquatorialCoordinates equatorialCoordinates = new EquatorialCoordinates(ra,dec);
        return equatorialCoordinates;
    }

    /**
     * @return l'ascension droite en radian "ra"
     */
    public double ra(){return lon();}

    /**
     * @return l'ascension droite en degrés "raDeg"
     */
    public double raDeg(){return lonDeg();}

    /**
     * @return l'ascension droite en heures "raHr"
     */
    public double raHr(){return Angle.toHr(lon());}

    /**
     * @return la déclinaison "dec"
     */
    public double dec(){return lat();}

    /**
     * @return la déclinaison en degrés "decDeg"
     */
    public double decDeg(){return latDeg();}

    @Override
    public String toString(){
        return String.format(Locale.ROOT,
                "(ra=%.4fh, dec=%.4f°)",
                raHr(),
                decDeg());
    }
}
