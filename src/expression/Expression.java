package expression;


import java.text.ParseException;




/**
 * Exp - a simple expression for calculating integers expressions. 
 * 
 * @author Eric McCreath
 *
 */


public abstract class Expression {
	
	public abstract double evaluate();
	public abstract String show();
	
	/*
	 *     <exp> ::= <number> | ( <exp> <binop> <exp> ) | <uniop> <exp> | <constant>
    		<binop> ::= * | / | + | - | **
    		<uniop> ::= -
    		<constant> ::= e | pi
	 */
	
	public static double tokenizeParseShowEvaluate(String text) {
		Tokenizer t = new SimpleTokenizer(text);
		Expression e = Expression.parse(t);
		return e.evaluate();
	}
	
	
	public static Expression parse(Tokenizer tok) {
		
		if (tok.current() instanceof Double) {
			double num = (double) tok.current();
			return new Number(num);
		} else if (tok.current() != null && tok.current().equals("(")) {
			tok.next();
			Expression exp1 = parse(tok);
			
			tok.next();
			if (tok.current() != null && tok.current().equals("+")) {
				tok.next();
				Expression exp2 = parse(tok);
				return new Addition(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("-")) {
				tok.next();
				Expression exp2 = parse(tok);
				return new Minus(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("*")) {
				tok.next();
				Expression exp2 = parse(tok);
				return new Multiplication(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("/")) {
				tok.next();
				Expression exp2 = parse(tok);
				return new Divide(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("^")) {
				tok.next();
				Expression exp2 = parse(tok);
				return new Power(exp1, exp2);
			}
		} else if (tok.current() != null && tok.current().equals("-")) {
			tok.next();
			Expression exp = parse(tok);
			return new Inverse(exp);
		} else if (tok.current() != null && tok.current().equals("e")) {
			return new E();
		} else if(tok.current() != null && tok.current().equals("p")) {
			return new Pi();
		} 
		return null;
		
	}
	


}
