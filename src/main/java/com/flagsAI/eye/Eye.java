package com.flagsAI.eye;

import com.flagsAI.enums.*;
import com.flagsAI.utils.ImageUtils;
import com.sun.jna.platform.win32.GDI32Util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by ganib on 23.09.2017.
 */
public class Eye {
    private static final String WINDOW_NAME = "BlueStacks";
    private static final String TESSDATAFOLDER = "C:\\Program Files (x86)\\Tesseract-OCR";
    private static final int TEMPLATE_DETECTION_FUNCTION = Imgproc.TM_CCOEFF_NORMED;

    private static Eye instance;
    private static ITesseract ocr = new Tesseract();

    private WinDef.HWND windowHandler;
    private Rectangle window;
    private ScreenType screenType;
    private Map<ImageEnum, String> images;
    private Map<FlagEnum, String> flags;

    private Eye() {
        this.windowHandler = User32.INSTANCE.FindWindow(null, WINDOW_NAME);
        if(windowHandler != null) {
            WinDef.RECT rect = new WinDef.RECT();
            if(User32.INSTANCE.GetWindowRect(this.windowHandler, rect) == true){
                window = rect.toRectangle();
            }
        }
        images = ImageUtils.initImageMap();
        flags = ImageUtils.initFlagMap();
        ocr.setDatapath(TESSDATAFOLDER);
        ocr.setLanguage("eng");
    }



    public static Eye getInstance(){
        if(instance == null){
            instance = new Eye();
            return instance;
        }
        else
            return instance;
    }

    public BufferedImage getScreenshot(){
        return GDI32Util.getScreenshot(this.windowHandler);
    }

    public ScreenType recognizeScreenType() throws IOException {
        for (ImageEnum imageEnum : ImageEnum.values()) {
            if(findImageOnScreen(imageEnum, imageEnum.getImageRatioToWindow()) != null){
                return ScreenType.getScreenTypeByImage(imageEnum);
            }
        }
        return ScreenType.ADS;
    }

    public String recognizeCountryName(BufferedImage image) throws TesseractException {
        return this.ocr.doOCR(ImageUtils.resizeImageForTextRecognition(image)).replace("\n", "").replace(" ", "").replace("1", "T").replaceAll("-", "");
    }

    public FlagEnum getFlagByCountryName(String countryName){
        return FlagEnum.valueOf(countryName.toUpperCase());
    }


    public Rectangle getWindow() {
        return window;
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public Rectangle findImageOnScreen(IEnum image, ImageRatioToWindow ratio) throws IOException {

        Mat img = ImageUtils.bufferedImageToMat(getScreenshot());
        Mat templ = ImageUtils.bufferedImageToMat(ImageUtils.resizeImageAccordingToWindow(ImageIO.read(new File(image.getClass() == ImageEnum.class ? images.get(image) : flags.get(image))), window,ratio));
        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(img, templ, result, TEMPLATE_DETECTION_FUNCTION);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        if(image.getClass() == FlagEnum.class){
            System.out.println("For flag " + image + " best ratio is " + mmr.maxVal);
        }
        if(mmr.maxVal > 0.55) {
            Point matchLoc;
            if (TEMPLATE_DETECTION_FUNCTION == Imgproc.TM_SQDIFF || TEMPLATE_DETECTION_FUNCTION == Imgproc.TM_SQDIFF_NORMED) {
                matchLoc = mmr.minLoc;
            } else {
                matchLoc = mmr.maxLoc;
            }
            Rectangle rectResult = new Rectangle(new java.awt.Point((int) matchLoc.x, (int) matchLoc.y));
            rectResult.add(new java.awt.Point((int) (matchLoc.x + templ.cols()), (int) (matchLoc.y + templ.rows())));
            return rectResult;
        }
        else
            return null;
    }
}
