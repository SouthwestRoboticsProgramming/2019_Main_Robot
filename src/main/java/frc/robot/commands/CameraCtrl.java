/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.*;
import frc.robot.*;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Timer;


public class CameraCtrl extends Command {
  private CameraDirection m_cam;
  private int m_direction;
  private Timer timer = new Timer();
  private OI m_oi;
  public CameraCtrl(CameraDirection camera, int direction) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(camera);
    m_cam = camera;
    m_direction = direction;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    
    Log.info("GOIING TO " + m_direction);
    m_cam.setAngle(m_direction);
    
    timer.start();
    // Robot.oi.pilot.setReversed(m_direction < 90);
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    //m_oi.pilot.getAnalog(Hand.kRight,AxisType.kX);
    // m_cam.setAngle(m_direction);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return timer.get() > 1d;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    // m_cam.setSpeed(0);
    // m_cam.setSpeed(0);
    // Log.info("CAMERA ENDED AT t=" + timer.get());
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    // Log.info("CAMERA INTERRUPTED AT t=" + timer.get());
  }
}
