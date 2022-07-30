package main;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Tree extends JTree implements TreeSelectionListener
{
	private static final long serialVersionUID = 1L;
	
	private Grid grid;
	
	public Tree(String firstNode, Element[] elt, Grid grid)
	{
		super(new DefaultMutableTreeNode(firstNode));
		DefaultMutableTreeNode node;
		this.grid = grid;
		String s;
		for(Element e : elt)
		{
			s = e.toString();
			s = s.substring(0,1).toUpperCase()+s.substring(1).toLowerCase();
			node = new DefaultMutableTreeNode(s);
			this.setCellRenderer(getRenderer(e.getImgPath()));
			addNode(node);
		}
		
		this.addTreeSelectionListener(this);
	}
	
	private DefaultTreeCellRenderer getRenderer(String imgPath)
	{
		DefaultTreeCellRenderer cellRenderer = new  DefaultTreeCellRenderer();
		cellRenderer.setLeafIcon(new ImageIcon(Window.getImage(imgPath)));
		
		return cellRenderer;
	}
	
    public void addNode(DefaultMutableTreeNode nodeToAdd) 
    {
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
        DefaultMutableTreeNode child = nodeToAdd;
        model.insertNodeInto(child, root, root.getChildCount());
        this.scrollPathToVisible(new TreePath(child.getPath()));
    }
    
    public void addNode(final String nodeToAdd) 
    {
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getModel().getRoot();
        DefaultMutableTreeNode child = new DefaultMutableTreeNode(nodeToAdd);
        model.insertNodeInto(child, root, root.getChildCount());
        this.scrollPathToVisible(new TreePath(child.getPath()));
    }
    
	@Override
	public void valueChanged(TreeSelectionEvent e) 
	{
		if(this.getLastSelectedPathComponent() != null)
		{
			grid.setSelectedElt(this.getLastSelectedPathComponent().toString().toLowerCase());
		}
	}
    
}
