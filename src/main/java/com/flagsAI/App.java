package com.flagsAI;

import com.flagsAI.enums.ImageRatioToWindow;
import com.flagsAI.eye.Eye;
import com.flagsAI.enums.FlagEnum;
import com.flagsAI.enums.ScreenType;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    static {
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    public static void main( String[] args ) throws IOException, InterruptedException, TesseractException {
        Eye eye = Eye.getInstance();
//        long time = new Date().getTime();
//        eye.templateMatching();
//        long time1 = new Date().getTime();
//        System.out.println(time1 - time);

        while (true) {
            ScreenType screenType = eye.recognizeScreenType();
            if (screenType != null) {
                switch (screenType) {
                    case GAME: {
                        String s = eye.recognizeCountryName(eye.getScreenshot());
                        System.out.println("Flag of county " + s + " can be founded at");
                        FlagEnum flagByCountryName = eye.getFlagByCountryName(s);
                        Rectangle countryBounds = eye.findImageOnScreen(flagByCountryName, ImageRatioToWindow.FLAG);
                        System.out.println(countryBounds);
                    }
                }
            }
        }

    }
}
