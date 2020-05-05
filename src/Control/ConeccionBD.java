package Control;
import java.sql.*;
import java.util.ArrayList;

public class ConeccionBD {
	private Connection coneccion;
	private Statement consulta;
	private ResultSetMetaData metadata;
	private String tablaActual;

	/*
	 * conecta a la base de datos
	 */

	public boolean conectar() {
		try {
			coneccion = DriverManager.getConnection("jdbc:mysql://localhost:3308/recetario", "recetarioUsuario", "recetarioUsuario");
			consulta = coneccion.createStatement();
			return true;
		} catch (Exception err) {
			System.out.println("Error de coneccion:" + err);
		}
		return false;
	}

	/*
	 * agrega datos a la base de datos
	 */
	public void removeDato(String nomTabla, String[] atributosNecesarios, int finalPrimarias) throws SQLException {
		String condicion = "";
		this.verificarTabla(nomTabla);
		for (int pos = 0; pos < atributosNecesarios.length; pos++) {
			if (this.metadata.getColumnTypeName(pos + 1) == "VARCHAR"
					|| this.metadata.getColumnTypeName(pos + 1) == "CHAR")
				atributosNecesarios[pos] = "'" + atributosNecesarios[pos] + "'";

			if (pos < finalPrimarias) {
				condicion += this.metadata.getColumnName(pos + 1) + "=" + atributosNecesarios[pos];
				if (pos + 1 < finalPrimarias)
					condicion += " && ";
			}
		}
		//System.out.println("DELETE FROM " + nomTabla + " WHERE " + condicion);
		consulta.executeUpdate("DELETE FROM " + nomTabla + " WHERE " + condicion);
	}

	public void addDato(String nomTabla, String[] atributos) throws SQLException {
		String valoresComoString = "";
		this.verificarTabla(nomTabla);
		
		/*
		 * transforma los atributos a string
		 * */
		for (int pos = 0; pos < atributos.length; pos++) {
			if (this.metadata.getColumnTypeName(pos + 1) == "VARCHAR"
					|| this.metadata.getColumnTypeName(pos + 1) == "CHAR")
				atributos[pos] = "'" + atributos[pos] + "'";

			valoresComoString += atributos[pos];
			if (pos + 1 < atributos.length)
				valoresComoString += ", ";
		}
		/*
		System.out.println("INSERT INTO " + nomTabla + "(" + this.getAtributosComoString(nomTabla) + ") VALUES ("
				+ valoresComoString + ");");
		*/
		consulta.executeUpdate("INSERT INTO " + nomTabla + "(" + this.getAtributosComoString(nomTabla) + ") VALUES ("
				+ valoresComoString + ");");
	}

	/*
	 * Este metodo toma la tabla y los atributos y los agrega a la tabla los
	 * primeros atributos de atributos son estrictamente claves primarias
	 * 
	 */
	public void updateDato(String nomTabla, String[] atributos, int finalPrimarias) throws SQLException {
		String valores = "", condicion = "";

		this.verificarTabla(nomTabla);

		for (int pos = 0; pos < atributos.length; pos++) {
			if (this.metadata.getColumnTypeName(pos + 1) == "VARCHAR"
					|| this.metadata.getColumnTypeName(pos + 1) == "CHAR")
				atributos[pos] = "'" + atributos[pos] + "'";

			if (pos < finalPrimarias) {
				condicion += this.metadata.getColumnName(pos + 1) + "=" + atributos[pos];
				if (pos + 1 < finalPrimarias)
					condicion += " && ";
			} else {
				valores += this.metadata.getColumnName(pos + 1) + "=" + atributos[pos];
				if (pos + 1 < atributos.length)
					valores += ", ";
			}
		}
		System.out.println("UPDATE " + nomTabla + " SET " + valores + " WHERE " + condicion);
		consulta.executeUpdate("UPDATE " + nomTabla + " SET " + valores + " WHERE " + condicion);
	}

	public String[] getDatos(String nomTabla, String condicion, int[] cualQuiero) throws SQLException {
		String[] respuesta = null;
		ResultSet rs;
		this.verificarTabla(nomTabla);
		/*
		System.out.println(
				"SELECT " + this.getAtributosComoString(nomTabla,cualQuiero) + " FROM " + nomTabla + " WHERE " + condicion + ";");
		*/
		rs = consulta.executeQuery(
				"SELECT " + this.getAtributosComoString(nomTabla,cualQuiero) + " FROM " + nomTabla + " WHERE " + condicion + ";");
		
		// toma el primer resultado y lo guarda en un array
		if(rs.next()) {
			respuesta = new String[cualQuiero.length];
			for(int x = 0;x<cualQuiero.length;x++) 
				respuesta[x] = rs.getString(x+1);
		}
		return respuesta;
	}
	public String[] getDatos(String nomTabla, String condicion, int finPrimarias) throws SQLException {
		String[] respuesta = null;
		ResultSet rs;
		this.verificarTabla(nomTabla);
		/*
		System.out.println(
				"SELECT " + this.getAtributosComoString(nomTabla) + " FROM " + nomTabla + " WHERE " + condicion + ";");
		*/
		rs = consulta.executeQuery(
				"SELECT " + this.getAtributosComoString(nomTabla) + " FROM " + nomTabla + " WHERE " + condicion + ";");
		int ite = 0;
		if(rs.next()) {
			respuesta = new String[this.metadata.getColumnCount()-2];
			for(int x = 0;x<this.metadata.getColumnCount()+1;x++){
				if(x>2) {
					respuesta[ite++] = rs.getString(x);
				}
			}
		}
	
		return respuesta;
	}
	public String[][] buscarDatos(String nomTabla, String nomColumna, String entrada, int[] cualQuiero)
			throws SQLException {
		
		ArrayList<String[]> respuesta = new ArrayList<String[]>();
		String[] aux = new String[cualQuiero.length];
		ResultSet rs;
		
		this.verificarTabla(nomTabla);
		/*
		System.out.println("SELECT " + this.getAtributosComoString(nomTabla,cualQuiero) + " FROM " + nomTabla + " WHERE "
				+ "SUBSTRING(" + nomColumna + ", 1, " + entrada.length() + ") = '" + entrada + "';");
		*/
		rs = consulta.executeQuery("SELECT " + this.getAtributosComoString(nomTabla,cualQuiero) + " FROM " + nomTabla + " WHERE "
				+ "SUBSTRING(" + nomColumna + ", 1, " + entrada.length() + ") = '" + entrada + "';");


		while (rs.next()) {
			for (int y = 0; y < cualQuiero.length; y++) {
				aux[y] = rs.getString(cualQuiero[y]);
			}
			respuesta.add(aux.clone());
		}
		 
		return this.getArray(respuesta);
	}

	private String[][] getArray(ArrayList<String[]> entrada) {
		String[][] resp = null;
		if (entrada.size() > 0) {
			resp = new String[entrada.size()][entrada.get(0).length];
			for (int x = 0; x < entrada.size(); x++)
				resp[x] = entrada.get(x);
		}
		return resp;
	}

	private void verificarTabla(String nomTabla) throws SQLException {
		ResultSet aux;
		if (this.tablaActual != nomTabla) {
			aux = consulta.executeQuery("SELECT * FROM " + nomTabla);
			this.metadata = aux.getMetaData();
			this.tablaActual = nomTabla;
		}
	}

	private String getAtributosComoString(String nomTabla, int[] cualQuiero) throws SQLException {
		
		String lista = "";
		this.verificarTabla(nomTabla);

		int ite = 0;
		for(int x = 0; x<metadata.getColumnCount()+1 && ite < cualQuiero.length; x++) {
			if(x == cualQuiero[ite]) {
				lista += metadata.getColumnName(x);
				if(ite < cualQuiero.length) {
					ite++;
				}
				if(ite < cualQuiero.length) {
					lista += ", ";
				}
			}
			
		}
		return lista;
	
	}

	private String getAtributosComoString(String nomTabla) throws SQLException {
		String lista = "";
		this.verificarTabla(nomTabla);
		for (int x = 0; x < metadata.getColumnCount(); x++) {
			lista += metadata.getColumnName(x + 1);
			if (x + 1 < metadata.getColumnCount())
				lista += ", ";
		}
		return lista;
	}
}
