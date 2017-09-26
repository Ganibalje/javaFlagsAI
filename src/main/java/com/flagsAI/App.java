package com.flagsAI;

import com.flagsAI.brain.Brain;
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

    public static void main( String[] args ) throws IOException, InterruptedException, TesseractException, AWTException {
        Brain brain = Brain.getInstance();
        brain.doShit();
    }
}
