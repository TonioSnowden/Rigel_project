package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * HygDatabaseLoader
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */

public enum HygDatabaseLoader implements StarCatalogue.Loader {
    INSTANCE;

    /**
     * Type énuméré col qui permet de classer les colonnes du catalgue HYG
     *
     */
    private enum col {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try(BufferedReader b = new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.US_ASCII))){
            String r = b.readLine();
            while ((r = b.readLine()) != null){
                
                String[] s = r.split(",");

                int hip = s[HygDatabaseLoader.col.HIP.ordinal()].isEmpty() ? 0 :
                        Integer.parseInt(s[HygDatabaseLoader.col.HIP.ordinal()]);

                String name;
                if(s[HygDatabaseLoader.col.PROPER.ordinal()].isEmpty()){

                    String bayer = s[HygDatabaseLoader.col.BAYER.ordinal()].isEmpty() ? "?" :
                            s[HygDatabaseLoader.col.BAYER.ordinal()];

                    String con = s[HygDatabaseLoader.col.CON.ordinal()];

                    name = String.format("%s %s" , bayer, con);

                }else{
                    name = s[HygDatabaseLoader.col.PROPER.ordinal()];
                }

                double ra = Double.parseDouble(s[HygDatabaseLoader.col.RARAD.ordinal()]);

                double dec = Double.parseDouble(s[HygDatabaseLoader.col.DECRAD.ordinal()]);

                double mag = s[HygDatabaseLoader.col.MAG.ordinal()].isEmpty() ? 0 :
                        Double.parseDouble(s[HygDatabaseLoader.col.MAG.ordinal()]);

                double color = s[HygDatabaseLoader.col.CI.ordinal()].isEmpty() ? 0 :
                        Double.parseDouble(s[HygDatabaseLoader.col.CI.ordinal()]);

                Star star = new Star(hip,name, EquatorialCoordinates.of(ra,dec),(float)mag,(float)color);

                builder.addStar(star);


        }

    }

    }
}
