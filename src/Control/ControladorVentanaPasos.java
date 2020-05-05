package Control;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import Vista.VentanaPasosGeneral;

public class ControladorVentanaPasos extends ControladorTablasGeneral{
	protected JComboBox<Integer> horaTiempo, minTiempo,segTiempo, temperatura,cantidad;
	protected JComboBox<String> tipoPaso,unidad;
	protected JTextComponent entradaTXT;
	protected JButton atras, adelante, elijeImagen, actualizar;
	protected int nroPaso;
	protected String idReceta;
	protected VentanaPasosGeneral ventana;
	protected String[] consultaActual; 
	protected ControlTablasPasos gestorConsultasPasos;

	/* 0: consultaEspero
	 * 1: consultaCaliento
	 * 2: consultaAgrego
	 */
	public ControladorVentanaPasos(String nomTabla,ConeccionBD coneccion,VentanaPasosGeneral ventana) {
		super(nomTabla,coneccion);
		this.gestorConsultasPasos = new ControlTablasPasos();
		this.nroPaso = 1;
		this.idReceta = "";
		this.ventana = ventana;
		this.consultaActual = null;

	}
	public void agregarComponentes(JComboBox<Integer> horaTiempo, JComboBox<Integer> minTiempo,JComboBox<Integer> segTiempo,
			JComboBox<Integer> temperatura,
			JTextComponent entradaTXT,
			JComboBox<Integer> cantidad,JComboBox<String> unidad, 
			JComboBox<String> tipoPaso, JButton atras, JButton adelante,JButton elijeImagen,JButton actualizar) {
		this.horaTiempo = horaTiempo;
		this.minTiempo = minTiempo;
		this.segTiempo = segTiempo;
		this.temperatura = temperatura;
		this.entradaTXT = entradaTXT;
		this.tipoPaso = tipoPaso;
		this.cantidad = cantidad;
		this.unidad = unidad;
		this.atras = atras;
		this.adelante = adelante;
		this.elijeImagen = elijeImagen;
		this.actualizar = actualizar;
		this.setModoInsertarBD(true);
	}
	/*
	 * nroPaso
	 * */
	public int getNroPaso() {
		return nroPaso;
	}

	/*
	 * idReceta
	 * */
	public void setIdReceta(String idReceta) {
		this.idReceta = idReceta;
		this.nroPaso = 1;
		this.sumarNroPaso(0);
	}
	public String getIdReceta() {
		return idReceta;
	}

	public void removeTuplasEspurias() throws SQLException {
		String[] tablas = {"ESPERO","CALIENTO","AGREGOING"};

		try {
			for(int x = 0;x<3;x++) {
				if(!this.getNomTabla().equals(tablas[x])){
					this.coneccion.removeDato(tablas[x], new String[] {this.getIdReceta(), this.nroPaso+""}, 2);
				}
			}
			this.consultaActual = null;

		}catch(Exception err) {
			err.printStackTrace();
		}		
	}
	public String calcularNomTabla() {
		if(this.tipoPaso.getSelectedIndex() == 0) 
			this.setNomTabla("ESPERO");
		else if(this.tipoPaso.getSelectedIndex() == 1) 
			this.setNomTabla("CALIENTO");
		else 
			this.setNomTabla("AGREGOING");
		return this.getNomTabla();
	}
	
	public void alClickearActualizar() throws SQLException{
		this.removeTuplasEspurias();
		if(this.tipoPaso.getSelectedIndex() == 0) {
			this.setNomTabla("ESPERO");
			this.consultaActual = new String[]{
				this.getIdReceta(), this.nroPaso+"",horaTiempo.getSelectedIndex()+"",
				minTiempo.getSelectedIndex()+"",segTiempo.getSelectedIndex()+""
			};
			

		}else if(this.tipoPaso.getSelectedIndex() == 1) {
			this.setNomTabla("CALIENTO");
			this.consultaActual = new String[]{
					this.getIdReceta(), this.nroPaso+"",horaTiempo.getSelectedIndex()+"",
					minTiempo.getSelectedIndex()+"",segTiempo.getSelectedIndex()+"",
					this.temperatura.getSelectedItem()+""
			};

		}else if(tipoPaso.getSelectedIndex() == 2){
			this.setNomTabla("AGREGOING");
			this.consultaActual = new String[]{
					this.getIdReceta(), this.nroPaso+"",horaTiempo.getSelectedIndex()+"",
					minTiempo.getSelectedIndex()+"",segTiempo.getSelectedIndex()+"",
					this.entradaTXT.getText().toUpperCase(),
					this.cantidad.getSelectedItem().toString(), this.unidad.getSelectedItem().toString()
			};
		}
		this.guardar(this.consultaActual, 2);
		this.adelante.setEnabled(true);
		
	}
	@Override
	public void guardar(String[] strComp, int finalPrimarias) throws SQLException {
		String existe[] = this.coneccion.getDatos(
				"PASO", "NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+this.nroPaso, new int[] {3});
		if(existe == null) {
			this.coneccion.addDato("PASO", new String[]{strComp[0], strComp[1],this.getNomTabla()});
			this.setModoInsertarBD(true);
		}else {
			this.coneccion.updateDato("PASO", new String[]{strComp[0], strComp[1],this.getNomTabla()},2);
			this.setModoInsertarBD(!existe[0].equals(this.getNomTabla()));
			this.setNomTabla(this.getNomTabla());
		}
		super.guardar(strComp, finalPrimarias);
	}
	public void asignarValoresAConsultas(String existe) throws SQLException {
		this.consultaActual = null;		
		if(existe != null) {
			this.consultaActual = this.coneccion.getDatos(
					existe,"NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+this.nroPaso,2);
			this.gestorConsultasPasos.setTablaActual(existe);

		}
	}
	protected void sumarNroPaso(int valorASumar) {	

		this.nroPaso+=valorASumar;
		String tablaSegunGuardado[], resp[];
		this.todoEnBlanco();
		try {
			this.atras.setEnabled(nroPaso > 1);
			if(valorASumar > 0)
				this.gestorConsultasPasos.mover(true);	
			else if(valorASumar < 0 ) {
				this.gestorConsultasPasos.mover(false);	
			}
			if(this.gestorConsultasPasos.getTablaSiguiente().equals(new String())) {
				resp = this.coneccion.getDatos(
					"PASO", "NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+(nroPaso+1), new int[]{3});
			}else {
				resp = new String[] {this.gestorConsultasPasos.getTablaSiguiente()};
			}
			
			if(resp != null) {
				this.gestorConsultasPasos.setTablaSiguiente(resp[0]);
				this.adelante.setEnabled(true);
			}else {
				this.adelante.setEnabled(false);
			}
			
			
			
			if(this.gestorConsultasPasos.getTablaActual().equals(new String())) {
				tablaSegunGuardado = this.coneccion.getDatos(
						"PASO", "NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+this.nroPaso, new int[] {3});
			}else {
				tablaSegunGuardado = new String[] {this.gestorConsultasPasos.getTablaActual()};
			}
			if(tablaSegunGuardado != null) {
				this.asignarValoresAConsultas(tablaSegunGuardado[0]);
				if(this.consultaActual != null) {
					if(tablaSegunGuardado[0].equals("ESPERO")) 
						this.accionAlEsperar();
					else if(tablaSegunGuardado[0].equals("CALIENTO"))
						this.accionAlCalentar();
					else if(tablaSegunGuardado[0].equals("AGREGOING")) 
						this.accionAlAgregar();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.addPath("src/img/acciones/"+
				this.getIdReceta()+this.getNroPaso()
		);
	}
	public void accionAlEsperar() throws SQLException {
		this.setNomTabla("ESPERO");
		this.ventana.modoEsperar();
		this.actualizarDatosDeComponentes("ESPERO");
	}
	public void accionAlCalentar() throws SQLException {
		this.setNomTabla("CALIENTO");
		this.ventana.modoCalentar();
		this.actualizarDatosDeComponentes("CALIENTO");
	}
	public void accionAlAgregar() throws SQLException {
		this.setNomTabla("AGREGOING");
		this.ventana.modoAgregar();
		this.actualizarDatosDeComponentes("AGREGOING");
	}
	public void irAtras() {
		this.sumarNroPaso(-1);
	}	
	public void irAdelante() {
		this.sumarNroPaso(1);
	}

	protected void actualizarDatosDeComponentes(String tabla) throws SQLException {
		if(tabla != null) {
			String salida[];
			
			salida = this.consultaActual;
			
			if(salida == null) {
				this.elijeImagen.setText("Agregar Imagen");
				
				actualizar.setIcon(new ImageIcon(
						getClass().getResource("../img/ParteDelDiseno/botonAgregarPaso.png"))
				);
				this.elijeImagen.setIcon(null);
				this.entradaTXT.setText("");
			}else {
				this.adelante.setEnabled(true);
				this.elijeImagen.setText("");
				
				actualizar.setIcon(new ImageIcon(
						getClass().getResource("../img/ParteDelDiseno/botonActualizarPaso.png"))
				);
				this.horaTiempo.setSelectedIndex(
						Integer.parseInt(salida[0])
				);
				this.minTiempo.setSelectedIndex(
						Integer.parseInt(salida[1])
				);			
				this.segTiempo.setSelectedIndex(
						Integer.parseInt(salida[2])
				);
			
				if(tabla.equals("ESPERO")) {
					this.ventana.modoEsperar();
					this.tipoPaso.setSelectedIndex(0);
				}else if(tabla.equals("CALIENTO")) {
					this.ventana.modoCalentar();
					this.tipoPaso.setSelectedIndex(1);
					this.temperatura.setSelectedItem(Integer.parseInt(salida[3]));
				}else if(tabla.equals("AGREGOING")){
					this.ventana.modoAgregar();
					this.tipoPaso.setSelectedIndex(2);
					this.entradaTXT.setText(salida[3]);
					this.cantidad.setSelectedItem(salida[4]);
					this.unidad.setSelectedItem(salida[5]);
				}
				this.addPath("src/img/acciones/"+
						this.getIdReceta()+this.getNroPaso()
				);
				this.elijeImagen.setIcon(this.getImageIcon());				
			}
		}
	}
	public void updateTablaPasos() {
		boolean estaEnLaSiguiente[];
		try {
			estaEnLaSiguiente = this.enQueTablaEsta();
			if(estaEnLaSiguiente[0] || estaEnLaSiguiente[1] || estaEnLaSiguiente[2]) {
				if(estaEnLaSiguiente[0]) 
					this.setNomTabla("ESPERO");
				else 
					this.setNomTabla(estaEnLaSiguiente[1]?"AGREGO":"CALIENTO");
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean[] enQueTablaEsta() throws SQLException {
		boolean EstaEnEspero = this.consultaActual.length == 5;
		boolean EstaEnCaliento = this.consultaActual.length == 6;
		boolean EstaEnAgrego = this.consultaActual.length == 8;
		return new boolean[] {EstaEnEspero,EstaEnAgrego,EstaEnCaliento};
	}
	public boolean esLaEntradaValida() {
		boolean tiempoInvalido = horaTiempo.getSelectedIndex()==0 && minTiempo.getSelectedIndex()==0 && segTiempo.getSelectedIndex()==0;
		boolean temperaturaInvalida = temperatura.getSelectedIndex()==0;
		boolean entradaInvalida = this.entradaTXT.getText().equals("");
		try {
			if(!entradaInvalida && this.tipoPaso.getSelectedIndex() == 2 && this.getModoAgregar())
				entradaInvalida=this.coneccion.getDatos("INGREDIENTE","NOMBREINGR='"+entradaTXT.getText()+"'", new int[]{3}) == null;
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return !tiempoInvalido && (
				this.tipoPaso.getSelectedIndex() == 0 ||
					(!temperaturaInvalida && this.tipoPaso.getSelectedIndex() == 1) ||
					(!entradaInvalida && this.tipoPaso.getSelectedIndex() == 2)
			
		);
	}
	protected void todoEnBlanco() {
		this.tipoPaso.setSelectedIndex(0);
		this.horaTiempo.setSelectedIndex(0);
		this.minTiempo.setSelectedIndex(0);
		this.segTiempo.setSelectedIndex(0);
		this.entradaTXT.setText("");
		this.temperatura.setSelectedIndex(0);
		this.actualizar.setIcon(new ImageIcon(
				getClass().getResource("../img/ParteDelDiseno/botonAgregarPaso.png"))
		);
	}
}
