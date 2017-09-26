package com.flagsAI.brain;

import com.flagsAI.enums.FlagEnum;
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
    private Eye eye = Eye.getInstance();
    private Hand hand;
    private String lastMessage = "";

    public void doShit() throws IOException, TesseractException {
        String lastCountryName = "";

        while (true) {
            ScreenType screenType = eye.recognizeScreenType();
            if (screenType != null) {
                switch (screenType) {
                    case MENU: {
                        //click to multiplayer start
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
                        printMessage("Click to restart");
                        break;
                    }
                    case GAME: {
                        BufferedImage screenshot = eye.getScreenshot();
                        String countryName = eye.recognizeCountryName(screenshot);
                        if(!lastCountryName.equals(countryName)) {
                            FlagEnum flagByCountryName = eye.getFlagByCountryName(countryName);
                            Rectangle countryBounds = eye.findImageOnScreen(flagByCountryName, ImageRatioToWindow.FLAG);
                            lastCountryName = countryName;
                            if(countryBounds == null){
//                                hand.click(countryBounds);
                                ImageIO.write(screenshot, "png", new File("D://cant_find_images/" + countryName + new Date().getTime()));
                            } else {
                                printMessage("Flag of county " + countryName + " can be founded at\n" + countryBounds);
                            }
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
