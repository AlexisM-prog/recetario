package Control;

public class ControlTablasPasos {
	private String tablaAnterior, tablaActual, tablaSiguiente;
	public ControlTablasPasos() {
		this.reset();
	}
	public void setTablaAnterior(String tablaAnterior) {
		this.tablaAnterior = tablaAnterior;
	}
	public void setTablaActual(String tablaActual) {
		this.tablaActual = tablaActual;
	}
	public void setTablaSiguiente(String tablaSiguiente) {
		this.tablaSiguiente = tablaSiguiente;
	}
	public String getTablaAnterior() {
		return this.tablaAnterior;
	}
	public String getTablaActual() {
		return this.tablaActual;
	}
	public String getTablaSiguiente() {
		return this.tablaSiguiente;
	}
	public void reset() {
		this.tablaAnterior = new String();
		this.tablaActual = new String();
		this.tablaSiguiente = new String();
	}
	public void mover(boolean esParaAdelante) {
		if(esParaAdelante) {
			this.tablaAnterior = this.tablaActual;
			this.tablaActual = this.tablaSiguiente;
			this.tablaSiguiente =  new String();
		}else {
			this.tablaSiguiente = this.tablaActual;
			this.tablaActual = this.tablaAnterior;
			this.tablaAnterior = new String();
		}
	}

}
