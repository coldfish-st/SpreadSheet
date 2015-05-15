package spreedsheet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import expression.Expression;

public class Spreadsheet implements Runnable, ActionListener, SelectionObserver, DocumentListener {

	private static final Dimension PREFEREDDIM = new Dimension(500, 400);
	/**
	 * Spreadsheet - a simple spreadsheet program. Eric McCreath 2015
	 */

	private static final String EXITCOMMAND = "exitcommand";
	private static final String CLEARCOMMAND = "clearcommand";
	private static final String SAVECOMMAND = "savecommand";
	private static final String OPENCOMMAND = "opencommand";
	private static final String EDITFUNCTIONCOMMAND = "editfunctioncommand";

	public HashMap<CellIndex, String> expressions = new HashMap<CellIndex, String>();

	JFrame jframe;
	WorksheetView worksheetview;
	FunctionEditor functioneditor;
	WorkSheet worksheet;
	WorkSheet sheet1, sheet2, sheet3;
	JButton calculateButton;
	JTextField cellEditTextField;
	JLabel selectedCellLabel;
	JFileChooser filechooser = new JFileChooser();
	JComboBox<String> multSheet;

	public Spreadsheet() {
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
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		bar.add(menu);
		makeMenuItem(menu, "New", CLEARCOMMAND);
		makeMenuItem(menu, "Open", OPENCOMMAND);
		makeMenuItem(menu, "Save", SAVECOMMAND);
		makeMenuItem(menu, "Exit", EXITCOMMAND);
		menu = new JMenu("Edit");
		bar.add(menu);
		makeMenuItem(menu, "EditFunction", EDITFUNCTIONCOMMAND);

		jframe.setJMenuBar(bar);


		// set up the tool area
		JPanel toolarea = new JPanel();
		multSheet = new JComboBox<String>();
		multSheet.setBackground(Color.white);
		multSheet.addItem("1");
		multSheet.addItem("2");
		multSheet.addItem("3");

		multSheet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (((String) multSheet.getSelectedItem()).equals("1")) {
					worksheet = sheet1;
					worksheetview = new WorksheetView(worksheet);
					//jframe.getContentPane().add(new JScrollPane(worksheetview), BorderLayout.CENTER);
					//jframe.getContentPane().add(toolarea, BorderLayout.PAGE_START);
				} else if (((String) multSheet.getSelectedItem()).equals("2")) {
					worksheet = sheet2;
					worksheetview = new WorksheetView(worksheet);
					//jframe.getContentPane().add(new JScrollPane(worksheetview), BorderLayout.CENTER);
					//jframe.getContentPane().add(toolarea, BorderLayout.PAGE_START);
				}else if (((String) multSheet.getSelectedItem()).equals("3")) {
					worksheet = sheet3;
					worksheetview = new WorksheetView(worksheet);
					//sjframe.getContentPane().add(new JScrollPane(worksheetview), BorderLayout.CENTER);
					//jframe.getContentPane().add(toolarea, BorderLayout.PAGE_START);
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

	private void makeMenuItem(JMenu menu, String name, String command) {
		JMenuItem menuitem = new JMenuItem(name);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		menuitem.setActionCommand(command);
	}


	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(EXITCOMMAND)) {
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

		} else if (ae.getActionCommand().equals("CALCULATE")) {
			CellIndex index = worksheetview.getSelectedIndex();
			String func = cellEditTextField.getText();
			double result = 0;

			if (func.length() > 1 && func.charAt(0) == '=' ) { //除法这些东西算不算也要加一下情况 0/0

				System.out.println(functioneditor.textarea.getText());

				try {
					result = Expression.tokenizeParseShowEvaluate(func, worksheet);
				} catch (Exception e) {

					e.printStackTrace();
				}
				worksheet.lookup(index).setText(Double.toString(result));
				expressions.put(index, func);

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
				//assign();
				worksheetview.repaint();
			} else {
				/*
				for(CellIndex i: expressions.keySet()) {
					String exp = expressions.get(i);
					result = 0;
					try {
						result = Expression.tokenizeParseShowEvaluate(exp, worksheet);
					} catch (Exception e) {

						e.printStackTrace();
					}
					worksheet.lookup(i).setText(Double.toString(result));
				}
				*/
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
				//assign();
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

		if (!expressions.containsKey(index)) {
			cellEditTextField.setText(worksheet.lookup(index).getText());
		} else {
			cellEditTextField.setText(expressions.get(index));

		}

		//cellEditTextField.setText(worksheet.lookup(index).show());
		assign();
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

		if (!expressions.containsKey(index)) {
			current.setText(cellEditTextField.getText());
			current.calcuate(worksheet);
			worksheetview.repaint();
		} else {
			String func = expressions.get(index);
			current.setText(Double.toString(Expression.tokenizeParseShowEvaluate(func, worksheet)));
			worksheetview.repaint();
			if (!cellEditTextField.getText().equals(func)) {
				expressions.remove(index);
			}
		}	
		//current.setText(cellEditTextField.getText());

		System.out.println(current.getText());
		System.out.println("test");
		worksheetview.repaint();
		//current.setText(cellEditTextField.getText());

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
