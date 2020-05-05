package Vista;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Control.ConeccionBD;
import Vista.Panel;

public class PantallaPrincipal extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton ingrediente, receta, buscadorReceta, buscadorIngrediente,btnCrearUnidad, reintentar;
	private JFrame vReceta, vIngrediente, vBuscarReceta, vBuscarIngrediente,vCrearUnidad;
	private Panel panel;
	private JLabel texto;
	private ConeccionBD coneccion;
	
	public PantallaPrincipal() {
		super("Elije una opcion");
		coneccion = new ConeccionBD();
	
		vReceta = new VentanaReceta(this,coneccion);
		vIngrediente = new VentanaIngrediente(this,coneccion);
		vBuscarReceta = new VentanaBuscarReceta(this,coneccion);
		vBuscarIngrediente = new VentanaBuscarIngrediente(this,coneccion);
		vCrearUnidad = new VentanaCrearUnidad(this,coneccion);
		
		this.crearVentana();
		this.muestraOculta();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setLayout(new BorderLayout());
		this.setSize(380,700);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
	}
	private void crearVentana() {
		panel = new Panel("../img/ParteDelDiseno/botonesPrincipal/fondo.png",new GridLayout(7,1,5,5));
        texto = new JLabel("");

        reintentar= new JButton();
        reintentar.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonesPrincipal/reconectar.png"))
		);
        reintentar.addActionListener(this);
		
		ingrediente= new JButton();
		ingrediente.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonesPrincipal/agregarIngredienteBoton.png"))
		);
		ingrediente.addActionListener(this);
		
		receta= new JButton();
		receta.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonesPrincipal/crearRecetaBoton.png"))
		);
		receta.addActionListener(this);
		
		buscadorReceta= new JButton();
		buscadorReceta.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonesPrincipal/buscarRecetaBoton.png"))
		);
		buscadorReceta.addActionListener(this);

		buscadorIngrediente= new JButton();
		buscadorIngrediente.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonesPrincipal/buscarIngredienteBoton.png"))
		);
		buscadorIngrediente.addActionListener(this);
		
		btnCrearUnidad = new JButton();
		btnCrearUnidad.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonesPrincipal/crearUnidad.png"))
		);
		btnCrearUnidad.addActionListener(this);
		
		panel.add(texto);
		panel.add(ingrediente);
		panel.add(receta);
		panel.add(btnCrearUnidad);
		panel.add(buscadorReceta);
		panel.add(buscadorIngrediente);
		panel.add(reintentar);
		
		this.add(panel);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == ingrediente) {
			vIngrediente.setVisible(true);
			this.setVisible(false);
		}else if(e.getSource() == receta) {
			vReceta.setVisible(true);
			this.setVisible(false);
		}else if(e.getSource() == reintentar) {
			muestraOculta();
		}else if(e.getSource() == this.buscadorReceta) {
			this.vBuscarReceta.setVisible(true);
			this.setVisible(false);
		}else if(e.getSource() == this.buscadorIngrediente) {
			this.vBuscarIngrediente.setVisible(true);
			this.setVisible(false);		
		}else if(e.getSource() == this.btnCrearUnidad) {
			this.vCrearUnidad.setVisible(true);
			this.setVisible(false);
		}
		
	}
	private void muestraOculta() {
		boolean hizo = this.coneccion.conectar();
		reintentar.setEnabled(!hizo);
		ingrediente.setEnabled(hizo);
		buscadorReceta.setEnabled(hizo);
		buscadorIngrediente.setEnabled(hizo);
		receta.setEnabled(hizo);
		btnCrearUnidad.setEnabled(hizo);
	}
}
