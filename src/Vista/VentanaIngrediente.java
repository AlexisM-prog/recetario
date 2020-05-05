package Vista;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import Control.ConeccionBD;
import Control.ControladorTablasGeneral;

public class VentanaIngrediente extends VentanaGeneral implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ControladorTablasGeneral control;

	public VentanaIngrediente(JFrame frame, ConeccionBD coneccion) {
		super(frame, coneccion,"Agregar ingrediente",
				new String[] {
						"Nombre ingrediente:",
						"Costo ingrediente (pesos/kg):",
						"Calorias por 100g:",
						"Proteinas por 100g:",
						"Grasas por 100g:"
				},
				new JTextComponent[] {
						new JTextField(),
						new JTextField(),
						new JTextField(),
						new JTextField(),
						new JTextField()
				}
		);		
		this.agregaVentana();
		
		this.control = new ControladorTablasGeneral("INGREDIENTE",coneccion);
	}	
	private void agregaVentana() {
		this.actualizar.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonAgregar.png"))
		);
		volver.addActionListener(this);
		actualizar.addActionListener(this);		
		elijeImagen.addActionListener(this);
		borrar.addActionListener(this);
		entradas[0].addKeyListener(this);
		
		panelTotal.add(panelP);
		panelTotal.add(panelSec);
		this.add(panelTotal);
		this.add(borrar,BorderLayout.SOUTH);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.accionesAlClickDefault(e, control)) {
			if(e.getSource() == this.actualizar) {
				this.actualizar(new String[] {
						this.entradas[0].getText().toUpperCase(),this.entradas[1].getText(),
						this.entradas[2].getText(),this.entradas[3].getText(),
						this.entradas[4].getText()
				},control);
			}else if(e.getSource() == this.borrar) {
				try {
					this.coneccion.removeDato("INGREDIENTE", new String[] {
							this.entradas[0].getText().toUpperCase(),this.entradas[1].getText(),
							this.entradas[2].getText(),this.entradas[3].getText(),
							this.entradas[4].getText()
					}, 1);
					this.entradas[0].setText("");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == entradas[0]) {
			elijeImagen.setEnabled(!entradas[0].getText().equals(""));

			String texto[];
			try {
				texto = this.coneccion.getDatos("INGREDIENTE", "NOMBREINGR='"+this.entradas[0].getText()+"'", new int[] {2,3,4,5});

				this.control.setModoInsertarBD(texto == null);
				if(this.control.getModoAgregar()) {
					this.elijeImagen.setText("Agregar Imagen");
					actualizar.setIcon(new ImageIcon(
							getClass().getResource("../img/ParteDelDiseno/botonAgregar.png"))
					);
									
					
					this.elijeImagen.setIcon(null);
					for(int x = 0;x<this.entradas.length-1;x++) 
						this.entradas[x+1].setText("");
				}else {
					//this.actualizar.setText("Actualizar Ingrediente");
					
					actualizar.setIcon(new ImageIcon(
							getClass().getResource("../img/ParteDelDiseno/botonActualizar.png"))
					);
					
					
					
					this.elijeImagen.setText("");
					this.control.addPath("src/img/ingrediente/"+this.entradas[0].getText().toLowerCase());
					System.out.println(this.control.url);
					this.elijeImagen.setIcon(this.control.getImageIcon());
					for(int x = 0;x<this.entradas.length-1;x++) 
						this.entradas[x+1].setText(texto[x]);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}else{	
			
		}				
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
}
