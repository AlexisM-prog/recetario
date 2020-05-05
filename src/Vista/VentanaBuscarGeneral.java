package Vista;
import Control.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;

import javax.swing.text.JTextComponent;

public class VentanaBuscarGeneral extends VentanaGeneral implements KeyListener, ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTable tabla;
	protected JButton abrirVentanaPasos;
	protected int lineas; 
	protected Object[] cabecera;
	protected String id;
	protected JScrollPane scrollTabla;
	protected DefaultTableModel modelo;
	protected ControladorTablasGeneral control;

	public VentanaBuscarGeneral(JFrame frame, ConeccionBD coneccion, String titulo,
			int lineas, Object cabecera[],String nomTabla, String id) {
		super(frame, coneccion, titulo, new String[] {
				null
			}, new JTextComponent[] {
				new JTextField(20)
			}
		);
		this.control = new ControladorTablasGeneral(nomTabla,coneccion);
		this.id = id;
		this.lineas = lineas;
		this.cabecera = cabecera;
		this.setSize(900,600);
		this.actualizarVentana();
		panelP.setLayout(new FlowLayout());
	}
	public void actualizarVentana() {
		entradas[0].addKeyListener(this);
		elijeImagen.setText("");
		modelo = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int rowIndex,int columnIndex){
				return false;
			}
		};
		volver.setIcon(new ImageIcon(
			getClass().getResource("../img/ParteDelDiseno/botonVolverMini.png"))
		);
		volver.setPreferredSize(new Dimension(110,40));
		
		for(Object aux: cabecera)
			modelo.addColumn(aux);
		modelo.setRowCount(lineas);

		tabla = new JTable(modelo);		
		tabla.setForeground(new Color(0,0,0));
		tabla.setBackground(new Color(224,224,224));
		
		scrollTabla = new JScrollPane(tabla); 
		scrollTabla.setBackground(new Color(224,224,224));

		if(this.control.getNomTabla().equals("INGREDIENTE")) {
			scrollTabla.setPreferredSize(new Dimension(420,380));
			tabla.setPreferredSize(new Dimension(0,355));
		}else {
			scrollTabla.setPreferredSize(new Dimension(420,160));
			tabla.setPreferredSize(new Dimension(0,160));

		}

		abrirVentanaPasos = new JButton("Agregar Pasos");
		abrirVentanaPasos.setEnabled(false);

		panelP.add(scrollTabla);
		panelP.remove(actualizar);
		
		panelTotal.add(panelP);
		panelTotal.add(panelSec);
		add(panelTotal);
	}
	public void actualizarTabla() {
	
		elijeImagen.setEnabled(!entradas[0].getText().equals(""));
		try {
			String[][] texto = this.coneccion.buscarDatos(this.control.getNomTabla(), this.id,this.entradas[0].getText(), new int[] {1,2,3,4});

			if(!(texto == null || entradas[0].getText().equals(""))) {
				for(int y = 0;y<this.lineas;y++) {
					for(int x = 0;x<this.cabecera.length;x++) {
						if(y<texto.length) {
							tabla.setValueAt(
									texto[y][x],
							y, x);
						}else{
							tabla.setValueAt("", y,x);
						}
				
					}
				}
			}else {
				for(int y = 0;y<this.lineas;y++) 
					for(int x = 0;x<this.cabecera.length;x++) 
						tabla.setValueAt("",y, x);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.accionesAlClickDefault(e, control)) {
			
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getSource() == entradas[0]) {
			this.actualizarTabla();
			this.control.addPath("src/img/"+this.control.getNomTabla().toLowerCase()+"/"+this.tabla.getValueAt(0, 0).toString().toLowerCase());
			this.elijeImagen.setIcon(this.control.getImageIcon());

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}
