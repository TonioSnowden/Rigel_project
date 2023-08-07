package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Représentation d'un modèle de Planète
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
*/

public enum PlanetModel implements CelestialObjectModel<Planet>{

    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);

    private final String name;
    private final double tropicYear;
    private final double lonJ2010;
    private final double lonPerige;
    private final double exentricity;
    private final double halfBDAXE;
    private final double inclination;
    private final double lonNode;
    private final double angularSize;
    private final double magnitude;

    public final static List<PlanetModel> ALL = Arrays.asList(MERCURY,VENUS,EARTH,MARS,JUPITER,SATURN,URANUS,NEPTUNE);


    /**
     * Construit un modèle de planète avec les differents arguments suivants :
     *
     * @param name
     * nom
     * @param tropicYear
     * période de révolution
     * @param lonJ2010
     * longitude à J2010
     * @param lonPerige
     * Longitude au périgée
     * @param exentricity
     * Excentricité de lonHelio'orbite
     * @param halfBDAXE
     * Demi grand-axe de lonHelio'orbite
     * @param inclination
     * Inclinaison de lonHelio'orbite à lonHelio'écliptique
     * @param lonNode
     * Longitude du nœud ascendant
     * @param angularSize
     * Taille angulaire
     * @param magnitude
     * Magnitude
     */
    private PlanetModel(String name,double tropicYear, double lonJ2010, double lonPerige, double exentricity,double halfBDAXE, double inclination,double lonNode,double angularSize,double magnitude){
        this.halfBDAXE=halfBDAXE;
        this.exentricity=exentricity;
        this.inclination=Angle.ofDeg(inclination);
        this.lonJ2010=Angle.ofDeg(lonJ2010);
        this.lonPerige=Angle.ofDeg(lonPerige);
        this.name=name;
        this.tropicYear=tropicYear;
        this.lonNode=Angle.ofDeg(lonNode);
        this.angularSize=angularSize;
        this.magnitude=magnitude;
    }

    /**
     * Calcule lonHelio'anomalie moyenne.
     *
     * @param daysSinceJ2010
     * Jours depuis J2010
     * @return Anomalie moyenne
     */
    private double ComputeM(double daysSinceJ2010){
        return Angle.normalizePositive(SunModel.EARTH_ANGULAR_VELOCITY*daysSinceJ2010/tropicYear+lonJ2010-lonPerige);
    }

    /**
     * Calcule lonHelio'anomalie vrai.
     *
     * @param meanAnomaly
     * Anomalie moyenne
     * @return anomalie vrai
     */
    private double ComputeV(double meanAnomaly){
        return Angle.normalizePositive(meanAnomaly + 2*exentricity*Math.sin(meanAnomaly));
    }

    /**
     * Calcule le rayon ou disctance au soleil
     *
     * @param trueAnomaly
     * Anomalie vrai
     * @return rayon
     */
    private double ComputeR(double trueAnomaly){
        return halfBDAXE*(1-exentricity*exentricity)/(1+exentricity*Math.cos(trueAnomaly));
    }

    /**
     * Calcule la longitude héliocentrique.
     *
     * @param trueAnomaly
     * Anomalie vrai
     * @return Longitude héliocentrique
     */
    private double ComputeL(double trueAnomaly){
        return Angle.normalizePositive(trueAnomaly + lonPerige);
    }

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double meanAnomaly = ComputeM(daysSinceJ2010);
        double trueAnomaly = ComputeV(meanAnomaly);
        double r = ComputeR(trueAnomaly);
        double lonHelio = ComputeL(trueAnomaly);

        double earthMeanAnomaly= EARTH.ComputeM(daysSinceJ2010);
        double earthTrueAnomaly= EARTH.ComputeV(earthMeanAnomaly);
        double earthR= EARTH.ComputeR(earthTrueAnomaly);
        double earthLonHelio= EARTH.ComputeL(earthTrueAnomaly);


        double latEclHelio = Angle.normalizePositive(Math.asin(Math.sin(lonHelio-lonNode)*Math.sin(inclination)));
        double rProjection = r*Math.cos(latEclHelio);
        double lonEclHelio = Angle.normalizePositive(Math.atan2(Math.sin(lonHelio-lonNode)*Math.cos(inclination),Math.cos(lonHelio-lonNode))+lonNode);

        double lonEclp;
        if((this == MERCURY) || (this==VENUS) || (this==EARTH)) {
            lonEclp = Angle.normalizePositive(Math.PI + earthLonHelio + Math.atan2(rProjection * Math.sin(earthLonHelio - lonEclHelio), earthR - rProjection * Math.cos(earthLonHelio - lonEclHelio)));
        } else {
            lonEclp = Angle.normalizePositive(lonEclHelio + Math.atan2(earthR*Math.sin(lonEclHelio-earthLonHelio),rProjection-earthR*Math.cos(lonEclHelio-earthLonHelio)));
        }
        double latEclp = Math.atan((rProjection*Math.tan(latEclHelio)*Math.sin(lonEclp-lonEclHelio))/(earthR*Math.sin(lonEclHelio-earthLonHelio)));

        double distanceToEarth = earthR*earthR+r*r-2*earthR*r*Math.cos(lonHelio-earthLonHelio)*Math.cos(latEclHelio);
        double angularSizeUA= Angle.normalizePositive(Angle.ofArcsec(angularSize)/Math.sqrt(distanceToEarth));
        double F = (1+Math.cos(lonEclp-lonHelio))/2;

        double approachMagnitude = magnitude +5*Math.log10(r*Math.sqrt(distanceToEarth)/Math.sqrt(F));

        EclipticCoordinates eclipticCoordinates=EclipticCoordinates.of(lonEclp,latEclp);
        EquatorialCoordinates equatorialCoordinates= eclipticToEquatorialConversion.apply(eclipticCoordinates);

        return new Planet(name,equatorialCoordinates,(float)angularSizeUA,(float)approachMagnitude);
        }

    }

