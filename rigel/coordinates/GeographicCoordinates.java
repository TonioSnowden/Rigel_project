package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

/**
 * Coordonnées géographiques
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public final class GeographicCoordinates extends SphericalCoordinates {

    /**
     * Constructeur
     * @param latitude
     * latitude
     * @param longitude
     * longitude
     */
    private GeographicCoordinates(double longitude, double latitude){
        super(longitude,latitude);
    }

    /**
     * Donne les coordonnées géographiques en retournant la longitude et la latitude en degrés "lonDeg" et "latDeg"
     * @param lonDeg
     * @param latDeg
     * @return les coordonnées géographiques "geographicCoordinates"
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg){
        Preconditions.checkInInterval(RightOpenInterval.symmetric(360), lonDeg);
        Preconditions.checkInInterval(ClosedInterval.symmetric(180), latDeg);
        GeographicCoordinates geographicCoordinates = new GeographicCoordinates(Angle.ofDeg(lonDeg),Angle.ofDeg(latDeg));
        return geographicCoordinates;
    }

    /**
     * Vérifie que la longitude en degrés est valide
     * @param lonDeg
     * longitude en degrés
     * @return "lonDeg" si elle appartient à l'intervalle valide pour les longitudes
     */
    public static boolean isValidLonDeg(double lonDeg){
        return RightOpenInterval.symmetric(360).contains(lonDeg);
    }

    /**
     * Vérifie que la latitude en degrés est valide
     * @param latDeg
     * latitude en degrés
     * @return "latDeg" si elle appartient à l'intervalle valide pour les latitudes
     */
    public static boolean isValidLatDeg(double latDeg){
        if(ClosedInterval.symmetric(180).contains(latDeg)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public double lon(){
        return super.lon();
    }
    @Override
    public double lonDeg(){
        return super.lonDeg();
    }
    @Override
    public double lat(){
        return super.lat();
    }
    @Override
    public double latDeg(){
        return super.latDeg();
    }
   @Override
    public String toString() {
       return String.format(Locale.ROOT,
               "(lon=%.4f°, lat=%.4f°)",
              lonDeg(),
               latDeg());
    }
}
