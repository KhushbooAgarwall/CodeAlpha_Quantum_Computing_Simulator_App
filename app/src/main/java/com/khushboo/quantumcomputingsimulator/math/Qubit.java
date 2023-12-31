package com.khushboo.quantumcomputingsimulator.math;

import android.util.Log;

import java.util.Random;

/**
 * Class representing a qubit
 * Basically worthless without VisualOperator(s)
 */
public class Qubit {

    public Complex[] matrix;
    private Random random;

    public Qubit() {
        prepare(false);
        random = new Random();
    }

    public Qubit(Complex[] complex) {
        if (complex != null && complex.length == 2) {
            matrix = complex;
            random = new Random();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void prepare(boolean value) {
        matrix = new Complex[] {new Complex(value ? 0 : 1), new Complex(value ? 1 : 0)};
    }

    public void zeroVector() {
        matrix = new Complex[] {new Complex(0), new Complex(0)};
    }

    public boolean measureZ() {
        double prob0 = Complex.multiply(Complex.conjugate(matrix[0]), matrix[0]).real;
        double prob1 = Complex.multiply(Complex.conjugate(matrix[1]), matrix[1]).real;
        if (prob0 + prob1 < 0.9999 || prob0 + prob1 > 1.0001) {
            Log.e("QBIT Error", "Too HIGH/low probability sum:\t" + (prob0 + prob1));
        }
        double rand = random.nextDouble();
        boolean value = rand > prob0;
        prepare(value);
        return value;
    }

    public boolean dirtyStateCheck() {
        return matrix[1].real == 1;
    }

    public void applyOperator(VisualOperator visualOperator) throws IllegalArgumentException {
        if (visualOperator.isMultiQubit()) throw new IllegalArgumentException("Only SQG are allowed here!");
        matrix = visualOperator.operateOn(new Qubit[]{this})[0].copy().matrix;
    }

    public String toString() {
        return matrix[0].toString3Decimals() + "\n" + matrix[1].toString3Decimals();
    }

    public String toStringModArg() {
        return matrix[0].toStringModArg() + "\n" + matrix[1].toStringModArg();
    }

    public Qubit copy() {
        Complex[] matrix2 = new Complex[2];
        matrix2[0] = matrix[0].copy();
        matrix2[1] = matrix[1].copy();
        return new Qubit(matrix2);
    }
}
