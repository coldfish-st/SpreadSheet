package expression;


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
		// TODO Auto-generated method stub
		return "(" + e1.show() + " / " + e2.show() + ")";
	}

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
	        return new Divide(e1.insertmult(ope), e2);
        }

	@Override
        Expression insertdiv(Expression ope) {
	        // TODO Auto-generated method stub
	        return new Divide(e1.insertdiv(ope), e2);
        }

}
