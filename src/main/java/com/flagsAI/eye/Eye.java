package com.flagsAI.eye;

import com.flagsAI.eye.enums.FlagEnum;
import com.flagsAI.eye.enums.IEnum;
import com.flagsAI.eye.enums.ImageEnum;
import com.flagsAI.eye.enums.ScreenType;
import com.flagsAI.utils.ImageUtils;
import com.sun.jna.platform.win32.GDI32Util;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ganib on 23.09.2017.
 */
public class Eye {
    private static final String WINDOW_NAME = "BlueStacks";
    private static final String TESSDATAFOLDER = "C:\\Program Files (x86)\\Tesseract-OCR";
    private static final int TEMPLATE_DETECTION_FUNCTION = Imgproc.TM_CCOEFF_NORMED;
    private static final double[] SIZE_SCALE = ImageUtils.generateSizeScaleArray(0.2, 1.51, 0.01);

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

    public Rectangle findImageOnScreen(IEnum image){
//        System.out.println("trying find " + image);

        BufferedImage screenshot = getScreenshot();
        Mat objectImage;
        if(image.getClass() == ImageEnum.class)
            objectImage = Highgui.imread(images.get(image), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        else
            objectImage = Highgui.imread(flags.get(image), Highgui.CV_LOAD_IMAGE_GRAYSCALE);
        Mat sceneImage = ImageUtils.bufferedImageToMat(screenshot);

        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
        featureDetector.detect(objectImage, objectKeyPoints);

        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);


        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        featureDetector.detect(sceneImage, sceneKeyPoints);
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        LinkedList<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        float nndrRatio = 0.7f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);

            }
        }

        if (goodMatchesList.size() >= 7) {

            java.util.List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
            java.util.List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

            LinkedList<org.opencv.core.Point> objectPoints = new LinkedList<>();
            LinkedList<org.opencv.core.Point> scenePoints = new LinkedList<>();

            for (int i = 0; i < goodMatchesList.size(); i++) {
                objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
                scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
            }

            MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
            objMatOfPoint2f.fromList(objectPoints);
            MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
            scnMatOfPoint2f.fromList(scenePoints);

            Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

            Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

            obj_corners.put(0, 0, new double[]{0, 0});
            obj_corners.put(1, 0, new double[]{objectImage.cols(), 0});
            obj_corners.put(2, 0, new double[]{objectImage.cols(), objectImage.rows()});
            obj_corners.put(3, 0, new double[]{0, objectImage.rows()});

            Core.perspectiveTransform(obj_corners, scene_corners, homography);

            double[] ltPoint = scene_corners.get(0, 0);
            double[] rbPoint = scene_corners.get(2, 0);
            Rectangle result = new Rectangle(new java.awt.Point((int)ltPoint[0], (int)ltPoint[1]));
            result.add(new java.awt.Point((int)rbPoint[0], (int)rbPoint[1]));
            return result;
        }


        return null;
    }

    public ScreenType recognizeScreenType(){
        for (ImageEnum imageEnum : ImageEnum.values()) {
            if(findImageOnScreen(imageEnum) != null){
                return ScreenType.getScreenTypeByImage(imageEnum);
            }
        }
        return null;
    }

    public String recognizeCountryName(BufferedImage image) throws TesseractException {
        return this.ocr.doOCR(ImageUtils.resizeImageForTextRecognition(image)).replace("\n", "").replace(" ", "");
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

    public Rectangle findFlagOnScreen(IEnum image){

        Mat img = ImageUtils.bufferedImageToMat(getScreenshot());
        Mat templ = Highgui.imread(flags.get(image), Highgui.CV_LOAD_IMAGE_GRAYSCALE);

        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(img, templ, result, TEMPLATE_DETECTION_FUNCTION);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        Point matchLoc;
        if (TEMPLATE_DETECTION_FUNCTION == Imgproc.TM_SQDIFF || TEMPLATE_DETECTION_FUNCTION == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmr.minLoc;
        } else {
            matchLoc = mmr.maxLoc;
        }
        Rectangle rectResult = new Rectangle(new java.awt.Point((int)matchLoc.x, (int)matchLoc.y));
        rectResult.add(new java.awt.Point((int)(matchLoc.x + templ.cols()),(int)(matchLoc.y + templ.rows())));

        return rectResult;
    }
}
