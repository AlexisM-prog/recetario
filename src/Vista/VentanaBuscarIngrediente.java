package Vista;



import javax.swing.JFrame;
import Control.ConeccionBD;
import Control.ControlTablasPasos;

public class VentanaBuscarIngrediente extends VentanaBuscarGeneral{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ControlTablasPasos historialTablas;

	public VentanaBuscarIngrediente(JFrame frame, ConeccionBD coneccion) {
		super(frame, coneccion,"Buscar ingrediente", 
			22, 
			new Object[] {
				"Nombre ingrediente","Costo en pesos","Calorias","Proteinas"
			},
			"INGREDIENTE","NOMBREINGR"
		);
		historialTablas = new ControlTablasPasos();
		volver.addActionListener(this);
	}
}
