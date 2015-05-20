package expression;

import javax.swing.JOptionPane;

import spreedsheet.Cell;
import spreedsheet.CellIndex;
import spreedsheet.Spreadsheet;
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

	public static double tokenizeParseShowEvaluate(String text, WorkSheet worksheet) throws Exception  {
		System.out.println(text);
		Tokenizer t = new SimpleTokenizer(text);
		System.out.println("t "+t);
		Expression e = Expression.parse(t, worksheet);
		System.out.println("e "+e);
		return e.evaluate();
	}

	/*
	 * 
	 * The below methods parse the grammar:
	 * 
	 * <exp> ::= <term> | <term> + <exp> | <term> - <exp>
	 * <term> ::= <ope> | <ope> * <term>  | <ope> / <term>
	 * <ope> :: = <val> | <val> ^ <ope> | <val> % <ope> 
	 * <val> ::= <num> |  ( (<exp> ) | - <exp>
	 */

	public static Expression parse(Tokenizer t, WorkSheet worksheet) throws Exception {
		if (t.current().equals("=")) {
			System.out.println("Test for function begining");
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


	public static Expression parseTerm(Tokenizer t, WorkSheet worksheet) throws Exception {
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

	public static Expression parseOpe(Tokenizer t, WorkSheet worksheet) throws Exception {
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

	public static Expression parseVal(Tokenizer t, WorkSheet worksheet) throws Exception {
		
		if ( t.current() instanceof String &&  ( (String) t.current()).length() >= 2   
				&& Character.isUpperCase(((String) t.current()).charAt(0)) 
				&& Character.isUpperCase(((String) t.current()).charAt(1))) {
			
			String func = (String) t.current();
			

			t.next();
			// exception for ().
			if (!((String)t.current()).equals("(")) {
				ParseException cell = new ParseException(t.current(), 1);  
				cell.feedback();
				return null;
			}
			
			t.next();
			// exception for cell index
			System.out.println("test for cell index "+t.current());
			if ( !Character.isUpperCase(((String)t.current()).charAt(0)) 
					|| ((String)t.current()).length() > 2) {
				ParseException cell = new ParseException(t.current(), 2);  
				cell.feedback();
				return null;
			}
			
			Object test = t.current();
			CellIndex index1 = new CellIndex((String) test);


			t.next();
			// exception for the colon
			if (!((String)t.current()).equals(":")) {
				ParseException cell = new ParseException(t.current(), 3);  
				cell.feedback();
				return null;
			}
			//t.parse(":");
			System.out.println("3Test for function begining"+t.current());
			t.next();
			// test for cell index second part
			if ( !Character.isUpperCase(((String)t.current()).charAt(0)) 
					|| ((String)t.current()).length() > 2) {
				ParseException cell = new ParseException(t.current(), 2);  
				cell.feedback();
				return null;
			}
			Expression exp2 = new  CellEle((String) t.current(), worksheet);
			CellIndex index2 = new CellIndex((String) t.current());
			t.next();

			int icol = index1.column();
			int irow = index1.row();
			int jcol = index2.column();
			int jrow = index2.row();
			System.out.println(icol);
			System.out.println(irow);
			System.out.println(jcol);
			System.out.println(jrow);
			int count = (jcol-icol+1) * (jrow-irow+1);
			double[] input = new double[count];
			int n = icol;
			int i =0;
			for (;irow < jrow+1; irow++) {

				for ( ; n < jcol+1; n++) {
					CellIndex index =new CellIndex(n, irow);
					Cell cell =  worksheet.tabledata.get(index);
					if (cell.getText().equals("")) {
						ParseException test1 = new ParseException(index.show(), 4);  
						test1.feedback();
						return null;
					}
					cell.calcuate(worksheet);
					input[i] = cell.value();
					//System.out.println("this is  "+ input[i]);
					i++;

				}
				n = icol;
			}
			t.next();
			System.out.println("this is input length in expression  is  "+ input.length);
			double num = worksheet.scriptFun (func, input);
			System.out.println("this is aaaa "+ num);
			return new Number(num);
		} else if (t.current() != null && t.current().equals("-")){
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
			System.out.println("The cell is "+cell);
			return new CellEle(cell, worksheet);

		} else if (t.current() != null && t.current().equals("e")) {
			t.next();
			return new E();

		} else if(t.current() != null && t.current().equals("p")) {
			t.next();
			return new Pi();

		}   else if (t.current() instanceof Double){
			double num = (double) t.current();
			t.next();
			System.out.println("test for num "+num);;
			return new Number(num);

		} else {
			System.out.println("test for exception");
			ParseException cell = new ParseException(t.current(), 0);
			cell.feedback();
		}
		return null;

	}


}
