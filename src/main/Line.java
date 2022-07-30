package main;

import java.util.ArrayList;
import java.util.List;

public class Line extends Element
{
	private int sens;
	private List<Line> lines = new ArrayList<>();
	
	public Line(int sens, int gridX, int gridY)
	{
		super("LINE", sens == 0 ?"resources/lineH.png":"resources/lineV.png", gridX, gridY);
		this.sens = sens;
	}
	
	public Line(int sens, int gridX, int gridY, boolean value)
	{
		super("LINE", sens == 0 ? (value ? "resources/lineH_on.png" : "resources/lineH.png") : 
								  (value ? "resources/lineV_on.png" : "resources/lineV.png"), gridX, gridY, value);
		this.sens = sens;
	}

	public boolean checkInput(Grid grid)
	{
		Element e1;
		
		if(sens == 0)
		{
			e1 = grid.getElementByCoord(getGridX()-grid.getSqrSize(), getGridY());
			
			if(e1 != null)
			{
					setVal(e1.getValue());
					return true;
			}
		}
		else
		{
			e1 = grid.getElementByCoord(getGridX(), getGridY()-grid.getSqrSize());

			if(e1 != null)
			{
					setVal(e1.getValue());
					return true;	
			}
		}
		
		return false;
	}
	
	public int getSens() 
	{
		return sens;
	}
	
	public void setSens(int sens)
	{
		this.sens = sens == 0 ? 0 : 1;
		this.setImg(sens == 0 ? (getValue() ? "resources/lineH_on.png" : "resources/lineH.png") : 
								(getValue() ? "resources/lineV_on.png" : "resources/lineV.png"));
	}

	public void setVal(boolean val)
	{
		this.setValue(val);
		this.setImg(sens == 0 ? (getValue() ? "resources/lineH_on.png" : "resources/lineH.png") : 
			(getValue() ? "resources/lineV_on.png" : "resources/lineV.png"));
		for(int i=0;i<lines.size();i++)
		{
			lines.get(i).setValue(val);
			lines.get(i).setImg(sens == 0 ? (lines.get(i).getValue() ? "resources/lineH_on.png" : "resources/lineH.png") : 
								 (lines.get(i).getValue() ? "resources/lineV_on.png" : "resources/lineV.png"));
		}
	}
	
	public void remove(int index)
	{
		if(index > 0)
			lines.remove(index-1);
	}
	
	public void removeAll()
	{
		lines.removeAll(lines);
	}
	
	public void add(Line line)
	{
		line.setVal(this.getValue());
		lines.add(line);
	}
	
	public Line get(int index)
	{
		if(index == 0)
			return this;
		return lines.get(index-1);
	}
	
	public int size()
	{
		return lines.size()+1;
	}
	
	/*
	public void remove(int index)
	{
		lines.remove(index);
	}
	
	public void removeAll()
	{
		lines.removeAll(lines);
		lines.add(this);
	}
	
	public void add(Line line)
	{
		line.setVal(this.getValue());
		lines.add(line);
	}
	
	public Line get(int index)
	{
		return lines.get(index);
	}
	
	public int size()
	{
		return lines.size();
	}
	*/

	
}
