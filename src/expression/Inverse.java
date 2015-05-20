package expression;

/**
 * An expression for the unary operation inverse.
 * @author Zhenge Jia 2015
 *
 */

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
