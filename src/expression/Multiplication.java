package expression;


/**
 * An expression for binary multiplication. 
 * 
 * @author Zhenge Jia 2015
 *
 */


public class Multiplication extends Expression {
	Expression e1, e2;
	
	@Override
        public 	double evaluate() {
		
		return Arith.mul(e1.evaluate(),e2.evaluate() )  ;
	}

	public Multiplication(Expression e1, Expression e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
        public	String show() {
		
		return "(" + e1.show() + " * " + e2.show() + ")";
	}

	// These methods are created for the order of operations.
	@Override
        Expression insertsub(Expression term) {
	        // TODO Auto-generated method stub
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
	        return new Multiplication(e1.insertmult(ope), e2);
        }

	@Override
        Expression insertdiv(Expression ope) {
	        // TODO Auto-generated method stub
	        return new Multiplication(e1.insertdiv(ope), e2);
        }
}
