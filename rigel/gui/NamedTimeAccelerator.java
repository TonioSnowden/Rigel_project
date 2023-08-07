package ch.epfl.rigel.gui;

import java.time.Duration;

/**
 * Enum NamedTimeAccelerator
 * @author Alexis Firome(314496)
 * @author Antoine Munier (314500)
 */

public enum NamedTimeAccelerator {
    TIMES_1("1x",TimeAccelerator.continuous(1)),
    TIMES_30("30x",TimeAccelerator.continuous(30)),
    TIMES_300("300x",TimeAccelerator.continuous(300)),
    TIMES_3000("3000x",TimeAccelerator.continuous(3000)),
    DAY("jour",TimeAccelerator.discrete(60,Duration.ofHours(24))),
    SIDEREAL_DAY("jour sidéral",TimeAccelerator.discrete(60,Duration.ofSeconds(86164)));

    private final String string;
    private final TimeAccelerator timeAccelerator;

    /**
     * Constructeur
     * @param string
     * @param timeAccelerator
     */
    NamedTimeAccelerator(String string, TimeAccelerator timeAccelerator){
        this.string=string;
        this.timeAccelerator=timeAccelerator;
    }

    /**
     * retourne le name
     * @return
     */
    public String getName() {
        return this.string;
    }

    /**
     * retourne le temps d'accéleration
     * @return
     */
    public TimeAccelerator getAccelerator(){
        return this.timeAccelerator;
    }

    @Override
    public String toString(){
        return this.string;
    }

}
