package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;
import java.util.Locale;
import java.util.Objects;

/**
 * Lune
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public final class Moon extends CelestialObject{
    private static String name = "Lune";
    private float phase;

    /**
     * Constructeur
     * @param equatorialPos
     * coordonnées équatoriales
     * @param angularSize
     * taille angulaire
     * @param magnitude
     * magnitude
     * @param phase
     * phase
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase){
     super(name,equatorialPos,angularSize,magnitude) ;
     Preconditions.checkInInterval(ClosedInterval.of(0,1),phase);
     this.phase=phase;
    }

    @Override
    public String info() {
        return name + String.format(Locale.ROOT,"(%.1f%%)", phase*100);
    }
}
