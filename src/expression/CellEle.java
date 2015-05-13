package expression;


import spreedsheet.Cell;
import spreedsheet.CellIndex;
import spreedsheet.WorkSheet;

public class CellEle extends Expression {
	
	String index;
	WorkSheet worksheet;
	
	public CellEle (String index, WorkSheet worksheet) {
		super();
		this.index = index;
		this.worksheet = worksheet;
	}
	@Override
	public double evaluate() {
		//System.out.println(index);
		CellIndex cellin = new CellIndex(index);
		
		Cell cell = worksheet.tabledata.get(cellin);
		cell.calcuate(worksheet); // used to get the value
		
		Double value = cell.value(); // need to calculate?
		
		if (value == null) {
			value = (double) 0;
		}
		return value;
	}

	@Override
	public String show() {
		
		return "cell";
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
