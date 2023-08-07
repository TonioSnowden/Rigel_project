package ch.epfl.rigel.math;

/**
 * Un interval.
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public abstract class Interval {
    private final double inf;
    private final double sup;

    /**
     * Construit un interval avec une borne inferieure et une borne superieure.
     *
     * @param sup
     *         borne superieure
     * @param inf
     *         borne inferieure
     */
    protected Interval(double inf, double sup){
        this.inf=inf;
        this.sup=sup;
    }

    /**
     * Retourne la borne inferieure de l'interval.
     * @return la borne inferieure de l'interval
     */
    public double low(){
        return inf;
    }

    /**
     * Retourne la borne superieure de l'interval.
     * @return la borne superieure de l'interval
     */
    public double high(){
        return sup;
    }

    /**
     * Retourne la taille de l'interval.
     * @return la taille de l'interval
     */
    public double size(){
        return sup-inf;
    }

    /**
     * Vérifie si une valeur appartient à un interval.
     *
     * @param v
     *        valeur
     * @return vrai si la valeur appartient à l'interval faux sinon
     */
    public abstract boolean contains(double v);

    @Override
    public final int hashCode(){
        throw new UnsupportedOperationException();
    }


    @Override
    public final boolean equals(Object object){
        throw new UnsupportedOperationException();
    }

}
