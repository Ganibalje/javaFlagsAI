package com.flagsAI.enums;

/**
 * Created by ganib on 23.09.2017.
 */
public enum ImageEnum implements IEnum{
    MULTYPLAYERBUTTON,
    MULTIPLAYERSEARCHINGFLAGS,
    MULTIPLAYERSEARCHINGFLAGS2,
    NICKNAME;

    private ImageRatioToWindow imageRatioToWindow;

    ImageEnum(ImageRatioToWindow imageRatioToWindow){
        this.imageRatioToWindow = imageRatioToWindow;
    }

    public ImageRatioToWindow getImageRatioToWindow() {
        return imageRatioToWindow;
    }
}
