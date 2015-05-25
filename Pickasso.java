import javax.swing.*;
import java.awt.*;

/**
 * simple colour picker tool */
public class Pickasso
{
	private JFrame frame;
	private JPanel mainPanel;
	private JLabel colourLabel;
	private Robot robot;

	public Pickasso()
	{
		frame = new JFrame("Pickasso");
		frame.setSize(160, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		frame.add(mainPanel);

		colourLabel = new JLabel();
		mainPanel.add(colourLabel);
		colourLabel.setHorizontalAlignment(JLabel.CENTER);
		colourLabel.setFont(new Font("monospaced", Font.PLAIN, 14));
		
		try
		{
			robot = new Robot();
		}
		catch(AWTException awtexcp)
		{
			System.err.println("Fatal error: " + awtexcp.getMessage() + "\nExiting.");
			System.exit(-1);
		}

		frame.setVisible(true);
		startDetection();
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
		Color currentCol = getCurrentColour();
		mainPanel.setBackground(currentCol);

		Color contrast = getContrasting(currentCol);
		colourLabel.setForeground(contrast);
		colourLabel.setText(getColourString(currentCol).toUpperCase());
	}

	/** 
	 * used to make sure the text is readable against the main panels background
	 * @param col the colour where the mouse is
	 * @return white/black colour depending on the background of the main panel */
	private Color getContrasting(Color col)
	{
		int balance;
		double temp = 1 - (0.3 * col.getRed() + 0.6 * col.getGreen() + 0.1 * col.getBlue()) / 255;
		if(temp < 0.5)
			balance = 0; // bright colour = dark font
		else
			balance = 255; // dark colour = light font
		return new Color(balance, balance, balance);
	}

	private String getColourString(Color current)
	{
		return "<html>#" + Integer.toHexString(current.getRed()) + Integer.toHexString(current.getGreen()) + Integer.toHexString(current.getBlue())
	    + "<br> R: " + Integer.toString(current.getRed()) + "<br> G: " + Integer.toString(current.getGreen()) + "<br> B: " + Integer.toString(current.getBlue()) + "</html>";
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
		return (int)MouseInfo.getPointerInfo().getLocation().getX();
	}

	/**
	 * @return current mouse Y position */
	private int getCurrentY()
	{
		return (int)MouseInfo.getPointerInfo().getLocation().getY();
	}
}
