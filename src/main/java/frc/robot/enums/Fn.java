/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.enums;

import edu.wpi.first.wpilibj.GenericHID.Hand;



/**
 * Add your docs here.
 */
public class Fn {
    public static Hand not(Hand hand) {
        return hand == Hand.kRight ? Hand.kLeft : Hand.kRight;
    }
}
