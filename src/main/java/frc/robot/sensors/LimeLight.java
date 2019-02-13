/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.sensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
;
// NetworkTable table = new NetworkTableInstance.getDefault().getTable("limelight");
// NetworkTableEntry tx = table.getEntry("tx");
// NetworkTableEntry ty = table.getEntry("ty");
// NetworkTableEntry ta = table.getEntry("ta");

//read values periodically
// double x = tx.getDouble(0.0);
// double y = ty.getDouble(0.0);
// double area = ta.getDouble(0.0);

public class LimeLight {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    //read values periodically
    public double getX() { // returns tx, or offset from crosshair
        return table.getEntry("tx").getDouble(0d);
    }
    public double getY() { // returns ty, or offset from crosshair
        return table.getEntry("ty").getDouble(0d);
    }
    public double getArea() { // return ta, or area as percentage of screen
        return table.getEntry("ta").getDouble(0d);
    }
    public double getMatch() { // returns tv, or whether or not there is a match, 0 or 1
        return table.getEntry("tv").getDouble(0d);
    }
    public double getHorizontalSize() { // returns thor, or horizontal sidelength of rought bounding boz
        return table.getEntry("thor").getDouble(0d);
    }
    public double getVerticalSize() { // returns tvert or tallest horizontal part of match
        return table.getEntry("tvert").getDouble(0d);
    }
}
