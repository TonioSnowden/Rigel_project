package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;

/**
 * SkyCanvasPainter
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */

public final class SkyCanvasPainter {
    Canvas canvas;
    GraphicsContext ctx;
    private final static String NORD = "N";
    private final static String SUD = "S";
    private final static String EST = "E";
    private final static String OUEST = "O";
    private static final ClosedInterval SUN_ALTITUDE  = ClosedInterval.of(0,25);

    /**
     * Constructeur du canvas et du contexte
     * @param canvas
     */
    public SkyCanvasPainter(Canvas canvas){
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    /**
     * méthode qui permet de remettre le canvas tout noir
     */
    public void clear(ObservedSky sky, Transform transform){
        ctx.setFill(SkyCanvasPainter.skyColor(sky,transform));
        ctx.fillRect(0,0, canvas.getWidth(),canvas.getHeight());
    }
    private static Color skyColor(ObservedSky sky, Transform transform){
        Point2D point2DSun=transform.transform(sky.sunPosition().x(),sky.sunPosition().y());
        double altitude = SUN_ALTITUDE.clip(sky.getAltDegSunHorizontalCoordinates())/10;
        Color color = Color.color(0.05*altitude,0.17*altitude,0.3*altitude);
        return color;
    }
    /**
     * méthode qui permet de dessiner l'horizon
     * @param projection
     * @param transform
     */
    public void drawHorizon(StereographicProjection projection, Transform transform){
        CartesianCoordinates center = projection.circleCenterForParallel(HorizontalCoordinates.of(0,0));
        double radius = projection.circleRadiusForParallel(HorizontalCoordinates.of(0,0));
        Point2D point0 = transform.transform(center.x(),center.y());
        radius = transform.deltaTransform(new Point2D(radius,0)).magnitude();
        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2d);
        ctx.strokeOval(point0.getX()-radius,point0.getY()-radius,radius*2,radius*2);
        drawOctants(projection,transform);
    }


    /**
     * méthode qui permet de dessiner le soleil
     * @param sky
     * @param projection
     * @param transform
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform transform){
        Sun sun = sky.sun();
        /**tableau des différentes couleurs du soleil**/
        Color[] color = {Color.YELLOW.deriveColor(Color.YELLOW.getHue(),Color.YELLOW.getSaturation(),
                Color.YELLOW.getBrightness(),0.25),Color.YELLOW,Color.WHITE};
        double size = projection.applyToAngle(sun.angularSize());
        Point2D point = transform.transform(sky.sunPosition().x(),sky.sunPosition().y());
        size = transform.deltaTransform(size,0).magnitude();
        drawCircle(size*2.2, color[0], point.getX(),point.getY());
        drawCircle(size+2,color[1], point.getX(),point.getY());
        drawCircle(size, color[2], point.getX(),point.getY());
        drawName(sun.name(), point.getX(), point.getY());
    }

    /**
     * méthode qui permet de dessiner la lune
     * @param sky
     * @param projection
     * @param transform
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection,Transform transform){
        Moon moon = sky.moon();
        Color color = Color.WHITE;
        double size = projection.applyToAngle(moon.angularSize());
        Point2D point = transform.transform(sky.moonPosition().x(),sky.moonPosition().y());
        size = transform.deltaTransform(new Point2D(size,0)).magnitude();
        drawCircle(size,color,point.getX(),point.getY());
        drawName(moon.name(), point.getX(), point.getY());
    }

    /**
     * méthode qui permet de dessiner les planètes
     * @param sky
     * @param projection
     * @param transform
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform transform) {
        int i = 0;
        /** création d'un tableau avec les positions de toutes les planètes**/
        double[] planetposition = sky.planetPositions();
        /** transformation des positions des planètes **/
        transform.transform2DPoints(planetposition,0,planetposition,0,planetposition.length/2);
        Color color = Color.LIGHTGRAY;
        for (Planet planet : sky.planets()) {
            double size = ecrete(planet.magnitude(),projection);
            /** transformation de la taille de chaque planète**/
            size = transform.deltaTransform(new Point2D(size,0)).magnitude();
            double coordX = planetposition[i*2];
            double coordY = planetposition[i*2+1];
            drawCircle(size,color,coordX,coordY);
            drawName(planet.name(),coordX,coordY);
            i++;
        }
    }

    /**
     * méthode qui permet de dessiner les étoiles
     * @param sky
     * @param projection
     * @param transform
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform transform){
        int i = 0;
        /** création d'un tableau avec les positions de toutes les étoiles**/
        double[] starsposition = sky.starPositions();
        /** transformation des positions des étoiles **/
        transform.transform2DPoints(starsposition,0,starsposition,0,starsposition.length/2);
        /** on dessine les astérismes avant les étoiles afin de bien distinguer l'étoile sinon elle serait derrière
         * le trait de l'asterism et donc cachée*/
        drawAsterisms(sky, starsposition);
        for (Star star: sky.stars()) {
            double size = ecrete(star.magnitude(),projection);
            /** transformation de la taille de chaque étoile**/
            size = transform.deltaTransform(new Point2D(size,0)).magnitude();
            Color color = BlackBodyColor.colorForTemperature(star.colorTemperature());
            double coordX = starsposition[i*2];
            double coordY = starsposition[i*2 + 1];
            drawCircle(size, color, coordX, coordY);
            i++;
        }

    }

    /**
     * méthode privée appelée dans le drawstars afin de dessiner les asterisms
     * @param sky
     * @param starsposition
     */
    private void drawAsterisms(ObservedSky sky, double[] starsposition){
        ctx.beginPath();
        ctx.setLineWidth(1);
        ctx.setStroke(Color.BLUE);
        List<Asterism> asterisms = sky.asterisms();
        boolean first;
        for (Asterism asterism : asterisms) {
            first = true;
            for (int i : sky.asterismIndices(asterism)) {
                double coordX = starsposition[i*2];
                double coordY = starsposition[i*2 + 1];
                /** vérifie si les coordonnées des asterisms sont contenues dans le canvas**/
                if(!canvas.getBoundsInLocal().contains(coordX,coordY) || first){
                    ctx.moveTo(coordX,coordY);
                    first = false;
                }else ctx.lineTo(coordX, coordY);
            }
        }
        ctx.stroke();
    }

    /**
     * méthode privée qui permet d'écreter la valeur de la magnitude donnée en argument
     * @param m
     * @return valeur écretée
     */
    private double ecrete(double m, StereographicProjection projection){
        ClosedInterval magnitude = ClosedInterval.of(-2,5);
        double mprime = magnitude.clip(m);
        double f = (99d - 17d * mprime)/140d;
        return f * projection.applyToAngle(Angle.ofDeg(0.5));

    }

    /**
     * méthode privée qui permet de dessiner un cercle plein
     * @param d
     * @param c
     * @param x
     * @param y
     */
    private void drawCircle(double d, Color c, double x, double y){
        ctx.setFill(c);
        ctx.fillOval(x-d/2d,y-d/2d,d,d);
    }

    /**
     * méthode privée qui permet de dessiner les points cardinaux
     * @param projection
     * @param transform
     */
    private void drawOctants(StereographicProjection projection, Transform transform){
        /** boucle qui permet via la méthode azOctantName de dessiner les octants les uns après les autres**/
        for(int i = 0 ; i < 360 ; i+=45) {
            HorizontalCoordinates octants = HorizontalCoordinates.ofDeg(i , -0.5);
            CartesianCoordinates octant = projection.apply(octants);
            /**transforme la position des octants**/
            Point2D point1 = transform.transform(octant.x(), octant.y());
            ctx.setFill(Color.RED);
            ctx.setTextBaseline(VPos.TOP);
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.fillText(octants.azOctantName(NORD, EST, SUD, OUEST), point1.getX(), point1.getY());
        }
    }
    private void drawName(String name, double x, double y ){
        if(!name.equals("Neptune")) {
            ctx.setFill(Color.WHITE);
            ctx.setFont(Font.font("Helvetica", 10));
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.setTextBaseline(VPos.TOP);
            ctx.fillText(name, x, y);
        }
    }
}