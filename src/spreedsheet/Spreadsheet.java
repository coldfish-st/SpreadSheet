package spreedsheet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.util.regex.*;

import expression.Expression;
import expression.ParseException;

public class Spreadsheet implements Runnable, ActionListener, SelectionObserver, DocumentListener {

	private static final Dimension PREFEREDDIM = new Dimension(500, 400);
	/**
	 * Spreadsheet - a simple spreadsheet program. 
	 * @Eric McCreath 2015
	 * 
	 * Modify some components of GUI.
	 * Add several components to implement functions.
	 * Implement the calculation and recalculation function for "calculate" button.
	 * @author Zhenge Jia 2015
	 */

	private static final String EXITCOMMAND = "exitcommand";
	private static final String CLEARCOMMAND = "clearcommand";
	private static final String SAVECOMMAND = "savecommand";
	private static final String OPENCOMMAND = "opencommand";
	private static final String EDITFUNCTIONCOMMAND = "editfunctioncommand";
	private static final String COPYCOMMAND = "copycommand";
	private static final String PASTECOMMAND = "pastecommand";
	private static final String CUTCOMMAND = "cutcommand";
	private static final String STRINGCOMMAND = "stringcommand";
	private static final String PERCENTCOMMAND = "percentcommand";
	private static final String DOLLORCOMMAND = "dollorcommand";
	private static final String DOUBLECOMMAND = "doublecommand";
	private static final String BOOLCOMMAND = "boolcommand";
	private static final String SWINGCOMMAND = "swingcommand";
	private static final String METALCOMMAND = "metalcommand";
	private static final String NIMBUSCOMMAND = "nimbuscommand";
	static final String FILENAME = "format.txt";
	static final String SHEET1 = "SHEET1";
	static final String SHEET2 = "SHEET2";
	static final String SHEET3 = "SHEET3";
	static int flag = 1;
	// Add one hashmap to store the cell contained expressions.
	public HashMap<CellIndex, String> expressions = new HashMap<CellIndex, String>();

	public static JFrame jframe;
	WorksheetView worksheetview;
	FunctionEditor functioneditor;
	WorkSheet worksheet;
	WorkSheet sheet1, sheet2, sheet3;
	JButton calculateButton;
	public static JTextField cellEditTextField;
	JLabel selectedCellLabel;
	JFileChooser filechooser = new JFileChooser();
	JComboBox<String> multSheet;
	Cell copyCell = new Cell();
	CellIndex copyCut;
	JMenuBar bar;

	public Spreadsheet() { // Enable user to change the style of app and store this preference.
		String content = null;
		File file = new File(FILENAME); 
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				char[] chars = new char[(int) file.length()];
				reader.read(chars);
				content = new String(chars);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// If there is no file exists, then it will generate a file which contain "1" representing skin of nimbus.
		} else {
			try {
				FileWriter out_txt = new FileWriter(file);
				out_txt.write("1");
				out_txt.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				FileReader reader = new FileReader(file);
				char[] chars = new char[(int) file.length()];
				reader.read(chars);
				content = new String(chars);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Read preferences flag from stored file.
		if (content.equals("1")) {
			try {
				try {
					UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		} else if (content.equals("2")) {
			try {
				try {
					UIManager.setLookAndFeel( "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		} else if (content.equals("0")){
			try {
				try {
					UIManager.setLookAndFeel( "javax.swing.plaf.metal.MetalLookAndFeel");			
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new Spreadsheet();
	}

	public void run() {

		jframe = new JFrame("Spreadsheet");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		worksheet = new WorkSheet();
		sheet1 = new WorkSheet();
		sheet2 = new WorkSheet();
		sheet3 = new WorkSheet();
		worksheetview = new WorksheetView(worksheet);

		functioneditor = new FunctionEditor(worksheet);
		worksheetview.addSelectionObserver(this);
		// set up the menu bar
		bar = new JMenuBar();
		JMenu menu = new JMenu("File");

		bar.add(menu);

		// set up the accelerator key for some menu items.
		makeMenuItem(menu, "New", CLEARCOMMAND, 'N');
		//menu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N));
		makeMenuItem(menu, "Open", OPENCOMMAND, 'O');
		makeMenuItem(menu, "Save", SAVECOMMAND, 'S');
		makeMenuItem(menu, "Exit", EXITCOMMAND, 'Q');

		// add the copy, cut, paste menus.
		menu = new JMenu("Edit");
		bar.add(menu);
		makeMenuItem(menu, "EditFunction", EDITFUNCTIONCOMMAND, 'E');
		makeMenuItem(menu, "Copy", COPYCOMMAND, 'C');
		makeMenuItem(menu, "Cut", CUTCOMMAND, 'X');
		makeMenuItem(menu, "Paste", PASTECOMMAND, 'V');

		// add the format menus.
		menu = new JMenu("Format");
		bar.add(menu);
		makeMenuItem(menu, "String", STRINGCOMMAND, 0);
		//makeMenuItem(menu, "Percent", PERCENTCOMMAND);
		makeMenuItem(menu, "Dollor", DOLLORCOMMAND, 0);
		makeMenuItem(menu, "Double", DOUBLECOMMAND, 0);
		makeMenuItem(menu, "Bool", BOOLCOMMAND, 0);

		// add menus for skins selection.
		menu = new JMenu("Skins");
		bar.add(menu);
		makeMenuItem(menu, "Swing", SWINGCOMMAND, 0);
		makeMenuItem(menu, "Metal", METALCOMMAND, 0);
		makeMenuItem(menu, "Nimbus", NIMBUSCOMMAND, 0);

		jframe.setJMenuBar(bar);


		// set up the tool area
		JPanel toolarea = new JPanel();
		multSheet = new JComboBox<String>();
		multSheet.setBackground(Color.white);
		multSheet.addItem("1");
		multSheet.addItem("2");
		multSheet.addItem("3");
		File file = new File(SHEET1);
		worksheet.save(file);
		file = new File(SHEET2);
		worksheet.save(file);
		file = new File(SHEET3);
		worksheet.save(file);
		multSheet.addActionListener(new ActionListener() {

			@SuppressWarnings("static-access")
			@Override
			// Use three file for multiple sheets
			public void actionPerformed(ActionEvent e) {

				if (multSheet.getSelectedItem() == "1") {
					
					// Save the file first
					if (flag == 1) {
						File file = new File(SHEET1);
						worksheet.save(file);
					} else if (flag == 2) {
						File file = new File(SHEET2);
						worksheet.save(file);
					} else {
						File file = new File(SHEET3);
						worksheet.save(file);
					}
					
					File file = new File(SHEET1);
					worksheet = WorkSheet.load(file);
					worksheetChange();
					//worksheetview.repaint();
					flag = 1;
				} else if(multSheet.getSelectedItem() == "2") {
					
					// Save the file first
					if (flag == 1) {
						File file = new File(SHEET1);
						worksheet.save(file);
					} else if (flag == 2) {
						File file = new File(SHEET2);
						worksheet.save(file);
					} else {
						File file = new File(SHEET3);
						worksheet.save(file);
					}
					
					File file = new File(SHEET2);
					worksheet = WorkSheet.load(file);
					worksheetChange();
					//worksheetview.repaint();
					flag =2;
				} else {
					
					// Save the file first
					if (flag == 1) {
						File file = new File(SHEET1);
						worksheet.save(file);
					} else if (flag == 2) {
						File file = new File(SHEET2);
						worksheet.save(file);
					} else {
						File file = new File(SHEET3);
						worksheet.save(file);
					}
					
					File file = new File(SHEET3);
					worksheet = WorkSheet.load(file);
					worksheetChange();
					//worksheetview.repaint();
					flag = 3;
				}

			}

		});

		toolarea.add(multSheet);
		calculateButton = new JButton("Calculate");
		calculateButton.addActionListener(this);
		calculateButton.setActionCommand("CALCULATE");
		toolarea.add(calculateButton);
		selectedCellLabel = new JLabel("--");
		selectedCellLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		toolarea.add(selectedCellLabel);
		cellEditTextField = new JTextField(20);
		cellEditTextField.getDocument().addDocumentListener(this);

		toolarea.add(cellEditTextField);

		jframe.getContentPane().add(new JScrollPane(worksheetview), BorderLayout.CENTER);
		jframe.getContentPane().add(toolarea, BorderLayout.PAGE_START);

		jframe.setVisible(true);
		jframe.setPreferredSize(PREFEREDDIM);
		jframe.pack();
	}

	private void makeMenuItem(JMenu menu, String name, String command, int mnemonic) {
		JMenuItem menuitem;
		if (mnemonic == 0) {
			menuitem = new JMenuItem(name);
		} else {
			menuitem = new JMenuItem(name, mnemonic);
			// set up the accelerator key for the menu items.
			if (mnemonic == 'C' || mnemonic == 'V'  || mnemonic == 'X' ) {
				// The first combo key should be "Alt"
				menuitem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, InputEvent.ALT_MASK));
			} else {
				// The first combo key should be "Ctrl"
				menuitem.setAccelerator(KeyStroke.getKeyStroke(mnemonic, InputEvent.CTRL_MASK));
			}

		}
		menu.add(menuitem);
		// enbale the function of each menu and button.
		menuitem.addActionListener(this);
		menuitem.setActionCommand(command);
	}


	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(EXITCOMMAND)) {
			// Clear the temp file for multiple sheets
			worksheet = new WorkSheet();
			File file = new File(SHEET1);
			worksheet.save(file);
			file = new File(SHEET2);
			worksheet.save(file);
			file = new File(SHEET3);
			worksheet.save(file);
			exit();
		} else if (ae.getActionCommand().equals(SAVECOMMAND)) {
			int res = filechooser.showOpenDialog(jframe);
			if (res == JFileChooser.APPROVE_OPTION) {
				worksheet.save(filechooser.getSelectedFile());
			}
		} else if (ae.getActionCommand().equals(OPENCOMMAND)) {
			int res = filechooser.showOpenDialog(jframe);
			if (res == JFileChooser.APPROVE_OPTION) {
				worksheet = WorkSheet.load(filechooser.getSelectedFile());
				worksheetChange();
			}
		} else if (ae.getActionCommand().equals(CLEARCOMMAND)) {
			worksheet = new WorkSheet();
			worksheetChange();
		} else if (ae.getActionCommand().equals(EDITFUNCTIONCOMMAND)) {
			functioneditor.setVisible(true);

		} else if (ae.getActionCommand().equals(SWINGCOMMAND)) { // change the skin to "Swing" style.
			File f_txt =new File(FILENAME);
			try {
				FileWriter out_txt = new FileWriter(f_txt);
				out_txt.write("0");
				out_txt.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Spreadsheet.jframe.setVisible(false);
			try {
				try {
					UIManager.setLookAndFeel( "javax.swing.plaf.metal.MetalLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
			new Spreadsheet();

		}else if (ae.getActionCommand().equals(METALCOMMAND)) { // change the skin to "Metal" style.
			File f_txt =new File(FILENAME);
			try {
				FileWriter out_txt = new FileWriter(f_txt);
				out_txt.write("2");
				out_txt.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Spreadsheet.jframe.setVisible(false);
			try {
				try {
					UIManager.setLookAndFeel( "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
			new Spreadsheet();

		}else if (ae.getActionCommand().equals(NIMBUSCOMMAND)) {  // change the skin to "Nimbus" style.
			File f_txt =new File(FILENAME);
			try {
				FileWriter out_txt = new FileWriter(f_txt);
				out_txt.write("1");
				out_txt.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Spreadsheet.jframe.setVisible(false);
			try {
				try {
					UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
			new Spreadsheet();
		}
		else if (ae.getActionCommand().equals(STRINGCOMMAND)) { // transfer the format of the selected cell to String

			CellIndex index = worksheetview.getSelectedIndex();
			Cell cell = worksheet.lookup(index);

			cell.removeBool();
			cell.romoveDollor();
			cell.forString();
			cell.setText(cell.getText());
			worksheetChange();

		} else if (ae.getActionCommand().equals(DOLLORCOMMAND)) { // transfer the format of the selected cell to Dollar

			CellIndex index = worksheetview.getSelectedIndex();
			Cell cell = worksheet.lookup(index);

			cell.removeBool();
			cell.forString();
			cell.forDollor();
			cell.setText(cell.getText());
			worksheetChange();

		} else if (ae.getActionCommand().equals(DOUBLECOMMAND)) { // transfer the format of the selected cell to Double

			CellIndex index = worksheetview.getSelectedIndex();
			Cell cell = worksheet.lookup(index);

			cell.removeBool();
			cell.romoveDollor();
			cell.calcuate(worksheet);
			cell.setText(cell.getText());
			worksheetChange();

		}  else if (ae.getActionCommand().equals(BOOLCOMMAND)) { // transfer the format of the selected cell to Boolean

			CellIndex index = worksheetview.getSelectedIndex();
			Cell cell = worksheet.lookup(index);

			cell.romoveDollor();
			cell.forBool();
			cell.setText(cell.getText());
			worksheetChange();

		} else if (ae.getActionCommand().equals(COPYCOMMAND)) { // Enable the selected cell to be copied.
			CellIndex index = worksheetview.getSelectedIndex();
			// store the cell in a variable for paste.
			copyCut = index;
			copyCell = worksheet.lookup(index);

		} else if (ae.getActionCommand().equals(CUTCOMMAND)) { // Enable the selected cell to be cut.

			CellIndex index = worksheetview.getSelectedIndex();
			String temp;
			temp = worksheet.lookup(index).getText();
			// Cut means remove from origin place. It should remove the element stored in hashmap. 
			if (temp.charAt(0) == '=') {
				expressions.remove(index);
			} 
			if (expressions.containsKey(index)) {
				expressions.remove(index);
			}
			worksheet.lookup(index).setText("");
			worksheet.lookup(index).forString();
			cellEditTextField.setText("");
			copyCell.setText(temp);


		} else if (ae.getActionCommand().equals(PASTECOMMAND)) { // Enable the selected cell to be pasted.
			// Get the value stored in copyCell and assign it to the selected cell.
			CellIndex index = worksheetview.getSelectedIndex();
			worksheet.lookup(index).setText(copyCell.getText());
			cellEditTextField.setText(copyCell.getText());
		} 
		else if (ae.getActionCommand().equals("CALCULATE")) { // Implement the function of calculation button.

			CellIndex index = worksheetview.getSelectedIndex();
			String func = cellEditTextField.getText();
			double result = 0;

			if (func.length() > 1 && func.charAt(0) == '=' ) {  // do the expression calculation when there starts with '='
				//  Get the result from tokenizeParseShowEvaluate method.
				try {
					result = Expression.tokenizeParseShowEvaluate(func, worksheet);
				} catch (Exception e) {
					e.printStackTrace();
					ParseException cell = new ParseException(func, 0);  
					cell.feedback();

				}
				worksheet.lookup(index).setText(Double.toString(result));
				expressions.put(index, func);
				// Recalculate the expressions stored in the whole table.
				for(CellIndex i: worksheet.tabledata.keySet()) {
					String exp = worksheet.tabledata.get(i).getText();
					result = 0;
					if (exp.length() > 1 && exp.charAt(0) == '=' ) {
						try {
							expressions.put(i, exp);
							result = Expression.tokenizeParseShowEvaluate(exp, worksheet);
						} catch (Exception e) {

							e.printStackTrace();
						}
						worksheet.lookup(i).setText(Double.toString(result));
					}

				}
				// Recalculate the expressions which invoke values from other expression.
				for(CellIndex i: expressions.keySet()) {
					String exp = expressions.get(i);
					result = 0;
					if (true) {
						try {
							result = Expression.tokenizeParseShowEvaluate(exp, worksheet);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}	
					worksheet.lookup(i).setText(Double.toString(result));
				}

				worksheetview.repaint();
			} else { // recalculate the whole table when the selected cell does not contain expression.

				worksheet.tabledata.get(index).calcuate(worksheet);

				for(CellIndex i: worksheet.tabledata.keySet()) {
					String exp = worksheet.tabledata.get(i).getText();
					result = 0;
					if (exp.length() > 1 && exp.charAt(0) == '=') {
						try {
							expressions.put(i, exp);
							result = Expression.tokenizeParseShowEvaluate(exp, worksheet);
						} catch (Exception e) {
							e.printStackTrace();
						}
						worksheet.lookup(i).setText(Double.toString(result));
					}
				}		
				for(CellIndex i: expressions.keySet()) {
					String exp = expressions.get(i);
					result = 0;
					try {
						result = Expression.tokenizeParseShowEvaluate(exp, worksheet);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(Spreadsheet.jframe, "Spread B The function " +exp+" cannot be found, please define it first");
						return;
					}
					worksheet.lookup(i).setText(Double.toString(result));
				}

				worksheetview.repaint();
			}
		}
	}

	private void worksheetChange() {
		worksheetview.setWorksheet(worksheet);
		functioneditor.setWorksheet(worksheet);
		worksheetview.repaint();
	}

	private void exit() {
		System.exit(0);
	}

	@Override
	public void update() {
		CellIndex index = worksheetview.getSelectedIndex();
		selectedCellLabel.setText(index.show());
		// It enables the cellEditTextField print out the expression for the modification.
		if (!expressions.containsKey(index)) {
			cellEditTextField.setText(worksheet.lookup(index).getText());
		} else {
			cellEditTextField.setText(expressions.get(index));

		}

		jframe.repaint();
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		try {
			textChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assign();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		try {
			textChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assign();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		try {
			textChanged();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assign();
	}

	private void textChanged() throws Exception {
		CellIndex index = worksheetview.getSelectedIndex();
		Cell current = worksheet.lookup(index);

		if (!expressions.containsKey(index)) { // It enbales the cell get the stored expression when re-click it.

			current.setText(cellEditTextField.getText());
			worksheetview.repaint();
		} else { 								// It enables users to do the modification of cell stored expression.
			String func = expressions.get(index);
			worksheetview.repaint();
			if (!cellEditTextField.getText().equals(func)) {
				expressions.remove(index);
			}
		}	
		worksheetview.repaint();
	}

	public void assign() {
		if ( ((String) multSheet.getSelectedItem()).equals("1") ) {
			sheet1 = worksheet;
		} else if ( ((String) multSheet.getSelectedItem()).equals("2")) {
			sheet2 = worksheet;
		} else {
			sheet3 = worksheet;
		}
	}










}
