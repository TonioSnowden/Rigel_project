package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;


/**
 * Une étoile
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public final class Star extends CelestialObject{

    private final static float ANGULAR_SIZE=0;
    private final int hipparcosId;
    private final float COLOR_INDEX_INTERVAL;

    /**
     * Construit une étoile
     * @param hipparcosId
     *         numéro hipparcos
     * @param name
     *         nom de l'étoile
     * @param equatorialPos
     *         position équatoriale
     * @param magnitude
     *         magnitude
     * @param colorIndex
     *         indice de couleur
     */
    public Star(int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex) {
        super(name,equatorialPos,ANGULAR_SIZE,magnitude);
        Preconditions.checkArgument(hipparcosId>=0);
        Preconditions.checkInInterval(ClosedInterval.of(-0.5,5.5),colorIndex);
        this.hipparcosId=hipparcosId;
        this.COLOR_INDEX_INTERVAL=colorIndex;
    }

    /**
     * Retourne le numéro hipparcos
     * @return le numéro hipparcos.
     */
    public int hipparcosId(){
        return hipparcosId;
    }

    /**
     * Retourne la temperature de l'étoile en degré kelvin
     * @return la temperature de l'étoile en degré kelvin.
     */
    public int colorTemperature(){
        double calculator = 0.92*COLOR_INDEX_INTERVAL;
        double T = 4600*(1/(calculator+1.7)+1/(calculator+0.62));
        return (int) T;
    }
}
