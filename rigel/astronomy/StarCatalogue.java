package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Un catalogue d'étoile
 *
 * @author Antoine Munier (314500)
 * @author Alexis Firome (314496)
 */
public final class StarCatalogue {
    private final List<Star> stars;
    private final Map<Asterism, List<Integer>> starsIndex;

    /**
     * Construit un catalogue d'étoile
     *
     * @param stars
     * Liste d'étoile
     * @param asterisms
     * Liste d'astérisme
     */
    public StarCatalogue(List<Star> stars, List<Asterism> asterisms){
        for (Asterism asterism : asterisms){
            if (!(stars.containsAll(asterism.stars()))) {
                throw new IllegalArgumentException();
            }
        }
        this.starsIndex=Collections.unmodifiableMap(starsIndex(stars,asterisms));
        this.stars= List.copyOf(stars);
    }

    /**
     * Retourne la liste d'étoile du catalogue
     * @return la liste d'étoile du catalogue
     */
    public List<Star> stars(){
        return stars;
    }

    /**
     * Retourne l'ensemble des astérismes du catalogue
     * @return l'ensemble des astérismes du catalogue
     */
    public Set<Asterism> asterisms(){
        return starsIndex.keySet();
    }

    /**
     * retourne la liste des index dans le catalogue des étoiles constituant l'astérisme donné
     * @param asterism
     * un astérisme
     * @return retourne la liste des index dans le catalogue des étoiles constituant l'astérisme donné
     */
    public List<Integer> asterismIndices(Asterism asterism){
        if (!(starsIndex.keySet().contains(asterism))){
            throw new IllegalArgumentException();
        }
        return starsIndex.get(asterism);
    }

    /**
     * Construit une table associative dont les clefs sont les astérismes et les valeurs sont les listes d'index correspondantes
     * @param stars
     * Liste d'étoile du catalogue
     * @param asterisms
     * Liste d'asterisme du catalogue
     * @return
     */
    private Map<Asterism, List<Integer>> starsIndex(List<Star> stars,List<Asterism> asterisms){
        Map<Asterism, List<Integer>> starsIndexConstructor = new HashMap<>();
        for (Asterism asterism : asterisms){
            List<Integer> index = new ArrayList<>();
            List<Star> starList = asterism.stars();
            for (Star star : starList){
                index.add(stars.indexOf(star));
            }
            starsIndexConstructor.put(asterism, Collections.unmodifiableList(index));
        }
        return starsIndexConstructor;
    }

    /**
     * Un constructeur de catalogue d'étoile
     *
     * @author Antoine Munier (314500)
     * @author Alexis Firome (314496)
     */
    public final static class Builder {
        private List<Star> stars;
        private List<Asterism> asterisms;

        /**
         * Initialise les attributs du constructeur
         */
        public Builder(){
            stars = new ArrayList<>();
            asterisms = new ArrayList<>();
        }

        /**
         * Ajoute une étoile
         * @param star
         * étoile
         * @return le constructeur avec l'étoile ajoutée
         */
        public Builder addStar(Star star) {
            stars.add(star);
            return this;
        }

        /**
         * Retourne une vue non modifiable de la Liste d'étoile du constructeur
         * @return une vue non modifiable de la Liste d'étoile du constructeur
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(stars);
        }

        /**
         * Ajoute un astérisme
         * @param asterism
         * astérisme
         * @return le constructeur avec l'astérisme ajouté
         */
        public Builder addAsterism(Asterism asterism) {
            asterisms.add(asterism);
            return this;
        }

        /**
         * Retourne une vue non modifiable de la Liste d'astérisme du constructeur
         * @return une vue non modifiable de la Liste d'astérisme du constructeur
         */
        public List<Asterism> asterisms() {
            return Collections.unmodifiableList(asterisms);
        }

        /**
         * Demande au chargeur loader d'ajouter au catalogue les étoiles et/ou astérismes qu'il obtient depuis le flot d'entrée
         * @param inputStream
         * Flot d'entrée
         * @param loader
         * chargeur loader
         * @return le constructeur
         * @throws IOException
         * en cas d'erreur d'entrée/sortie,
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * retourne le catalogue contenant les étoiles et astérismes ajoutés jusqu'alors au bâtisseur.
         * @return le catalogue contenant les étoiles et astérismes ajoutés jusqu'alors au bâtisseur.
         */
        public StarCatalogue build() {
            return new StarCatalogue(this.stars(), this.asterisms());
        }
    }

    /**
     * Un chargeur de catalogue d'étoiles et d'astérismes.
     *
     * @author Antoine Munier (314500)
     * @author Alexis Firome (314496)
     */
    public interface Loader {
        /**
         * charge les étoiles et/ou astérismes du flot d'entrée et les ajoute au catalogue en cours de construction du bâtisseur
         * @param inputStream
         * Flot d'entrée
         * @param builder
         * batisseur
         * @throws IOException
         * en cas d'erreur d'entrée/sortie.
         */
         public abstract void load(InputStream inputStream, Builder builder) throws IOException;
    }
}
