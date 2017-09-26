package com.flagsAI.enums;

/**
 * Created by Siarhei.Beliabniou on 25.09.2017.
 */
public enum ImageRatioToWindow {
    FLAG(new double[]{230/517, 171/989}),
    MULTIPLAYERSEARCHING(new double[]{72/543, 72/1036}),
    MULTIPLAYERSEARCHING2(new double[]{72/543, 72/1036}),
    MAINMENUFLAGS(new double[]{252/543, 65/1036}),
    MULTIPLAYERGAME(new double[]{252, 543, 35/1036}),
    LOSE(new double[]{204/543, 160/1036}),
    LOSEBUTTON(new double[]{74/543, 72/1036}),
    WIN(new double[]{302/543, 164/1036}),
    WINBUTTON(new double[]{75/543, 69/1036}),
    DRAW(new double[]{234/543, 195/1036});

    private double[] ratio;

    ImageRatioToWindow(double[] ratio){
        this.ratio = ratio;
    }

    public double[] getRatio() {
        return ratio;
    }
}
