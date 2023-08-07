package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.util.Collections;
import java.util.List;

/**
 * Un astérisme
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public final class Asterism {
    private final List<Star> stars;

    /**
     * Construit un astérisme
     * @param stars
     *         Liste d'étoiles
     */
    public Asterism(List<Star> stars){
        Preconditions.checkArgument(!(stars.isEmpty()));
        this.stars= List.copyOf(stars);
    }

    /**
     * Retourne la liste d'étoiles
     * @return la liste d'étoiles
     */
    public List<Star> stars(){
        return stars;
    }
}
