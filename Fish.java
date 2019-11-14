import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Fish extends JLabel {

	public final boolean isBomb, fromLeft;
	private final ImageIcon PIC;
	private Random r;
	private int angle;
	private final double SCREENX, SCREENY;
	private double eCX, eCY, x, y, k, h;

	public Fish (double screenx, double screeny, int startCatX, int endCatX, int endCatY, boolean bomb, boolean fromL) {

		SCREENX = screenx;
		SCREENY = screeny;
		
		h = startCatX;
		eCX = endCatX;
		eCY = endCatY;
		
		fromLeft = fromL;
		isBomb = bomb;

		if (isBomb) {
			PIC = new ImageIcon(new ImageIcon("bomb.png").getImage().getScaledInstance((int) (SCREENX/20), (int) (SCREENX/20), Image.SCALE_DEFAULT));

		}
		else {
			PIC = new ImageIcon(new ImageIcon("fish.gif").getImage().getScaledInstance((int) (SCREENX/15), (int) (SCREENX/15), Image.SCALE_DEFAULT));
		}

		setIcon(PIC);

		if (fromLeft) {
			x=0;
		}
		else {
			x=SCREENX;
		}

		setVisible(true);

		r = new Random();
		angle = r.nextInt(4)+1;

	}

	public void move(double speed) {

		if (fromLeft) {
			x += speed * (SCREENX/1920);
		}
		else {
			x -= speed * (SCREENX/1920);
		}

		switch (angle) {
		case 1:
			k = SCREENY/20;
			break;
		case 2:
			k = SCREENY/10;
			break;
		case 3:
			k = SCREENY/15;
			break;
		case 4:
			k = SCREENY/25;
			break;
		}
		y = ((eCY-k)/Math.pow((eCX-h), 2))*Math.pow((x-h), 2)+k;

		setBounds((int)(x-(this.getWidth()/2.0)), (int)y, PIC.getIconWidth(), PIC.getIconHeight()); 
		this.repaint(); 
	}
}