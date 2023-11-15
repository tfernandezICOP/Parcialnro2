package servicio;

import entidades.Estado;
import entidades.Liberada;
import entidades.Ocupada;
import entidades.Reservada;

public class EstadoServic {
	public Estado obtenerEstadoPorNombre(String nombreEstado) {
	    String estadoEnMinusculas = nombreEstado.toLowerCase();
	    
	    if ("liberada".equals(estadoEnMinusculas)) {
	        return new Liberada();
	    } else if ("reservada".equals(estadoEnMinusculas)) {
	        return new Reservada();
	    } else if ("ocupada".equals(estadoEnMinusculas)) {
	        return new Ocupada();
	    } else {
	        throw new IllegalArgumentException("Nombre de estado no reconocido: " + nombreEstado);
	    }
	}
}
