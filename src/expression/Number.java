package expression;

/**
 * NumExp - an expression for integers. 
 * 
 * @author Zhenge Jia 2015
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
