package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Bean contenant les paramètres déterminant la portion du ciel visible sur l'image
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public class ViewingParametersBean {
    private ObjectProperty<Double>  fieldOfViewDeg;
    private ObjectProperty<HorizontalCoordinates> center;

    /**
     * Constructeur
     */
    public ViewingParametersBean(){
        this.center=new SimpleObjectProperty<>();
        this.fieldOfViewDeg=new SimpleObjectProperty<>();
    }

    /**
     * retourne le domaine de vue en degrés
     * @return
     */
    public Double getFieldOfViewDeg() {
        return fieldOfViewDeg.get();
    }

    /**
     * retourne le domaine de vue de la propriété en degrés
     * @return
     */
    public ObjectProperty<Double> fieldOfViewDegProperty() {
        return fieldOfViewDeg;
    }

    /**
     * modifie la valeur du domaine de vue en degrés
     * @param fieldOfViewDeg
     */
    public void setFieldOfViewDeg(Double fieldOfViewDeg) {
        this.fieldOfViewDeg.set(fieldOfViewDeg);
    }

    /**
     * retourne le centre du domaine de vue
     * @return
     */
    public HorizontalCoordinates getCenter() {
        return center.get();
    }

    /**
     * retourne le centre du domaine de la propriété
     * @return
     */
    public ObjectProperty<HorizontalCoordinates> centerProperty() {
        return center;
    }

    /**
     * modifie la valeur du centre du domaine de vue
     * @param center
     */
    public void setCenter(HorizontalCoordinates center) {
        this.center.set(center);
    }
}
