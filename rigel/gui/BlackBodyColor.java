package ch.epfl.rigel.gui;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

/**
 * BlackBodyColor
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public class BlackBodyColor {

    private static final List<String> colorList = colorList("/bbr_color.txt");

    /**
     * renvoit la couleur par rapport à la température de l'étoile
     * @param temperature
     * @return la couleur associée à la température de l'étoile
     */
    public static Color colorForTemperature(int temperature) {
        return Color.web(colorList.get(getIndexOnTheColorList(roundTo100(temperature))));
    }

    /**
     * méthode privée qui liste les couleurs du dossier ressource
     * @param text
     * @return une liste de string avec les couleurs disponibles
     */
    private static List<String> colorList(String text){
        InputStream inputStream = BlackBodyColor.class.getResourceAsStream(text);
        List<String> colorList = new ArrayList<>();
        try (BufferedReader b = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII))) {
            String line = "";
            while (((line = b.readLine()) != null)){
                if ((line.charAt(0) != '#') && (line.charAt(10) == '1')){
                    String color = line.substring(80,87);
                    colorList.add(color);
                }
        }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return colorList;
    }

    /**
     * méthode privée qui donne l'index de la couleur dans la list de couleur
     * @param roundedTemperature
     * @return l'index de la colorlist
     */

    private static int getIndexOnTheColorList(int roundedTemperature){
        return (roundedTemperature-1000)/100;
    }

    /**
     * méthode privée qui permet d'avoir la température en kelvin transformée pour correspondre à la couleur de l'étoile
     * @param temperatureInKelvin
     * @return une température
     */
    private static int roundTo100(double temperatureInKelvin){
        int tens = (int) (temperatureInKelvin % 100);
        return (int) (tens > 50 ? temperatureInKelvin + (100-tens) : temperatureInKelvin - tens);
    }
}
