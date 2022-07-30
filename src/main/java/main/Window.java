package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Window extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private Element[] gates;
	private Element input, output;
	private JPanel treePan = new JPanel();
	private Grid grid = new Grid();
	private JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePan, grid);
	
	public Window(int w, int h)
	{
		super();
		this.setTitle("Logisim");
		this.setMinimumSize(new Dimension(w, h));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		
		gates = setupGates();
		
		input = new Element("INPUT", "input_icon.png", 0, 0);
		output = new Element("OUPUT", "output_icon.png", 0, 0);
		
		treePan.setBackground(Color.WHITE);
		
		treePan.add(new Tree("I/O", new Element[] {input, output}, grid));
		treePan.add(new Tree("Gates", gates, grid));
		
		split.setDividerLocation(this.getWidth()/4);
		
		this.getContentPane().add(split);
		
		this.setVisible(true);
	}
	
	public Element[] setupGates()
	{
		return new Element[] { new Element( "OR", "or_icon.png", 0, 0, 2),
							   new Element("XOR", "xor_icon.png", 0, 0, 2) };
	}
	
	public static Image getImage(final String pathAndFileName)
	{

		final URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
		System.out.println(pathAndFileName);
		System.out.println(url);
		return Toolkit.getDefaultToolkit().getImage(url);
	}

}
