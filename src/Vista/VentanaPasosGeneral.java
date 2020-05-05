package Vista;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import Control.ConeccionBD;
import Control.ControladorTablasGeneral;
import Control.ControladorVentanaPasos;

public class VentanaPasosGeneral extends VentanaGeneral implements ItemListener, ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JButton atras, adelante, agregarIngrediente;
	private JComboBox<String> tipoPaso;
	private JComboBox<Integer> horaTiempo, minTiempo, segTiempo, temperatura;
	private JLabel labeldosPuntos,labeldosPuntos2;
	private JPanel panelTiempo, panelAtrasAdelante;
	protected ControladorVentanaPasos controladorVPasos;
	private VentanaElijeIngrediente vElijeIngrediente;
	
	public VentanaPasosGeneral(JFrame frame, ConeccionBD coneccion, String titulo,
			String[] txtLabels, JTextComponent[] entradas)  {
		super(frame, coneccion,titulo, txtLabels,entradas);
		
		this.vElijeIngrediente = new VentanaElijeIngrediente(this,this, coneccion);
		this.controladorVPasos = new ControladorVentanaPasos("", coneccion, this);
		this.setResizable(true);
		this.actualizaVentana();
		this.controladorVPasos.agregarComponentes(horaTiempo, minTiempo, segTiempo, 
				temperatura,
				this.vElijeIngrediente.entradas[0],
				this.vElijeIngrediente.cantidad, this.vElijeIngrediente.unidad, 
				tipoPaso, atras, adelante, elijeImagen, actualizar);
		this.controladorVPasos.esLaEntradaValida();
	}
	private void actualizaVentana() {
		agregarIngrediente = new JButton();
		agregarIngrediente.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonVentanaIngrediente.png"))
		);
		agregarIngrediente.addActionListener(this);

		volver.addActionListener(this);
		
		elijeImagen.addActionListener(this);
		
		actualizar.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonAgregarPaso.png"))
		);
		volver.setIcon(
				new ImageIcon(new ImageIcon(
						getClass().getResource("../img/ParteDelDiseno/botonVolver.png"))
						.getImage()
						.getScaledInstance(215, 120, Image.SCALE_DEFAULT)
				)
		);
		actualizar.addActionListener(this);
		
		panelP.setLayout(new GridLayout(4,2,0,5));			

		atras = new JButton();
		atras.setEnabled(false);
		atras.addActionListener(this);
		
		adelante = new JButton();
		adelante.setEnabled(false);
		adelante.addActionListener(this);
		
		tipoPaso = new JComboBox<String>();
		tipoPaso.addItem("Espero");
		tipoPaso.addItem("Caliento");
		tipoPaso.addItem("Agrego ingrediente");
		tipoPaso.addItemListener(this);
		
		horaTiempo = new JComboBox<Integer>();	
		minTiempo = new JComboBox<Integer>();
		segTiempo = new JComboBox<Integer>();
		temperatura = new JComboBox<Integer>();

		for(int h = 0;h<24;h++)
			horaTiempo.addItem(h);
		for(int m = 0;m<60;m+=5) 
			minTiempo.addItem(m);
		for(int s = 0;s<60;s+=10) 
			segTiempo.addItem(s);
		for(int t = 0;t<120;t+=10)
			temperatura.addItem(t);
		
		tipoPaso.setBackground(colorFondo);
		tipoPaso.setForeground(colorLetra);

		horaTiempo.setBackground(colorFondo);
		horaTiempo.setForeground(colorLetra);
		
		minTiempo.setBackground(colorFondo);
		minTiempo.setForeground(colorLetra);
		
		segTiempo.setBackground(colorFondo);
		segTiempo.setForeground(colorLetra);
		
		temperatura.setBackground(colorFondo);
		temperatura.setForeground(colorLetra);

		segTiempo.addItemListener(this);
		minTiempo.addItemListener(this);
		horaTiempo.addItemListener(this);
		
		temperatura.addItemListener(this);
		
		labeldosPuntos = new JLabel(":");
		labeldosPuntos2 = new JLabel(":");
		

		
		
		panelTiempo = new JPanel();
		panelTiempo.setLayout(new GridBagLayout());
		panelTiempo.add(horaTiempo);
		panelTiempo.add(labeldosPuntos);
		panelTiempo.add(minTiempo);
		panelTiempo.add(labeldosPuntos2);
		panelTiempo.add(segTiempo);
		panelTiempo.setBackground(colorFondo);
		
		panelAtrasAdelante = new JPanel();
		panelAtrasAdelante.setLayout(new GridLayout(1,2));
		panelAtrasAdelante.add(adelante);

		panelP.add(tipoPaso,1);
		panelP.add(panelTiempo,3);
		panelP.add(temperatura);	
		panelP.add(actualizar);
		panelP.add(volver);
		
		panelTotal.add(panelP);
		panelTotal.add(panelSec);

		this.add(panelTotal);
		this.add(atras,BorderLayout.WEST);
		this.add(adelante,BorderLayout.EAST);
	}
	@Override
	public void guardarImagen(ControladorTablasGeneral control) {
		this.ventanaElije.showOpenDialog(this);
		this.controladorVPasos.addPath("src/img/acciones/"+
			this.controladorVPasos.getIdReceta()+this.controladorVPasos.getNroPaso()
		);
		this.controladorVPasos.moveImageToURL(ventanaElije.getSelectedFile());
		this.elijeImagen.setIcon(this.controladorVPasos.getImageIcon());
		this.elijeImagen.setText("Imagen Guardada");	
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		boolean entradaCorrecta = this.controladorVPasos.esLaEntradaValida();
		this.actualizar.setEnabled(entradaCorrecta);
		this.elijeImagen.setEnabled(entradaCorrecta);
		
		if(e.getSource() == tipoPaso) {
			this.controladorVPasos.setModoInsertarBD(false);
			if(tipoPaso.getSelectedIndex()== 0) {
				this.controladorVPasos.setNomTabla("ESPERO");
				modoEsperar();
			}else if(tipoPaso.getSelectedIndex() == 1) {
				this.controladorVPasos.setNomTabla("CALIENTO");
				modoCalentar();
			}else if(tipoPaso.getSelectedIndex() == 2) {
				this.controladorVPasos.setNomTabla("AGREGOING");
				modoAgregar();
			}				
		}else if(e.getSource() == horaTiempo || e.getSource() == minTiempo || 
				e.getSource() == segTiempo || e.getSource() == temperatura) {
			
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean entradaCorrecta;
		if(!this.accionesAlClickDefault(e, controladorVPasos)) {
			if(e.getSource() == actualizar){
				try {
					this.controladorVPasos.alClickearActualizar();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}else if(e.getSource() == atras) {
				this.controladorVPasos.irAtras();
			}else if(e.getSource() == adelante) {
				this.controladorVPasos.irAdelante();
			}else if(e.getSource() == agregarIngrediente) {
				this.vElijeIngrediente.setVisible(true);
				this.setEnabled(false);
				this.vElijeIngrediente.agregarItem();
			}
			entradaCorrecta= this.controladorVPasos.esLaEntradaValida();
			this.actualizar.setEnabled(entradaCorrecta);
			this.elijeImagen.setEnabled(entradaCorrecta);
		}
	}

	public void modoEsperar() {
		this.setTitle("Esperar");
		this.agregarIngrediente.setEnabled(false);
		this.temperatura.setEnabled(false);
		this.txtEntradas[2].setEnabled(false);
		this.txtEntradas[2].setText("-----");
		this.actualizar.setEnabled(false);
		
		panelP.remove(5);
		panelP.add(temperatura,5);	

	}
	public void modoCalentar() {
		this.setTitle("Calentar");
		
		this.agregarIngrediente.setEnabled(false);
		this.temperatura.setEnabled(true);
		this.txtEntradas[2].setEnabled(true);	
		this.txtEntradas[2].setText("Temperatura");
		this.actualizar.setEnabled(false);

		panelP.remove(5);
		panelP.add(temperatura,5);	

	}
	public void modoAgregar() {
		this.setTitle("Agregar Ingrediente");
		this.agregarIngrediente.setEnabled(true);
		this.temperatura.setEnabled(false);
		this.txtEntradas[2].setEnabled(true);
		this.txtEntradas[2].setText("Ingrediente");
		this.actualizar.setEnabled(false);

		panelP.remove(5);
		panelP.add(agregarIngrediente,5);	

	}
}