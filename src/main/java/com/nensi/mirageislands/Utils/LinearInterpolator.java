package com.nensi.mirageislands.Utils;

public class LinearInterpolator {
    public double[] interpolate(double from, double to, int max) {
        final double[] res = new double[max];
        for (int i = 0; i < max; i++) {
            res[i] = from + i * ((to - from) / (max - 1));
        }
        return res;
    }
}
