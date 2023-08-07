package ch.epfl.rigel.coordinates;

import java.util.Locale;

/**
 * Coordonnées cartésienne
 *
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public final class CartesianCoordinates {
    private final double abscisse;
    private final double ordonnee;

    /**
     * Construit des coordonnées cartésienne
     *
     * @param x
     * abscisse
     * @param y
     * ordonnée
     */
    private CartesianCoordinates(double x, double y){
        this.abscisse=x;
        this.ordonnee=y;
    }

    /**
     * Méthode de construction de coordonnée cartésienne
     *
     * @param x
     * abscisse
     * @param y
     * ordonnée
     * @return les coordonnées cartésiennes.
     */
    public static CartesianCoordinates of(double x, double y){
        return new CartesianCoordinates(x, y);
    }

    /**
     * retourne l'abscisse
     * @return l'abscisse
     */
    public double x(){
        return abscisse;
    }

    /**
     * retourne l'ordonnée
     * @return l'ordonnée
     */
    public double y(){
        return ordonnee;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "(abscisse=%, ordonnée=%)",
                x(),
                y());
    }

}
