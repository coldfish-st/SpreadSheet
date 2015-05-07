package expression;


import spreedsheet.WorkSheet;



/**
 * Exp - a simple expression for calculating integers expressions. 
 * 
 * @author Eric McCreath
 * @param <WorkSheet>
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

	public static double tokenizeParseShowEvaluate(String text, WorkSheet worksheet) {
		System.out.println(text);
		Tokenizer t = new SimpleTokenizer(text);
		System.out.println(t);
		Expression e = Expression.parse(t, worksheet);
		System.out.println(e);
		return e.evaluate();
	}


	public static Expression parse(Tokenizer tok, WorkSheet worksheet) {
		Object t = tok.current();
		if (tok.current() instanceof Double) {
			double num = (double) t;
			Expression exp1 = new Number(num);
			tok.next();
			if (tok.current() != null && tok.current().equals("+")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Addition(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("-")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Minus(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("*")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Multiplication(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("/")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Divide(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("^")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Power(exp1, exp2);
			} else {
				return new Number(num);
			}
		} else if (tok.current() != null && tok.current().equals("(")) {
			tok.next();
			Expression exp1 = parse(tok, worksheet);

			tok.next();
			if (tok.current() != null && tok.current().equals("+")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Addition(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("-")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Minus(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("*")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Multiplication(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("/")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Divide(exp1, exp2);
			} else if (tok.current() != null && tok.current().equals("^")) {
				tok.next();
				Expression exp2 = parse(tok, worksheet);
				return new Power(exp1, exp2);
			}
		} else if (tok.current() != null && tok.current().equals("-")) {
			tok.next();
			Expression exp = parse(tok, worksheet);
			return new Inverse(exp);
			
		} else if (tok.current() != null && tok.current().equals("e")) {
			return new E();
			
		} else if(tok.current() != null && tok.current().equals("p")) {
			return new Pi();
			
		} else if (t instanceof String && Character.isUpperCase(((String) t).charAt(0))) {
			tok.next();
			System.out.println("This cell is "+t);
			return new CellEle((String) t, worksheet);
		}
			
		return null;

	}



}
