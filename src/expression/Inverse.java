package expression;


public class Inverse extends Expression {
	Expression e;
	@Override
	public double evaluate() {
		
		return -e.evaluate();
	}
	public Inverse(Expression e) {
		super();
		this.e = e;

	}
	@Override
	public String show() {
		
		return " - "+e.show();
	}

}
