package com.flagsAI.enums;

/**
 * Created by ganib on 23.09.2017.
 */
public enum ImageEnum implements IEnum{
    MULTIPLAYERSEARCHINGFLAG(ImageRatioToWindow.MULTIPLAYERSEARCHING),
    MULTIPLAYERSEARCHINGFLAG2(ImageRatioToWindow.MULTIPLAYERSEARCHING2),
    MAINMENUFLAGS(ImageRatioToWindow.MAINMENUFLAGS),
    MULTIPLAYERGAME(ImageRatioToWindow.MULTIPLAYERGAME),
    LOSE(ImageRatioToWindow.LOSE),
    LOSEBUTTON(ImageRatioToWindow.LOSEBUTTON),
    WIN(ImageRatioToWindow.WIN),
    WINBUTTON(ImageRatioToWindow.WINBUTTON),
    DRAW(ImageRatioToWindow.DRAW),
    STARTGAME(ImageRatioToWindow.STARTGAME);

    private ImageRatioToWindow imageRatioToWindow;

    ImageEnum(ImageRatioToWindow imageRatioToWindow){
        this.imageRatioToWindow = imageRatioToWindow;
    }

    public ImageRatioToWindow getImageRatioToWindow() {
        return imageRatioToWindow;
    }
}
