package expression;


public class Power extends Expression {
	Expression e1, e2;
	@Override
	public double evaluate() {
		
		return Math.pow (e1.evaluate(), e2.evaluate())  ;
	}

	public Power(Expression e1, Expression e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@Override
	public String show() {
		
		return "(" + e1.show() + " ^ " + e2.show() + ")";
	}

}
