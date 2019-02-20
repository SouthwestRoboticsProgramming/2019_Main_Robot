
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import frc.robot.subsystems.DriveTrain;
import frc.utilities.Duo;
import frc.utilities.Tuple;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Interfaces.*;
import frc.robot.Log;

public class AutonomousGyroDrive extends Command {
  double inches;
  double speed;
  double angle;
  DriveTrain m_sub;
  IEncoder m_encoder;
  public AutonomousGyroDrive(DriveTrain m_drive, IEncoder encoder, double inches, double speed, double angle) {
    this.m_sub = m_drive;
    this.speed = speed;
    this.angle = angle;
    this.m_encoder = encoder;
    this.inches = inches;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);

  }
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    this.m_encoder.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
     Duo dir = m_sub.autonomousGyroCalc(speed, angle);
    m_sub.arcadeDrive( (double) dir.second, (double) dir.first);
    
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    Log.info("Encoder = " + m_encoder.x());
    return m_encoder.x() > inches;
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
