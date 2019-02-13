/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.utilities;

/**
 * Add your docs here.
 */
public class Calc {
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
}
