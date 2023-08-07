package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.*;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.extension.PopUp;
import ch.epfl.rigel.extension.PopUpListener;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Programme principal du projet
 *
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public class Main extends Application {

    private final ObserverLocationBean observerLocationBean=new ObserverLocationBean();
    private final DateTimeBean dateTimeBean=new DateTimeBean();
    private final TimeAnimator timeAnimator=new TimeAnimator(dateTimeBean);
    private SkyCanvasManager skyCanvasManager;
    private final ViewingParametersBean viewingParametersBean =new ViewingParametersBean();

    private static final String playText = "\uf04b";  /** chaine de caractère représentant le bouton play **/
    private static final String pauseText = "\uf04c";  /** chaine de caractère représentant le bouton pause **/

    public static void main(String[] args){
        launch(args);
    }

    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hs = resourceStream("/hygdata_v3.csv")) {
            try (InputStream as = resourceStream("/asterisms.txt")) {
                StarCatalogue catalogue = new StarCatalogue.Builder()
                        .loadFrom(hs, HygDatabaseLoader.INSTANCE).loadFrom(as,AsterismLoader.INSTANCE)
                        .build();


                dateTimeBean.setZonedDateTime(ZonedDateTime.now());

                timeAnimator.setAccelerator(NamedTimeAccelerator.TIMES_300.getAccelerator());

                observerLocationBean.setCoordinates(
                        GeographicCoordinates.ofDeg(6.57, 46.52));


                viewingParametersBean.setCenter(
                        HorizontalCoordinates.ofDeg(180.000000000001, 42));
                viewingParametersBean.setFieldOfViewDeg(100.0);

                this.skyCanvasManager = new SkyCanvasManager(
                        catalogue,
                        dateTimeBean,
                        observerLocationBean,
                        viewingParametersBean);

                Canvas sky = skyCanvasManager.canvas();
                Pane root = new Pane(sky);

                sky.widthProperty().bind(root.widthProperty());
                sky.heightProperty().bind(root.heightProperty());

                BorderPane borderPane = new BorderPane(root, createControlBar(), null, createInformationBar(), null);

                primaryStage.setScene(new Scene(borderPane));
                primaryStage.setTitle("Rigel");


                primaryStage.setMinWidth(800);
                primaryStage.setMinHeight(600);

                primaryStage.show();

                sky.requestFocus();

                PopUp popUp = new PopUp();
                popUp.addMouseListener(new PopUpListener());
            }
        }
    }

    /**
     * méthode créant la barre de controle
     * @return la barre de controle
     */
    private HBox createControlBar(){
        HBox controlBar = new HBox(createObservationPosition(),new Separator(Orientation.VERTICAL),createObservationInstant(),new Separator(Orientation.VERTICAL),createTimeFLow());
        controlBar.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        return controlBar;
    }

    /**
     * méthode créant le sous-panneau contenant la position d'observation
     * @return le sous-panneau contenant la position d'observation
     */
    private HBox createObservationPosition(){
        HBox hBox = new HBox();
        hBox.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        hBox.getChildren().addAll(createLonLatNode(true,"Longitude (°) :"));
        hBox.getChildren().addAll(createLonLatNode(false,"Latitude (°) :"));
        return hBox;
    }

    /**
     * méthode créant le sous panneau représenant les coordonnées géographiques
     * @param isLon
     * si l'on souhaite représenté la longitude
     * @param label
     * texte associé
     * @return le sous panneau représenant les coordonnées géographiques sous forme d'une liste de noeud
     */
    private List<Node> createLonLatNode(boolean isLon, String label){
        Label text = new Label(label);

        NumberStringConverter stringConverter =
                new NumberStringConverter("#0.00");

        UnaryOperator<TextFormatter.Change> coordinates = (change -> {
            try {
                String newText =
                        change.getControlNewText();
                double newCoordinatesDeg =
                        stringConverter.fromString(newText).doubleValue();
                return ((isLon) ? GeographicCoordinates.isValidLonDeg(newCoordinatesDeg) : GeographicCoordinates.isValidLatDeg(newCoordinatesDeg))/** en fonction de l'argument vérifie si les nouvelles coordonnées sont valides pour la latitude ou la longitude**/
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });

        TextFormatter<Number> textFormatter = new TextFormatter<>(stringConverter, 0, coordinates);
        TextField hourTextField = new TextField();
        hourTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        hourTextField.setTextFormatter(textFormatter);
        if (isLon){
            textFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());
        } else {
            textFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());
        }

        return List.of(text,hourTextField);
        }


    /**
     * méthode créant le sous-panneau contenant l'instant d'observation
     * @return le sous-panneau contenant l'instant d'observation
     */
    private HBox createObservationInstant(){
        Label dateText = new Label("Date :");
        DatePicker datePicker = new DatePicker(ZonedDateTime.now().toLocalDate());
        datePicker.setStyle("-fx-pref-width: 120;");
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.disableProperty().bind(timeAnimator.getRunning()); /** désactive les liens quand l'animateur tourne**/

        Label text = new Label("Heure :");

        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter,ZonedDateTime.now().toLocalTime());

        TextField hourTextField = new TextField(ZonedDateTime.now().toLocalTime().toString());
        hourTextField.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");

        hourTextField.setTextFormatter(timeFormatter);
        timeFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());

        ObservableList<ZoneId> observableList = FXCollections.observableList(ZoneId.getAvailableZoneIds().stream().sorted().map(ZoneId::of).collect(Collectors.toList()));
        ComboBox timeZoneBox = new ComboBox(observableList);
        timeZoneBox.setValue(ZonedDateTime.now().getZone());
        timeZoneBox.setStyle("-fx-pref-width: 180;");
        dateTimeBean.zoneProperty().bindBidirectional(timeZoneBox.valueProperty());
        timeZoneBox.disableProperty().bind(timeAnimator.getRunning());

        HBox instantObservationBar = new HBox(dateText,datePicker,text,hourTextField,timeZoneBox);
        instantObservationBar.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        return instantObservationBar;
    }

    /**
     * méthode créant le sous-panneau contenant l'écoulement du temps
     * @return le sous-panneau contenant l'écoulement du temps
     */
    private HBox createTimeFLow() {

        try (InputStream fontStream = getClass().getResourceAsStream("/Font Awesome 5 Free-Solid-900.otf")) {
            ChoiceBox<NamedTimeAccelerator> choiceBox = new ChoiceBox<>();
            choiceBox.setItems(FXCollections.observableArrayList(NamedTimeAccelerator.values()));
            choiceBox.disableProperty().bind(timeAnimator.getRunning());
            choiceBox.setValue(NamedTimeAccelerator.TIMES_300);

            timeAnimator.timeAcceleratorProperty().bind(Bindings.select(choiceBox.valueProperty(),"accelerator"));

            Font fontAwesome = Font.loadFont(fontStream, 15);

            Button resetButton = new Button("\uf0e2");
            resetButton.setFont(fontAwesome);
            resetButton.setOnMouseClicked(event -> {
               dateTimeBean.setZonedDateTime(ZonedDateTime.now());
            });
            resetButton.disableProperty().bind(timeAnimator.getRunning());
            Button playPauseButton = new Button();
            playPauseButton.setFont(fontAwesome);

            playPauseButton.setOnMouseClicked(event -> {
                if (timeAnimator.getRunning().get()) {
                    timeAnimator.stop();
                } else {
                    timeAnimator.start();
                }
            });

            playPauseButton.textProperty().bind(Bindings.when(timeAnimator.getRunning()).then(pauseText).otherwise(playText));/** ce lien permet de modifier l'image du bouton pour que le bouton play soit affiché quand l'animation ne tourne pas et vise-versa**/

            HBox hBox=new HBox(choiceBox,resetButton,playPauseButton);
            hBox.setStyle("-fx-spacing: inherit;");
            return hBox;

        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * méthode créant la barre d'information'
     * @return la barre d'information'
     */
    private BorderPane createInformationBar() {
        Text leftText = new Text();
        leftText.textProperty().bind(Bindings.format("Champ de vue : %.1f°", viewingParametersBean.fieldOfViewDegProperty()));

        Text centerText = new Text();
        centerText.textProperty().bind(Bindings.createStringBinding(() -> {
            if (skyCanvasManager.objectUnderMouseProperty().getValue() == null) {
                return " ";
            } else {
                return skyCanvasManager.objectUnderMouseProperty().getValue().info();
            }
        }, skyCanvasManager.objectUnderMouseProperty()));

        Text rightText = new Text();
        rightText.textProperty().bind(Bindings.format("Azimut : %.2f°, hauteur : %.2f°", skyCanvasManager.mouseAzDegProperty(), skyCanvasManager.mouseAltDegProperty()));

        BorderPane borderPane = new BorderPane(centerText, null, rightText, null, leftText);
        borderPane.setStyle("-fx-padding: 4; -fx-background-color: white;");

        return borderPane;
    }
}
