package expression;


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
