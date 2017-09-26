package com.flagsAI.enums;

/**
 * Created by Siarhei.Beliabniou on 25.09.2017.
 */
public enum ImageRatioToWindow {
    FLAG(new double[]{230.0/517.0, 171.0/989.0}),
    MULTIPLAYERSEARCHING(new double[]{72.0/543.0, 72.0/1036.0}),
    MULTIPLAYERSEARCHING2(new double[]{72.0/543.0, 72.0/1036.0}),
    MAINMENUFLAGS(new double[]{252.0/543.0, 65.0/1036.0}),
    MULTIPLAYERGAME(new double[]{252.0/543.0, 35.0/1036.0}),
    LOSE(new double[]{204.0/543.0, 160.0/1036.0}),
    LOSEBUTTON(new double[]{74.0/543.0, 72.0/1036.0}),
    WIN(new double[]{302.0/543.0, 164.0/1036.0}),
    WINBUTTON(new double[]{75.0/543.0, 69/1036.0}),
    DRAW(new double[]{234.0/543.0, 195.0/1036.0});

    private double[] ratio;

    ImageRatioToWindow(double[] ratio){
        this.ratio = ratio;
    }

    public double[] getRatio() {
        return ratio;
    }
}
