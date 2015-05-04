package expression;

/**
 * NumExp - an expression for integers. 
 * 
 * @author Eric McCreath
 *
 */


public class Number extends Expression {
   double num;
	
	@Override
	public double evaluate() {
		
		return num;
	}

	@Override
	public String show() {
		
		return "" + num;
	}

	public Number(double d) {
		super();
		this.num = d;
	}

}
