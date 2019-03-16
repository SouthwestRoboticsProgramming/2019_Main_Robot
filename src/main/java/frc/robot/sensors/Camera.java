/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;

/**
 * Add your docs here.
 */
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import org.opencv.core.Range;
import java.util.function.*;
import org.opencv.core.Scalar;
import java.util.ArrayList;
import org.opencv.core.Size;
import java.awt.Point;
import frc.robot.Log;

public class Camera {
    private UsbCamera camera;
    private Mat source = new Mat();
    private Mat output = new Mat();

    private final Range HUE_RANGE = new Range(52,62);
    private final Range SAT_RANGE = new Range(231,255);
    private final Range VAL_RANGE = new Range(207,237);

    private final int HUE = 0;
    private final int SAT = 1;
    private final int VAL = 2;

    private static final int RESOLUTION_WIDTH = 160;
    private static final int RESOLUTION_HEIGHT = 120;

    private static final double FIELD_OF_VIEW_X = 61;
    private static final double FIELD_OF_VIEW_Y = 34.3;

    private boolean failedToFetch = false;

    private Point matchLocation;
    private Point matchAngle;

    public Mat getRaw() {
        return source;
    }
    public Mat getRefined() {
        return output;
    }
    public Point location() {
        return matchLocation;
    }
    public Point locationAngle() {
        return matchAngle;
    }
    public boolean failed() {
        return failedToFetch;
    }

    
    public Camera() {
        // camera = CameraServer.getInstance().startAutomaticCapture();
        new Thread(() -> {
            
            camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
            
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
            
            Mat source = new Mat();
            Mat output = new Mat();
            
            while(!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        }).start();
    }

    private void proccess() {
        Imgproc.cvtColor(source,source,Imgproc.COLOR_BGR2HSV);
        // Imgproc.blur(output,output, new Size(RESOLUTION_WIDTH,RESOLUTION_HEIGHT)); // maybe should be somthing like new Size(3,3) or new Size(100,50)?
        short[][] img = new short[RESOLUTION_HEIGHT][RESOLUTION_WIDTH];
        double[] color;
        for(short i = 0; i < RESOLUTION_HEIGHT; i++) {
            for(short j = 0; j < RESOLUTION_WIDTH; j++) {
                color = output.get(i,j);
                img[i][j] = val(inRange(color[HUE],HUE_RANGE) && inRange(color[SAT], SAT_RANGE) && inRange(color[VAL], VAL_RANGE));
            }
        }

        // map(output, x -> inRange(x.val[HUE], HUE_RANGE) && inRange(x.val[SAT], SAT_RANGE) && inRange(x.val[VAL], VAL_RANGE) ? new Scalar(four(255)) : new Scalar(four(0)));
        // boolean[][] image = new boolean[RESOLUTION_HEIGHT][RESOLUTION_WIDTH];
        // for (short i = 0; i < output.rows(); i ++ ) {
        //     for (short j = 0; j < output.cols(); j++) {
        //         image[i][j] = output.get(i,j).equals(new Scalar(four(255)));
        //     }
        // }
        // int[][] connections = new int[RESOLUTION_HEIGHT][RESOLUTION_WIDTH];
        // for (short i = 0; i < image.length; i ++) {
        //     for (short j = 0; j < image[i].length; j ++) {
        //         connections[i][j] += val(image[Math.min(i+1, RESOLUTION_HEIGHT-1)][j])  + val(image[Math.max(i-1,0)][j]) + val(image[i][Math.min(j+1, RESOLUTION_WIDTH)]) + val(image[i][Math.max(j-1,0)]);
        //     }
        // }
        img = connections(connections(img));
        short[] maximumLocation = new short[RESOLUTION_HEIGHT];
        short[] maximum = new short[RESOLUTION_HEIGHT];
        for (short i = 0; i < img.length; i ++ ) {
            maximumLocation[i] = maxLocation(img[i]);
        }
        for (short i = 0; i < maximumLocation.length; i ++) {
            maximum[i] = img[i][maximumLocation[i]];
        }
        int row = maxLocation(maximum);
        int col = max(maximum);
        matchLocation.x = col;
        matchLocation.y = row;
        //matchAngle.x = (matchLocation.x - RESOLUTION_WIDTH / 2d) / (RESOLUTION_WIDTH / 2) * FIELD_OF_VIEW_X;
        matchAngle.x = (int) ((matchLocation.x - RESOLUTION_WIDTH / 2d) / (RESOLUTION_WIDTH / 2) * FIELD_OF_VIEW_X);
        //matchAngle.y = (matchLocation.y - RESOLUTION_HEIGHT / 2d)/ (RESOLUTION_HEIGHT/ 2) * FIELD_OF_VIEW_Y;
        matchAngle.y = (int) ((matchLocation.y - RESOLUTION_HEIGHT / 2d) / (RESOLUTION_HEIGHT / 2) * FIELD_OF_VIEW_Y);

    }
    private static short[][] connections(short[][] image) {
        short[][] out = new short[RESOLUTION_HEIGHT][RESOLUTION_WIDTH];
        for (short i = 0; i < image.length; i ++) {
            for (short j = 0; j < image[i].length; j ++) {
                if (j != RESOLUTION_WIDTH-1) {
                    out[i][j] += image[i][j+1];
                }
                if (j != 0) {
                    out[i][j] += image[i][j-1];
                }
                if (i != RESOLUTION_HEIGHT-1) {
                    out[i][j] += image[i+1][j];
                }
                if (i != 0) {
                    out[i][j] += image[i-1][j];
                }
                // out[i][j] += image[Math.min(i+1, RESOLUTION_HEIGHT-1)][j]  + image[Math.max(i-1,0)][j] + image[i][Math.min(j+1, RESOLUTION_WIDTH)] + image[i][Math.max(j-1,0)];
            }
        }
        return out;
    }
    private short maxLocation(short[] lst) {
        short loc = 0;
        short max = lst[0];
        for (short i = 1; i < lst.length; i++) {
            if (lst[i] > max) {
                max = lst[i];
                loc = i;
            }
        }
        return loc;
    }
    private static short max(short[] lst) {
        short max = lst[0];
        for (short i = 1; i < lst.length; i++) {
            if (lst[i] > max) {
                max = lst[i];
            }
        }
        return max;
    }
    private static Mat map(Mat in, Function<Scalar,Scalar> fn) {
        for(short i = 0; i < in.rows(); i ++) {
            for (short j = 0; j < in.cols() ; j ++) {
                // in.setTo(new Scalar(fn.apply(in.get(i,j) )));
                in.row(i).col(j).setTo(fn.apply(new Scalar(in.get(i,j)) ));
            }
        }
        return in;
    }
    // private static Mat blur(Mat mat) {
    //     if (mat.rows() != 0) {
    //         Mat mat0 = mat.rowRange(new Range(0,1)).colRange(new Range(0,1));
    //     }

    // }
    // private static void blursqr(Mat mat, double bluriness) {
    //     if (mat.cols() >= 2 && mat.rows() >= 2) {
    //         map(mat, x -> add(add(add(add(x,new Scalar(mat.get(0,0)).mul(four(bluriness))), new Scalar(mat.get(0,1))), new Scalar(mat.get(1,0))), new Scalar(mat.get(1,1))));
    //     }
    // }
    // private static Scalar add(Scalar a, Scalar b) {
    //     return new Scalar(a.val[0] + b.val[0], a.val[1] + b.val[1], a.val[2] + b.val[2], a.val[3] + b.val[3]);
    // }
    // private static Scalar add(double a, Scalar b) {
    //     return add(new Scalar(four(a)), b);
    // }
    // private static Scalar add(Scalar a, double b) {
    //     return add(b,a);
    // }
    // private static Mat add(Mat a, Scalar b) {
    //     Range range = new Range(0,5);
 
    //     return map(a, x -> add(b, x));
    // }


    // private static Scalar negate(Scalar a) {
    //     for (byte i = 0; i < 4 ; i++) {
    //         a.val[i] = -i;
    //     }
    //     return a;
    // }
    private static double[] four(double a) {
        double[] lst = {a,a,a,a};
        return lst;
    }
    private static boolean inRange(double x, Range range){
        // return (range.end <= x)  && (x <= range.start) ? 255 : 0;
        return (range.end <= x) && (x <= range.start);
    }
    private static short val(boolean x) {
        return (short) (x ?  1: 0);
    }
}

