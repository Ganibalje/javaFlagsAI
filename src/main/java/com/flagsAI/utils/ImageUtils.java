package com.flagsAI.utils;

import com.flagsAI.enums.FlagEnum;
import com.flagsAI.enums.ImageEnum;
import com.flagsAI.enums.ImageRatioToWindow;
import net.coobird.thumbnailator.Thumbnails;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ganib on 23.09.2017.
 */
public class ImageUtils {

    private static final String IMAGES_PATH = ImageUtils.class.getClassLoader().getResource("images").getPath();
    private static final String FLAGS_PATH = ImageUtils.class.getClassLoader().getResource("flags").getPath();
    private static final int DEFAULT_IMAGE_CODE = BufferedImage.TYPE_3BYTE_BGR;

    public static Map<ImageEnum, String> initImageMap() {
        HashMap<ImageEnum, String> result = new HashMap<ImageEnum, String>();

        for (File file : new File(IMAGES_PATH).listFiles()) {
            try {
                ImageEnum imageEnum = ImageEnum.valueOf(removeExtension(file.getName()).toUpperCase());
                if (imageEnum != null)
                    result.put(imageEnum, file.getAbsolutePath());
            }
            catch (Exception ex){
                System.out.println("File " + file.getName() + " does not match ImageEnum");
            }
        }
        return result;
    }

    private static String removeExtension(String string){
        if(string == null)
            return null;
        int pos = string.lastIndexOf('.');
        return pos == -1 ? string : string.substring(0,pos);
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        if(bi.getType() != DEFAULT_IMAGE_CODE)
            bi = changeBufferedImageType(bi, DEFAULT_IMAGE_CODE);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    private static BufferedImage changeBufferedImageType(BufferedImage in, int type){
        BufferedImage convertedImg = new BufferedImage(in.getWidth(), in.getHeight(), type);
        convertedImg.getGraphics().drawImage(in, 0, 0, null);
        return convertedImg;
    }


    public static BufferedImage resizeImageForTextRecognition(BufferedImage image){
        return image.getSubimage((int)(image.getWidth()*0.05), (int)(image.getHeight() * 0.27), (int)(image.getWidth()*0.9), (int)(image.getHeight()*0.24));
    }

    public static BufferedImage resizeImageAccordingToWindow(BufferedImage img, Rectangle window, ImageRatioToWindow ratio) throws IOException {
        return Thumbnails.of(img).size((int)(window.getWidth()*ratio.getRatio()[0]), (int)(window.getHeight()*ratio.getRatio()[1])).asBufferedImage();
    }

    public static Map<FlagEnum, String> initFlagMap() {
        HashMap<FlagEnum, String> result = new HashMap<FlagEnum, String>();

        for (File file : new File(FLAGS_PATH).listFiles()) {
            try {
                FlagEnum imageEnum = FlagEnum.valueOf(removeExtension(file.getName()).replace(" ", "").toUpperCase());
                if (imageEnum != null)
                    result.put(imageEnum, file.getAbsolutePath());
            }
            catch (Exception ex){
                System.out.println("File " + file.getName() + " does not match FlagEnum");
            }
        }
        return result;

    }

    public static double[] generateSizeScaleArray(double start, double end, double step){
        int size = (int)((end-start)/step);
        double[] result = new double[size];

        for(double i = start, j=0; i<= end && j<=size; i+=step, j++)
            result[(int)j] = i;
        return result;
    }
}
