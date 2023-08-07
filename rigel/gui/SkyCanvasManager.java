package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;



/**
 * Gestionnaire de canevas
 * @author Alexis Firome(314496)
 * @author Antoine Munier (314500)
 */
public class SkyCanvasManager {

    private final DoubleBinding mouseAzDeg;
    private final DoubleBinding mouseAltDeg;
    private final ObjectBinding<CelestialObject> objectUnderMouse;

    private final ObservableValue<StereographicProjection> projection;
    private final ObservableValue<ObservedSky> observedSky;
    private final ObservableValue<Transform> planeToCanvas;
    private final ObjectProperty<Point2D> mousePosition;
    private final ObservableValue<HorizontalCoordinates> mouseHorizontalPosition;
    private final Canvas canvas;
    private final static double MAX_RANGE = 10;
    private final SkyCanvasPainter skyCanvasPainter;
    private static final RightOpenInterval NORMALIZE_POSITIVE_INTERVAL_AZIMUTH = RightOpenInterval.of(0,360); /** interval dans lequel l'azimuth doit prendre ses valeurs **/
    private static final ClosedInterval NORMALIZE_POSITIVE_INTERVAL_ALTITUDE  = ClosedInterval.of(5,90); /** interval dans lequel l'altitude doit prendre ses valeurs **/
    private static final ClosedInterval FIELD_OF_VIEW_INTERVAL = ClosedInterval.of(30,150);
    private boolean centerSet;
    private HorizontalCoordinates firstCenter;
    /**
     * Crée un gestionnaire de canevas
     * @param starCatalogue
     * catalogue d'étoiles
     * @param dateTimeBean
     * bean contenant l'instant d'observation
     * @param observerLocationBean
     * bean contenant la position de l'observateur
     * @param viewingParametersBean
     * bean contenant les paramètres déterminant la portion du ciel visible sur l'image
     */
    public SkyCanvasManager(StarCatalogue starCatalogue, DateTimeBean dateTimeBean, ObserverLocationBean observerLocationBean, ViewingParametersBean viewingParametersBean) {
        this.canvas = new Canvas(800,600);

        this.skyCanvasPainter = new SkyCanvasPainter(canvas);



        this.projection = Bindings.createObjectBinding(() -> new StereographicProjection(viewingParametersBean.getCenter()), viewingParametersBean.centerProperty());

        this.observedSky = Bindings.createObjectBinding(() -> new ObservedSky(dateTimeBean.getZonedDateTime(),
                        observerLocationBean.getCoordinates(),
                        projection.getValue(),
                        starCatalogue), /** Création d'un lien pour instancier et actualiser le ciel observé **/
                observerLocationBean.coordinatesProperty(),
                dateTimeBean.dateProperty(),
                dateTimeBean.timeProperty(),
                dateTimeBean.zoneProperty(),
                projection);
        observedSky.addListener(e -> drawAll());

        this.mousePosition = new SimpleObjectProperty<>(Point2D.ZERO);
        canvas.setOnMouseMoved(e -> mousePosition.set(new Point2D(e.getX(), e.getY())));



        this.planeToCanvas = Bindings.createObjectBinding(() -> {
            double canvasWidth = canvas.getWidth();
            double canvasHeight = canvas.getHeight();
            double imageWidth = projection.getValue().applyToAngle(Angle.ofDeg(viewingParametersBean.getFieldOfViewDeg()));
            double dilatationFactor = (canvasWidth == 0) ? 1 : canvasWidth / imageWidth;
            return Transform.translate(canvasWidth / 2, canvasHeight / 2).createConcatenation(Transform.scale(dilatationFactor, -dilatationFactor));
        },  /**Création d'un lien pour instancier et actualiser la transformation du plan au canvas**/
                viewingParametersBean.fieldOfViewDegProperty(),
                canvas.heightProperty(),
                canvas.widthProperty());
        planeToCanvas.addListener(e -> drawAll());


        this.mouseHorizontalPosition = Bindings.createObjectBinding(() -> {
            try {
                Point2D point2D = planeToCanvas.getValue().inverseTransform(mousePosition.getValue());

                return projection.getValue().inverseApply(CartesianCoordinates.of(point2D.getX(), point2D.getY()));
            } catch (NonInvertibleTransformException e) {
                return null;
            }
            },
                mousePosition,
                planeToCanvas,
                projection);

        this.objectUnderMouse = Bindings.createObjectBinding(() -> {
            try {
                Point2D point2D = planeToCanvas.getValue().inverseTransform(mousePosition.getValue());
                double maxRangeInProjection = planeToCanvas.getValue().inverseDeltaTransform(MAX_RANGE,0).getX();

                return observedSky.getValue().objectClosestTo(CartesianCoordinates.of(point2D.getX(),point2D.getY()),maxRangeInProjection).orElse(null);
            } catch (NonInvertibleTransformException e) {
                return null;
            }
            },
                mousePosition,
                observedSky,
                planeToCanvas);

        this.mouseAltDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.getValue().altDeg(),mouseHorizontalPosition);

        this.mouseAzDeg = Bindings.createDoubleBinding(() -> mouseHorizontalPosition.getValue().azDeg(),mouseHorizontalPosition);

        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case RIGHT:
                    viewingParametersBean.centerProperty().setValue(HorizontalCoordinates.ofDeg(NORMALIZE_POSITIVE_INTERVAL_AZIMUTH.reduce(viewingParametersBean.getCenter().azDeg()+10),viewingParametersBean.getCenter().altDeg()));
                    break;
                case LEFT:
                    viewingParametersBean.centerProperty().setValue(HorizontalCoordinates.ofDeg(NORMALIZE_POSITIVE_INTERVAL_AZIMUTH.reduce(viewingParametersBean.getCenter().azDeg()-10),viewingParametersBean.getCenter().altDeg()));
                    break;
                case DOWN:
                    viewingParametersBean.centerProperty().setValue(HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(),NORMALIZE_POSITIVE_INTERVAL_ALTITUDE.clip(viewingParametersBean.getCenter().altDeg()-5)));
                    break;
                case UP:
                    viewingParametersBean.centerProperty().setValue(HorizontalCoordinates.ofDeg(viewingParametersBean.getCenter().azDeg(),NORMALIZE_POSITIVE_INTERVAL_ALTITUDE.clip(viewingParametersBean.getCenter().altDeg()+5)));
                    break;
            }/** Modification du centre de projection si une des fléches du clavier est pressée  **/
            event.consume();
        });


        canvas.setOnMouseDragged(event -> {
            try {
                    if (!centerSet){
                        firstCenter = viewingParametersBean.getCenter();
                        centerSet=true;
                    }
                    Point2D point2D = planeToCanvas.getValue().inverseTransform(new Point2D(event.getX(), event.getY()));
                    HorizontalCoordinates currentCoordinates = projection.getValue().inverseApply(CartesianCoordinates.of(point2D.getX(), point2D.getY()));

                    viewingParametersBean.centerProperty().setValue(
                            HorizontalCoordinates.ofDeg(
                                    NORMALIZE_POSITIVE_INTERVAL_AZIMUTH.reduce(firstCenter.azDeg() + (mouseAzDeg.get()-currentCoordinates.azDeg())),
                                    NORMALIZE_POSITIVE_INTERVAL_ALTITUDE.clip(firstCenter.altDeg() + (mouseAltDeg.get() -currentCoordinates.altDeg()))));

            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
            }

        });

        canvas.setOnMouseReleased(event -> centerSet=false);

        canvas.setOnScroll(event -> {
            double newFieldOfView = viewingParametersBean.getFieldOfViewDeg() +
                    ((Math.abs(event.getDeltaX())>Math.abs(event.getDeltaY())) ? event.getDeltaX() : event.getDeltaY());
            viewingParametersBean.fieldOfViewDegProperty().setValue(FIELD_OF_VIEW_INTERVAL.clip(newFieldOfView));
        });/** modification par zoom du champ d'observation**/

        canvas.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()){
                canvas.requestFocus();
                event.consume();
            }
        });
        drawAll();
    }


    /**
     * retourne l'altitude de la souris sous forme d'observableDoubleValue
     * @return retourne l'altitude de la souris
     */
    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }

    /**
     * retourne l'azimut de la souris sous forme d'observableDoubleValue
     * @return retourne l'azimut de la souris
     */
    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * retourne la liste des objets celestes sous forme d'observableDoubleValue
     * @return la liste des objets celestes
     */
    public ObservableValue<CelestialObject> objectUnderMouseProperty() {
        return objectUnderMouse;
    }

    /**
     * retourne le canevas
     * @return le canevas
     */
    public Canvas canvas() {
        return canvas;
    }

    /**
     * Méthode privé qui dessine tous les élément du painter
     */
    private void drawAll(){
        skyCanvasPainter.clear(observedSky.getValue(),planeToCanvas.getValue());
        skyCanvasPainter.drawStars(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        skyCanvasPainter.drawPlanets(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        skyCanvasPainter.drawSun(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        skyCanvasPainter.drawMoon(observedSky.getValue(), projection.getValue(), planeToCanvas.getValue());
        skyCanvasPainter.drawHorizon(projection.getValue(),planeToCanvas.getValue());
    }

}
