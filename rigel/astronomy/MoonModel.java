package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

/**
 * Représentation d'un modèle de Lune
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
*/
public enum MoonModel implements CelestialObjectModel{
    MOON;

    /**
     * Différentes constantes utiles pour la méthode at()
     */
    private static final double meanLon = Angle.ofDeg(91.929336);
    private static final double meanLonPerigee = Angle.ofDeg(130.143076);
    private static final double lonNode = Angle.ofDeg(291.682547);
    private static final double INCLINATION = Angle.ofDeg(5.145396);
    private static final double EXENCTRICITY = 0.0549;
    private static final double ANGULAR_SIZE_FROM_EARTH = Angle.ofDeg(0.5181);
    private static final float MAGNITUDE = 0;

    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double lonOrbital = Angle.ofDeg(13.1763966)*daysSinceJ2010+meanLon;
        double meanMoonAnomaly = lonOrbital-Angle.ofDeg(0.1114041)*daysSinceJ2010-meanLonPerigee;

        Sun sun = SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);

        double meanSunAnomaly = sun.meanAnomaly();
        double lonEclpSun= sun.eclipticPos().lon();

        double Ev = Angle.ofDeg(1.2739)*Math.sin(2*(lonOrbital-lonEclpSun)-meanMoonAnomaly);
        double Ae = Angle.ofDeg(0.1858)*Math.sin(meanSunAnomaly);
        double A3 = Angle.ofDeg(0.37)*Math.sin(meanSunAnomaly);

        double correctedAnomaly = meanMoonAnomaly + Ev - A3 -Ae;

        double Ec = Angle.ofDeg(6.2886)*Math.sin(correctedAnomaly);
        double A4 = Angle.ofDeg(0.214)*Math.sin(2*correctedAnomaly);

        double correctedLonOrbital = lonOrbital + Ev + Ec - Ae + A4;

        double variation = Angle.ofDeg(0.6583)*Math.sin(2*(correctedLonOrbital-lonEclpSun));
        double truelonOrbital = correctedLonOrbital + variation;

        double meanAnomalyNA = lonNode-Angle.ofDeg(0.0529539)*daysSinceJ2010;
        double correctedMeanAnomalyNA = meanAnomalyNA-Angle.ofDeg(0.16)*Math.sin(meanSunAnomaly);

        double lonEclp = Angle.normalizePositive(Math.atan2(Math.sin(truelonOrbital-correctedMeanAnomalyNA)*Math.cos(INCLINATION),Math.cos(truelonOrbital-correctedMeanAnomalyNA))+correctedMeanAnomalyNA);
        double latEclp = Math.asin(Math.sin(truelonOrbital-correctedMeanAnomalyNA)*Math.sin(INCLINATION));

        double phase = (1-Math.cos(truelonOrbital-lonEclpSun))/2;
        double distanceMoonEarth = (1-EXENCTRICITY*EXENCTRICITY)/(1+EXENCTRICITY*Math.cos(correctedAnomaly+Ec));
        double angularSize = Angle.normalizePositive(ANGULAR_SIZE_FROM_EARTH/distanceMoonEarth);

        EclipticCoordinates eclipticCoordinates = EclipticCoordinates.of(lonEclp,latEclp);
        EquatorialCoordinates equatorialCoordinates = eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Moon(equatorialCoordinates,(float) angularSize,MAGNITUDE,(float)phase);
    }
}
