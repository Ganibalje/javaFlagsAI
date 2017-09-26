package com.flagsAI.hand;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by ganib on 23.09.2017.
 */
public class Hand {

    private static Hand hand;
    private Rectangle window;
    private Robot robot = new Robot();

    private Hand(Rectangle window) throws AWTException {
        this.window = window;
    }

    public static Hand getInstance(Rectangle window) throws AWTException {
        if(hand == null){
            hand = new Hand(window);
            return hand;
        }
        else
            return hand;
    }

    public static Hand getInstance(){
        if(hand == null)
            return null;
        else
            return hand;
    }

    public void click(Rectangle rect) throws InterruptedException {
        Point rectangleMiddle = getRectangleMiddle(rect);
        robot.mouseMove(window.x + rectangleMiddle.x, window.y + rectangleMiddle.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        Thread.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    private Point getRectangleMiddle(Rectangle rectangle){
        return new Point(rectangle.x+(rectangle.width)/2, rectangle.y + (rectangle.height)/2);
    }
}
