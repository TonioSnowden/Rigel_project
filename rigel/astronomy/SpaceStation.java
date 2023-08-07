package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public class SpaceStation extends CelestialObject {

    private final static float ANGULAR_SIZE = 0;

    public SpaceStation(String name, EquatorialCoordinates equatorialPos, float magnitude){
        super(name,equatorialPos,ANGULAR_SIZE,magnitude);
    }
}
