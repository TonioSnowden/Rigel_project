package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * Conversion des coordonnées équatoriales vers horizontales
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private final double Sl;
    private final double sinΦ;
    private final double cosΦ;

    /**
     * Constructeur
     * @param when
     * date/heure des coordonnées équatoriales
     * @param where
     *lieu des coordonnées équatoriales
     */
    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where){
        this.Sl = SiderealTime.local(when,where);
        this.cosΦ = Math.cos(where.lat());
        this.sinΦ = Math.sin(where.lat());
    }

    @Override
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {
        double H = Sl - equ.ra();
        double h = Math.sin(equ.dec()) * sinΦ +
                Math.cos(equ.dec()) * cosΦ * Math.cos(H);
        double azimut = Math.atan2(-Math.cos(equ.dec()) * cosΦ * Math.sin(H),
                (Math.sin(equ.dec()) - sinΦ * h));
        return HorizontalCoordinates.of(Angle.normalizePositive(azimut),Math.asin(h));
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
