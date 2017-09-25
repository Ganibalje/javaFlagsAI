package com.flagsAI.enums;

/**
 * Created by Siarhei.Beliabniou on 25.09.2017.
 */
public enum ImageRatioToWindow {
    FLAG(new double[]{230/517, 171/989});

    private double[] ratio;

    ImageRatioToWindow(double[] ratio){
        this.ratio = ratio;
    }

    public double[] getRatio() {
        return ratio;
    }
}
