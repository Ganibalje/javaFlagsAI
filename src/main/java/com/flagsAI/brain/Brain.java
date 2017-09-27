package com.flagsAI.brain;

import com.flagsAI.enums.FlagEnum;
import com.flagsAI.enums.ImageEnum;
import com.flagsAI.enums.ImageRatioToWindow;
import com.flagsAI.enums.ScreenType;
import com.flagsAI.eye.Eye;
import com.flagsAI.hand.Hand;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Brain {
    private static Brain instance;
    private Eye eye = Eye.getInstance();
    private Hand hand  = Hand.getInstance(eye.getWindow());
    private String lastMessage = "";

    private Brain() throws AWTException {

    }

    public static Brain getInstance() throws AWTException {
        if(instance == null){
            instance = new Brain();
        }
        return instance;
    }

    public void doShit() throws IOException, TesseractException, InterruptedException {
        String lastCountryName = "";

        while (true) {
            ScreenType screenType = eye.recognizeScreenType();
            if (screenType != null) {
                switch (screenType) {
                    case MENU: {
                        //click to multiplayer start
                        Rectangle startRect = eye.findImageOnScreen(ImageEnum.STARTGAME, ImageEnum.STARTGAME.getImageRatioToWindow());
                        hand.click(startRect);
                        printMessage("Menu");
                        break;
                    }
                    case GAME_SEARCHING:{
                        printMessage("Game searching");
                        break;
                    }
                    case DRAW_SCREEN:{} //go down
                    case LOSE_SCREEN:{} //go down
                    case WIN_SCREEN:{
                        //click to restart
                        Rectangle rect = eye.findImageOnScreen(ImageEnum.GOBACK, ImageEnum.GOBACK.getImageRatioToWindow());
                        hand.click(rect);
                        printMessage("Click to restart");
                        break;
                    }
                    case ADS:{
                        Thread.sleep(5000);
                        Rectangle rect = eye.findImageOnScreen(ImageEnum.GOBACK, ImageEnum.GOBACK.getImageRatioToWindow());
                        hand.click(rect);
                    }
                    case GAME: {
                        Thread.sleep(1500);
                        BufferedImage screenshot = eye.getScreenshot();
                        String countryName = eye.recognizeCountryName(screenshot);
                        if(!lastCountryName.equals(countryName)) {
                            if(FlagEnum.contains(countryName)) {
                                FlagEnum flagByCountryName = eye.getFlagByCountryName(countryName);
                                Rectangle countryBounds = eye.findImageOnScreen(flagByCountryName, ImageRatioToWindow.FLAG);
                                lastCountryName = countryName;
                                if (countryBounds == null) {
                                    ImageIO.write(screenshot, "png", new File("C://cant_find_images/" + countryName + new Date().toString() + ".png"));
                                    System.out.println("Flag of " + countryName + " cant be found");
                                } else {
                                    hand.click(countryBounds);
                                    printMessage("Flag of county " + countryName + " can be founded at\n" + countryBounds + "\n");
                                }
                            } else {
                                System.out.println("Can't recognize country with name " + countryName);
                            }
                        } else {
                            printMessage("Waiting for player");
                        }
                        break;
                    }

                }
            }
        }
    }

    private void printMessage(String message){
        if(!message.equals(lastMessage)){
            System.out.println(message);
            lastMessage = message;
        }
    }

}
