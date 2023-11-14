package entidades;

import java.util.Date;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		Resto resto = new Resto();
	    resto.inicializarMesas(2, 3, 1); 
	    resto.mostrarInfoResto(new Date()); 
	  
	    
	    resto.capacidad(new Date());
        resto.agregarmesa(15, 2, 0);
        resto.mostrarTodasLasMesas();
        resto.eliminarmesa(15);
        resto.mostrarTodasLasMesas();

        
     }
	
}

