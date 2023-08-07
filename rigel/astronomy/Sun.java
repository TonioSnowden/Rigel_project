package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
/**
 * Soleil
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */

public final class Sun extends CelestialObject{
    private static String NAME = "Soleil";
    private static float MAGNITUDE = -26.7f;
    private EclipticCoordinates eclipticPos;
    private float meanAnomaly;


    /**
     * Constructeur
     * @param eclipticPos
     * coordonnées écliptiques
     * @param equatorialPos
     * coordonnées équatoriales
     * @param angularSize
     * taille angulaire
     * @param meanAnomaly
     * anomalie moyenne donnée
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos,
               float angularSize, float meanAnomaly){
        super(NAME,equatorialPos,angularSize,MAGNITUDE);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;
    }

    /**
     * @return les coordonnées écliptiques "eclipticPos"
     */
    public EclipticCoordinates eclipticPos(){
        return eclipticPos;
    }

    /**
     * @return l'anomalie moyenne donnée "meanAnomaly"
     */
    public double meanAnomaly(){
        return meanAnomaly;
    }
}
