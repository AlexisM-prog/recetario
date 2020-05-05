package Vista;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;
import Control.*;

public class VentanaGeneral extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean modoAgregar;
	protected Panel panelP;
	protected JPanel panelSec, panelTotal;
	protected JButton elijeImagen, borrar, volver, actualizar;
	protected JLabel[] txtEntradas;
	protected JFrame frame;
	protected JFileChooser ventanaElije;
	protected ImageIcon imagen;
	protected JTextComponent[] entradas;
	protected Color colorFondo, colorLetra;
	protected ConeccionBD coneccion;

	public VentanaGeneral(JFrame frame, ConeccionBD coneccion,String titulo,String[] txtLabels, JTextComponent[] entradas) {
		super(titulo);
		
		colorFondo = new Color(255,235,59);
		colorLetra = new Color (0, 0, 0);
		
		this.frame = frame;
		this.coneccion = coneccion;
		this.modoAgregar = true;
		this.entradas = entradas;
		this.txtEntradas = new JLabel[txtLabels.length];
		this.crearVentana(txtLabels);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setSize(900,500);
		this.setLocationRelativeTo(null);
		this.setVisible(false);
		this.setResizable(false);
	}
	private void crearVentana(String txtLabel[]) {
		panelP = new Panel("../img/ParteDelDiseno/panelP.png",new GridLayout(6,3,0,5));
		panelSec = new JPanel(new GridLayout(1,1));	
		
		panelTotal = new JPanel(new GridLayout(1,2));
		ventanaElije = new JFileChooser();
		
		elijeImagen= new JButton("Agregar imagen");
		elijeImagen.setBackground(new Color(255, 235, 59));
		elijeImagen.setEnabled(false);
		
		actualizar= new JButton();
		actualizar.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonActualizar.png"))
		);
		actualizar.setBackground(new Color(255,255,255));
		
		volver= new JButton();
		volver.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonVolver.png"))
		);
		volver.setBackground(new Color(255,255,255));

		/*
		 * add panelP
		 * */
		for(int x = 0;x<txtLabel.length;x++) {
			txtEntradas[x]= new JLabel(txtLabel[x]);
			txtEntradas[x].setForeground(colorLetra);
			panelP.add(txtEntradas[x]);
			if(x<entradas.length && entradas[x] != null){
				entradas[x].setFont(new Font("Arial",Font.ITALIC,16));
				entradas[x].setBackground(colorFondo);
				entradas[x].setForeground(colorLetra);
				panelP.add(entradas[x]);
			}
		}
		borrar = new JButton("Borrar");
		borrar.setForeground(new Color(227, 0, 0));
		borrar.setBackground(new Color(255, 255, 255));

		panelP.add(actualizar);
		panelP.add(volver);
		/*
		 *add panelSec 
		 * */
		panelSec.add(elijeImagen);
		/*
		 * add panelTotal
		 */
		panelTotal.add(panelP);
		panelTotal.add(panelSec);


	}
	protected void actualizar(String[] elementos, ControladorTablasGeneral control) throws NullPointerException{
		boolean valorValido = true;
		for(int x = 0;x<entradas.length;x++) {
			txtEntradas[x].setForeground(colorLetra);
			if(entradas[x] != null && entradas[x].getText().equals("")) {
				txtEntradas[x].setForeground(new Color(255,0,0));
				valorValido = false;
			}
		}
		if(valorValido){
			try {
				control.guardar(elementos,1);

				for(int x = 0;x<this.entradas.length;x++)
					if(entradas[x] != null)
						this.entradas[x].setText("");
		
				this.elijeImagen.setIcon(control.getImageIcon());
				this.elijeImagen.setText("Actualizar imagen");
		
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		if(entradas[0] != null)
			this.entradas[0].setText("");
	}


	public void guardarImagen(ControladorTablasGeneral control) {
		this.ventanaElije.showOpenDialog(this);
		/*
		System.out.println("src/img/"+
			control.getNomTabla().toLowerCase()+"/"+
			this.entradas[0].getText().toLowerCase()
		);
		*/
		control.addPath("src/img/"+control.getNomTabla().toLowerCase()+"/"+this.entradas[0].getText().toLowerCase());
		control.moveImageToURL(ventanaElije.getSelectedFile());
		this.elijeImagen.setIcon(control.getImageIcon());
		this.elijeImagen.setText("Imagen Guardada");
	}
	public boolean accionesAlClickDefault(ActionEvent e, ControladorTablasGeneral control) {
		boolean estaAqui = false;
		if(e.getSource() == volver) {
			this.setVisible(false);
			frame.setVisible(true);
			estaAqui = true;
		}else if(e.getSource() == elijeImagen) {
			this.guardarImagen(control);
			estaAqui = true;
		}
		return estaAqui;
		
	}
}
