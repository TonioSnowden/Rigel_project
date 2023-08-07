package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.nio.ShortBuffer;
import java.util.Locale;

/**
 * Coordonnées horizontales
 *
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 *
 */

public final class HorizontalCoordinates extends SphericalCoordinates {

    /**
     * Constructeur
     * @param latitude
     * latitude
     * @param longitude
     * longitude
     */
    private HorizontalCoordinates(double longitude, double latitude){
        super(longitude,latitude);
    }

    /**
     * Donne les coordonnées horizontales en retournant l'azimut "az" et la hauteur "alt"
     * @param az
     * azimut
     * @param alt
     * altitude
     * @return les coordonnées horizontales en radian "horizontalCoordinates"
     */
    public static HorizontalCoordinates of(double az, double alt){
        Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU),az);
        Preconditions.checkInInterval(ClosedInterval.symmetric(Angle.TAU/2.0),alt);
        HorizontalCoordinates horizontalCoordinates = new HorizontalCoordinates(az,alt);
        return horizontalCoordinates;
    }

    /**
     * Donne les coordonnées horizontales en retournant l'azimut en degrés "azDeg" et la hauteur en degrés "altDeg"
     * @param azDeg
     * azimut en degrés
     * @param altDeg
     *altitide en degrés
     * @return les coordonnées horizontales en degrés "horizontalCoordinatesDeg"
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg){
        return of(Angle.ofDeg(azDeg),Angle.ofDeg(altDeg));
    }

    /**
     * @return l'azimut "az"
     */
    public double az() {return lon(); }

    /**
     * @return l'azimut en degrés "azDeg"
     */
    public double azDeg(){ return lonDeg(); }

    /**
     * @return la hauteur "alt"
     */
    public double alt(){ return lat(); }

    /**
     * @return la hauteur en degrés "altDeg"
     */
    public double altDeg(){ return latDeg(); }

    /**
     * Donne l'octant dans lequel se trouve les coordonnées données
     * @param n
     * nord
     * @param e
     * est
     * @param s
     * sud
     * @param w
     * ouest
     * @return l'octant qui contient ces coordonnées "octantName[i]"
     */
    public String azOctantName(String n, String e, String s, String w){
        String[] octantName = {n,n+e,e,s+e,s,s+w,w,n+w};
        int octantNum = (int) (azDeg() + 22.5)/45;
        if(octantNum<8){
            return octantName[octantNum];
        }else{
            return octantName[0];
        }
    }

    /**
     * Donne la distance angulaire entre le récepteur et l'endroit donné en argument
     * @param that
     * lieu dont on veut connaitre la distance angulaire avec le récepteur
     * @return la distance angulaire
     */
    public double angularDistanceTo(HorizontalCoordinates that){
        double angularDistance = Math.sin(alt()) * Math.sin(that.alt()) +
                Math.cos(alt()) * Math.cos(that.alt()) * Math.cos(az() - that.az());
        return Math.acos(angularDistance);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,"(az=%.4f°, alt=%.4f°)", azDeg(),altDeg());
    }
}
