package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

/**
 * Coordonnées Sphériques
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 *
 */

abstract class SphericalCoordinates {
    private final double longitude;
    private final double latitude;

    /**
     * Constructeur
     * @param latitude
     * latitude
     * @param longitude
     * longitude
     */
    SphericalCoordinates(double longitude, double latitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }

    /**
     * @return la longitude "longitude"
     */
    double lon(){
        return longitude;
    }

    /**
     * @return la longitude en degrés "lonDeg"
     */
    double lonDeg(){
        double lonDeg = Angle.toDeg(longitude);
        return lonDeg;
    }

    /**
     * @return la latitude "latitude"
     */
    double lat(){
        return latitude;
    }

    /**
     * @return la latitude en degrés "latDeg"
     */
    double latDeg(){
        double latDeg = Angle.toDeg(latitude);
        return latDeg;
    }

    @Override
    public final int hashCode() {throw new UnsupportedOperationException();}
    @Override
    public final boolean equals(Object object){throw new UnsupportedOperationException();}
}
