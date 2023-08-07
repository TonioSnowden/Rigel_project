package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;
/**
 * Un interval ouvert à droite.
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public final class RightOpenInterval extends Interval {

    /**
     * Construit un interval ouvert à droite avec une borne inférieure et supérieure.
     *
     * @param low
     *         borne inférieure
     * @param high
     *         borne supérieure
     */
    private RightOpenInterval(double low, double high) {
        super(low, high);
    }

    /**
     * Construit un interval ouvert à droite avec une borne inférieure et supérieure.
     *
     *@param low
     *         borne inférieure
     * @param high
     *         borne supérieure
     * @throws IllegalArgumentException
     *          si la borne inférieure est supérieure à la borne supérieure
     * @return un interval ouvert à droite et borné
     */
    public static RightOpenInterval of(double low, double high){
        Preconditions.checkArgument(low<high);
            RightOpenInterval interval = new RightOpenInterval(low, high);
            return interval;

    }

    /**
     * Construit un interval ouvert centré en zero.
     *
     * @param size
     *         taille de l'interval
     * @throws IllegalArgumentException
     *          si la taille de l'interval est inférieur à zéro
     * @return un interval ouvert à droite et borné
     */
    public static RightOpenInterval symmetric(double size){
        Preconditions.checkArgument(size>0);
            RightOpenInterval interval = new RightOpenInterval(-size/2, size/2);
            return interval;

    }

    /**
     * Réduit un argument sur son interval
     *
     * @param v
     *         un argument
     * @return l'argument réduit
     */
    public double reduce(double v){
        v = v - (high() - low())*Math.floor((v - low())/(high() - low()));
        return v;
    }

    @Override
    public boolean contains(double v) {
        if(low() <= v && high() > v ){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String toString(){
        return String.format(Locale.ROOT,
                "[%s,%s[",
                low(),
                high());
    }
}
