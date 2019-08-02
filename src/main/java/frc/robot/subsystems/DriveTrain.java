/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import frc.robot.commands.Drive;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Interfaces.IGyro.Axis;
import edu.wpi.first.wpilibj.GenericHID.Hand;
//import frc.utilities.Tuple;
// import frc.robot.sensors.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// import frc.utilities.Calc;
// import frc.utilities	Duo;
import edu.wpi.first.wpilibj.AnalogGyro;
//import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
//import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.interfaces.Gyro;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Interfaces.*;
import frc.robot.sensors.*;
import frc.robot.*;
import frc.utilities.*;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import frc.robot.sensors.LimeLight;
import frc.robot.sensors.In;

public class DriveTrain extends Subsystem {

	public static double speedFraction = .5;
	public static double turnFraction = .75;

	private static final double MAX_CHANGE = .5;
	// private static final double MAX_SPEED = 1;
	private	double Pl = 0;
	private double Pr = 0;

	private double Pradius = 0;
	private double Ptheta = 0;

	private final boolean isInvertedLeft = true;
	private final boolean isInvertedRight = false;

	private final double LOCKED_JOY_EFFECT = .3;

	private final double MAG_TO_BRAKE = .1;

	private double modifier = 1;
	
	// private final double SPEED_DIFFERENTIAL = 0.2;
	//private Tuple<Double,Double> previous_heading = new Tuple<Double,Double>(0d,0d);
	
	private WPI_TalonSRX leftRearMotor;
	private WPI_TalonSRX rightRearMotor;
	private WPI_TalonSRX rightFrontMotor;
	private WPI_TalonSRX leftFrontMotor;
	private SpeedControllerGroup leftSpeedController;
	private SpeedControllerGroup rightSpeedController;
	
	private DifferentialDrive m_drive;
	//private OI m_oi;
	


	private final WheelEncoder left;
	private final WheelEncoder right;
	
	public DriveTrain()
	{
		
		leftRearMotor        = new WPI_TalonSRX(RobotMap.LEFT_REAR_MOTOR);
		rightRearMotor       = new WPI_TalonSRX(RobotMap.RIGHT_REAR_MOTOR);
		rightFrontMotor      = new WPI_TalonSRX(RobotMap.RIGHT_FRONT_MOTOR);
		leftFrontMotor       = new WPI_TalonSRX(RobotMap.LEFT_FRONT_MOTOR);
		leftSpeedController  = new SpeedControllerGroup(leftFrontMotor,leftRearMotor);
		rightSpeedController = new SpeedControllerGroup(rightFrontMotor,rightRearMotor);

		leftSpeedController.setInverted(this.isInvertedLeft);
		rightSpeedController.setInverted(this.isInvertedRight);
		
		m_drive              = new DifferentialDrive(leftSpeedController, rightSpeedController);
		
		left  = new WheelEncoder(leftRearMotor, false);
		right = new WheelEncoder(rightRearMotor, true);



		//calibrating

	}

	// public IEncoder getLeftEncoder() {
	// 	return new WheelEncoder(this.leftRearMotor, true);
	// }
	// public IEncoder getRightEncoder() {
	// 	return new WheelEncoder(this.rightRearMotor, false);
	// }
	public IEncoder getEncoder(Hand hand) {
		return 	hand == Hand.kRight ? right : left ; 
	}

	@Override
    public void initDefaultCommand() {
		setDefaultCommand(new Drive(this));

        // Set the default command for a subsystem here.
		//setDefaultCommand(new CanDriveCommand(this));

	}


    public void drive(double left, double right) {
		left = (left - Pl) * MAX_CHANGE + Pl;
		right = (right - Pr) * MAX_CHANGE + Pr;
		m_drive.tankDrive( left * modifier,  right * modifier); // multiplied by neg 1
		Pl = left;
		Pr = right;
    }
    public void brake() {
		leftRearMotor.stopMotor();
		leftFrontMotor.stopMotor();
		rightRearMotor.stopMotor();
		rightFrontMotor.stopMotor();
	}
	// public void driveCommandPeriodic() {
	// 	double x = m_oi.getDriveXAxisValue();
	// 	double y = m_oi.getDriveYAxisValue();
	// 	Duo cDir = new	Duo(x,y);
	// 	Duo change = cDir.sub(pDir);
	// 	if (change.mag() > MAX_CHANGE) {
	// 		// double a = Math.pow(x,2) + Math.pow(y,2);
	// 		// double b = -2 * x * (double) pDir.first - 2 * y * (double) pDir.second;
	// 		// double c = Math.pow((double) pDir.first,2) + Math.pow((double) pDir.second,2) - MAX_CHANGE;
	// 		// double k = (-b + Math.sqrt(b*b - 4*a*c)) / 2 / a;
	// 		// x = k * x;
	// 		// y = k * y;
	// 		// cDir = cDir.op(q -> (double) q * k);
	// 		double k = Math.sqrt(Math.pow(MAX_CHANGE,2) / (double) change.op(a -> Math.pow((double) a,2) ).add());
	// 		change = change.mult(k);
	// 		cDir = pDir.add(change);
	// 	}
	// 	this.pDir = cDir;
	// 	this.arcadeDrive(x, y);
	// }

	// public void followLine() {
	// 	// double skew = In.lime.getSkew();
	// 	Log.info(In.lime.getSkew());
	// }
	public void tankDrivePeriodic(double l, double r, boolean l_trigg, boolean r_trigg) {
		// double l = Robot.oi.pilot.getAnalog(Hand.kLeft);
		// double r = Robot.oi.pilot.getAnalog(Hand.kRight);
		// l = (l - Pl) * MAX_CHANGE + Pl ;
		// r = (r - Pr) * MAX_CHANGE + Pr;
		// boolean right = Robot.oi.pilot.getTrigger(Hand.kRight) == 1d;
		// boolean left  = Robot.oi.pilot.getTrigger(Hand.kLeft) == 1d;
		boolean rht = r_trigg && !l_trigg;
		boolean lft = l_trigg && !r_trigg;
		this.drive(Calc.val(lft) * l + Calc.val(rht) * (r + l * LOCKED_JOY_EFFECT) + l * Calc.val(Calc.nor(lft,rht)) * .5,Calc.val(rht) * r + Calc.val(lft) * (l + r * LOCKED_JOY_EFFECT) + r * Calc.val(Calc.nor(rht,lft))*.5 );
		// Pl = l;
		// Pr = r;
	}
	public void arcadeDrivePeriodic() {
		double theta = Robot.oi.pilot.getAnalog(AxisType.kX);
		double r = Robot.oi.pilot.getAnalog(AxisType.kY);

		theta = (theta - Ptheta) * MAX_CHANGE + Ptheta;
		r = (r - Pradius) * MAX_CHANGE + Pradius;

		this.arcadeDrive(r, theta);

		Ptheta = theta;
		Pradius = r;
	}

    // public	Duo autonomousGyroCalc(double speed, double degrees) {
	// 	double gyroAng    = this.getAngle();
	// 	double currentAng = Calc.fxAng(Math.toRadians(90 - gyroAng));
		
	// 	double rateZ = - Math.toRadians(gyro.getRateZ());

	// 	double changeAng  = Calc.fxAng(Math.toRadians(degrees) - currentAng);

	// 	// double turnSpeed = - 1.5 * changeAng/Math.max(Math.abs(gyro.getAccelZ()) * 2, 1);
	// 	double turnSpeed = 10 * (changeAng - rateZ);
	// 	Log.info("Turn Speed = " + turnSpeed);
    // 	return new	Duo(turnSpeed, speed);
	// }
	public	Duo autonomousGyroCalc(double speed, double degrees) {
		double currentAng = Calc.fxDeg(In.gyro.theta(Axis.Z)); // Maybe for some reason getAccel(); 
		double rateZ = In.gyro.omega();
		double changeAng  = Calc.fxDeg(degrees - currentAng);
		Log.info("GYRO = " + currentAng);
		// double turnSpeed = - 1.5 * changeAng/Math.max(Math.abs(gyro.getAccelZ()) * 2, 1);
		double turnSpeed = (changeAng - rateZ / 3d ) / 30d;
		// Log.info("Turn Speed = " + turnSpeed);
    	return new	Duo(turnSpeed, speed);
	}

	public void absFace(double degrees) {
		double currentAng = Calc.fxDeg(In.gyro.theta(Axis.Z));
		double changeAng  = Calc.fxDeg(degrees - currentAng);
		double turnSpeed  = (changeAng - In.gyro.omega() / 3) / 30;
		m_drive.arcadeDrive(0,turnSpeed);
	}


	public void limelightFollow() {
		//double chngAng = Calc.fxDeg(newDegrees - currentDegrees);
		//double turnSpeed = (chngAng - In.gyro.omega() / 3d) / 30d;
		//double turnSpeed = Math.sqrt(Math.abs(chngAng))*Math.signum(chngAng)/180;
		//Log.info(tx);
		double tx = In.lime.getX();
		double ty = In.lime.getY();
		Log.info("ty: "+ty);
		double left = Math.pow(Math.max(Math.min(ty / 20.5, .5),-.5),2d) + .3;//Math.max(Math.min(ty, .3),0);
		double right = Math.pow(Math.max(Math.min(-ty /20.5,.5),-.5),2d) + .3;//Math.max(Math.min(-ty, .3),0);
		this.drive(-left,-right);
	}

	public void lineFollow(double l, double r) {
		//Based off https://github.com/NateDKing/Nate-2019/blob/master/FRC%202019%20TEST/src/main/java/frc/robot/subsystems/DriveSubsystem.java
		double tx = In.lime.getX();
		double ty = In.lime.getY();
		double lineOffSet = (tx / 15 * .25);
		drive.tankDrive(l+lineOffSet, r-lineOffSet)
	}
	/*
	public void driveCommandPeriodic() {
    	Tuple<Double,Double> input = new Tuple<>();
    	input.first =  turnFraction * m_oi.getDriveXAxisValue();
		input.second = speedFraction * m_oi.getDriveYAxisValue();
		Tuple<Double,Double> difference = new Tuple<Double,Double>(input.second - previous_heading.first, input.first - previous_heading.second );
    	if(difference.mag() > this.SPEED_DIFFERENTIAL) {
    		double a = Math.sqrt(Math.pow(this.SPEED_DIFFERENTIAL,2) / ( Math.pow(difference.first,2) + Math.pow(difference.second, 2)));
			input = new Tuple<Double, Double>(difference.first * a, difference.second * a);
			//.apply((q,u) -> q + u, v_p);
    	}
    	this.v_p = v_c;
    	m_drive.arcadeDrive(v_c.x, v_c.y);
	}
	*/
    public void arcadeDrive(double speed, double rotation) {
		m_drive.arcadeDrive(speed,-rotation);
	}
	public void isHalf(boolean val) {
		modifier = 1d - Calc.val(val) * .3;
	}
	/*
    public void gyroDrive() {
    	V vec = gyroCalc(new V( m_oi.getDriveXAxisValue(), m_oi.getDriveYAxisValue() ));
    	m_drive.arcadeDrive(vec.y, vec.x);
    }
    public V gyroCalc(V v_c) {
    	double inputAng   = atrig(v_c.x, v_c.y); // this is radians
    	double gyroAng    = gyro.getAngleZ();    // this is degrees
    	double currentAng = fxAng(radians(90-gyroAng));
    	double changeAng  = inputAng - currentAng;
    	double turnSpeed = - fxAng(changeAng) / Math.max(Math.abs(gyro.getAccelZ()) * 2, 1);
    	if (v_c.mag() <= .15 && !m_oi.x.get() ) {
    		return new V(0,0);
    	}
    	if (!m_oi.x.get()) {

//    		V v_drive = new V( - fxAng(changeAng), Math.hypot(v_c.x, v_c.y));
    		V v_drive = new V(turnSpeed, v_c.mag() * .5);
    		return v_drive;
    	}
    	else {
    		V v_drive = new V(turnSpeed, -.5);
    		return v_drive;
    	}
    }
    public V autonomousGyroCalc(double speed, double degrees) {
    	double gyroAng    = gyro.getAngleZ();    // this is degrees
    	double currentAng = fxAng(radians(90-gyroAng));
    	double changeAng  = radians(degrees) - currentAng;
    	double turnSpeed = - fxAng(changeAng) / Math.max(Math.abs(gyro.getAccelZ()) * 2, 1);
    	return new V(turnSpeed * 10, speed);
    }
    private static double radians(double degrees) {
    	return degrees * Math.PI / 180;
    }
    private static double degrees(double radians) {
    	return radians * 180 / Math.PI;
    }
    //takes side lengths x and y and returns angle.
	private static double atrig(double x, double y) {
//		if (Math.hypot(x, y)<.3) return Math.PI/2; // returning default value.
		
//		return fxAng(Math.atan(y/x) - Math.signum(Math.signum(x)-1)*Math.PI);
		if (x == 0 && y == 0) return 0;
		return 	x < 0 ? 
				Math.atan(y/x) + Math.PI :
				Math.atan(y/x);
	}
	
    private static double fxAng(double theta) {
    	if (-Math.PI < theta && theta <= Math.PI)
    		return theta;
    	else
    		return fxAng(theta - Math.signum(theta) * 2 * Math.PI);
    }
    public V smooth(V v_c, double speed_diff) {
    	V v_d = v_c.apply((q,u) -> q - u, this.v_p);
    	if (v_d.mag() > speed_diff) {
    		final double a = Math.sqrt( Math.pow(speed_diff, 2) / Math.pow(v_d.mag(),2) );
    		return (this.v_p = v_d.apply(q -> q * a).apply((q,u) -> q + u, this.v_p));
    	}
    	return (this.v_p = v_c);
	}
	*/
}
