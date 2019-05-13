/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.controllers.*;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import frc.robot.Interfaces.*;
import frc.robot.Interfaces.IController.Button;
import frc.robot.commands.*;
import frc.robot.controllers.Xbox;
import edu.wpi.first.wpilibj.command.Command;
import frc.utilities.Calc;
import frc.robot.subsystems.*;

import java.util.function.Function;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public final IController pilot;
	public final IController arm_pilot;
	// public final IController arm_button_stick;


	// public Xbox xbox;
	// private TwoJoysticks twoJoy;

	private static final int PORT_XBOX = 0;
	private static final int PORT_LEFT = 2;
	private static final int PORT_RIGHT = 1;

	private static final int PORT_BUTTON = 3;

	public OI() {

		// System.out.println("starting OI...  " + Robot.vacuum);
		// xbox = new Xbox(PORT_XBOX);
		// twoJoy = new TwoJoysticks(PORT_LEFT, PORT_RIGHT);
		// JoystickButton a = xbox.getButton(Hand.kLeft, 1);
		// JoystickButton three = twoJoy.getButton(Hand.kRight, 3);
		
		// We reversed these to make it not go backwards
		// pilot = new TwoJoysticks(Hand.kRight, PORT_RIGHT, PORT_LEFT);
		// arm_pilot  = new Xbox(PORT_XBOX);
		pilot = new TwoJoysticks(Hand.kRight, PORT_LEFT, PORT_RIGHT);
		
		arm_pilot  = new OneJoystick(PORT_XBOX);
		// arm_button_stick = new OneJoystick(PORT_BUTTON);

		// pilot.getButton(Button.PopRamp).whenPressed(new PopRamp(Robot.ramp));
		pilot.getButton(Button.ToggleRearUp).toggleWhenPressed(new RearUp(Robot.climb));
		pilot.getButton(Button.SpinCameraForwards).whenPressed(new CameraCtrl(Robot.cam, 174));
		pilot.getButton(Button.SpinCameraBackwards).whenPressed(new CameraCtrl(Robot.cam, 12));
		pilot.getButton(Button.Face).toggleWhenPressed(new LineFollowToggle());
		
		pilot.getButton(Button.SetIsHalf).whenPressed( new Command() {
			public boolean isFinished() { return true; }
			public void initialize() {
				Robot.drive.isHalf(true);
			}
		});
		pilot.getButton(Button.SetIsFull).whenPressed( new Command() {
			public boolean isFinished() { return true; }
			public void initialize() {
				Robot.drive.isHalf(false);
			}
		});
		pilot.getButton(Button.SetIsReversed).whenPressed( new Command() {
			public boolean isFinished() { return true; } 
			public void initialize() {
				pilot.setReversed(true);
			}
		});
		pilot.getButton(Button.SetIsForward).whenPressed( new Command() {
			public boolean isFinished() {return true; }
			public void initialize() {
				pilot.setReversed(false);
			}
		});
		

		// arm_pilot.getButton(Button.ToggleVac).toggleWhenPressed(new VacuumToggle(Robot.arm, Robot.vacuum));
		arm_pilot.getButton(Button.VacOn).whenPressed(new VacuumSet( Robot.vacuum, true));
		arm_pilot.getButton(Button.VacOff).whenPressed(new VacuumSet( Robot.vacuum, false));

		Command gotoBottomHatch = new SetArm(Robot.arm, 20d, Robot.arm.EXTEND_MAX_COUNT / 5,0d);
		Command placeHatch = new SetArm(Robot.arm, 124d, Robot.arm.EXTEND_MAX_COUNT / 5, 0d);
		Command ballPickUp = new SetArm(Robot.arm, 62d, Robot.arm.EXTEND_MAX_COUNT, 0d);
		Command cargoBallPlace  = new SetArm(Robot.arm, 120d, Robot.arm.EXTEND_MAX_COUNT, 0d);
		Command rocketBallPlace = new SetArm(Robot.arm, 124d, 40000, 0);
		arm_pilot.getButton(Button.ArmToVertical).whenPressed(new SetArm(Robot.arm, 90d, 0, 0));
		arm_pilot.getButton(Button.BottomHatch).whenPressed(gotoBottomHatch);
		arm_pilot.getButton(Button.ToggleLimitSwitchs).toggleWhenPressed(new Command() {
			public boolean isFinished() {return false;}
			public void initialize() {
				Arm.limitSwitchesEnabled(false);
			}
			public void interrupted() {
				Arm.limitSwitchesEnabled(true);
			}
		});
		arm_pilot.getButton(Button.PlaceHatch).whenPressed(placeHatch);
		arm_pilot.getButton(Button.BallPlace).whenPressed(cargoBallPlace);
		arm_pilot.getButton(Button.RocketBall).whenPressed(rocketBallPlace);
		arm_pilot.getButton(Button.returnToDefault).whenPressed(new ArmCtrl(Robot.arm));
		arm_pilot.getButton(Button.BallPickUp).whenPressed(new SetArm(Robot.arm, 45d, Robot.arm.EXTEND_MAX_COUNT,0));
	

		// arm_pilot.getButton(Button.HeightTo)

		// arm_pilot.getButton(Button.HeightTo1).whenPressed(new Command() {
		// 	public void initialize() {Arm.isManualControl = false;}
		// 	public boolean isFinished() {
		// 		return Arm.isManualControl = (Arm.isManualControl || Math.abs(Robot.arm.shoulder.theta() - 90d) <= 4d) && (Math.abs(Robot.arm.shoulder.omega()) <= 5d);
		// 	}
		// 	public void execute() { Robot.arm.setShoulderAng(90); }
		// });

		// arm_pilot.getButton(Button.ToggleBrake).toggleWhenPressed(new Command() {
		// 	public boolean isFinished() {return false;}
		// 	public void initialize() {Robot.arm.setBrake(true);}
		// 	public void interrupted() {Robot.arm.setBrake(false);}
		// });
		// arm_pilot.getButton(Button.HeightTo1).whenPressed(new ArmToHeight(Robot.m_arm, 1));
		// arm_pilot.getButton(Button.HeightTo2).whenPressed(new ArmToHeight(Robot.m_arm, 2));

	}





/*
	public JoystickButton x;
	public JoystickButton y;
	public JoystickButton a;
	public JoystickButton b;
  
	  private XboxController xbox;
  
	  private static final int PORT = 0;
	public enum XboxButtonMap{
		A(1),
		B(2),
		X(3),
		Y(4),
		LeftTrigger(5),
		RightTrigger(6),
		Windows(7),
		Select(8),
		LeftJoystick(9),
		RightJoystick(10);
		private int num;
		XboxButtonMap(int buttonNumber)
		{
			num = buttonNumber;
		}
		public int toInt()
		{
			return num;
		}
  }
	public OI() {
		// controller =
		xbox = new XboxController(PORT);
	}
	public double getLeftTrigger() {
		return xbox.getTriggerAxis(Hand.kLeft);
	}
	public double getRightTrigger() {
		return xbox.getTriggerAxis(Hand.kRight);
	}
	public double getDriveXAxisValue() {
		return xbox.getX(Hand.kLeft);
	}
	public double getDriveYAxisValue() {
		return -1 * xbox.getY(Hand.kLeft);
  }
  public double getPOV() {
    return xbox.getPOV();
  }

  XboxButtonMap.A.toInt() 
*/

  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
}
