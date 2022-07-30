package main;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;

public class Element
{
	private String name;
	private String imgPath;
	private Image img;
	private int gridX, gridY, size = 1;
	private boolean value = false;
	private char direction = 'R';
	
	public Element(String name, String imgPath, int gridX, int gridY)
	{
		this.name = name;
		this.imgPath = imgPath;
		this.img = Window.getImage(imgPath);
		this.gridX = gridX;
		this.gridY = gridY;
	}
	
	public Element(String name, String imgPath, int gridX, int gridY, int size)
	{
		this.name = name;
		this.imgPath = imgPath;
		this.img = Window.getImage(imgPath);
		this.gridX = gridX;
		this.gridY = gridY;
		this.size = size;
	}
	
	public Element(String name, String imgPath, int gridX, int gridY, boolean value)
	{
		this.name = name;
		this.imgPath = imgPath;
		this.img = Window.getImage(imgPath);
		this.value = value;
		this.gridX = gridX;
		this.gridY = gridY;
	}
	
	public void draw(Graphics g, int[] coord, int size)
	{
		g.drawImage(img, coord[0], coord[1], size, size, null);
	}

	@Override
	public String toString()
	{
		return this.name;
	}
	
	public int getGridX() 
	{
		return gridX;
	}

	public void setGridX(int gridX) 
	{
		this.gridX = gridX;
	}

	public int getGridY() 
	{
		return gridY;
	}

	public void setGridY(int gridY) 
	{
		this.gridY = gridY;
	}
	
	public int[] getCoord()
	{
		return new int[] {this.gridX, this.gridY};
	}
	
	public void setCoord(int[] coord)
	{
		this.gridX = coord[0];
		this.gridY = coord[1];
	}

	public String getName() 
	{
		return this.name;
	}

	public String getImgPath() 
	{
		return this.imgPath;
	}

	public void setImg(String imgPath)
	{
		this.imgPath = imgPath;
		this.img = Window.getImage(imgPath);
	}
	
	public void inverseValue()
	{
		setValue(!value);
	}
	
	public boolean getValue()
	{
		return this.value;
	}
	
	public void setValue(boolean val)
	{
		System.out.println(this.getName()+" "+value+" "+val);
		this.value = val;
		if(name.equals("INPUT") || name.equals("OUTPUT"))
		{
			String str = name.toLowerCase();
			if(value)
			{
				System.out.println("on");
				setImg(str+"_on.png");
			}
			else
			{
				System.out.println("off");
				setImg(str+".png");
			}
		}
	}

	public boolean isRuleOkay(boolean[] inputTab)
	{
		if(inputTab == null)
			return false;
		
		int count = 0;
		System.out.println(Arrays.toString(inputTab));
		
		if(getName().equals("OR"))
		{
			for(int i=0;i<inputTab.length;i++)
			{
				if(inputTab[i])
					return true;
			}
		}
		else if(getName().equals("XOR"))
		{
			for(int i=0;i<inputTab.length-1;i++)
			{
				if(inputTab[i] == inputTab[i+1])
					count++;
			}
			if(count != inputTab.length-1)
				return true;
		}
		else if(getName().equals("AND"))
		{

		}
		
		return false;
	}
	
	public char getDirection() 
	{
		return direction;
	}

	public void setDirection(char direction) 
	{
		this.direction = direction;
	}

	public int getSize() 
	{
		return size;
	}

	public void setSize(int size) 
	{
		this.size = size;
	}
	
}
