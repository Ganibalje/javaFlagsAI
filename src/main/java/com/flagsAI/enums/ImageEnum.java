package com.flagsAI.enums;

/**
 * Created by ganib on 23.09.2017.
 */
public enum ImageEnum implements IEnum{
    FLAG(ImageRatioToWindow.FLAG),
    MULTIPLAYERSEARCHING(ImageRatioToWindow.MULTIPLAYERSEARCHING),
    MULTIPLAYERSEARCHING2(ImageRatioToWindow.MULTIPLAYERSEARCHING2),
    MAINMENUFLAGS(ImageRatioToWindow.MAINMENUFLAGS),
    MULTIPLAYEERGAME(ImageRatioToWindow.MULTIPLAYEERGAME),
    LOSE(ImageRatioToWindow.LOSE),
    LOSEBUTTON(ImageRatioToWindow.LOSEBUTTON),
    WIN(ImageRatioToWindow.WIN),
    WINBUTTON(ImageRatioToWindow.WINBUTTON);

    private ImageRatioToWindow imageRatioToWindow;

    ImageEnum(ImageRatioToWindow imageRatioToWindow){
        this.imageRatioToWindow = imageRatioToWindow;
    }

    public ImageRatioToWindow getImageRatioToWindow() {
        return imageRatioToWindow;
    }
}
