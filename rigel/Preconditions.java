package ch.epfl.rigel;
import ch.epfl.rigel.math.Interval;

/**
 * Des préconditions
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */

public final class Preconditions {

    /**
     * Constructeur par défaut de Preconditions.
     */
    private Preconditions() {}

    /**
     * Vérifie qu'un argument est vrai.
     *
     * @param isTrue
     *          Argument a vérifié
     * @throws IllegalArgumentException
     *          si l'argument est faux
     */
    public static void checkArgument(boolean isTrue){
            if(!isTrue){
                throw new IllegalArgumentException();
            }
        }
    /**
     * Vérifie qu'une valeur appartient à un interval donné.
     * @param interval
     *          un interval
     * @param value
     *          une valeur
     * @throws IllegalArgumentException
     *          si la valeur n'appartient pas à l'interval
     * @return la valeur si elle appartient à l'interval
     */
    public static double checkInInterval(Interval interval, double value){
             if(interval.contains(value)){
            return value;
        }else{
            throw new IllegalArgumentException();
             }
        }
}


