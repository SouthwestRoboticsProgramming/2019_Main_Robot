/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.utilities;
import java.util.function.Function;
public class Lst<A> {
// Lst<Double> list = new Lst(1, new Lst(2, new Lst(3, new Lst(4, null))));
	private A 		head = null;
	private Lst<A> 	tail = null;
    public Lst()        {}
	public Lst(A a)     {this.head = a;}
	public Lst(A x, Lst<A> xs) {
		this.head = x;
		this.tail = xs;
	}
	public A head()     {return this.head;}
    public Lst<A> tail(){return this.tail;}
    
    public <B> Lst<B> map(Function<A,B> fn) {
        if (this.tail == null) return new Lst<B>(fn.apply(this.head()));
        else                   return new Lst<B>(fn.apply(this.head()), this.tail().map(fn));
    }
    public <B> Lst<Tuple<A,B>> zip(Lst<B> arg0) {
        if (this.tail == null || arg0.tail == null) return new Lst<Tuple<A,B>>(new Tuple<A,B>(this.head, arg0.head));
        else                                        return new Lst<Tuple<A,B>>(new Tuple<A,B>(this.head, arg0.head), this.tail.zip(arg0.tail));
    }
    public static Lst<Integer> range(int min, int max) {
        if (min > max)  return new Lst<Integer>();
        else            return new Lst<Integer>(min, range(min + 1, max));
    }
    public static Lst<Integer> range(int max) {
        return range(0,max);
    }
    
}