package expression;
/**
 * An expression for the binary division
 * @author Zhenge Jia
 *
 */

public class Divide extends Expression {
	Expression e1, e2;
	@Override
	public double evaluate() {
		
		return Arith.div(e1.evaluate(), e2.evaluate())  ;
	}

	public Divide(Expression e1, Expression e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}
	
	@Override
	public String show() {
		
		return "(" + e1.show() + " / " + e2.show() + ")";
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
	        
	        return new Divide(e1.insertmult(ope), e2);
        }

	@Override
        Expression insertdiv(Expression ope) {
	        
	        return new Divide(e1.insertdiv(ope), e2);
        }

}
