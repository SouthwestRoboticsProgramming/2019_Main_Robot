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
import frc.utilities.Calc;
import frc.robot.Log;
/**
 * Add your docs here.
 */
public class OneJoystick implements IController {
    private Hand hand = Hand.kRight;
    private Joystick joystick;

    public OneJoystick(int right) {
        this.joystick = new Joystick(right);
    }

    public void defaultDrive(DriveTrain train) {
        train.arcadeDrivePeriodic();
    }

    public void lineFollowDrive(DriveTrain train) {
        train.arcadeDrivePeriodic();
    }

    public double getAnalog(Hand hand, AxisType axis) {
        // return joystick.getAxis(axis) * (Calc.val(axis.equals(AxisType.kY))*-2 + 1);
        return joystick.getAxis(axis) * (Calc.val(axis.equals(AxisType.kY)) * -2 + 1);
    }
    public JoystickButton getButton(Hand hand, int id) {
        return new JoystickButton(joystick, id);
    }
    public JoystickButton getButton(int id) {
        return getButton(Hand.kLeft,id);
    }
    public JoystickButton getButton(Button button) {
        switch(button) {
            // case TestCommand : return getButton(hand, 5);
            case Retract : return getButton(3);
            case Extend : return getButton(5);
            case ArmToVertical : return getButton(11); // test
            case BottomHatch : return getButton(7);
            case PlaceHatch : return getButton(8);
            case BallPickUp : return getButton(9);
            case BallPlace  : return getButton(10);
            case RocketBall : return getButton(12);
            // case ToggleVac : return getButton(1);
            case VacOn: return getButton(1);
            case VacOff: return getButton(2);
            case ToggleLimitSwitchs : return getButton(6);
            case returnToDefault : return getButton(4);
            default          : return getButton( 5);
        }
    }
    public double getTrigger(Hand hand) {
        return joystick.getTrigger() ? 1d : 0d;
        // return Calc.val(Joytick.getBuggon(1).get());
    }

}