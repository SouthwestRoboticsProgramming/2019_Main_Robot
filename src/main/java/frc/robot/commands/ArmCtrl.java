/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Interfaces.IController.Button;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.subsystems.Logger;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.RobotMap;
import frc.robot.Log;
import frc.robot.sensors.In;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import frc.robot.OI;
import frc.robot.Robot;

import frc.utilities.Calc;

public class ArmCtrl extends Command {
  // private final OI oi;
  private final Arm arm;

  private double previousWrist = 0;
  private double previousShoulder = 0;
  private double previousExtention = 0;


  private final double MAX_CHNG_W = .8;
  private final double MAX_CHNG_S = .6;
  private final double MAX_CHNG_E = .2;
  private final double MAX_EXTENTION = .3;

  private final double SHOULDER_INPUT_MODIFIER = .7;

  public ArmCtrl(Arm m_arm) {
    requires(m_arm);
    this.arm = m_arm;
  }
  
  protected void execute() {
      arm.setWrist(Robot.oi.arm_pilot.getAnalog(AxisType.kZ) * .7);
      arm.setShoulder(previousShoulder += (Robot.oi.arm_pilot.getAnalog(AxisType.kY) * SHOULDER_INPUT_MODIFIER  - previousShoulder) * MAX_CHNG_S);
      // arm.setWrist( previousWrist    += (oi.arm_pilot.getAnalog(AxisType.kZ)- previousWrist) * MAX_CHNG_W);
      double extend_input  = Calc.val(Robot.oi.arm_pilot.getButton(Button.Extend).get());
      double retract_input = Calc.val(Robot.oi.arm_pilot.getButton(Button.Retract).get());
      arm.setExtention((extend_input - retract_input) * MAX_EXTENTION);
      arm.calibrate();
      // Log.info(arm.shoulder.get());
    // }

    // Log.info("EXTEND THETA = " + arm.extend.theta());
    // Log.info("EXTENTION THETA = " + arm.extend.get());
    // Log.info("SHOULDER THETA = " + arm.shoulder.theta());

    // if (!Arm.isManualControl) {
    //   Log.info("Is Manual Control: "+ Arm.isManualControl);
    // }

    // Log.info("is Manual Control = >>> " + Arm.isManualControl + " <<<");

    // Log.info("WRIST THETA  = " + arm.wrist.get());
    // Log.info(In.lime.getSkew());
    // Log.info("IS EXTEND TO HIGH = " + arm.isExtendTooHigh());
    // Log.info("IS EXTEND TO LOW = " + arm.isExtendTooLow());
    
    // Log.info("IS WRIST TO HIGH = " + arm.isWristTooHigh());
    // Log.info("IS WRIST TO LOW = " + arm.isWristTooLow());
    
    // Log.info("IS SHOULDER TO HIGH = " + arm.isShoulderTooHigh());
    // Log.info("IS SHOULDER TO LOW = " + arm.isShoulderTooLow());
    

    // arm.setExtention(oi.arm_pilot.getTrigger(Hand.kRight));
    //KEEP THIS
    // double lft_t = oi.arm_pilot.getTrigger(Hand.kLeft); // left and right triggers
    //  double rht_t = oi.arm_pilot.getTrigger(Hand.kRight);
    // double trigger = Math.max(lft_t, rht_t); // No Math.abs because triggers are always between 0 and 1
    // trigger = -Calc.eq(lft_t, trigger) * lft_t + // lft is negative because it indicates contracting while rht is expanding
    //            Calc.eq(rht_t, trigger) * rht_t ;
    // trigger = (trigger - previousExtention) * MAX_CHNG_E + previousExtention;
    // arm.setExtention(trigger);
    //.info("SHOULDER SAYS $ " + arm.shoulder.theta());
    //Log.info("WRIST    SAYS $ " + arm.wrist.theta());

  }

    // Cal Commented the 59 lines below this Feb 21 2019.
    // if (inProcessOfBreaking) {
    //   if (breakCounter < 10) { // goes for .2 seconds
    //     breakCounter++;
    //     Log.info("Waiting!");
    //   }
    //   else {
    //     inProcessOfBreaking = false;
    //     breakCounter = 0;
    //     Log.info("Done waiting!");
    //   }
    // }
    // else {
    //   //if (((((Math.abs(shoulder_val) < .1) && (Math.abs(previousShoulder)>.1))||((Math.abs(shoulder_val) > .1) && (Math.abs(previousShoulder)<.1))))) {
    //   if ((Math.abs(shoulder_val) < .1)) {
    //     arm.setBreak(true);
    //     if (Math.abs(previousShoulder)>.1) {
    //       inProcessOfBreaking = true;
    //       Log.info("Changing break states!");
    //     }
    //   }
    //   else if ((Math.abs(shoulder_val) > .1)) {
    //     arm.setBreak(false);
    //     if (Math.abs(previousShoulder)<.1) {
    //       inProcessOfBreaking = true;
    //       Log.info("Changing break states!");
    //     }
    //   }
    //   else if (((isShoulderTooHigh() && (shoulder_val >= 0))||(isShoulderTooLow() && (shoulder_val <= 0)))&&!isBreakOn) {
    //     Log.info("shoulder too high/low");
    //     shoulder_val = 0;
    //     arm.setShoulder(shoulder_val);
    //   }
    //   else {
    //     //shoulder_val = (shoulder_val - previousShoulder) * MAX_CHNG_S + previousShoulder;
    //     shoulder_val = Math.min(Math.abs(shoulder_val), .6) * Math.signum(shoulder_val) ;
    //     if (Math.abs(shoulder_val) < .01) {
    //       shoulder_val = 0;
    //       arm.brakeShoulder();
    //     }
    //   }
    // }



    // arm.setShoulder(shoulder_val * .3);
    

    // if ((isWristTooHigh() && (wrist_val >= 0)) || (isWristTooLow() && (wrist_val <= 0))) {
    //   wrist_val = 0;
    //   arm.setWrist(0);
    // }
    // else {
    //   wrist_val    = (wrist_val    - previousWrist   ) * MAX_CHNG_W + previousWrist   ;
    //   if (Math.abs(wrist_val) < .01) {
    //     wrist_val = 0;
    //     arm.brakeWrist();
    //   }
    //   else {
    //     arm.setWrist(   wrist_val   );
    //   }
    // }
    
    


    // Cal commented the 6 lines below this  Feb 21 2019
    // double trigger;
    // if (lft_t >= rht_t) {
    //   trigger = -lft_t;
    // }
    // else {
    //   trigger = rht_t;
    // }


// 2019-53
// YYYY-MM-DD
// we commented the 32 lines below Feb 25 2019
  //   if (isExtendTooHigh()) {
  //     if (trigger >= 0) {
  //       isPastLimit = true;
  //     }
  //     else {
  //       isPastLimit = false;
  //     }
  //     extensionPastLimit = 0;
  //   }
  //   if (isExtendTooLow() && (trigger >= 0)) {
  //     trigger = 0;
  //   }
  //   else if (isExtendTooHigh() && (trigger <= 0)) {
  //     trigger = 0;
  //   }
  //   else {
  //     //trigger = (trigger - previousExtention) * MAX_CHNG_E + previousExtention;
  //     if (Math.abs(trigger) < .01) {
  //       trigger = 0;
  //       arm.brakeExtention();
  //     }
  //   }
    
  //   arm.setExtention(trigger * .3);

  //   if (isPastLimit) {
  //     extensionPastLimit += trigger;
  //   }
  //   previousExtention = trigger; // used for smoothing
  //   previousWrist = wrist_val;
  //   previousShoulder = shoulder_val;
  //   previouslyBreaking = breaking;
  

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
    
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {

  }
}
