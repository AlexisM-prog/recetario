package Control;
import java.util.Date;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;


public class ControladorBuscarPasos extends ControladorVentanaPasos{
	
	private JTextArea txtArea, informacionNutricional, txtAreaIngredientes;
	private JTable tabla;
	private JButton muestra;
	private int precioTotal;
	private double calorias, proteinas, grasas;
	protected ControladorReceta gestorConsultasRecetas;
	private int nroListaIngrediente;
	private Calendar calendario;
	
	public ControladorBuscarPasos(String nomTabla, ConeccionBD coneccion) {
		super(nomTabla, coneccion, null);

	}
	public void agregarComponentes(JButton atras, JButton adelante,
			JTextArea txtArea, JTextArea informacionNutricional,
			JTable tabla, JButton muestra, JTextArea txtAreaIngredientes) {
		
		gestorConsultasRecetas = new ControladorReceta();
		this.atras = atras;
		this.adelante = adelante;	
		this.txtArea = txtArea;
		this.informacionNutricional = informacionNutricional;
		this.tabla = tabla;
		this.muestra = muestra;
		this.txtAreaIngredientes = txtAreaIngredientes;
		this.calendario = Calendar.getInstance();		
	}
	
	@Override
	public String getIdReceta() {
		int selectedRow = this.tabla.getSelectedRow();
		if(selectedRow > -1) 
			return (String) tabla.getValueAt(selectedRow, 0);
		return (String) tabla.getValueAt(0, 0);
	}
	public void asignarValoresAConsultas(String existe[]) throws SQLException {
		
		if(existe != null) {
			this.consultaActual = this.coneccion.getDatos(existe[0],
					"NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+this.nroPaso,2);
			this.gestorConsultasPasos.setTablaActual(existe[0]);
		}
	}
	@Override
	public void sumarNroPaso(int valorASumar) {
		if(valorASumar == 0) {
			this.gestorConsultasPasos.reset();
			this.nroPaso = 1;
		}
		super.sumarNroPaso(valorASumar);
		if(this.consultaActual == null) {
			this.txtArea.setText("");
		}
		this.muestra.setIcon(this.getImageIcon(300,150));
	}
	@Override
	public void accionAlEsperar() {
		this.setNomTabla("ESPERO");
		this.txtArea.setText("Espera "+this.consultaActual[0]+
				":"+this.consultaActual[1]+":"+
				this.consultaActual[2]);
	}
	@Override
	public void accionAlCalentar() {
		this.setNomTabla("CALIENTO");
		this.txtArea.setText("Calienta por "+
				this.consultaActual[0]+":"
				+this.consultaActual[1]+
				":"+this.consultaActual[2]
				+" a "+this.consultaActual[3]+" grados");
	}
	@Override
	public void accionAlAgregar() {
		this.setNomTabla("AGREGOING");
		this.txtArea.setText("Agrega "+this.consultaActual[4]+" "+this.consultaActual[5].toLowerCase()+" de "+
			this.consultaActual[3].toLowerCase()+" en "+
			this.consultaActual[0]+":"+
			this.consultaActual[1]+":"+
			this.consultaActual[2]);	
	}
	public void actualizaInfoNutricional() {
		if(!this.gestorConsultasRecetas.getIdReceta().equals(this.getIdReceta())) {
			String[] datosIngrediente = null, enCualEsta = null, existePaso = null, datosUnidad = null;

			this.resetInfoNutricional();	
			try {
				int paso = 1;
				this.nroListaIngrediente = 1;
				this.calendario.setTime(new Date());
				this.escribirTituloIngrediente();
				
				do{
					existePaso = this.coneccion.getDatos("PASO", 
						"NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+paso,
					2);
					if(existePaso != null) {
						if(!this.getIdReceta().equals("")) {
							if(existePaso[0].equals("AGREGOING")) {
								enCualEsta = this.coneccion.getDatos(
									"AGREGOING","NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+paso,new int[] {3,4,5,6,7,8});
								datosIngrediente = this.coneccion.getDatos("INGREDIENTE", "NOMBREINGR='"+enCualEsta[3]+"'", new int[] {2,3,4,5});
								datosUnidad = this.coneccion.getDatos("UNIDAD", "NOMBREUNIDAD='"+enCualEsta[5]+"'", new int[] {2});
								
								Integer equivalenteEnGramos = Integer.parseInt(datosUnidad[0])*Integer.parseInt(enCualEsta[4]);// regla del 3
							
								precioTotal+=(Integer.parseInt(datosIngrediente[0])*equivalenteEnGramos/100);// regla del 3, el 100 es porque es cada 100 gramos
								calorias+=((double)Integer.parseInt(datosIngrediente[1])*equivalenteEnGramos/100);
								proteinas+=((double)Integer.parseInt(datosIngrediente[2])*equivalenteEnGramos/100);
								grasas+=((double)Integer.parseInt(datosIngrediente[3])*equivalenteEnGramos/100);
								this.agregarIngredienteTxtArea(enCualEsta[3].toLowerCase(), nroListaIngrediente++);
							
							}else {
								enCualEsta = this.coneccion.getDatos(existePaso[0], "NOMBREREC='"+this.getIdReceta()+"' && NROPASO="+paso, new int[] {3,4,5});
								

							}
							this.calendario.add(Calendar.HOUR, Integer.parseInt(enCualEsta[0]));
							this.calendario.add(Calendar.MINUTE, Integer.parseInt(enCualEsta[1]));
							this.calendario.add(Calendar.SECOND, Integer.parseInt(enCualEsta[2]));

						}
					}
					paso++;
				}while(existePaso != null);
				this.escribirInfoNutricional();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}		
			this.gestorConsultasRecetas.setIdReceta(this.getIdReceta());
		}

	}

	public void resetInfoNutricional() {
		this.precioTotal=0;
		this.calorias=0;
		this.proteinas=0;
		this.grasas=0;		
	}
	private void escribirInfoNutricional() {		
		informacionNutricional.setText("");
		informacionNutricional.append("Precio Total: "+precioTotal+" $U");
		informacionNutricional.append("\n");		
		informacionNutricional.append("Tiempo de preparacion: "+
				this.calendario.getTime().toString().substring(11, 19)
		);
		informacionNutricional.append("\n");
		informacionNutricional.append("INFORMACION NUTRICIONAL");
		informacionNutricional.append("\n");
		informacionNutricional.append("Proteinas:\t"+calorias);
		informacionNutricional.append("\n");
		informacionNutricional.append("Calorias:\t"+proteinas);
		informacionNutricional.append("\n");
		informacionNutricional.append("Proteinas:\t"+grasas);	
		
	}	
	private void escribirTituloIngrediente() {
		txtAreaIngredientes.setText("");
		txtAreaIngredientes.append("LISTA DE INGREDIENTES");
		txtAreaIngredientes.append("\n");

	}
	private void agregarIngredienteTxtArea(String ingrediente, int numero){
		txtAreaIngredientes.append(numero+". "+ingrediente);
		txtAreaIngredientes.append("\n");
	}
	@Override
	protected void todoEnBlanco() {
	
	}
}
