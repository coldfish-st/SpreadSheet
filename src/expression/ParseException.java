package expression;

import javax.swing.JOptionPane;

import spreedsheet.Spreadsheet;

/**
 * 
 * @author Eric McCreath - GPLv2
 */


public class ParseException extends Exception {
	
	/**
	 * 
	 */
        private static final long serialVersionUID = 1L;
	static Object e;
	static int flag;
	
	public ParseException(Object current, int flag) {
	        this.e = current;
	        this.flag = flag;
        }
	
	@SuppressWarnings("deprecation")
        public  void feedback() {
		if (flag == 0) {
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "flag0 The input "+e+" has synax error");
			Thread.currentThread().stop();
		}
		if (flag == 1) { // for the () sign
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "flag1 The input "+e+" has synax error, it should be bracket like '(' or ')' here");
			Thread.currentThread().stop();
		}
		if (flag == 2) { // for the cell index
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "flag2 The input "+e+" has synax error, it should be cell index like 'A1' here");
			Thread.currentThread().stop();
		}
		if (flag == 3) { // for the colon
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "flag3 The input "+e+" has synax error, it should be colon ':'' here");
			Thread.currentThread().stop();
		}
		if (flag == 4) { // for the cell index value
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "flag4 The input cell "+e+" has no value");
			Thread.currentThread().stop();
		}
		if (flag == 5) { // for the multiple equal sign
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "flag5 The input should have only one equal sign for calculation");
			Thread.currentThread().stop();
		}
		
		//return e;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
