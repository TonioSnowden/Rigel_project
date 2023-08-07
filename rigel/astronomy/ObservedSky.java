package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Représentation d'un ciel observé
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public class ObservedSky {

    private static final int SUN=0; /** index du Soleil dans la liste des objets celestes**/
    private Sun sun;
    private Moon moon;
    private List<Planet> planets;
    private static final int MOON=1; /** index du Soleil dans la liste des objets celestes**/
    private static final int PLANET_START=2; /** index du Soleil dans la liste des objets celestes**/
    private static final int PLANET_END=8;
    private static final int STARS_START=9;
    private final StarCatalogue starCatalogue;
    private double[] observedSky;
    private List<CelestialObject> celestialObjects;
    private static final List<PlanetModel> PLANET_OBSERVED = planetObserved();
    private HorizontalCoordinates sunHorizontalCoordinates;

    /**
     * Construit le ciel observé
     * @param observationMoment
     *        moment d'observation
     * @param geographicCoordinates
     *        coordonnées géographiques
     * @param stereographicProjection
     *        projection stereographique
     * @param starCatalogue
     *        catalogue des étoiles
     */

    public ObservedSky(ZonedDateTime observationMoment, GeographicCoordinates geographicCoordinates, StereographicProjection stereographicProjection,StarCatalogue starCatalogue){
        this.starCatalogue=starCatalogue;

        double daysSinceJ2010 = Epoch.J2010.daysUntil(observationMoment);
        EclipticToEquatorialConversion eclipticToEquatorialConversion = new EclipticToEquatorialConversion(observationMoment);
        EquatorialToHorizontalConversion equatorialToHorizontalConversion = new EquatorialToHorizontalConversion(observationMoment,geographicCoordinates);

        List<CelestialObject> celestialObjects = new ArrayList<>();

        Sun sun=SunModel.SUN.at(daysSinceJ2010,eclipticToEquatorialConversion);
        this.sun=sun;
        this.sunHorizontalCoordinates = equatorialToHorizontalConversion.apply( sun.equatorialPos());
        celestialObjects.add(sun);
        Moon moon=MoonModel.MOON.at(daysSinceJ2010,eclipticToEquatorialConversion);
        this.moon=moon;
        celestialObjects.add(moon);


        List<Planet> planets = new ArrayList<>();

        for (PlanetModel planetModel : PLANET_OBSERVED){
            celestialObjects.add(planetModel.at(daysSinceJ2010,eclipticToEquatorialConversion));
            planets.add(planetModel.at(daysSinceJ2010,eclipticToEquatorialConversion));
        }
        this.planets=List.copyOf(planets);


        celestialObjects.addAll(starCatalogue.stars());
        this.celestialObjects=celestialObjects;

        double[] observedSky = new double[celestialObjects.size()*2];

        for (int i=0;i<celestialObjects.size();i++){
            CartesianCoordinates cartesianCoordinates = stereographicProjection.apply(equatorialToHorizontalConversion.apply(celestialObjects.get(i).equatorialPos()));
            observedSky[i*2] = cartesianCoordinates.x();
            observedSky[i*2+1]= cartesianCoordinates.y();
        }

        this.observedSky=observedSky;

    }

    public Sun sun(){
        return sun;
    }

    public CartesianCoordinates sunPosition() {
        return CartesianCoordinates.of(observedSky[SUN],observedSky[SUN+1]);
    }

    public double getAltDegSunHorizontalCoordinates() {
        return sunHorizontalCoordinates.altDeg();
    }

    public Moon moon(){
        return moon;
    }

    public CartesianCoordinates moonPosition(){
        return CartesianCoordinates.of(observedSky[MOON],observedSky[MOON+1]);
    }

    public List<Planet> planets(){
        return List.copyOf(planets);
    }

    public double[] planetPositions(){
        double[] planetPosition = new double[14];
        for(int i=PLANET_START*2;i<PLANET_END*2;i++){
            planetPosition[i-PLANET_START*2]=observedSky[i];
        }
        return planetPosition;
    }

    public List<Star> stars(){
        return List.copyOf(starCatalogue.stars());
    }

    public double[] starPositions(){
        double[] starPosition = new double[stars().size()*2];
        for(int i=STARS_START*2;i<observedSky.length;i++){
            starPosition[i-STARS_START*2]=observedSky[i];
        }
        return starPosition;
    }

    public List<Asterism> asterisms(){
        return List.copyOf(starCatalogue.asterisms());
    }

    public List<Integer> asterismIndices(Asterism asterism){
        return List.copyOf(starCatalogue.asterismIndices(asterism));
    }

    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates cartesianCoordinates, double maxRange){
        double Xa = cartesianCoordinates.x();
        double Ya = cartesianCoordinates.y();
        double minDistance = maxRange;
        Optional<CelestialObject> celestialObject = Optional.empty();
        for (int i=0; i<celestialObjects.size() ;i++){
            double Xb = observedSky[i*2];
            double Yb = observedSky[i*2+1];
            double distance = Math.sqrt((Xb-Xa)*(Xb-Xa)+(Yb-Ya)*(Yb-Ya));
            if (((distance < maxRange) && (distance<minDistance))){
                minDistance=distance;
                celestialObject=Optional.of(celestialObjects.get(i));
            }
        }
        return celestialObject;
    }

    private static List<PlanetModel> planetObserved(){
        List<PlanetModel> planetModels = new ArrayList<>();
        for (PlanetModel planetModel : PlanetModel.ALL){
            if(!planetModel.equals(PlanetModel.EARTH)){
                planetModels.add(planetModel);
            }
        }
        return planetModels;
    }
}
