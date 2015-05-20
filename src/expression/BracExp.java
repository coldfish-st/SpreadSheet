package expression;

/**
 *  An expression for the bracket operations.
 * @author Zhenge Jia 2015
 *
 */

public class BracExp extends Expression {
	Expression e;
	@Override
	public double evaluate() {
		
		return e.evaluate();
	}

	@Override
	public String show() {
		
		return "(" + e.show() + ")";
	}

	
	public BracExp(Expression e) {

		this.e = e;
	}
	// These methods are created for the order of operations.
	@Override
	Expression insertsub(Expression term) {
		
		return new Minus(term, this);
	}

	@Override
	Expression insertadd(Expression term) {
		// TODO Auto-generated method stub
		return new Addition(term, this);
	}

	@Override
	Expression insertmult(Expression ope) {
		// TODO Auto-generated method stub
		return new Multiplication(ope, this);
	}

	@Override
	Expression insertdiv(Expression ope) {
		// TODO Auto-generated method stub
		return new Divide(ope, this);
	}

}
