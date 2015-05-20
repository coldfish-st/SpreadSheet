package expression;

/**
 * An expression for the constant PI
 * @author Zhenge Jia 2015
 *
 */

public class Pi extends Expression {

	@Override
	public double evaluate() {
		
		return 3.14159;
	}

	@Override
	public String show() {
		
		return " "+"pi";
	}
	@Override
        Expression insertsub(Expression term) {
	       
	        return new Minus(term, this);
        }
	// These methods are created for the order of operations.
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
