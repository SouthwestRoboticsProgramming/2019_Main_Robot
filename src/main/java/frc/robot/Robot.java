/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
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
import frc.robot.subsystems.Ramp;
import frc.robot.commands.AutonomousChain;
import frc.robot.commands.Drive;
import frc.robot.sensors.In;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.subsystems.*;
import frc.robot.commands.*;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static DriveTrain drive;
  public static final Arm arm = new Arm();
  public static final Ramp ramp = new Ramp();
  public static final Climber climb = new Climber();
  public static final Vacuum vacuum = new Vacuum();
  public static final Camera cam = new Camera(); 

  private static final OI oi = new OI();

  public static final double roundDuration = .02;
 /*
  abreviations :S
  x = position
  v = velocity 
  a = acceleration

  theta = angular position
  omega = angular velocity
  alpha = angular acceleration
 */

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  Command m_teleopCommand;
  /*
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    In.gyro.calibrate();
    Log.info("calibrating");
    System.out.println("Starting robot init..." + vacuum); 
    
    drive = new DriveTrain(oi);

    m_autonomousCommand = new AutonomousChain(drive);
    m_teleopCommand = new TeleopCtrl(drive,arm,cam,oi);
    
    m_chooser.setDefaultOption("Teleop Command", m_teleopCommand);

    CameraServer.getInstance().startAutomaticCapture();
    
    // UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    // camera.setVideoMode(PixelFormat.kBGR , 300, 150, 20);

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
    //Log.info("teleopInit");
    
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }

    m_teleopCommand.start();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //Log.info("teleopPeriodic");
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
