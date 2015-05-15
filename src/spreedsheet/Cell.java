package spreedsheet;
import expression.Expression;

/**
 * Cell - an object of this class holds the data of a single cell. 
 * 
 * @author Eric McCreath
 */

public class Cell {

	private String text; // this is the text the person typed into the cell
	private Double calculatedValue; // this is the current calculated value for
	private String temp;								// the cell

	public Cell(String text) {
		this.text = text;
		calculatedValue = null;
	}

	
	public Cell() {
		text = "";
		calculatedValue = null;
	}
	
	public Double value() {
		return calculatedValue;
	}

	public void calcuate(WorkSheet worksheet) {
		try {
						
			//calculatedValue = Expression.tokenizeParseShowEvaluate(text, worksheet);
			
			calculatedValue = Double.parseDouble(text);
		} catch (NumberFormatException nfe) {
			calculatedValue = null;
		}
	}
	
	

	public String show() { // this is what is viewed in each Cell
		return calculatedValue == null ? text : calculatedValue.toString();
	}
	public void forString() {
		calculatedValue = null;
	}
	
	public void forDollor() {
		if (text.charAt(0) != '$' ) {
			text = "$" +text;
		}
	}
	
	public void romoveDollor() {
		if (text.charAt(0) == '$' ) {
			text= text.substring(1);
		}
	}
	public void forBool() { 
		if ( text != "TRUE" &&  text !="FALSE") {
			
			temp = text;
			if (text == "0") {
				text = "FALSE";
			}else {
				text = "TRUE";
			}
		} 
		
	}
	public void removeBool() {
		if (temp != null) {
			text = temp;
			temp = null;
		}
	}
	
	@Override
	public String toString() {
		return text + "," + calculatedValue;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public boolean isEmpty() {
		return text.equals("");
	}
}
