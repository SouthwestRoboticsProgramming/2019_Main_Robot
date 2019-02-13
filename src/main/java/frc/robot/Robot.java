/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.ADIS16448_IMU;
import frc.robot.sensors.LimeLight;
import frc.robot.sensors.VerticalGyro;
import frc.utilities.*;
import frc.robot.Interfaces.IGyro.Axis;
import frc.robot.subsystems.DriveTrain;
import frc.robot.commands.AutonomousChain;
import frc.robot.commands.AutonomousGyroDrive;
import frc.robot.commands.Drive;
import frc.robot.sensors.In;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static DriveTrain m_drive;
  private static OI m_oi;



  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  Command m_driveCommand;
  /*
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    In.gyro.calibrate();
    Log.info("calibrating");

    m_oi = new OI();
    m_drive = new DriveTrain(m_oi);

    m_autonomousCommand = new AutonomousChain(m_drive);

    m_driveCommand = new Drive(m_drive);
    m_chooser.setDefaultOption("Drive Command", m_driveCommand);
//    m_chooser.addOption("Autonomous Command", m_autonomousCommand);
    // m_chooser.addObject("Drive command",new DriveCommand(m_drive));
    // chooser.addOption("My Auto", new MyAutoCommand());




  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    // Log.info("Angle = " + m_drive.getAngle());
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {


    // m_autonomousCommand = m_chooser.getSelected();
    
    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {



  //   if(m_drive.getLeftEncoder() < 300) {
  //     NumberTuple direction = m_drive.autonomousGyroCalc(.2, 90.001d);
  //     m_drive.arcadeDrive((double) direction.first, (double) direction.second);
  //   }
  // else {
  //   m_drive.brake();
  // }

    Scheduler.getInstance().run();
    // NumberTuple direction = m_drive.autonomousGyroCalc(.2, 90.001d);
    // m_drive.arcadeDrive((double) direction.first, (double) direction.second);
    
  }

  @Override
  public void teleopInit() {
    Log.info("teleopInit");
    
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    m_driveCommand.start();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Log.info("LIME VALUE  = " + In.lime.getX());
    Log.info("teleopPeriodic");
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
