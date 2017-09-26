package com.flagsAI.enums;

/**
 * Created by ganib on 23.09.2017.
 */
public enum ScreenType {
    MENU,
    GAME,
    ADS,
    GAME_SEARCHING,
    WIN_SCREEN,
    LOSE_SCREEN,
    DRAW_SCREEN;



    public static ScreenType getScreenTypeByImage(ImageEnum image){
        switch (image){
            case MAINMENUFLAGS:
                return MENU;
            case MULTIPLAYERSEARCHING:
                return GAME_SEARCHING;
            case MULTIPLAYERSEARCHING2:
                return GAME_SEARCHING;
            case MULTIPLAYEERGAME:
                return GAME;
            case WIN:
                return WIN_SCREEN;
            case LOSE:
                return LOSE_SCREEN;
            case DRAW:
                return DRAW_SCREEN;
            default:
                return ADS;
        }
    }
}
