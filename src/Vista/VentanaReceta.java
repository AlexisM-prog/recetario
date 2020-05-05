package Vista;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import Control.ConeccionBD;
import Control.ControladorTablasGeneral;

public class VentanaReceta extends VentanaGeneral implements ActionListener, KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JComboBox<String> tipoReceta;
	protected JButton abrirVentanaPasos;
	protected VentanaPasosGeneral vPasos;
	protected ControladorTablasGeneral control;

	public VentanaReceta(JFrame frame, ConeccionBD coneccion) {
		super(frame, coneccion,"Crear receta", new String[]{
				"Nombre receta:",
				"Tipo de receta:",
				"Descripcion:",
				"Comentario:",
				"Paso:"
			},
			new JTextComponent[] {
					new JTextField(),
					null,
					new JTextField(),
					new JTextField(),
			}
		);
		this.vPasos = new VentanaPasosGeneral(this,coneccion,"Esperar",
			new String[] {
				"Que quiere hacer?",
				"Tiempo",
				"-----"
			},new JTextComponent[] {
					null,
					null,
					null
		});
		this.agregaVentana();
		this.control = new ControladorTablasGeneral("RECETA",coneccion);
	}
	public void agregaVentana() {
		actualizar.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonAgregar.png"))
		);
		

		abrirVentanaPasos = new JButton();
		abrirVentanaPasos.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonVentanaPaso.png"))
		);
		abrirVentanaPasos.setEnabled(false);

		tipoReceta = new JComboBox<String>();
		tipoReceta.addItem("Postre");
		tipoReceta.addItem("Carne");
		tipoReceta.addItem("Pasta");
		tipoReceta.addItem("Ensalada");
		tipoReceta.addItem("Plato Principal");
		tipoReceta.setBackground(colorFondo);
		tipoReceta.setForeground(colorLetra);
		
		
		borrar.addActionListener(this);
		abrirVentanaPasos.addActionListener(this);
		volver.addActionListener(this);
		actualizar.addActionListener(this);		
		elijeImagen.addActionListener(this);
		entradas[0].addKeyListener(this);
		
		panelP.add(abrirVentanaPasos, 8);
		panelP.add(tipoReceta, 3);
		
		panelTotal.add(panelP);
		panelTotal.add(panelSec);
		
		this.add(panelTotal);
		this.add(borrar,BorderLayout.SOUTH);
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.accionesAlClickDefault(e, control)){
			if(e.getSource() == this.actualizar) {
				this.actualizar(new String[] {
						this.entradas[0].getText().toUpperCase(),this.tipoReceta.getSelectedIndex()+"",
						this.entradas[2].getText(),this.entradas[3].getText()
				},control);			
			}else if(e.getSource() == abrirVentanaPasos) {
				this.setVisible(false);
				vPasos.setVisible(true);
				vPasos.controladorVPasos.setIdReceta(this.entradas[0].getText().toUpperCase());
			}else if(e.getSource() == this.borrar) {
				try {
					for(String tabla: new String[] {"ESPERO","CALIENTO","AGREGOING"}) {
						this.coneccion.removeDato(tabla,new String[] {
								this.entradas[0].getText().toUpperCase()
						}, 1);
					}
				
					this.coneccion.removeDato("AGREGOING",new String[]{this.entradas[0].getText().toUpperCase()}, 1);
					this.coneccion.removeDato("ESPERO",new String[]{this.entradas[0].getText().toUpperCase()}, 1);
					this.coneccion.removeDato("CALIENTO",new String[]{this.entradas[0].getText().toUpperCase()}, 1);
					this.coneccion.removeDato("PASO",new String[]{this.entradas[0].getText().toUpperCase()}, 1);
					this.coneccion.removeDato("RECETA",new String[]{this.entradas[0].getText().toUpperCase()}, 1);
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

			try {
				String texto[] = this.coneccion.getDatos("RECETA", "NOMBREREC='"+this.entradas[0].getText()+"'",new int[] {2,3,4});
				this.control.setModoInsertarBD(texto == null);
				if(this.control.getModoAgregar()) {
					this.elijeImagen.setText("Agregar Imagen");
					this.actualizar.setIcon(new ImageIcon(
							getClass().getResource("../img/ParteDelDiseno/botonAgregar.png"))
					);
			
					
					this.abrirVentanaPasos.setEnabled(false);
					this.elijeImagen.setIcon(null);
					for(int x = 0;x<this.entradas.length-1;x++) 
						if(entradas[x+1] !=null)
							this.entradas[x+1].setText("");
				}else {
					this.elijeImagen.setText("");
					actualizar.setIcon(new ImageIcon(
							getClass().getResource("../img/ParteDelDiseno/botonActualizar.png"))
					);
					this.abrirVentanaPasos.setEnabled(true);
					this.control.addPath("src/img/receta/"+this.entradas[0].getText().toLowerCase());
					this.elijeImagen.setIcon(this.control.getImageIcon());
					this.tipoReceta.setSelectedIndex(
							Integer.parseInt(texto[0])
					);
										
					this.entradas[2].setText(texto[1]);
					this.entradas[3].setText(texto[2]);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}else {

		}
	}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
