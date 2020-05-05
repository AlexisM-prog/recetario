package Vista;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import Control.ConeccionBD;
import Control.ControlUnidades;
import Control.ControladorTablasGeneral;

public class VentanaCrearUnidad extends VentanaGeneral implements ActionListener, KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Integer> comboEquivalente;
	private ControlUnidades controlUnidades;

	public VentanaCrearUnidad(JFrame frame, ConeccionBD coneccion) {
		super(frame, coneccion, "Crear Unidad", new String[] {
				"Nombre unidad:",
				"Equivalente en gramos:"
		}, new JTextComponent[] {
				new JTextField(),
				null
		});
		controlUnidades = new ControlUnidades("UNIDAD", coneccion);
		this.crearVentana();
	}
	private void crearVentana() {
		volver.addActionListener(this);

		comboEquivalente = new JComboBox<Integer>();
		comboEquivalente.addItem(1);
		for(int x = 1;x<30;x++)
			comboEquivalente.addItem(x*10);
		comboEquivalente.setBackground(colorFondo);
		actualizar.addActionListener(this);
		entradas[0].addKeyListener(this);

		panelP.add(comboEquivalente,3);
		panelP.setLayout(new GridLayout(3,2,0,5));
		
		panelTotal.add(panelP);
		panelTotal.add(panelSec);
		
		this.add(panelTotal);
		this.add(borrar,BorderLayout.SOUTH);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.accionesAlClickDefault(e, controlUnidades)) {
			if(e.getSource() == actualizar) {
				try {
					this.controlUnidades.guardar(new String[] {
							this.entradas[0].getText().toUpperCase(),
							this.comboEquivalente.getSelectedItem().toString()
					}, 1);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	@Override
	public void guardarImagen(ControladorTablasGeneral control) {
		this.ventanaElije.showOpenDialog(this);
		this.controlUnidades.addPath("src/img/"+this.controlUnidades.getNomTabla().toLowerCase()+"/"+this.entradas[0].getText().toLowerCase());
		this.controlUnidades.moveImageToURL(ventanaElije.getSelectedFile());
		this.elijeImagen.setIcon(control.getImageIcon());
		this.elijeImagen.setText("Imagen Guardada");
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getSource() == entradas[0]) {
			if(this.controlUnidades.verificarQueExista(entradas[0], comboEquivalente)){
				this.actualizar.setIcon(new ImageIcon(
						getClass().getResource("../img/ParteDelDiseno/botonAgregar.png"))
				);
			}else {
				this.actualizar.setIcon(new ImageIcon(
						getClass().getResource("../img/ParteDelDiseno/botonActualizar.png"))
				);
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
}
