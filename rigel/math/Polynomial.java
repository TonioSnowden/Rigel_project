package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * Polynomial
 * @author Alexis Firome (314496)
 * @author Antoine Munier (314500)
 */
public final class Polynomial {

    private final double[] coefficients;

    /**
     * Constructeur
     * @param coefficients
     */
    private Polynomial(double[] coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Donne les coefficients du polynome par ordre décroissant selon la puissance de x
     * @param coefficientN
     * @param coefficients
     * @return le polynome ordonné
     */
    public static Polynomial of(double coefficientN, double... coefficients) {
        Preconditions.checkArgument(coefficientN != 0 );
        double[] orderedCoeff = new double[coefficients.length + 1];
        orderedCoeff[0] = coefficientN;
        System.arraycopy(coefficients, 0, orderedCoeff, 1, coefficients.length);

        return new Polynomial(orderedCoeff);
    }

    /**
     * Donne le polynome sous la forme de Horner
     * @param x
     * @return la forme de Horner "y"
     */
    public double at(double x) {
        double y = coefficients[0];
        for (int i = 1; i < coefficients.length; ++i) {
            y = y * x + coefficients[i];
        }
        return y;
    }
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < coefficients.length -1; ++i) {
            if(coefficients[i] != 0){
                if(coefficients[i]!=1){
                    if(coefficients[i]!=-1){
                        string.append(coefficients[i]);
                    } else {
                        string.append("-");
                    }
                }
                string.append("x");
                if(coefficients.length-i-1>1){
                    string.append("^");
                    string.append(coefficients.length-i-1);
                } if(coefficients[i+1]> 0) {
                    string.append("+");
                }
            }
        }
        string.append(coefficients[coefficients.length-1]);
            return string.toString();
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


