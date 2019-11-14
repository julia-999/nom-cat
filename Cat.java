import java.awt.Image;
import javax.swing.*;

public class Cat extends JLabel { 

	private final ImageIcon catClosed, catOpen, catCrying;
	private final double SCREENX, SCREENY;
	private int picWidth, picHeight;
	private String closed, open, crying;
	public boolean isOpen, isCrying;

	public Cat (int catType, int loc, double x, double y) {
		
		SCREENX = x;
		SCREENY = y;
		picWidth = (int) (SCREENX/8);
		picHeight = (int) (SCREENX/16*3);
		
		switch (catType) { 
		case 1:
			closed = "greycatclosemouth.PNG";
			open = "greycatopenmouth.PNG";
			crying = "greycatcrying.gif";
			break;
		case 2:
			closed = "whitecatclosemouth.PNG";
			open = "whitecatopenmouth.PNG";
			crying = "whitecatcrying.gif";
			break;
		case 3:
			closed = "browncatclosemouth.PNG";
			open = "browncatopenmouth.PNG";
			crying = "browncatcrying.gif";
			break;
		case 4:
			closed = "orangecatclosemouth.PNG";
			open = "orangecatopenmouth.PNG";
			crying = "orangecatcrying.gif";
			break;
		}
		
		catClosed = new ImageIcon(new ImageIcon(closed).getImage().getScaledInstance(picWidth, picHeight, Image.SCALE_DEFAULT));
		catOpen = new ImageIcon(new ImageIcon(open).getImage().getScaledInstance(picWidth, picHeight, Image.SCALE_DEFAULT));
		catCrying = new ImageIcon(new ImageIcon(crying).getImage().getScaledInstance(picWidth, picHeight, Image.SCALE_DEFAULT));
		closeMouth();
		
		if (loc == 1)
		{
			setBounds((int) (SCREENX/5 - picWidth/2), (int) (SCREENY/5*4 - picHeight/2), (int) picWidth, (int) picHeight);
		}
		if (loc == 2) {
			setBounds((int) (SCREENX/5*4 - picWidth/2), (int) (SCREENY/5*4 - picHeight/2), (int) picWidth, (int) picHeight);
		}
		
		setVisible(true);
		setFocusable(true);
		
	}
	
	public void openMouth() {
		super.setIcon(catOpen);
		isOpen = true;
		repaint();
	}

	public void closeMouth() {
		super.setIcon(catClosed);
		isOpen = false;
		repaint();
	}
	
	public void cry() {
		super.setIcon(catCrying);
		isCrying = true;
		repaint();
	}
}