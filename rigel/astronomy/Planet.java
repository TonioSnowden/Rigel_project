package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Planet
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */

public final class Planet extends CelestialObject {

    /**
     * Constructeur
     * @param name
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     */
    public Planet(String name, EquatorialCoordinates equatorialPos,float angularSize, float magnitude){
        super(name, equatorialPos, angularSize, magnitude);
    }
}
