package org.usfirst.frc.team948.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the NIVision class to do vision processing. 
 * The image is acquired from the USB Webcam, then a circle is overlayed on it. 
 * The NIVision class supplies dozens of methods for different types of processing. 
 * The resulting image can then be sent to the FRC PC Dashboard with setImage()
 */
public class Robot extends SampleRobot {
    int session;
    Image frame;
    Image binaryFrame;
    NIVision.Range TOTE_HUE_RANGE = new NIVision.Range(24, 49);	//Default hue range for yellow tote
	NIVision.Range TOTE_SAT_RANGE = new NIVision.Range(67, 255);	//Default saturation range for yellow tote
	NIVision.Range TOTE_VAL_RANGE = new NIVision.Range(49, 255);	//Default value range for yellow tote
    public void robotInit() {

        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam0",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
    }

    public void operatorControl() {
        NIVision.IMAQdxStartAcquisition(session);

        /**
         * grab an image, find all yellow objects, draw the circle, and provide it for the camera server
         * which will in turn send it to the dashboard.
         */ 	
        NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);

        while (isOperatorControl() && isEnabled()) {
        	//grabs image from the session
            NIVision.IMAQdxGrab(session, frame, 1);
            //fills the binaryFrame with the threshold HSV values
            NIVision.imaqColorThreshold(binaryFrame, frame, 255, NIVision.ColorMode.RGB, TOTE_HUE_RANGE, TOTE_SAT_RANGE, TOTE_VAL_RANGE);
            //draws circle on binary image
            NIVision.imaqDrawShapeOnImage(binaryFrame, binaryFrame, rect,
                    DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
            
            CameraServer.getInstance().setImage(binaryFrame);
            
            /** robot code here! **/
            Timer.delay(0.005);		// wait for a motor update time
        }
        NIVision.IMAQdxStopAcquisition(session);
    }

    public void test() {
    }
}
