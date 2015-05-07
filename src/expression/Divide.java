package expression;


public class Divide extends Expression {
	Expression e1, e2;
	@Override
	public double evaluate() {
		
		return e1.evaluate() / e2.evaluate();
	}

	public Divide(Expression e1, Expression e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@Override
	public String show() {
		// TODO Auto-generated method stub
		return "(" + e1.show() + " / " + e2.show() + ")";
	}

}