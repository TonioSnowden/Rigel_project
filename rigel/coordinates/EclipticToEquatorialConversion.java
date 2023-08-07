package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.Epoch;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

/**
 * un changement de système de coordonnées depuis les coordonnées écliptiques vers les coordonnées équatoriales, à un instant donné.
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final ZonedDateTime when;
    private final double T;
    private final static Polynomial polyODE=Polynomial.of(Angle.ofArcsec(0.00181),
            -(Angle.ofArcsec( 0.0006)),
            -(Angle.ofArcsec(46.815)),
            Angle.ofDMS(23, 26, 21.45));
    private final double oDE;
    private final double cos;
    private final double sin;

    /**
     * Construit un changement de système de coordonnées entre les coordonnées écliptiques et les coordonnées équatoriales pour un couple date/heure.
     *
     * @param when
     *         un couple date/heure
     */
    public EclipticToEquatorialConversion(ZonedDateTime when){
        this.when=when;
        this.T = Epoch.J2000.julianCenturiesUntil(when);
        this.oDE = polyODE.at(T);
        this.cos=Math.cos(oDE);
        this.sin=Math.sin(oDE);
    }

    @Override
    public EquatorialCoordinates apply(EclipticCoordinates eclipticCoordinates) {
        double lon = eclipticCoordinates.lon();
        double lat = eclipticCoordinates.lat();

        double rightAscension = Math.atan2(Math.sin(lon)*cos
                -Math.tan(lat)*sin,
                Math.cos(lon));
        double declination = Math.asin(Math.sin(lat)*cos
                + Math.cos(lat)*sin*Math.sin(lon));
        EquatorialCoordinates equatorialCoordinates = EquatorialCoordinates.of(Angle.normalizePositive(rightAscension), declination);
        return equatorialCoordinates;
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

}