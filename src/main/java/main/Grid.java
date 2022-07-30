package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Grid extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private int size = 50, x = 0, y = 0;
	private int[] initial = new int[2], initTemp = new int[2], lastTemp = new int[2];
	private String selectedElt = "or";
	private List<Element> elt = new ArrayList<>();
	private List<Line> lines = new ArrayList<>();
	private Line tempLine;
	private int mouseButton;
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	// Lorsque l'element change de valeur, on cherche si il y a des lignes autour. Pour chaque ligne qu'il y a
	// autour on fait un "checkInput".
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Grid()
	{
		super();
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		tempLine = new Line(0, 0, 0);
		setup();
	}
	
	public void setup()
	{
		
		this.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e)
			{
				int[] coord = getCoordCase(e.getX(), e.getY());
				Element el = getElementByCoord(coord[0], coord[1]);
				String name;
				mouseButton = e.getButton();
				
				if(el != null)
				{
					name = el.getName();
					if(name.equals("INPUT") || name.equals("OUTPUT"))
					{
						el.inverseValue();
						//refreshLinesPos(el);
					}
					repaint();
				}
				
				if(e.getClickCount() == 2)
				{
					int s = 3;
					if(selectedElt.toUpperCase().equals("INPUT") || selectedElt.toUpperCase().equals("OUTPUT"))
						s = 1;
					
					if(isEmpty(coord[0], coord[1], s))
					{
						elt.add(new Element(selectedElt.toUpperCase(), selectedElt+".png", 
								coord[0], coord[1], s));
					}
					repaint();
				}
				
				if(e.getButton() == 3 && el == null)
				{
					tempLine = new Line(0, coord[0], coord[1], false);
					lines.add(tempLine);
					repaint();
				}
				
				initial[0] = x;
				initial[1] = y;
				initTemp[0] = e.getX();
				initTemp[1] = e.getY();
				lastTemp[0] = e.getX();
				lastTemp[1] = e.getY();
				
			}
			
			public void mouseReleased(MouseEvent e)
			{
				//lines.remove(tempLine);
				refreshLines(lines);
				refreshAll();
				refreshLines(lines);
				refreshAll();

				
				
				repaint();
			}
			
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			
			public void mouseDragged(MouseEvent e)
			{	
				
				if(mouseButton == 1)
				{
					x = (initial[0]+(initTemp[0] - lastTemp[0]));
					y = (initial[1]+(initTemp[1] - lastTemp[1]));
					lastTemp[0] = e.getX();
					lastTemp[1] = e.getY();
				}
				else if(mouseButton == 3)
				{
					lineFunction(e);
				}
				
				repaint();
			}
			
		});
		
		this.addMouseWheelListener(new MouseAdapter() {
			
			public void mouseWheelMoved(MouseWheelEvent e) {
                	int oldSize = size, coeff = 3;
                    System.out.println("Scale : "+size);
                    
                    if (e.getWheelRotation() > 0) 
                    {
                    	if(size > 10)
                    		size -= coeff;
                    } 
                    else 
                    {
                    	if(size < 100)
                    		size += coeff;
                    }
                    
                    refreshCoord(elt, oldSize);
                    refreshCoord(lines, oldSize);
                    
                    repaint();
            }
			
		});
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int gridX = x >= 0 ? -x%size : -x%size - size;
		int gridY = y >= 0 ? -y%size : -y%size - size;
		drawGrid(g, gridX, gridY, this.getWidth()+size, this.getHeight()+size, size);
		g.setColor(Color.BLUE);
		g.setFont(new Font(Font.SANS_SERIF, Font.TRUETYPE_FONT, 15));
		g.drawString(x+" "+gridX+" "+y+" "+gridY+" "+(this.getWidth()+size)+" "+(this.getHeight()+size), 3, g.getFont().getSize());
		displayElt(g, elt);
		displayLines(g, lines);
	}
	
	public void lineFunction(MouseEvent e)
	{
		int[] coord = getCoordCase(e.getX(), e.getY());
		int diffX, diffY;
		
		tempLine.checkInput(this);
		
		if(tempLine.size() >= 2)
		{
				Line[] l = new Line[] {tempLine.get(0), tempLine.get(1)};
				Line lastLine = tempLine.get(tempLine.size()-1);
				if(l[0].getGridX() == l[1].getGridX())
				{
					diffY = l[0].getGridY() < l[1].getGridY() ? 1 : -1;
					
					if(diffY == 1)
					{
						if(lastLine.getGridY() > coord[1])
						{
							tempLine.remove(tempLine.size()-1);
						}
						else if(getElementByCoord(lastLine.getGridX(), coord[1]) == null)
						{
							if(lastLine.getGridY()+size == coord[1])
								tempLine.add(new Line(1, lastLine.getGridX(), coord[1]));
						}
					}
					else
					{
						if(lastLine.getGridY() < coord[1])
						{
							tempLine.remove(tempLine.size()-1);
						}
						else if(getElementByCoord(lastLine.getGridX(), coord[1]) == null)
						{
							if(lastLine.getGridY()-size == coord[1])
								tempLine.add(new Line(1, lastLine.getGridX(), coord[1]));
						}
					}
				}
				else if(l[0].getGridY() == l[1].getGridY())
				{
					diffX = l[0].getGridX() < l[1].getGridX() ? 1 : -1;

					if(diffX == 1)
					{
						if(lastLine.getGridX() > coord[0])
						{
							tempLine.remove(tempLine.size()-1);
						}
						else if(getElementByCoord(coord[0], lastLine.getGridY()) == null)
						{
							if(lastLine.getGridX()+size == coord[0])
								tempLine.add(new Line(0, coord[0], lastLine.getGridY()));
						}
					}
					else
					{
						if(lastLine.getGridX() < coord[0])
						{
							tempLine.remove(tempLine.size()-1);
						}
						else if(getElementByCoord(coord[0], lastLine.getGridY()) == null)
						{
							if(lastLine.getGridX()-size == coord[0])
								tempLine.add(new Line(0, coord[0], lastLine.getGridY()));
						}
					}
				}
			
			
		}
		else if(getElementByCoord(coord[0], coord[1]) == null)
		{	
			if(tempLine.size() == 0)
			{
				tempLine.add(new Line(0, coord[0], coord[1]));
			}
			else if(tempLine.size() == 1)
			{
				if(((coord[0] == tempLine.get(0).getGridX()-size || coord[0] == tempLine.get(0).getGridX()+size)
					&& coord[1] == tempLine.get(0).getGridY()))
					
				{
					if(tempLine.get(0).getSens() == 1)
					{
						tempLine.setSens(0);
					}
					tempLine.add(new Line(0, coord[0], coord[1]));
				}
				else if((coord[1] == tempLine.get(0).getGridY()-size || coord[1] == tempLine.get(0).getGridY()+size)
						&& coord[0] == tempLine.get(0).getGridX())
				{
					if(tempLine.get(0).getSens() == 0)
					{
						tempLine.setSens(1);
					}
					tempLine.add(new Line(1, coord[0], coord[1]));
				}
				
			}
		}
	}
	
	public void displayElt(Graphics g, List<Element> elt)
	{
		for(int i=0;i<elt.size();i++)
		{
			if(isOnScreen(elt.get(i)))
			{
				elt.get(i).draw(g, getRealCoord(elt.get(i)), size*elt.get(i).getSize());
			}
		}
	}
	
	public void displayLines(Graphics g, List<Line> lines)
	{
		if(lines == null)
			return;
		Line l;
		for(int i=0;i<lines.size();i++)
		{
			l = lines.get(i);
			for(int j=0;j<l.size();j++)
			{
				if(isOnScreen(l.get(j)))
				{
					l.get(j).draw(g, getRealCoord(l.get(j)), size);
				}
			}
		}
	}
	
	public int[] getRealCoord(Element elt)
	{
		return new int[] {elt.getGridX() - x, elt.getGridY() - y};
	}
	
	public boolean isOnScreen(Element elt)
	{
		if(elt.getGridX() >= x-size && elt.getGridX() <= x+this.getWidth()+size)
		{
			if(elt.getGridY() >= y-size && elt.getGridY() <= y+this.getHeight()+size)
			{
				return true;
			}
		}
		return false;
	}
	
	public static void drawGrid(Graphics g, int x, int y, int w, int h, int size)
	{
		for(int j=0;j<h;j+=size)
		{
			for(int i=0;i<w;i+=size)
			{
				g.drawRect(x+i, y+j, size, size);
			}
		}
	}
	
	public int[] getCoordCase(int mouseX, int mouseY)
	{
		int newX = x >= 0 ? -x%size : -x%size - size;
		int newY = y >= 0 ? -y%size : -y%size - size;
		
		while(newX+size < mouseX)
			newX += size;
		
		while(newY+size < mouseY)
			newY += size;
		
		return new int[] {x+newX, y+newY};
	}
	
	public void refreshCoord(List<?> elements, int lastSize)
	{
		Line l;
		Element e;
		for(int i=0;i<elements.size();i++)
		{
			e = (Element)elements.get(i);
			if(e.getName().equals("LINE"))
			{
				l = (Line)e;
				for(int j=0;j<l.size();j++)
				{
					l.get(j).setGridX((l.get(j).getGridX()*size)/lastSize);
					l.get(j).setGridY((l.get(j).getGridY()*size)/lastSize);
				}
			}
			else
			{
				e.setGridX((e.getGridX()*size)/lastSize);
				e.setGridY((e.getGridY()*size)/lastSize);
			}
		}
	}
	
	public boolean checkCoord(List<Line> list, int x, int y)
	{
		for(Line l : list)
		{
			if(l.getGridX() == x && l.getGridY() == y)
				return true;
		}
		
		return false;
	}
	
	public void setSelectedElt(String str)
	{
		this.selectedElt = str;
	}
	
	public int getSqrSize()
	{
		return this.size;
	}
	
	public Element getElementByCoord(int x, int y)
	{
		for(Element e : elt)
		{
			
			if(x >= e.getGridX() && x < e.getGridX()+size*e.getSize())
			{
				if(y >= e.getGridY() && y < e.getGridY()+size*e.getSize())
				{
					return e;
				}
			}
			
			//if(e.getGridX() == x && e.getGridY() == y)
				//return e;
		}
		
		for(Line li : lines)
		{
			for(int i=0;i<li.size();i++)
			{
				if(li.get(i).getGridX() == x && li.get(i).getGridY() == y)
					return li.get(i);
			}
		}
		
		for(int i=0;i<tempLine.size();i++)
		{
			if(tempLine.get(i).getGridX() == x && tempLine.get(i).getGridY() == y)
				return tempLine.get(i);
		}
		
		return null;
	}
	
	public boolean isEmpty(int x, int y, int coeffSize)
	{
		for(int j=0;j<coeffSize;j++)
		{
			for(int i=0;i<coeffSize;i++)
			{
				if(getElementByCoord(x+j*size, y+i*size) != null)
					return false;
			}
		}
		return true;
	}
	
	public Line getLineByCoord(int x, int y)
	{
		for(Line li : lines)
		{
			for(int i=0;i<li.size();i++)
			{
				if(li.get(i).getGridX() == x && li.get(i).getGridY() == y)
					return li.get(i);
			}
		}
		
		/*for(int i=0;i<tempLine.size();i++)
		{
			if(tempLine.get(i).getGridX() == x && tempLine.get(i).getGridY() == y)
				return tempLine.get(i);
		}*/
		
		return null;
	}
	
	public void refreshLines(List<Line> list)
	{
		for(Line li : list)
		{
			li.checkInput(this);
		}
	}
	
	public void refreshLinesPos(Element e)
	{
		Line l;
		int x = e.getGridX(), y = e.getGridY(), s = size * e.getSize();
		char pos = e.getDirection();
		switch(pos)
		{
		case 'R':
			l = getLineByCoord(x+s, y);
			
			if(l != null)
			{
				l.checkInput(this);
			}
			break;
		case 'L':
			l = getLineByCoord(x-size, y);
			if(l != null)
			{
				l.checkInput(this);
			}
			break;
		case 'U':
			l = getLineByCoord(x, y-size);
			if(l != null)
			{
				l.checkInput(this);
			}
			break;
		case 'D':
			l = getLineByCoord(x, y+s);
			if(l != null)
			{
				l.checkInput(this);
			}
			break;
		}
	}
	
	public void refresh(Element elt)
	{
		if(elt.getName().equals("INPUT"))
			return;
		boolean[] temp = new boolean[elt.getSize()];
		boolean[] tab = {};
		int count = 0;
		if(elt.getDirection() == 'R')
		{
			for(int i=0;i<elt.getSize();i++)
			{
				if(getLineByCoord(elt.getGridX()-size, elt.getGridY()+size*i) != null)
				{
					count++;
					temp[i] = (getLineByCoord(elt.getGridX()-size, elt.getGridY()+size*i)).getValue();
				}
			}
			
			tab = new boolean[count];
			count = 0;
			
			for(int i=0;i<elt.getSize();i++)
			{
				if(getLineByCoord(elt.getGridX()-size, elt.getGridY()+size*i) != null)
				{
					tab[count] = (getLineByCoord(elt.getGridX()-size, elt.getGridY()+size*i)).getValue();
					count++;
				}
			}
		}
		
		if(elt.isRuleOkay(tab))
		{
			elt.setValue(true);
			//refreshLinesPos(elt);
		}
		else
		{
			elt.setValue(false);
			//refreshLinesPos(elt);
		}
		
		refreshLinesPos(elt);
		
	}
	
	public void refreshAll()
	{
		for(Element e : elt)
		{
			refresh(e);
		}
	}
	
}
