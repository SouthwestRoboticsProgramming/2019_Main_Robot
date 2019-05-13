/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.enums.Somatotype;
import frc.robot.Log;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import frc.utilities.Calc;
/**
 * Add your docs here.
 */
public class Vacuum extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
    private final int SOLENOID_ID = 6 ; // 7 in original robot
    private static final int PCM_ID = 37; // THis can't be set to 0;
    
    private static final int VACUUM_1 = 0;
    private static final int VACUUM_2 = 1;
    
    private final WPI_VictorSPX vacuum_motor_1;
    private final WPI_VictorSPX vacuum_motor_2;

    private final Solenoid vacuumSolenoid;

    private boolean status = false;

    public Vacuum() { 
        System.out.println("Starting Vacuum()...");
        this.vacuumSolenoid = new Solenoid(PCM_ID, SOLENOID_ID);
        vacuum_motor_1 = new WPI_VictorSPX(VACUUM_1);
        vacuum_motor_2 = new WPI_VictorSPX(VACUUM_2);
        System.out.println("....Vacuum() completed");
    }

    //private final OneSolenoid solenoid = new OneSolenoid(SOLENOID_ID);

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  //public void setState(Somatotype state) {
  //  solenoid.set(state);
  //
  public void setSolenoid(boolean state) {
    Log.info("set state to "+state);
    vacuumSolenoid.set(state);
  }
  public void setVacuum(boolean state) {
    double v = Calc.val(state);
    vacuum_motor_1.set(v);
    vacuum_motor_2.set(v);
  }
  public void toggle() {
    status = ! status;
    set(status);
  }
  public void set(boolean state) {
    status = state;
    setSolenoid(state);
    setVacuum(state);
  }
}
