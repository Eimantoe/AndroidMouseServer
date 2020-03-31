package Utilities;

import Common.BaseObservable;
import Common.MouseAction;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseActionExecuter extends BaseObservable<MouseActionExecuter.Listener> {

    public interface Listener
    {
        public void onAbortAction();
    }

    private Robot robot;
    private int motionCoef;
    private int numberOfLinesToScroll;

    public MouseActionExecuter() {
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void executeAction(MouseAction action)
    {
        if (action.isMouseMotion) {
            final Point location = MouseInfo.getPointerInfo().getLocation();
            final int x = (int) (location.getX() + (action.x*motionCoef));
            final int y = (int) (location.getY() + (action.y*motionCoef));
            robot.mouseMove(x, y);

            return;
        }

        switch (action.action)
        {
            case MouseAction.ACTION_L_CLICK:
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                break;
            case MouseAction.ACTION_R_CLICK:
                //robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                break;
            case MouseAction.ACTION_UP:
                robot.mouseWheel(-numberOfLinesToScroll);
                break;
            case MouseAction.ACTION_DOWN:
                robot.mouseWheel(numberOfLinesToScroll);
                break;
            case MouseAction.ACTION_L_RELEASE:
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                break;
        }
    }

    public void setMotionCoef(int motionCoef) {
        this.motionCoef = motionCoef;
    }

    public void setLinesToScroll(int value) {
        this.numberOfLinesToScroll = value;
    }

    public int getMotionCoef() {
        return motionCoef;
    }

    public int getNumberOfLinesToScroll() {
        return numberOfLinesToScroll;
    }

    public void setDefaultParameters()
    {
        numberOfLinesToScroll = 3;
        motionCoef = 50;
    }

}
