/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.*;
import frc.robot.Robot;

public class SetArm extends Command {
  private Arm arm;
  private double shoulderPos;
  private int extentionCount;

  
  public SetArm(Arm arm, double shoulderPos, int extentionCount, double wristPos) {
    this.arm = arm;
    this.shoulderPos = shoulderPos;
    this.extentionCount = extentionCount;
    requires(arm);
    // Arm.isManualControl = false;
    // this.wristPos = wristPos;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Robot.arm.setShoulderAng(shoulderPos);
    Robot.arm.setExtendCount(extentionCount);
    Robot.arm.calibrate();
    // Robot.arm.setExtendCount(extentionCount);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    // return false;
    return ((Math.abs(arm.shoulder.theta() - shoulderPos) <= 4d)) && (Math.abs(arm.extend.get() - extentionCount) <= 1000);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    // Arm.isManualControl = true;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    // Arm.isManualControl = true;
  }
}
