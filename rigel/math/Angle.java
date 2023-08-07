package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Un angle.
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public final class Angle {
    public final static double TAU=2*Math.PI;
    private static final double SEC_TO_RAD=TAU/(3600*360);
    private static final double RAD_TO_HOUR=24/TAU;
    private static final RightOpenInterval normalizePositiveInterval = RightOpenInterval.of(0,TAU);
    private static final RightOpenInterval ofDMSInterval = RightOpenInterval.of(0,60);

    private Angle(){
    }
    /**
     * Normalise un angle en radian.
     *
     * @param rad
     *         un angle en radian
     * @return l'angle normalisé
     */
    public static double normalizePositive(double rad){
        return normalizePositiveInterval.reduce(rad);
    }

    /**
     * Retourne l'angle correspondant au nombre de secondes d'arc donné.
     *
     * @param sec
     *         nombre de secondes d'un arc
     * @return l'angle en radian correspondant
     */
    public static double ofArcsec(double sec){
        return sec*SEC_TO_RAD;
    }

    /**
     * retourne l'angle correspondant à l'angle en degrés, minutes, secondes.
     *
     * @param deg
     *         degrés de l'angle
     * @param min
     *         nombre de minutes du degrés
     * @param sec
     *         nombre de secondes de la minute
     * @throws IllegalArgumentException
     *          si les minutes et/ou les secondes ne sont pas comprises entre 0 et 60
     * @return l'angle en radian correspondant
     */
    public static double ofDMS(int deg, int min, double sec){
        Preconditions.checkArgument(deg >= 0);
        Preconditions.checkInInterval(ofDMSInterval,min);
        Preconditions.checkInInterval(ofDMSInterval,sec);
        return ofDeg(deg + (sec/60 + min)/60);
    }

    /**
     * retourne l'angle en radian correspondant à l'angle en degrés donné.
     *
     * @param deg
     *         angle en degré
     * @return l'angle correspondant en radian
     */
    public static double ofDeg(double deg){
        return Math.toRadians(deg);
    }

    /**
     * Retourne l'angle en degrés correspondant à l'angle donné en radian.
     *
     * @param rad
     *         angle en radian
     * @return l'angle correspondant en degrés
     */
    public static double toDeg(double rad){
        return Math.toDegrees(rad);
    }

    /**
     * Retourne l'angle correspondant à l'angle en heures donné.
     *
     * @param hr
     *         angle en heure
     * @return l'angle correspondant en radian
     */
    public static double ofHr(double hr){
        return hr/RAD_TO_HOUR;
    }

    /**
     * Retourne l'angle en heures correspondant à l'angle donné.
     *
     * @param rad
     *         angle en radian
     * @return l'angle correspondant en heure
     */
    public static double toHr(double rad){ return rad*RAD_TO_HOUR;  }
}
