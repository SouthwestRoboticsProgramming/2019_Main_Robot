/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.enums;

/**
 * Add your docs here.
 */
public enum Somatotype {
    kExpand(1), kContract(0);
    
    // endo is short, ecto is long
    private int val;
    private Somatotype(int val) {
        this.val = val;
    }
    public boolean toBoolean() {
        return val == 1;
    }
}
