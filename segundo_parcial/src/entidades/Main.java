package entidades;

import java.util.Scanner;

import igu.InicializarResto;
import igu.Menu;

public class Main {
    private static int eleccion;

    private static void mostrarMenu() {
        System.out.println("--- Menu de Acciones ---");
        System.out.println("1. Mostrar todas las mesas");
        System.out.println("2. Mostrar información del restaurante");
        System.out.println("3. Realizar reserva");
        System.out.println("4. Liberar mesa");
        System.out.println("5. Dar mesa de alta");
        System.out.println("6. Dar de baja una mesa");
        System.out.println("7. Listar mesas por estado");
        System.out.println("8. Listar mesas por estado y fecha");
        System.out.println("9. Ocupar mesa");
        System.out.println("0. Salir");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        InicializarResto inicio = new InicializarResto();
        inicio.setVisible(true);
        		
        
        
        
   /*     Resto resto = new Resto(1, "Paucke ATENEO Inmaculada", "General Lopez 2545", "Santa fe");
        resto.inicializarMesasPredeterminado();

        do {
            mostrarMenu();
            System.out.print("Opcion: ");
            eleccion = sc.nextInt();

            switch (eleccion) {
                case 1:
                    resto.mostrarTodasLasMesas();
                    break;
                case 2:
                    resto.mostrarInfoPorConsola();
                    break;
                case 3:
                    resto.realizarReserva();
                    break;
                case 4:
                    resto.liberarMesa();
                    break;
                case 5:
                    resto.darDeAltaMesa();
                    break;
                case 6:
                    resto.darDeBajaMesa();
                    break;
                case 7:
                    resto.listarMesasPorEstado();
                    break;
                case 8:
                    resto.listarMesasPorEstadoYFecha();
                    break;
                case 9:
                	resto.ocuparMesa();
                	break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        } while (eleccion != 0);
    }
    */
}
}