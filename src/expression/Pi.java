package expression;


public class Pi extends Expression {

	@Override
	public double evaluate() {
		
		return 3.14159;
	}

	@Override
	public String show() {
		
		return " "+"pi";
	}

}
