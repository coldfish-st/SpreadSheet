package expression;

import javax.swing.JOptionPane;

import spreedsheet.Spreadsheet;

/**
 * 
 * @author Eric McCreath - GPLv2
 */


public class ParseException extends Exception {
	
	static Object e;
	static int flag;
	
	public ParseException(Object current, int flag) {
	        this.e = current;
	        this.flag = flag;
        }
	
	public Object feedback() {
		if (flag == 0) {
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "flag0 The input "+e+" has synax error");
			return e;
		}
		return e;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
