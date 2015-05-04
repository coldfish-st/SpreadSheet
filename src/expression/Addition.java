package expression;


/**
 * AddExp - an expression for binary addition. 
 * 
 * @author Eric McCreath
 *
 */

public class Addition extends Expression {
	Expression e1, e2;
	
	@Override
        public	double evaluate() {
		
		return e1.evaluate() + e2.evaluate();
	}

	public Addition(Expression e1, Expression e2) {
		super();
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
        public String show() {
		
		return "(" + e1.show() + " + " + e2.show() + ")";
	}

}
