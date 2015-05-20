package expression;

/**
 * An expression for the constant e.
 * @author Zhenge Jia 2015
 *
 */

public class E extends Expression {

	@Override
	public double evaluate() {
		
		return 2.71828;
	}

	@Override
	public String show() {
		
		return " e";
	}
	// These methods are created for the order of operations.
	@Override
        Expression insertsub(Expression term) {
	       
	        return new Minus(term, this);
        }
	@Override
        Expression insertadd(Expression term) {
	     
	        return new Addition(term, this);
        }
	@Override
        Expression insertmult(Expression ope) {
	       
	        return new Multiplication(ope, this);
        }
	@Override
        Expression insertdiv(Expression ope) {
	        
	        return new Divide(ope, this);
        }
}
