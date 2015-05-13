package expression;

public class Mod extends Expression {
	Expression e1, e2;
	@Override
	public double evaluate() {
		
		return e1.evaluate() % e2.evaluate();
	}
	
	public Mod(Expression e1, Expression e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public String show() {
		
		return "(" + e1.show() + " % " + e2.show() + ")";
	}

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
