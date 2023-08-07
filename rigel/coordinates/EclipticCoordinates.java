package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Coordonnées écliptiques
 *
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */

public final class EclipticCoordinates extends SphericalCoordinates{

    /**
     * Constructeur
     * @param latitude
     * @param longitude
     */
    private EclipticCoordinates(double latitude, double longitude) { super(latitude, longitude); }

    /**
     * Donne les coordonnées écliptiques en retournant la longitude "lon" et la latitude "lat".
     *
     * @param lon
     * longitude
     * @param lat
     * latitude
     * @return les coordonnées écliptiques "eclipticCoordinates"
     */
    public static EclipticCoordinates of(double lon, double lat){
        Preconditions.checkInInterval(RightOpenInterval.of(0, Angle.TAU), lon);
        Preconditions.checkInInterval(ClosedInterval.symmetric(Angle.TAU/2.0), lat);
        EclipticCoordinates eclipticCoordinates = new EclipticCoordinates(lon,lat);
        return eclipticCoordinates;
    }

    @Override
    public double lon(){return super.lon();}
    @Override
    public double lonDeg(){return super.lonDeg();}
    @Override
    public double lat(){return super.lat();}
    @Override
    public double latDeg(){return super.latDeg();}

    @Override
    public String toString(){
        return String.format(Locale.ROOT,
                "(λ=%.4f°, β=%.4f°)",
                lonDeg(),
                latDeg());
    }

}
