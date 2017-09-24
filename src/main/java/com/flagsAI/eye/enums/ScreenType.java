package com.flagsAI.eye.enums;

/**
 * Created by ganib on 23.09.2017.
 */
public enum ScreenType {
    MENU,
    GAME,
    ADS,
    GAME_SEARCHING,
    WIN_SCREEN,
    DRAW_SCREEN;



    public static ScreenType getScreenTypeByImage(ImageEnum image){
        switch (image){
            case MULTYPLAYERBUTTON:
                return MENU;
            case MULTIPLAYERSEARCHINGFLAGS:
                return GAME_SEARCHING;
            case MULTIPLAYERSEARCHINGFLAGS2:
                return GAME_SEARCHING;
            case NICKNAME:
                return GAME;
            default:
                return ADS;
        }
    }
}
