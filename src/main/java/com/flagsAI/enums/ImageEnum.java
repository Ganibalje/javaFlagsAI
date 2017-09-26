package com.flagsAI.enums;

/**
 * Created by ganib on 23.09.2017.
 */
public enum ImageEnum implements IEnum{
    LOSE(ImageRatioToWindow.LOSE),
    DRAW(ImageRatioToWindow.DRAW),
    WIN(ImageRatioToWindow.WIN),
    MULTIPLAYERGAME(ImageRatioToWindow.MULTIPLAYERGAME),
    MULTIPLAYERSEARCHINGFLAG(ImageRatioToWindow.MULTIPLAYERSEARCHING),
    MULTIPLAYERSEARCHINGFLAG2(ImageRatioToWindow.MULTIPLAYERSEARCHING2),
    MAINMENUFLAGS(ImageRatioToWindow.MAINMENUFLAGS),
    LOSEBUTTON(ImageRatioToWindow.LOSEBUTTON),
    WINBUTTON(ImageRatioToWindow.WINBUTTON),
    STARTGAME(ImageRatioToWindow.STARTGAME),
    GOBACK(ImageRatioToWindow.GOBACK);

    private ImageRatioToWindow imageRatioToWindow;

    ImageEnum(ImageRatioToWindow imageRatioToWindow){
        this.imageRatioToWindow = imageRatioToWindow;
    }

    public ImageRatioToWindow getImageRatioToWindow() {
        return imageRatioToWindow;
    }
}
