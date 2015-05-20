package expression;

/**
 *  An expression for the binary subtraction.
 * @author Zhenge Jia 2015
 *
 */

public class Minus extends Expression {
	Expression e1, e2;
	
	@Override
	public double evaluate() {
		
		return Arith.sub(e1.evaluate(), e2.evaluate());
	}

	public Minus(Expression e1, Expression e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@Override
	public String show() {
		
		return  "(" + e1.show() + " - " + e2.show() + ")";
	}

	// These methods are created for the order of operations.
	@Override
        Expression insertsub(Expression term) {
	        // TODO Auto-generated method stub
	        return new Minus(e1.insertsub(term), e2);
        }

	@Override
        Expression insertadd(Expression term) {
	        // TODO Auto-generated method stub
	        return new Minus(e1.insertadd(term), e2);
        }

	@Override
        Expression insertmult(Expression ope) {
	        // TODO Auto-generated method stub
	        return new Minus(e1.insertmult(ope), e2);
        }

	@Override
        Expression insertdiv(Expression ope) {
	        // TODO Auto-generated method stub
	        return new Minus(e1.insertdiv(ope), e2);
        }

}
