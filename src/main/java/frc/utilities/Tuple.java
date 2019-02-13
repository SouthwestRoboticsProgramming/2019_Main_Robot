/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.utilities;

/**
 * Add your docs here.
 */
import java.util.function.BiFunction;
import java.util.function.Function;

public class Tuple<A,B> {
	public A first;
	public B second;
	public Tuple(A a, B b)  {
		this.first = a;
		this.second = b;
	}
	public Tuple() {}
	public String toString() {
		return "(" + this.first.toString() + " " + this.second.toString() + ")";
	}
}