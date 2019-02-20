/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.utilities.Duo;
import frc.robot.sensors.*;
import frc.robot.Interfaces.IGyro.Axis;
public class InertialDamper extends Command {
  private final double left_v;
  private final double right_v;
  private final DriveTrain m_drive;
  private final double omega;

  public InertialDamper(DriveTrain m_drive) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    left_v  = m_drive.getEncoder(Hand.kLeft).omega();
    right_v = m_drive.getEncoder(Hand.kRight).omega();
    omega = In.gyro.omega(Axis.Z);
    this.m_drive = m_drive;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Duo current = new Duo(m_drive.getEncoder(Hand.kLeft).omega(), m_drive.getEncoder(Hand.kRight).omega());
    Duo goal    = new Duo(left_v, right_v);
    Duo dir     = goal.sub(current).div(5).add(current).div(100);
    m_drive.drive((double) dir.first, (double) dir.second);
  }

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
