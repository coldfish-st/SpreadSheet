package spreedsheet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.Test;

/**
 * 
 * SpreadsheetTest - This is a simple integration test.  
 * We basically set some text within cells of the spread sheet and check they evaluate correctly.
 * 
 * @author Eric McCreath
 * 
 */

public class SpreadsheetTest  {
	 
	protected static final String sumandmaxfunctions = 
			"SUM(values) { "
			+ "sum = 0.0;"
			+ " i = 0; "
			+ "while (i<values.length) {"
			+ "	 sum = sum +values[i];"
			+ " 	i++; "
			+ "}"
			+ " return sum;"
			+ "}\n"+
			"MAX(values) {"
			+ "	max = values[0];  "
			+ "	i = 1; "
			+ "	while (i < values.length) {  "
			+ "		if (values[i] > max) {  "
			+ "		max = values[i]; } i++; "
			+ "	}  "
			+ "	return max;"
			+ "}\n"+
			"MIN(values) {"
			+ "min = values[0];"
			+ "i = 1;"
			+ "while (i < values.length) {"
			+ "	if (values[i] < min) { "
			+ "		min = values[i];"
			+ "	}"
			+ "	 i++;"
			+ "}"
			+ "return min;"
			+ "}\n"+
			"AVE(values) { "
			+ "sum = 0.0; "
			+ "i = 0; "
			+ "while (i<values.length) { "
			+ "	sum = sum +values[i]; "
			+ "	i++; } "
			+ "ave = sum / values.length; "
			+ "return ave;"
			+ "}";
	Spreadsheet gui;
	
	@Test
	public void testSimple() {
		gui = new Spreadsheet();
		try {

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					selectAndSet(1, 3, "Some Text");
					selectAndSet(4, 1, "5.12");
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C2")).show(),
					"Some Text");
			assertEquals(gui.worksheet.lookup(new CellIndex("A5")).show(),
					"5.12");
		} catch (InvocationTargetException e) {
			fail();
		} catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void testExpressionCal() {
		gui = new Spreadsheet();
		try {

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					selectAndSet(2, 3, "Some Text");
					selectAndSet(3, 3, "23.4");
					selectAndSet(4, 3, "34.1");
					selectAndSet(5, 3, "=2.6+C4*C5");
					selectAndSet(6, 3, "=C5+2.0*2^(4-9%6)");
					selectAndSet(7, 3, "=-(2.6+C4)*(-C5)");
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C3")).show(), "Some Text");
			assertEquals(gui.worksheet.lookup(new CellIndex("C4")).show(), "23.4");
			assertEquals(gui.worksheet.lookup(new CellIndex("C5")).show(), "34.1");
			assertEquals(gui.worksheet.lookup(new CellIndex("C6")).show(), "800.54");
			assertEquals(gui.worksheet.lookup(new CellIndex("C7")).show(), "38.1");
			assertEquals(gui.worksheet.lookup(new CellIndex("C8")).show(), "886.6");
		} catch (InvocationTargetException e) {
			fail();
		} catch (InterruptedException e) {
			fail();
		}
	}

	@Test
	public void testFunctionCal() {
		gui = new Spreadsheet();
		try {

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					gui.functioneditor.textarea.setText(sumandmaxfunctions);
					gui.functioneditor.updateWorksheet();
					selectAndSet(2, 3, "1.1");
					selectAndSet(3, 3, "2.2");
					selectAndSet(4, 3, "3.3");
					selectAndSet(5, 3, "=SUM(C3:C5)");
					selectAndSet(6, 3, "=MAX(C3:C5)");
					selectAndSet(7, 3, "=MIN(C3:C5)");
					selectAndSet(8, 3, "=AVE(C3:C5)");
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C3")).show(), "1.1");
			assertEquals(gui.worksheet.lookup(new CellIndex("C4")).show(), "2.2");
			assertEquals(gui.worksheet.lookup(new CellIndex("C5")).show(), "3.3");
			assertEquals(gui.worksheet.lookup(new CellIndex("C6")).show(), "6.6");
			assertEquals(gui.worksheet.lookup(new CellIndex("C7")).show(), "3.3");
			assertEquals(gui.worksheet.lookup(new CellIndex("C8")).show(), "1.1");
			//assertEquals(gui.worksheet.lookup(new CellIndex("C9")).show(), "2.2");
			
		} catch (InvocationTargetException e) {
			
			fail();
		} catch (InterruptedException e) {
			fail();
		}
	}
	@Test
	public void testRecalculate() {
		gui = new Spreadsheet();
		try {

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					gui.functioneditor.textarea.setText(sumandmaxfunctions);
					gui.functioneditor.updateWorksheet();
					selectAndSet(2, 3, "1.1");
					selectAndSet(3, 3, "2.2");
					selectAndSet(4, 3, "3.3");
					selectAndSet(5, 3, "=SUM(C3:C5)");
					selectAndSet(6, 3, "=MAX(C3:C5)");
					selectAndSet(7, 3, "=MIN(C3:C5)");
					selectAndSet(8, 3, "=AVE(C3:C5)");
					gui.calculateButton.doClick();
					selectAndSet(2, 3, "4.4");
					gui.calculateButton.doClick();
				}
			});
			assertEquals(gui.worksheet.lookup(new CellIndex("C3")).show(), "4.4");
			assertEquals(gui.worksheet.lookup(new CellIndex("C4")).show(), "2.2");
			assertEquals(gui.worksheet.lookup(new CellIndex("C5")).show(), "3.3");
			assertEquals(gui.worksheet.lookup(new CellIndex("C6")).show(), "9.9");
			assertEquals(gui.worksheet.lookup(new CellIndex("C7")).show(), "4.4");
			assertEquals(gui.worksheet.lookup(new CellIndex("C8")).show(), "2.2");
			assertEquals(gui.worksheet.lookup(new CellIndex("C9")).show(), "3.3");
			
		} catch (InvocationTargetException e) {
			
			fail();
		} catch (InterruptedException e) {
			fail();
		}
	}

	private void selectAndSet(int r, int c, String text) {
		gui.worksheetview.addRowSelectionInterval(r, r);
		gui.worksheetview.addColumnSelectionInterval(c, c);
		gui.cellEditTextField.setText(text);
	}
}
