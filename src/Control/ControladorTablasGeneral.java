package Control;
import java.awt.Image;
import java.io.File;
import java.sql.SQLException;

import javax.swing.ImageIcon;

public class ControladorTablasGeneral {
	public String url;
	protected boolean modoInsertarBD;
	protected ConeccionBD coneccion;
	private String nomTabla;
	
	
	public ControladorTablasGeneral(String nomTabla,ConeccionBD coneccion) {
		this.modoInsertarBD = true;
		this.nomTabla = nomTabla;
		this.coneccion = coneccion;
	}
	public boolean getModoAgregar() {
		return modoInsertarBD;
	}
	public void setModoInsertarBD(boolean modoInsertarBD) {
		this.modoInsertarBD = modoInsertarBD;
	}
	public String getNomTabla() {
		return nomTabla;
	}
	public void setNomTabla(String nomTabla) {
		this.nomTabla = nomTabla;
	}

	public void addPath(String url) {
		this.url = url;
	}
	public ImageIcon getImageIcon() throws NullPointerException{
		ImageIcon image = new ImageIcon(url);
		
		return new ImageIcon(image.getImage().getScaledInstance(450, 562, Image.SCALE_DEFAULT));
	}
	public ImageIcon getImageIcon(int x,int y) throws NullPointerException{
		ImageIcon image = new ImageIcon(url);
		
		return new ImageIcon(image.getImage().getScaledInstance(x, y, Image.SCALE_DEFAULT));
	}
	public void moveImageToURL(File origen) {
		try {
			origen.renameTo(new File(url));
		}catch(Exception err) {
		}
	}
	public void guardar(String[] strComp, int finalPrimarias) throws SQLException {
		if(modoInsertarBD) {
			this.coneccion.addDato(nomTabla, strComp);
		}else {
			this.coneccion.updateDato(nomTabla, strComp,finalPrimarias);
		}
		this.addPath("img/"+nomTabla.toLowerCase()+"/"+strComp[0].toLowerCase());
	}
	
}
