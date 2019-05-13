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
import frc.utilities.Calc;
import frc.robot.subsystems.DriveTrain;
/**
 * Add your docs here.
 */
public class TwoJoysticks implements IController {
    private Joystick left;
    private Joystick right;
    private Hand dominantHand;
    private Hand nondominantHand;
    public double speedFactor;

    private boolean isReversed = false;

    public TwoJoysticks(Hand dominanthand, int left, int right) {
        this.left = new Joystick(left);
        this.right = new Joystick(right);
        this.dominantHand = dominanthand;
        if (dominantHand == Hand.kRight){
            this.nondominantHand = Hand.kLeft;
        }
        else {
            this.nondominantHand = Hand.kRight;
        }
    }
    public void defaultDrive(DriveTrain train) {
        if (!isReversed) {
            train.tankDrivePeriodic(getAnalog(Hand.kLeft), getAnalog(Hand.kRight), Calc.val(getTrigger(Hand.kLeft)), Calc.val(getTrigger(Hand.kRight)));
        }
        else {
            train.tankDrivePeriodic(-getAnalog(Hand.kRight), -getAnalog(Hand.kLeft),  Calc.val(getTrigger(Hand.kRight)), Calc.val(getTrigger(Hand.kLeft)));
        }
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
            case PopRamp     : return getButton(nondominantHand, 2);
            // case ToggleClaw  : return getButton(dominantHand, 8);
            case ToggleRearUp: return getButton(nondominantHand, 4);
            case SpinCameraForwards: return getButton(dominantHand, 3);
            case SpinCameraBackwards: return getButton(dominantHand, 2);
            case Face        : return getButton(dominantHand, 11);
            case SetIsHalf   : return getButton(dominantHand, 4);
            case SetIsFull   : return getButton(dominantHand, 5);
            case SetIsReversed: return getButton(dominantHand, 8);
            case SetIsForward: return getButton(dominantHand, 9);
            default          : return getButton(dominantHand, 10);
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
    public void reverse() {
        isReversed = ! isReversed;
    }
    public void setReversed(boolean val) {
        isReversed = val;
    }
}
