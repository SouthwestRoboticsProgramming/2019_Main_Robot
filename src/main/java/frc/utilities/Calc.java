/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.utilities;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import java.util.function.Function;
/**
 * Add your docs here.
 */
public class Calc {
    public static double h = .1;

    public static double atrig(double opp, double adj) {
        return fxRad( Math.atan(opp/adj)
                    + Math.signum(Math.signum(adj) - 1) * Math.abs(Math.signum(adj))
                    * Math.PI );
    }
    public static double fxRad(double radians) { // converts a angle in degrees so its within the range -180 - 180
    	if (-Math.PI < radians && radians < Math.PI)
    		return radians;
    	else
    		return fxRad(radians - Math.signum(radians) * 2 * Math.PI);
    }
    public static double fxDeg(double degrees) {
        if (-180 < degrees && degrees <= 180) 
            return degrees;
        else
            return fxDeg(degrees - Math.signum(degrees) * 360);
    }
    public static Duo quadratic(double a, double b, double c) {
        return new Duo(1, -1).mult(Math.sqrt(b*b - 4 * a * c)).sub(b).div(2 * a);
    }

    public static double integral(Function<Double,Double> fn, double x) {
        return Math.abs(x) < h ? (fn.apply(x) + fn.apply(0d)) / 2 * x : integral(fn, x - Math.signum(x) * h) + (fn.apply(x) + fn.apply(x - Math.signum(x) * h)) / 2 * h;
    }

    public static double eq(double x, double y) {
        return 1 - Math.abs(Math.signum(x - y));
    }
    public static double neq(double x, double y) {
        return Math.abs(Math.signum(x-y));
    }
    public static double gteq(double x, double y) {
        return Math.signum(Math.signum(x - y) + 1);
    }
    public static double lteq(double x, double y) {
        return gteq(y,x);
    }
    public static double gt(double x, double y) {
        return gteq(x,y) * neq(x,y);
    }
    public static double lt(double x, double y) {
        return 1 - gteq(x,y);
    }
}
