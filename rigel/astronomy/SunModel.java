package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Représentation d'un modèle de Soleil
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public enum SunModel implements CelestialObjectModel<Sun>{
    SUN;

    private static final double LON_J2010 = Angle.ofDeg(279.557208);
    private static final double LON_PERIGE = Angle.ofDeg(283.112438);
    private static final double EXENTRICITY = 0.016705;
    private static final double EXENTRICITY_POWER_2 = EXENTRICITY*EXENTRICITY;
    public static final double EARTH_ANGULAR_VELOCITY = Angle.TAU/365.242191;
    private static final double THETA_0 = Angle.ofDeg(0.533128);

    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAnomaly = Angle.normalizePositive(daysSinceJ2010*EARTH_ANGULAR_VELOCITY+LON_J2010-LON_PERIGE);
        double trueAnomaly = Angle.normalizePositive(meanAnomaly+2*EXENTRICITY*Math.sin(meanAnomaly));
        double lonEclp=Angle.normalizePositive(trueAnomaly+LON_PERIGE);
        double latEclp=0;
        double angularSize = Angle.normalizePositive(THETA_0*((1+EXENTRICITY*Math.cos(trueAnomaly))/(1-EXENTRICITY_POWER_2)));
        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lonEclp,latEclp);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Sun(eclipticCoordinates,equatorialCoordinates,(float) angularSize,(float)meanAnomaly);
    }
}
