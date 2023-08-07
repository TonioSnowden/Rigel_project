package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;

import java.time.ZonedDateTime;

/**
 * TimeAnimator
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */


public final class TimeAnimator extends AnimationTimer {
    private DateTimeBean dateTimeBean;
    private ObjectProperty<TimeAccelerator> accelerator;
    private SimpleBooleanProperty running;
    private long initialTime;
    private ZonedDateTime origin;

    public TimeAnimator(DateTimeBean dateTimeBean){
        this.dateTimeBean=dateTimeBean;
        this.accelerator=new SimpleObjectProperty<>();
        this.running= new SimpleBooleanProperty();
        setRunning(false);
    }

    /**
     * modifie le temps d'accéleration
     * @param timeAccelerator
     */
    public void setAccelerator(TimeAccelerator timeAccelerator){
        accelerator.set(timeAccelerator);
    }

    /**
     * retourne le temps d'accéleration
     * @return
     */
    public TimeAccelerator getAccelerator(){
        return accelerator.get();
    }

    /**
     * retourne le temps d'accéleration de la propriété
     * @return
     */
    public ObjectProperty<TimeAccelerator> timeAcceleratorProperty(){
        return accelerator;
    }

    /**
     * modifie l'état du running
     * @param state
     */
    private void setRunning(boolean state){
        running.set(state);
    }

    /**
     * retourne l'état du running
     * @return
     */
    public ObservableBooleanValue getRunning(){ return running; }

    @Override
    public void handle(long now) {
        if (origin ==null){
            initialTime = now;
            origin = dateTimeBean.getZonedDateTime();
        } else {
            long realElapsedTime = now - initialTime;
            dateTimeBean.setZonedDateTime(accelerator.get().adjust(origin, realElapsedTime));
        }
    }

    @Override
    public void start(){
        setRunning(true);
        origin =null;
        initialTime=0;
        super.start();
    }

    @Override
    public void stop(){
        super.stop();
        setRunning(false);
    }
}
