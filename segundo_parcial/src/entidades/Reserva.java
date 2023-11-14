package entidades;

import java.util.Date;

public class Reserva {
	private int id;
    private Date fecha;
    private String nombreApellido;
    private int comensales;
    private Mesa mesa;
	
    
    public Reserva(int id, Date fecha, String nombreApellido, int comensales, Mesa mesa) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.nombreApellido = nombreApellido;
		this.comensales = comensales;
		this.mesa = mesa;
	}


	public Reserva() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getNombreApellido() {
		return nombreApellido;
	}


	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
	}


	public int getComensales() {
		return comensales;
	}


	public void setComensales(int comensales) {
		this.comensales = comensales;
	}


	public Mesa getMesa() {
		return mesa;
	}


	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}
    
    
}
