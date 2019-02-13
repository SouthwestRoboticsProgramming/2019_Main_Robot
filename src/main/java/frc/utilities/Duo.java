package frc.utilities;

import java.util.function.BiFunction;
import java.util.function.Function;
public class Duo extends Tuple<Number, Number> {

	public Duo(Number one, Number two){
		super(one, two);
	}
	public double mag() {
		return Math.hypot((double) this.first, (double) this.second);	
	}
	public Tuple<Double,Double> mask() {
		return new Tuple<Double,Double>((double) this.first, (double) this.second);
	}
	public Number merge(BiFunction<Number,Number,Number> fn) {
		return fn.apply(this.first, this.second);
	}

	public Number add() {
		return (double) this.first + (double) this.second;
	}
	public Number sub() {
		return (double) this.first - (double) this.second;
	}
	public Number mult() {
		return (double) this.first * (double) this.second;
	}
	public Number div() {
		return (double) this.first / (double) this.second;
	}

	public Duo op(Function<Number,Number> fn) {
		return new Duo(fn.apply(this.first), fn.apply(this.second));
	}

	public Duo add(Number x) {
		return this.op(a ->(double) a + (double) x);
	}
	public Duo sub(Number x) {
		return this.op(a -> (double) a - (double) x);
	}
	public Duo div(Number x) {
		return this.op(a -> (double) a / (double) x);
	}
	public Duo mult(Number x) {
		return this.op(a -> (double) a * (double) x);
	}
	public Duo op(BiFunction<Number,Number,Number> fn, Duo other) {
		return new Duo(fn.apply(this.first, other.first), fn.apply(this.second, other.second));
	}
	public Duo add(Duo other) {
		return this.op((a,b) -> (double) a + (double) b, other);
	}
	public Duo sub(Duo other) {
		return this.op((a,b) -> (double) a - (double) b, other);
	}
	public Duo mult(Duo other) {
		return this.op((a,b) -> (double) a * (double) b, other);
	}
	public Duo div(Duo other) {
		return this.op((a,b) -> (double) a / (double) b, other);
	}
	
	//  public Tuple<Double,Double> op(BiFunction<Double,Double,Double> fn, Tuple<C,D> tup) {
	//  	return new Tuple<Double,Double>(fn.apply((double) first, (double) tup.first), fn.apply((double) second, (double) tup.second));
	// }
	
}