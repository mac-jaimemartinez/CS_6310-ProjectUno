package Gallhp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Demo {
	//Declare Global Frame Components and Containers
	private JTextField tfDimension;
	private JTextField tfLeftEdge;
	private JTextField tfRightEdge;
	private JTextField tfTopEdge;
	private JTextField tfBottomEdge;
	private JComboBox jcbVariantSelection;
	private JTextArea detailsTA;
	private JButton runBtn;
	private int varSelection = 0;
	
	//get screensize
	private final static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private final static double WIDTH = SCREEN_SIZE.getWidth();
	private final static double HEIGHT = SCREEN_SIZE.getHeight();

	public JPanel createContentPane(){
		// We create a bottom JPanel to place everything on.
		JPanel totalGUI = new JPanel(new BorderLayout());

		// Creation of a Panel to contain the title labels
		JPanel titlePanel = new JPanel();
		totalGUI.add(titlePanel, BorderLayout.NORTH);

		// Creation of a Panel to contain variant label and combobox
		JPanel variantPanel = new JPanel();
		totalGUI.add(variantPanel, BorderLayout.WEST);

		// Creation of a Panel to contain variant run details
		JPanel detailPanel = new JPanel();
		totalGUI.add(detailPanel, BorderLayout.EAST);

		// Creation of a Panel to run button details
		JPanel runPanel = new JPanel();
		totalGUI.add(runPanel, BorderLayout.SOUTH);

		////////////////////Add components to panels//////////////////////////

		//////////////Title Panel/////////////////////////
		JLabel titleLabel = new JLabel("Gallhp");
		titlePanel.add(titleLabel);

		////////////Variant Panel/////////////////////
		JPanel valuesPanel = new JPanel();
		valuesPanel.setLayout(new GridLayout(12,1));
		
		JLabel variantLabel = new JLabel("Variant");
		valuesPanel.add(variantLabel);
		String[] variantArr = {"Primitive 'double' array","Primitive 'float' array", "Wrapped 'Float' array", "Primitive 'double' object"};
		jcbVariantSelection = new JComboBox(variantArr);
		jcbVariantSelection.insertItemAt("--Select Variant--", 0);
		jcbVariantSelection.setSelectedIndex(0);
		jcbVariantSelection.addItemListener(new VariantComboBoxListener());
		valuesPanel.add(jcbVariantSelection);
		
		JLabel dimLabel = new JLabel("Dimension");
		valuesPanel.add(dimLabel);
		tfDimension = new JTextField(10);
		valuesPanel.add(tfDimension);
		
		JLabel topEdgeLabel = new JLabel("Top Edge");
		valuesPanel.add(topEdgeLabel);
		tfTopEdge = new JTextField(10);
		valuesPanel.add(tfTopEdge);
		
		JLabel botEdgeLabel = new JLabel("Bottom Edge");
		valuesPanel.add(botEdgeLabel);
		tfBottomEdge = new JTextField(10);
		valuesPanel.add(tfBottomEdge);
		
		JLabel leftEdgeLabel = new JLabel("Left Edge");
		valuesPanel.add(leftEdgeLabel);
		tfLeftEdge = new JTextField(10);
		valuesPanel.add(tfLeftEdge);
		
		JLabel rightEdgeLabel = new JLabel("Right Edge");
		valuesPanel.add(rightEdgeLabel);
		tfRightEdge = new JTextField(10);
		valuesPanel.add(tfRightEdge);
		
		variantPanel.add(valuesPanel);

		//////////////Detail Panel/////////////////////////
		detailsTA = new JTextArea();
		detailsTA.setEditable(false);

		JScrollPane jsp = new JScrollPane(detailsTA);
		jsp.setPreferredSize(new Dimension((int) (WIDTH * .85), (int) (HEIGHT * .85)));
		detailPanel.add(jsp);

		//////////////Save Panel/////////////////////////
		runBtn = new JButton("Run");
		runBtn.addActionListener(new ButtonListener());
		runPanel.add(runBtn);

		totalGUI.setOpaque(true);
		return totalGUI;
	}

	private static void createAndShowGUI() {

		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Gallhp");

		//Create and set up the content pane.
		Demo demo = new Demo();
		frame.setContentPane(demo.createContentPane());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize((int)WIDTH, (int)HEIGHT);
		frame.setVisible(true);
	}

	private boolean validSelection(JComboBox cb) {
		if(cb.getSelectedIndex() == 0)
			return false;
		else{
			return true;
		}
	}

	private void clearAll() {
		detailsTA.setText("");
		tfDimension.setText("");
		tfLeftEdge.setText("");
		tfRightEdge.setText("");
		tfTopEdge.setText("");
		tfBottomEdge.setText("");

	}

	public boolean validArgs() {
		if(!isInt(tfDimension.getText())){
			printError("The value for dimension must be a positive integer");
			return false;
		}
		else if(!isDecimal(tfTopEdge.getText()) || Double.parseDouble(tfTopEdge.getText()) > 100.0){
			printError("The value for Top Edge must be in the range [0.0,100.0].");
			return false;
		}
		else if(!isDecimal(tfBottomEdge.getText()) || Double.parseDouble(tfBottomEdge.getText()) > 100.0){
			printError("The value for Bottom Edge must be in the range [0.0,100.0].");
			return false;
		}
		else if(!isDecimal(tfLeftEdge.getText()) || Double.parseDouble(tfLeftEdge.getText()) > 100.0){
			printError("The value for Left Edge must be in the range [0.0,100.0].");
			return false;
		}
		else if(!isDecimal(tfRightEdge.getText()) || Double.parseDouble(tfRightEdge.getText()) > 100.0){
			printError("The value for Right Edge must be in the range [0.0,100.0].");
			return false;
		}
		
		
		return true;
	}

	private static boolean isInt(String str)
	{
		return str.matches("\\d+(\\d+)?");  //match an integer number.
	}

	private static boolean isDecimal(String str){
		return str.matches("\\d+(\\.\\d+)?");  //match a number with optional decimal.
	}

	private void printError(String errMsg){
		JOptionPane.showMessageDialog(null, errMsg, "Invalid Input", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	class VariantComboBoxListener implements ItemListener{
		@Override
		public void itemStateChanged(ItemEvent arg0) {
			JComboBox cb = (JComboBox)arg0.getSource();						
			clearAll();		
			varSelection = cb.getSelectedIndex();
		}
	}

	class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(validSelection(jcbVariantSelection)){

				// Process the command line options
				if(validArgs()){
					int dimension = Integer.parseInt(tfDimension.getText());

					//pick variant to run
					if(varSelection == 1){
						// Run the simulation and print results
						double topEdgeTemp = Double.parseDouble(tfTopEdge.getText());
						double bottomEdgeTemp = Double.parseDouble(tfBottomEdge.getText());
						double leftEdgeTemp = Double.parseDouble(tfLeftEdge.getText());
						double rightEdgeTemp = Double.parseDouble(tfRightEdge.getText());

						// Start the collection of meta data
						MetaData metaData = new MetaData();
						metaData.startTiming();
						//Run simulation
						ArrayDoublePlate simulation = new ArrayDoublePlate();

						//form output stings
						String simTitle = "Run Simulation: Primitive double array plate.\n" +
										  "_____________________________________\n";
						String simOutput = simulation.runSimulation(dimension, topEdgeTemp, bottomEdgeTemp,
								leftEdgeTemp, rightEdgeTemp, metaData);

						String metaTitle = "_____________________________________\n";
						// Print the meta data about the simulation
						String metaOutput = metaData.print() + "\n";

						detailsTA.setText(simTitle + simOutput + metaTitle + metaOutput);
					}
					else if(varSelection == 2){ // Run the simulation and print results
						float topEdgeTemp = Float.parseFloat(tfTopEdge.getText());
						float bottomEdgeTemp = Float.parseFloat(tfBottomEdge.getText());
						float leftEdgeTemp = Float.parseFloat(tfLeftEdge.getText());
						float rightEdgeTemp = Float.parseFloat(tfRightEdge.getText());

						// Start the collection of meta data
						MetaData metaData = new MetaData();
						metaData.startTiming();
						//Run simulation
						ArrayFloatPrimitivePlate simulation = new ArrayFloatPrimitivePlate();

						//form output stings
						String simTitle = "Run Simulation: Primitive float array plate.\n" +
										  "_____________________________________\n";
						String simOutput = simulation.runSimulation(dimension, topEdgeTemp, bottomEdgeTemp,
								leftEdgeTemp, rightEdgeTemp, metaData);

						String metaTitle = "_____________________________________\n";
						// Print the meta data about the simulation
						String metaOutput = metaData.print() + "\n";

						detailsTA.setText(simTitle + simOutput + metaTitle + metaOutput);
					}
					else if(varSelection == 3){ // Run the simulation and print results
						float topEdgeTemp = Float.parseFloat(tfTopEdge.getText());
						float bottomEdgeTemp = Float.parseFloat(tfBottomEdge.getText());
						float leftEdgeTemp = Float.parseFloat(tfLeftEdge.getText());
						float rightEdgeTemp = Float.parseFloat(tfRightEdge.getText());

						// Start the collection of meta data
						MetaData metaData = new MetaData();
						metaData.startTiming();
						//Run simulation
						ArrayFloatObjectPlate simulation = new ArrayFloatObjectPlate();

						//form output stings
						String simTitle = "Run Simulation: Wrapped float array plate.\n" +
								   		  "_____________________________________\n";
						String simOutput = simulation.runSimulation(dimension, topEdgeTemp, bottomEdgeTemp,
								leftEdgeTemp, rightEdgeTemp, metaData);

						String metaTitle = "_____________________________________\n";					
						// Print the meta data about the simulation
						String metaOutput = metaData.print() + "\n";

						detailsTA.setText(simTitle + simOutput + metaTitle + metaOutput);
					}
					else if(varSelection == 4){ // Run the simulation and print results
						double topEdgeTemp = Double.parseDouble(tfTopEdge.getText());
						double bottomEdgeTemp = Double.parseDouble(tfBottomEdge.getText());
						double leftEdgeTemp = Double.parseDouble(tfLeftEdge.getText());
						double rightEdgeTemp = Double.parseDouble(tfRightEdge.getText());

						// Start the collection of meta data
						MetaData metaData = new MetaData();
						metaData.startTiming();
						//Run simulation
						LatticePlate simulation = new LatticePlate();

						//form output stings
						String simTitle = "Run Simulation: Primitive double Object plate.\n" +
										   "_____________________________________\n";	
						String simOutput = simulation.runSimulation(dimension, topEdgeTemp, bottomEdgeTemp,
								leftEdgeTemp, rightEdgeTemp, metaData);

						String metaTitle = "_____________________________________\n";	
						// Print the meta data about the simulation
						String metaOutput = metaData.print() + "\n";

						detailsTA.setText(simTitle + simOutput + metaTitle + metaOutput);
					}
				}
			}
			else{
				JOptionPane.showMessageDialog(null, "Please Select A Variant", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
			}				
		}
	}

}
