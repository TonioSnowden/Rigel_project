package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

/**
 * Objet Céleste
 *
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 *
 */
abstract public class CelestialObject {
    private String name;
    private EquatorialCoordinates equatorialPos;
    private float angularSize;
    private float magnitude;

    /**
     * Constructeur
     * @param name
     * nom de l'objet
     * @param equatorialPos
     * coordonnées équatoriales de l'objet
     * @param angularSize
     * taille angulaire de l'objet
     * @param magnitude
     * magnitude de l'objet
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude){
        Preconditions.checkArgument(angularSize>=0);
        this.angularSize = angularSize;
        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.magnitude = magnitude;

    }

    /**
     * @return le nom de l'objet "name"
     */
    public String name(){
        return name;
    }

    /**
     * @return les coordonnées équatoriales de l'objet "equatorialPos"
     */
    public EquatorialCoordinates equatorialPos(){
        return equatorialPos;
    }

    /**
     * @return la taille angulaire de l'objet "angularSize"
     */
    public double angularSize(){
        return angularSize;
    }

    /**
     * @return la magnitude de l'objet "magnitude"
     */
    public double magnitude(){
        return magnitude;
    }

    /**
     * @return un texte informatif sur l'objet en question
     */
    public String info(){
        return name();
    }
    @Override
    public String toString(){
        return info();
    }
}
