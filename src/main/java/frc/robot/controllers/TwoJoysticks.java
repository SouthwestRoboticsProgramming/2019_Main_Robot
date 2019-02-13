/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.controllers;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Interfaces.IController;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;


/**
 * Add your docs here.
 */
public class TwoJoysticks implements IController {
    private Joystick left;
    private Joystick right;
    private Hand defaultHand = Hand.kLeft;

    public TwoJoysticks(int left, int right) {
        this.left = new Joystick(left);
        this.right = new Joystick(right);
    }

    public double getAnalog(Hand hand, AxisType axis) {
        double output;
        switch(hand) {
            case kLeft  : output = left.getAxis(axis); break;
            case kRight : output = -right.getAxis(axis); break;
            default     : output = getAnalog(defaultHand, axis); break;
        }
        return output;
    }

    public JoystickButton getButton(Hand hand, int id) {
        JoystickButton output;
        switch(hand) {
            case kLeft :output = new JoystickButton(left, id); break;
            case kRight:output = new JoystickButton(right,id); break;
            default    :output = getButton(defaultHand, id)  ; break;
        }
        return output;
    }

}
