/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Interfaces;

/**
 * Add your docs here.
 */
public interface IEncoder {
    void reset();
    double modifier();

    double theta(); // angle
    default double x() {
        return modifier() * theta();
    }

    double omega(); // angular velocity
    default double v() { // velocity
        return modifier() * omega();
    }

    // double alpha();
    // default double acceleration() {
    //     return modifier() * alpha();
    // }
}
