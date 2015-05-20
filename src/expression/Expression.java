package expression;

import javax.swing.JOptionPane;

import spreedsheet.Cell;
import spreedsheet.CellIndex;
import spreedsheet.Spreadsheet;
import spreedsheet.WorkSheet;



/**
 * Exp - a simple expression for calculating integers expressions. 
 * This is class is for the expression parse. 
 * There are four related parse methods which are for the order of the operations.
 * Get the idea from Eric's code in drawgui.addmultlang.
 * @author Zhenge Jia
 * @param <WorkSheet>
 *
 */


public abstract class Expression {

	public abstract double evaluate();
	public abstract String show();


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

	// This method is for the first level of operations contain '+' and '-'.
	public static Expression parse(Tokenizer t, WorkSheet worksheet) throws Exception {

		// The expression must start with  '=' sign. To begin with, we need to get rid of it.
		if (t.current().equals("=")) {
			t.next();
		}

		Expression term = parseTerm(t, worksheet);
		// Do it recursively to pass the value into parseTerm method. 
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
	// Set these two methods for the order of plus and minus in the operations.
	abstract Expression insertsub(Expression term);
	abstract Expression insertadd(Expression term);

	public static Expression parseTerm(Tokenizer t, WorkSheet worksheet) throws Exception {

		Expression ope = parseOpe(t, worksheet);
		// Do it recursively to pass the value into parseOpe method. 
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

	// Set these two methods for the order of multiply and divide in the operations.
	abstract Expression insertmult(Expression ope);
	abstract Expression insertdiv(Expression ope);

	public static Expression parseOpe(Tokenizer t, WorkSheet worksheet) throws Exception {

		Expression val = parseVal(t, worksheet);
		// Do it recursively to pass the value into parseOpe method. 
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
	
	// The parseVal will detect the base for all expressions and report the syntax errors. 
	public static Expression parseVal(Tokenizer t, WorkSheet worksheet) throws Exception {
		
		// This is the case for function call. 
		if ( t.current() instanceof String &&  ( (String) t.current()).length() >= 2   
				&& Character.isUpperCase(((String) t.current()).charAt(0)) 
				&& Character.isUpperCase(((String) t.current()).charAt(1))) {
			
			String func = (String) t.current();

			t.next();
			// exception for '('.
			if (!((String)t.current()).equals("(")) {
				ParseException cell = new ParseException(t.current(), 1);  
				cell.feedback();
				return new Number(0.0);
			}

			t.next();
			// exception for cell index in correct form.
			if ( ((String)t.current()).length() > 2 
					|| !Character.isUpperCase(((String)t.current()).charAt(0)) ) {
				ParseException cell = new ParseException(t.current(), 2);  
				cell.feedback();
				return new Number(0.0);
			}

			Object test = t.current();
			CellIndex index1 = new CellIndex((String) test);


			t.next();
			// exception for the colon in function call.
			if (!((String)t.current()).equals(":")) {
				ParseException cell = new ParseException(t.current(), 3);  
				cell.feedback();
				return new Number(0.0);
			}

			t.next();
			// exception for cell index on the  second part
			if ( !Character.isUpperCase(((String)t.current()).charAt(0)) 
					|| ((String)t.current()).length() > 2) {
				ParseException cell = new ParseException(t.current(), 2);  
				cell.feedback();
				return new Number(0.0);
			}
			
			Expression exp2 = new  CellEle((String) t.current(), worksheet);
			CellIndex index2 = new CellIndex((String) t.current());
			t.next();
			
			// exception for ')'.
			if (!((String)t.current()).equals(")")) {
				ParseException cell = new ParseException(t.current(), 1);  
				cell.feedback();
				return new Number(0.0);
			}
			
			int icol = index1.column();
			int irow = index1.row();
			int jcol = index2.column();
			int jrow = index2.row();
		
			int count = (jcol-icol+1) * (jrow-irow+1);
			double[] input = new double[count];
			int n = icol;
			int i =0;
			// Set the array of cells value for the function
			for (;irow < jrow+1; irow++) {
				for ( ; n < jcol+1; n++) {
					CellIndex index =new CellIndex(n, irow);
					Cell cell =  worksheet.tabledata.get(index);
					if (cell.getText().equals("")) {
						ParseException test1 = new ParseException(index.show(), 4);  
						test1.feedback();
						return new Number(0.0);
					}
					cell.calcuate(worksheet);
					input[i] = cell.value();
					i++;

				}
				n = icol;
			}
			t.next();
			// Get the value.
			double num = worksheet.scriptFun (func, input);
			return new Number(num);
			
		} else if (t.current() != null && t.current().equals("-")){ // For the inverse
			t.next();
			Expression exp2 = parseVal(t, worksheet);
			return new Inverse(exp2);
			
		} else if (t.current() != null && t.current().equals("(")) {
			t.next();
			Expression exp2 = parse(t, worksheet);
			t.next(); // consume the ")"
			return new BracExp(exp2);

		} else if (t.current() instanceof String  && ((String) t.current()).length() >1
				&& Character.isUpperCase(((String) t.current()).charAt(0)) 
				&&	Character.isDigit(((String) t.current()).charAt(1))) {// Read value in the cell
			
			String cell = (String) t.current();
			t.next();
			return new CellEle(cell, worksheet);

		} else if (t.current() != null && t.current().equals("e")) { // Invoke e value.
			t.next();
			return new E();

		} else if(t.current() != null && t.current().equals("p")) { // Invoke PI value.
			t.next();
			return new Pi();

		}   else if (t.current() instanceof Double){ // Get the double input.
			double num = (double) t.current();
			t.next();
			return new Number(num);

		} else if (((String)t.current()).equals("=")) { // Detect the multiple '=' input.
			ParseException cell = new ParseException(t.current(), 5);
			cell.feedback();
			return new Number(0.0);
			
		} else { // Detect the non-logic input. 
			ParseException cell = new ParseException(t.current(), 0);
			cell.feedback();
			return new Number(0.0);
		}


	}


}
