package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;

/**
 * bean contenant la position de l'observateur
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public class ObserverLocationBean {
    private DoubleProperty latDeg;
    private DoubleProperty lonDeg;
    private ObservableValue<GeographicCoordinates> coordinates;

    /**
     * Constructeur
     */
    public ObserverLocationBean() {
        this.latDeg = new SimpleDoubleProperty();
        this.lonDeg = new SimpleDoubleProperty();
        createObjectBinding();
    }

    /**
     * retourne les coordonnées géographiques
     * @return
     */
    public GeographicCoordinates getCoordinates() {
        return coordinates.getValue();
    }

    /**
     * retourne les coordonnées géographiques observables
     * @return
     */
    public ObservableValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * modifie la valeur des coordonnées géographiques
     * @param coordinates
     */
    public void setCoordinates(GeographicCoordinates coordinates) {
        latDeg.setValue(coordinates.latDeg());
        lonDeg.setValue(coordinates.lonDeg());
    }

    /**
     * retourne la longitude en degrés
     * @return
     */
    public Double getLonDeg() {
        return lonDeg.get();
    }

    /**
     * retourne la longitude en degrés de la propriété
     * @return
     */
    public DoubleProperty lonDegProperty() {
        return lonDeg;
    }

    /**
     * modifie la longitude en degrés
     * @param lonDeg
     */
    public void setLonDeg(Double lonDeg) {
        this.lonDeg.set(lonDeg);
    }

    /**
     * retourne la latitude en degrés
     * @return
     */
    public Double getLatDeg() {
        return latDeg.get();
    }

    /**
     * retourne la latitude en degrés de la propriété
     * @return
     */
    public DoubleProperty latDegProperty() {
        return latDeg;
    }

    /**
     * modifie la valeur de la latitude en degrés
     * @param latDeg
     */
    public void setLatDeg(Double latDeg) {
        this.latDeg.set(latDeg);
    }

    /**
     * crée un objet aux coordonnées données
     */
    private void createObjectBinding() {
        this.coordinates = Bindings.createObjectBinding(
                () -> GeographicCoordinates.ofDeg(lonDeg.getValue(), latDeg.getValue()), lonDeg, latDeg);
    }
}
