package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Bean contenant l'instant d'observation
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public final class DateTimeBean {
    private ObjectProperty<LocalDate> date;
    private ObjectProperty<LocalTime> time;
    private ObjectProperty<ZoneId> zone;

    /**
     * Constructeur
     */
    public DateTimeBean(){
        this.date=new SimpleObjectProperty<>();
        this.time=new SimpleObjectProperty<>();
        this.zone=new SimpleObjectProperty<>();
    }

    /**
     * retourne la date
     * @return date
     */
    public ObjectProperty<LocalDate> dateProperty(){
        return date;
    }

    /**
     * retourne la date de la propriété
     * @return
     */
    public LocalDate getDate(){
        return date.get();
    }

    /**
     * modifie la valeur de la date
     * @param date
     */
    public void setDate(LocalDate date){
        this.date.set(date);
    }

    /**
     * retourne le temps de la propriété
     * @return
     */
    public ObjectProperty<LocalTime> timeProperty(){
        return time;
    }

    /**
     * retourne le temps
     * @return
     */
    public LocalTime getTime(){
        return time.get();
    }

    /**
     * modifie la valeur du temps
     * @param time
     */
    public void setTime(LocalTime time){
        this.time.set(time);
    }

    /**
     * retourne la zone de propriété
     * @return
     */
    public ObjectProperty<ZoneId> zoneProperty(){
        return zone;
    }

    /**
     * retourne la zone
     * @return
     */
    public ZoneId getZone(){
        return zone.get();
    }

    /**
     * modifie la valeur de la zone
     * @param zone
     */
    public void setZone(ZoneId zone){
        this.zone.set(zone);
    }

    /**
     * retourne la zoneDateTime avec la zone la date et le temps
     * @return
     */
    public ZonedDateTime getZonedDateTime(){
        return ZonedDateTime.of(getDate(),getTime(),getZone());
    }

    /**
     * modifie la valeur de la zoneDateTime
     * @param zonedDateTime
     */
    public void setZonedDateTime(ZonedDateTime zonedDateTime){
        setDate(zonedDateTime.toLocalDate());
        setTime(zonedDateTime.toLocalTime());
        setZone(zonedDateTime.getZone());
    }
}
