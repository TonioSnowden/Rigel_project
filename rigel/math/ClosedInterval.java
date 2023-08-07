package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Un interval fermé.
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public final class ClosedInterval extends Interval{

    /**
     * Construit un interval fermé avec une borne inférieure et supérieure.
     *
     * @param low
     *         borne inférieure
     * @param high
     *         borne supérieure
     */
    private ClosedInterval(double low, double high) {
                super(low, high);
    }

    /**
     * Construit un interval fermé avec une borne inférieure et supérieure.
     *
     * @param low
     *         borne inférieure
     * @param high
     *         borne supérieure
     * @throws IllegalArgumentException
     *          si la borne inférieure est supérieure à la borne supérieure
     * @return l'interval créé avec une borne inférieure et une borne supérieure
     */
    public static ClosedInterval of(double low, double high){
       Preconditions.checkArgument(low<high);
            ClosedInterval interval = new ClosedInterval(low, high);
            return interval;
    }

    /**
     * Construit un interval fermé centré en zéro.
     *
     * @param size
     *         taille de l'interval
     * @throws IllegalArgumentException
     *          si la taille est inférieur à zéro
     * @return l'interval fermé centré en zéro
     */
    public static ClosedInterval symmetric(double size){
        Preconditions.checkArgument(size>0);
            ClosedInterval interval = new ClosedInterval(-size/2, size/2);
            return interval;

    }

    /**
     * Ecrête un argument à son interval
     *
     * @param v
     *         un argument
     * @return
     *         l'argument écrêté
     */
    public double clip(double v){
        if(v<=low()){
            v=low();
            return v;
        }
        if(v>=high()){
            v=high();
            return v;
        }else {
            return v;
        }
    }

    @Override
    public String toString(){
        return String.format(Locale.ROOT,
                "[%s,%s]",
                low(),
                high());
    }

    @Override
    public boolean contains(double v) {
            if (low() <= v && high() >= v) {
                return true;
            } else {
                return false;
            }

    }
}
