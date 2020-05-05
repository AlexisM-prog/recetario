package Vista;
import Control.ConeccionBD;
import Control.ControladorBuscarPasos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class VentanaBuscarReceta extends VentanaBuscarGeneral implements ActionListener, MouseListener{

	private JTextArea informacionNutricional, txtAreaIngredientes, txtArea;
	private JPanel panelPasos;
	private JButton atras, siguiente, muestra;
	private JScrollPane scrollTxtAreaIngrediente;
	private ControladorBuscarPasos controlBPasos;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VentanaBuscarReceta(JFrame frame, ConeccionBD coneccion) {
		super(frame, coneccion,"Buscar receta", 
			10, 
			new Object[] {
				"Nombre receta","Tipo receta","Descripcion","Comentario"
			},
			"RECETA","NOMBREREC"
		);
		controlBPasos = new ControladorBuscarPasos("ESPERO",coneccion);
		this.agregarAVentana();
		controlBPasos.agregarComponentes(atras, siguiente, txtArea, informacionNutricional, tabla, muestra,txtAreaIngredientes);
	}
	private void agregarAVentana() {

		volver.addActionListener(this);
		siguiente = new JButton();
		atras = new JButton();
		
		muestra = new JButton();
		muestra.setBackground(new Color(255,255,255));
		siguiente.addActionListener(this);
		atras.addActionListener(this);
		
		informacionNutricional = new JTextArea(5,20);
		informacionNutricional.setEditable(false);
		informacionNutricional.setBackground(new Color(224,224,224));

		txtAreaIngredientes = new JTextArea(5,15);
		txtAreaIngredientes.setEditable(false);
		txtAreaIngredientes.setBackground(new Color(224,224,224));

		scrollTxtAreaIngrediente = new JScrollPane(txtAreaIngredientes); 
		scrollTxtAreaIngrediente.setMinimumSize(new Dimension(100,100));
		
		txtArea = new JTextArea(5,20);
		txtArea.setEditable(false);
		txtArea.setBackground(new Color(224,224,224));


		
		panelPasos = new JPanel(); 
		panelPasos.setLayout(new BorderLayout());

		panelPasos.add(txtArea);
		panelPasos.add(atras,BorderLayout.WEST);
		panelPasos.add(siguiente ,BorderLayout.EAST);
		panelPasos.add(muestra, BorderLayout.SOUTH);
		tabla.addMouseListener(this);
		
		panelP.add(scrollTxtAreaIngrediente);
		panelP.add(informacionNutricional);
		panelP.add(panelPasos);

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == tabla) {
			try {
				actualizarTabla();

			}catch(Exception err) {
				err.printStackTrace();
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.accionesAlClickDefault(e, controlBPasos)) {
			if(e.getSource() == siguiente) {
				this.controlBPasos.sumarNroPaso(1);
			}else if(e.getSource() == atras) {
				this.controlBPasos.sumarNroPaso(-1);
			}
		}
	}
	@Override
	public void actualizarTabla() {
		super.actualizarTabla();
		this.controlBPasos.sumarNroPaso(0);
		this.controlBPasos.actualizaInfoNutricional();	
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
}
