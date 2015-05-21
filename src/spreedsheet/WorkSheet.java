package spreedsheet;
import java.io.File;
import java.util.HashMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import expression.Arith;
import expression.ParseException;

/**
 * WorkSheet - this stores the information for the worksheet. This is made up of
 * all the cells. all the cells of the worksheet.
 * 
 * @author Eric McCreath 
 * 
 * Add the function compiling method for the user-defined functions.
 * 
 * @author Zhenge Jia
 * 
 */

public class WorkSheet {

	private static final String SPREAD_SHEET = "SpreadSheet";
	private static final String INDEXED_CELL = "IndexedCell";
	private static final String CELL = "Cell";
	private static final String CELL_INDEX = "CellIndex";
	private static final String FUNCTIONTEXT = "FunctionText";
	private static final String FUNCTIONS = "Functions";

	public HashMap<CellIndex, Cell> tabledata;
	// For simplicity the table data is just stored as a hashmap from a cell's
	// index to the
	// cells data. Cells that are not yet part of this mapping are assumed
	// empty.
	// Once a cell is constructed the object is not replaced in this mapping,
	// rather
	// the data of the cell can be modified.

	private String functions;

	public WorkSheet() {
		tabledata = new HashMap<CellIndex, Cell>();
		functions = "";
	}

	public Cell lookup(CellIndex index) {
		Cell cell = tabledata.get(index);
		if (cell == null) { // if the cell is not there then create it and add
			// it to the mapping
			cell = new Cell();
			tabledata.put(index, cell);
		}
		return cell;
	}

	// calculate all the values for each cell that makes up the worksheet
	public void calculate() {
		for (CellIndex i : tabledata.keySet()) {
			tabledata.get(i).calcuate(this);
		}
	}

	public void save(File file) {
		StoreFacade sf = new StoreFacade(file, SPREAD_SHEET);
		for (CellIndex i : tabledata.keySet()) {
			if (!tabledata.get(i).isEmpty()) {
				sf.start(INDEXED_CELL);
				sf.addText(CELL_INDEX, i.show());
				sf.addText(CELL, tabledata.get(i).getText());
			}
		}
		sf.start(FUNCTIONTEXT);
		sf.addText(FUNCTIONS, functions);
		sf.close();
	}

	public static WorkSheet load(File file) {
		LoadFacade lf = LoadFacade.load(file);
		WorkSheet worksheet = new WorkSheet();
		String name;
		while ((name = lf.nextElement()) != null) {
			if (name.equals(INDEXED_CELL)) {
				String index = lf.getText(CELL_INDEX);
				String text = lf.getText(CELL);
				worksheet.put(index, text);
			} else if (name.equals(FUNCTIONTEXT)) {
				worksheet.functions = lf.getText(FUNCTIONS);
			}
		}
		return worksheet;
	}

	public String getFuctions() {
		return functions;
	}

	public void setFunctions(String fun) {
		functions = fun;
	}

	private void put(String index, String text) {
		tabledata.put(new CellIndex(index), new Cell(text));
	}
	
	// The method get the function in String format and compile it in JavaScript grammar.
	@SuppressWarnings("deprecation")
        public double scriptFun(String func, double[] input) throws Exception {

		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine engine = sem.getEngineByName("javascript");

		String funcAll = functions;
		String[] parts;
		
		parts = funcAll.split("\n");// Split it by the symbol '\n'
		String function = "";
		
		for (int i = 0; i < parts.length; i++) {// Assign the typed function for compiling.
			if ( parts[i].startsWith(func))
				function =parts[i];
		}
		
		if (function.equals("")) { // Detect the situation where the input function could not be defined.
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "Worksheet The function " +func+" cannot be found, please define it first");
			Thread.currentThread().stop();
			return 0;
		}
		
		double result = 0;
		try { // Compile the function using eval() method.
			engine.eval("function "+function);
			Invocable jsInvoke = (Invocable) engine;
			result = (double) jsInvoke.invokeFunction(func, input);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(Spreadsheet.jframe, "Worksheet The function " +func+" cannot be compile, please check input function first");
			Thread.currentThread().stop();
			return 0;
		}
		// Round number
		return Arith.round(result, 9);

	}
}
