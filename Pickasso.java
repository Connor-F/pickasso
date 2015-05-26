import javax.swing.*;
import java.awt.*;

/**
 * simple colour picker tool 
 * that displays the hexadecimal and RGB representation of the colour the mouse if hovering over */
public class Pickasso
{
	private JFrame frame;
	private JPanel mainPanel;
	private JLabel colourLabel;
	private Robot robot; // this contains most of the functionality
	private Point mousePos;
	/** the colour where the mouse currently is */
	private Color currentCol;

	public Pickasso()
	{
		try
		{
			robot = new Robot();
		}
		catch(AWTException awtexcp)
		{
			System.err.println("Fatal error: " + awtexcp.getMessage() + "\nExiting.");
			System.exit(-1);
		}

		initialiseInterface();
		startDetection();
	}

	/**
	 * sets up the interface and its components */
	private void initialiseInterface()
	{
		frame = new JFrame("Pickasso");
		frame.setSize(160, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		frame.add(mainPanel);

		colourLabel = new JLabel();
		mainPanel.add(colourLabel);
		colourLabel.setHorizontalAlignment(JLabel.CENTER);
		colourLabel.setFont(new Font("monospaced", Font.PLAIN, 14));
		frame.setVisible(true);
	}

	/**
	 * constantly refreshes the data in each component in the panel */
	private void startDetection()
	{
		while(true)
			setData();
	}

	/**
	 * sets the data for each component */
	private void setData()
	{
		mousePos = MouseInfo.getPointerInfo().getLocation();
		currentCol = getCurrentColour();

		mainPanel.setBackground(currentCol);

		Color contrast = getContrasting();
		colourLabel.setForeground(contrast); // makes sure we can read the text against the panels background
		colourLabel.setText(getColourString());
	}

	/** 
	 * used to make sure the text is readable against the main panels background
	 * @param col the colour where the mouse is
	 * @return white/black colour depending on the background of the main panel */
	private Color getContrasting()
	{
		double temp = 1 - (0.3 * currentCol.getRed() + 0.6 * currentCol.getGreen() + 0.1 * currentCol.getBlue()) / 255;
		if(temp < 0.5)
			return new Color(0, 0, 0); // bright colour = dark font
		return new Color(255, 255, 255); //dark colour = light font
	}

	/**
	 * allows the interface to show a textual representation of the current colour the mouse is on
	 * @return a string containing the hexadecimal and rgb representation of the current colour.
	 *  The string is formatted using html to allow for multiple lines in one component (e.g. a JLabel with multiple line) */
	private String getColourString()
	{
		StringBuilder finalText = new StringBuilder();

		int red = currentCol.getRed();
		int green = currentCol.getGreen();
		int blue = currentCol.getBlue();

	    String redHex = Integer.toHexString(red);
		String greenHex = Integer.toHexString(green);
		String blueHex = Integer.toHexString(blue);

		// the following makes sure each hex part of the colour is 2 chars long
		// without this the parts may be missing a leading "0" after being converted to the hex string
		if(redHex.length() != 2)
			redHex = "0" + redHex;
		if(greenHex.length() != 2)
			greenHex = "0" + greenHex;
		if(blueHex.length() != 2)
			blueHex = "0" + blueHex;
		finalText.append("<html>#" + redHex + greenHex + blueHex + "<br>R: " + red + "<br>G: " + green + "<br>B: " + blue + "</html>");

		return finalText.toString();
	}

	/**
	 * used to get the colour where the mouse currently is
	 * @return the color where the mouse is at */
	private Color getCurrentColour()
	{
		return robot.getPixelColor(getCurrentX(), getCurrentY());
	}

	/**
	 * @return current mouse X position */
	private int getCurrentX()
	{
		return (int)mousePos.getX();
	}

	/**
	 * @return current mouse Y position */
	private int getCurrentY()
	{
		return (int)mousePos.getY();
	}
}
