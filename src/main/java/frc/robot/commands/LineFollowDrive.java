package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.DriveTrain;
import frc.robot.sensors.WheelEncoder;
import frc.robot.Log;

public class LineFollowDrive extends Command {
  DriveTrain m_sub;
  public LineFollowDrive(DriveTrain m_drive) {
    this.m_sub = m_drive;
    requires(m_sub);

  }
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    // this.m_encoder.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //  Duo dir = m_sub.autonomousGyroCalc(speed, angle);
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
