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
		
		return Arith.add(e1.evaluate(), e2.evaluate()) ;
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

	@Override
        Expression insertsub(Expression term) {
	        
	        return new Addition(e1.insertsub(term), e2);
        }

	@Override
        Expression insertadd(Expression term) {
	        // TODO Auto-generated method stub
	        return new Addition(e1.insertadd(term), e2);
        }

	@Override
        Expression insertmult(Expression ope) {
	        
	        return new Addition(e1.insertmult(ope), e2);
        }

	@Override
        Expression insertdiv(Expression ope) {
	        // TODO Auto-generated method stub
	        return new Addition(e1.insertdiv(ope), e2);
        }

}
