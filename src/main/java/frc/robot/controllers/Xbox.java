/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.controllers;
import frc.robot.Interfaces.IController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Log;
/**
 * Add your docs here.
 */
import edu.wpi.first.wpilibj.buttons.JoystickButton;
public class Xbox implements IController {
    final int PORT;
    private XboxController xbox;

    public Xbox(int port) {
        PORT = port;
        xbox = new XboxController(PORT);
    }

    public double getAnalog(Hand hand, AxisType axis) {
        double output = 0;
        switch(axis) {
            case kX : output = xbox.getX(hand); break;
            case kY : output = xbox.getY(hand); break;
        }
        return output;
    }
    public JoystickButton getButton(Hand hand, int id) {
        return new JoystickButton(xbox, id);
    }
    public void rumble(double amount) {
        xbox.setRumble(RumbleType.kLeftRumble, amount);
        xbox.setRumble(RumbleType.kRightRumble,amount);
    }
}
