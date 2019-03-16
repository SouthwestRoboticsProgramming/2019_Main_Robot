/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.controllers;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Robot;
import frc.robot.Interfaces.IController;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import frc.robot.subsystems.DriveTrain;
/**
 * Add your docs here.
 */
public class TwoJoysticks implements IController {
    private Joystick left;
    private Joystick right;
    private Hand dominantHand;



    public TwoJoysticks(Hand dominanthand, int left, int right) {
        this.left = new Joystick(left);
        this.right = new Joystick(right);
        this.dominantHand = dominanthand;
    }

    public void defaultDrive(DriveTrain train) {
        train.tankDrivePeriodic();
    }

    public double getAnalog(Hand hand, AxisType axis) {
        double output;
        switch(hand) {
            case kLeft  : output = left.getAxis(axis); break;
            case kRight : output = right.getAxis(axis); break;
            default     : output = getAnalog(dominantHand, axis); break;
        }
        return output;
    }

    public JoystickButton getButton(Hand hand, int id) {
        JoystickButton output;
        switch(hand) {
            case kLeft :output = new JoystickButton(left, id); break;
            case kRight:output = new JoystickButton(right,id); break;
            default    :output = getButton(dominantHand, id)  ; break;
        }
        return output;
    }
    public JoystickButton getButton(Button button) {
        switch(button) {
            case PopRamp     : return getButton(dominantHand, 2);
            case ToggleClaw  : return getButton(dominantHand, 3);
            case ToggleRearUp: return getButton(dominantHand, 4);
            default          : return getButton(dominantHand, 11);
        }
    }
    public double getTrigger(Hand hand) {
        if (hand == Hand.kRight) {
            return right.getTrigger() ? 1 : 0;
        }
        else {
            return left.getTrigger() ? 1 : 0;
        }
    }
}
