package Vista;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Panel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String path;
	public Panel(String path, LayoutManager layout) {
		super(layout);
		this.path = path;
	}

	@Override
	public void paint(Graphics g){
		Image imagenFondo = new ImageIcon(getClass().getResource(path)).getImage();
        g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(),
                this);
	
		setOpaque(false);
		super.paint(g);
	}
}
