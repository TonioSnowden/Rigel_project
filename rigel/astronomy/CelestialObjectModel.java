package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
/**
 * Représentation d'un modèle d'objet céleste
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public interface CelestialObjectModel<O>{

    /**
     * Retourne l'objet modélisé par le modèle O
     * @param daysSinceJ2010
     *         nombre de jours après l'époque J2010
     * @param eclipticToEquatorialConversion
     *         Conversion pour obtenir les coordonnées équatoriales à partir de données écliptiques
     * @return
     */
    public abstract O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}
