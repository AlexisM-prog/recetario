package Vista;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import Control.ConeccionBD;

public class VentanaElijeIngrediente extends VentanaGeneral implements ActionListener, KeyListener,ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JComboBox<String> unidad;
	protected JComboBox<Integer> cantidad;
	private VentanaPasosGeneral ventana;

	public VentanaElijeIngrediente(VentanaPasosGeneral ventana, JFrame frame, ConeccionBD coneccion) {
		super(frame, coneccion, "Elije ingrediente", 
				new String[] {
						"Nombre:",
						"Cantidad:",
						"Unidad:"
				},new JTextComponent[] {
						new JTextField(""),
						null,
						null,
				}
		);
		
		this.ventana = ventana;
		this.setSize(600,300);
		panelP.setLayout(new GridLayout(4,2));
		this.actualizarVentana();
	}
	private void actualizarVentana() {
		entradas[0].addKeyListener(this);
		actualizar.setEnabled(false);
		actualizar.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonSeleccionar.png"))
		);
		
		volver.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonVolverMini.png"))
		);
		volver.setBackground(new Color(255,255,255));
		

		actualizar.addActionListener(this);
		volver.addActionListener(this);
		
		unidad = new JComboBox<String>();
		cantidad = new JComboBox<Integer>();
		for(int x = 1;x<10;x++) {
			cantidad.addItem(x*5);
		}
		cantidad.setBackground(colorFondo);
		cantidad.setForeground(colorLetra);
		
		unidad.setBackground(colorFondo);
		unidad.setForeground(colorLetra);
		
		unidad.addItemListener(this);
		cantidad.addItemListener(this);
		
		panelP.add(cantidad,3);
		panelP.add(unidad,5);
		panelP.add(volver);
		this.add(panelTotal);
	}
	
	private void habilitarBoton() {
		boolean entradaCorrecta = false;
		try {
			if(!entradas[0].equals("")) 
				entradaCorrecta = this.coneccion.getDatos(
					"INGREDIENTE","NOMBREINGR='"+entradas[0].getText()+"'", new int[]{3}) != null;
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		this.actualizar.setEnabled(entradaCorrecta);
		this.elijeImagen.setEnabled(entradaCorrecta);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == actualizar) {
			this.ventana.actualizar.setEnabled(
					this.ventana.controladorVPasos.esLaEntradaValida()
			);
		}
		this.setVisible(false);
		this.frame.setEnabled(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getSource() == this.entradas[0]) {
			this.habilitarBoton();
		}		
	}
	public void agregarItem() {
		try {
			String resp[][] = this.coneccion.buscarDatos("UNIDAD","NOMBREUNIDAD", "",new int[] {1});
			if(resp != null) {
				unidad.removeAllItems();
				for(int x = 0;x<resp.length;x++) {
					unidad.addItem(resp[x][0]);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cantidad || e.getSource() == unidad) {
			this.habilitarBoton();
		}		
	}
	@Override
	public void keyTyped(KeyEvent e) {		
	}

	@Override
	public void keyPressed(KeyEvent e) {		
	}

}
