package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.function.Function;

import static ch.epfl.rigel.coordinates.CartesianCoordinates.*;

/**
 * Projection stéréographiques
 *
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {
    private final double lambda0;
    private final double cosp1;
    private final double sinp1;
    private final double p1;
    private final static RightOpenInterval Horizontal_Coord = RightOpenInterval.of(0, Angle.TAU);

    /**
     * Consructeur
     * @param center
     * coordonnées horizontales centrées
     */
    public StereographicProjection(HorizontalCoordinates center){
        this.lambda0=center.az();
        this.p1=center.alt();
        this.cosp1 = Math.cos(center.alt());
        this.sinp1 = Math.sin(center.alt());
    }

    /**
     * Donne les coordonnées du centre du cercle
     * @param hor
     * point par lequel passe le projeté du parallèle
     * @return coordonnées du centre du cercle "circleCenter"
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor){
        double cy = cosp1 / (Math.sin(hor.alt())+sinp1);
         CartesianCoordinates circleCenter = CartesianCoordinates.of(0,cy);
        return circleCenter;
    }

    /**
     * Donne le rayon du cercle
     * @param parallel
     * coordonnées horizontales parallèles
     * @return le rayon du cercle "r"
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel){
        double r = Math.cos(parallel.lat())/(Math.sin(parallel.lat())+sinp1);
        return r;
    }

    /**
     * Donne le diamètre
     * @param rad
     * taille angulaire de la sphère "rad"
     * @return le diamètre
     */
    public double applyToAngle(double rad){
        double d = 2*Math.tan(rad/4);
        return d;
    }

    /**
     * Donne les coordonnées horizontales du point dont la projection est le point de coordonnées cartésiennes xy
     * @param xy
     * coordonnées cartésiennes "xy"
     * @return les coordonnées horizontales "horizontalCoordinates"
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy){
        double x = xy.x();
        double y = xy.y();
        double p = Math.sqrt(x*x + y*y);
        double sinc = 2*p / ( p*p + 1);
        double cosc = (1-p*p) / (p*p + 1);
        double lambda = 0;
        double phi = 0;
        if ((x==0) && (y==0)) {
            lambda = lambda0;
            phi = p1;
        } else {
            lambda = Math.atan2(x * sinc, p * cosp1 * cosc - y * sinp1 * sinc) + lambda0;
            phi = Math.asin(cosc * sinp1 + (y * sinc * cosp1) / p);
        }
        HorizontalCoordinates horizontalCoordinates = HorizontalCoordinates.of(Horizontal_Coord.reduce(lambda),phi);
        return horizontalCoordinates;
    }

    @Override
    public CartesianCoordinates apply(HorizontalCoordinates azAlt) {
        double lamda = azAlt.az();
        double sinp = Math.sin(azAlt.alt());
        double cosp = Math.cos(azAlt.alt());
        double delta = lamda - lambda0;
        double d = 1+ (sinp* sinp1) + cosp*cosp1* Math.cos(delta);
        double x = (cosp*Math.sin(delta))/d;
        double y = ((sinp*cosp1)-(cosp*sinp1*Math.cos(delta)))/d;
        CartesianCoordinates cartesianCoordinates = CartesianCoordinates.of(x,y);
        return cartesianCoordinates;
    }
}
