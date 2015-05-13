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

	/*
	 * 
	 * The below methods parse the grammar:
	 * 
	 * <exp> ::= <term> | <term> + <exp> | <term> - <exp>
	 * <term> ::= <ope> | <ope> * <term>  | <ope> / <term>
	 * <ope> :: = <val> | <val> ^ <ope> | <val> % <ope> 
	 * <val> ::= <num> |  ( <exp> )
	 */

	public static Expression parse(Tokenizer t, WorkSheet worksheet) {
		if (t.current().equals("=")) {
			t.next();
		}
		Expression term = parseTerm(t, worksheet);
		if (t.current() != null && t.current().equals("+")) {
			t.next();
			Expression exp2 = parse(t, worksheet);
			return exp2.insertadd(term);
		} else if (t.current() != null && t.current().equals("-")) {
			t.next();
			Expression exp2 = parse(t, worksheet);
			return exp2.insertsub(term);

		} else{
			return term;
		}
			
	}
	
	abstract Expression insertsub(Expression term);
	abstract Expression insertadd(Expression term);
	
	
	public static Expression parseTerm(Tokenizer t, WorkSheet worksheet) {
		Expression ope = parseOpe(t, worksheet);
		if (t.current() != null && t.current().equals("*")) {
			t.next();
			Expression exp2 = parseTerm(t, worksheet);
			return exp2.insertmult(ope);
		} else if (t.current() != null && t.current().equals("/")) {
			t.next();
			Expression exp2 = parseTerm(t, worksheet);
			return exp2.insertdiv(ope);

		} else{
			return ope;
		}
	}
	
	
	abstract Expression insertmult(Expression ope);
	abstract Expression insertdiv(Expression ope);
	
	public static Expression parseOpe(Tokenizer t, WorkSheet worksheet) {
		Expression val = parseVal(t, worksheet);
		if (t.current() != null && t.current().equals("^")) {
			t.next();
			Expression exp2 = parseOpe(t, worksheet);
			return new Power(val, exp2);
		} else if (t.current() != null && t.current().equals("%")) {
			t.next();
			Expression exp2 = parseOpe(t, worksheet);
			return new Mod(val, exp2);
		} else {
			return val;
		}
			
	}
	
	public static Expression parseVal(Tokenizer t, WorkSheet worksheet) {
		if (t.current() != null && t.current().equals("-")){
			t.next();
			Expression exp2 = parseVal(t, worksheet);
			//t.next();
			return new Inverse(exp2);
		} else if (t.current() != null && t.current().equals("(")) {
			t.next();
			Expression exp2 = parse(t, worksheet);
			t.next(); // consume the ")" - not we should really check that it is a ")"
			return new BracExp(exp2);
		} else if (t.current() instanceof String && Character.isUpperCase(((String) t.current()).charAt(0)) && 
				Character.isDigit(((String) t.current()).charAt(1))) {
			String cell = (String) t.current();
			t.next();
			return new CellEle(cell, worksheet);
		} else if (t.current() != null && t.current().equals("e")) {
			t.next();
			return new E();

		} else if(t.current() != null && t.current().equals("p")) {
			t.next();
			return new Pi();

		} else {
			double num = (double) t.current();
			t.next();

			return new Number(num);

		}
			
	}


}
