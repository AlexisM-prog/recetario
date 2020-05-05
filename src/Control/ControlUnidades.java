package Control;

import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

public class ControlUnidades extends ControladorTablasGeneral{

	public ControlUnidades(String nomTabla, ConeccionBD coneccion) {
		super(nomTabla, coneccion);
	}

	public boolean verificarQueExista(JTextComponent entrada, JComboBox<Integer> comboEquivalente) {
		boolean esta = false;
		try {
			String[] resp = this.coneccion.getDatos(
					this.getNomTabla(), "NOMBREUNIDAD='"+entrada.getText().toUpperCase()+"'", new int[] {2});
			modoInsertarBD = resp == null;
			if(modoInsertarBD) {
				comboEquivalente.setSelectedIndex(0);
				esta = true;
			}else 
				comboEquivalente.setSelectedItem(Integer.parseInt(resp[0]));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return esta;
	}
	
}
