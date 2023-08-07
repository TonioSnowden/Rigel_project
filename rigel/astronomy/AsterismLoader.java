package ch.epfl.rigel.astronomy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AsterismLoader
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */

public enum AsterismLoader implements StarCatalogue.Loader {
    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try(BufferedReader b = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))){
            String l ;
            String[] s;
            List<Star> astar = new ArrayList<>();

            while ((l = b.readLine())!=null){
                s = l.split(",");
                 for(String hip : s){
                     int numHip = Integer.parseInt(hip);
                     for(Star star : builder.stars()){
                         if(star.hipparcosId() == numHip){
                             astar.add(star);
                         }
                     }
                 }
                 Asterism asterism = new Asterism(astar);
                 builder.addAsterism(asterism);
                 astar.clear();
            }
        }
    }
}
