package entidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Resto {
    private int idResto;
    private String nombre;
    private String domicilio;
    private String localidad;
    private List<Mesa> mesas;
    private List<Reserva> reservas;
    Scanner sc = new Scanner(System.in);

    public Resto(int idResto, String nombre, String domicilio, String localidad) {
        super();
        this.idResto = idResto;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.localidad = localidad;
        this.mesas = new ArrayList<>();  
        this.reservas = new ArrayList<>();
    }


    public Resto() {
        super();
        mesas = new ArrayList<>();
        reservas = new ArrayList<>();
    }


    public int getIdResto() {
        return idResto;
    }

    public void setIdResto(int idResto) {
        this.idResto = idResto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public void inicializarMesas(int mesas2personas, int mesas4personas, int mesas6personas) {
        mesas = new ArrayList<>();

        int capacidad2personas = 2;
        int capacidad4personas = 4;
        int capacidad6personas = 6;

        for (int i = 1; i <= mesas2personas; i++) {
            mesas.add(new Mesa(i, i, capacidad2personas, 0, new Liberada(), this));
        }
        for (int i = mesas2personas + 1; i <= mesas2personas + mesas4personas; i++) {
            mesas.add(new Mesa(i, i, capacidad4personas, 0, new Liberada(), this));
        }
        for (int i = mesas2personas + mesas4personas + 1; i <= mesas2personas + mesas4personas + mesas6personas; i++) {
            mesas.add(new Mesa(i, i, capacidad6personas, 0, new Liberada(), this));
        }
    }

    public void inicializarMesasPredeterminado() {
        inicializarMesas(4, 4, 3);
    }

    public void mostrarTodasLasMesas() {
        for (Mesa mesa : mesas) {
            System.out.println(mesa.toString());
        }
    }

    @Override
    public String toString() {
        return "Resto [nombre=" + nombre + ", domicilio=" + domicilio + ", localidad=" + localidad + "]";
    }

    public void mostrarInfoResto(Date fecha) {
        System.out.println("Informacion del restaurante en la fecha: " + fecha);
        System.out.println(toString());
        for (Mesa mesa : mesas) {
            System.out.println(mesa.toString());
        }
        for (Reserva reserva : reservas) {
            System.out.println("Reserva - Mesa " + reserva.getMesa().getNro_mesa() +
                    ", Comensales: " + reserva.getComensales() +
                    ", Nombre: " + reserva.getNombreApellido() +
                    ", Fecha: " + reserva.getFecha());
        }
    }

    public List<Mesa> mesasPorEstado(Estado estado) {
        List<Mesa> mesasEnEstado = new ArrayList<>();
        for (Mesa mesa : mesas) {
            if (mesa.getEstado().getClass() == estado.getClass()) {
                mesasEnEstado.add(mesa);
            }
        }
        return mesasEnEstado;
    }

    public List<Mesa> mesasPorEstadoYFecha(Estado estado, Date fecha) {
        List<Mesa> mesasEnEstadoYFecha = new ArrayList<>();
        for (Reserva reserva : reservas) {
            if (reserva.getFecha().equals(fecha) && reserva.getMesa().getEstado().getClass() == estado.getClass()) {
                mesasEnEstadoYFecha.add(reserva.getMesa());
            }
        }
        return mesasEnEstadoYFecha;
    }

    public void capacidad(Date fecha) {
        int capacidadTotal = 0;
        int mesasOcupadas = 0;
        int mesasLibres = 0;
        int mesasReservadas = 0;

        for (Mesa mesa : mesas) {
            capacidadTotal += mesa.getCapacidad();
            if (mesa.getEstado() instanceof Ocupada) {
                mesasOcupadas += mesa.getCapacidad();
            } else if (mesa.getEstado() instanceof Reservada) {
                mesasReservadas += mesa.getCapacidad();
            } else {
                mesasLibres += mesa.getCapacidad();
            }
        }

        System.out.println("Capacidad total: " + capacidadTotal);
        System.out.println(" Ocupadas: " + mesasOcupadas);
        System.out.println(" Libres: " + mesasLibres);
        System.out.println(" Reservadas: " + mesasReservadas);
    }

    

    public void mostrarInfoPorConsola() {
        System.out.println("--- Mostrar Informacion del Restaurante ---");
        System.out.print("Ingrese la fecha (formato dd/MM/yyyy): ");
        String fechaStr = sc.nextLine();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = dateFormat.parse(fechaStr);
            mostrarInfoResto(fecha);
        } catch (ParseException e) {
            System.out.println("Formato de fecha no valido. Intentelo de nuevo.");
        }
    }

    public void realizarReserva() {
        System.out.println("--- Realizar Reserva ---");

        System.out.print("Ingrese la fecha de la reserva (formato dd/MM/yyyy): ");
        String fechaStr = sc.nextLine();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = dateFormat.parse(fechaStr);

            System.out.print("Ingrese su nombre y apellido: ");
            String nombreApellido = sc.nextLine();

            System.out.print("Ingrese la cantidad de comensales: ");
            int comensales = sc.nextInt();

            // Mostrar mesas disponibles para reservar
            System.out.println("Mesas disponibles para reserva:");
            List<Mesa> mesasDisponibles = mesasPorEstado(new Liberada());
            for (Mesa mesa : mesasDisponibles) {
                System.out.println(mesa.toString());
            }

            // Seleccionar una mesa para la reserva
            System.out.print("Ingrese el numero de mesa que desea reservar: ");
            int nroMesa = sc.nextInt();

            Mesa mesaReservar = null;
            for (Mesa mesa : mesasDisponibles) {
                if (mesa.getNro_mesa() == nroMesa) {
                    mesaReservar = mesa;
                    break;
                }
            }

            if (mesaReservar != null) {
                // Crear y agregar la reserva
                Reserva nuevaReserva = new Reserva(reservas.size() + 1, fecha, nombreApellido, comensales, mesaReservar);
                reservas.add(nuevaReserva);

                // Cambiar el estado de la mesa a Reservada
                mesaReservar.reservar();

                System.out.println("Reserva realizada correctamente:\n" + nuevaReserva.getClass().getSimpleName());
            } else {
                System.out.println("Numero de mesa no valido. La reserva no pudo ser realizada.");
            }

        } catch (ParseException e) {
            System.out.println("Formato de fecha no valido. Intentelo de nuevo.");
        }
        sc.nextLine();
    }

    public void mostrarMesasOcupadas() {
        System.out.println("--- Mesas Ocupadas ---");
        List<Mesa> mesasOcupadas = mesasPorEstado(new Ocupada());

        if (mesasOcupadas.isEmpty()) {
            System.out.println("No hay mesas ocupadas");
        } else {
            for (Mesa mesa : mesasOcupadas) {
                System.out.println(mesa.toString());
            }
        }
    }

    public void liberarMesa() {
        mostrarMesasOcupadas();

        if (!mesasPorEstado(new Ocupada()).isEmpty()) {
            System.out.print("Ingrese el numero de la mesa que desea liberar: ");
            int nroMesa = sc.nextInt();

            Mesa mesaALiberar = null;

            for (Mesa mesa : mesas) {
                if (mesa.getNro_mesa() == nroMesa && mesa.getEstado() instanceof Ocupada) {
                    mesaALiberar = mesa;
                    break;
                }
            }

            if (mesaALiberar != null) {
                mesaALiberar.liberar();
                System.out.println("Mesa " + nroMesa + " liberada correctamente.");
            } else {
                System.out.println("Numero de mesa no valido o la mesa no esta ocupada");
            }
        }
    }
    
    public void darDeAltaMesa() {
        System.out.println("--- Alta de Mesa ---");
        System.out.print("Ingrese el numero de la nueva mesa: ");
        int nroMesa = sc.nextInt();

        System.out.print("Ingrese la capacidad de la nueva mesa: ");
        int capacidad = sc.nextInt();

        System.out.print("Ingrese el consumo inicial de la nueva mesa: ");
        int consumo = sc.nextInt();

        Mesa nuevaMesa = new Mesa(mesas.size() + 1, nroMesa, capacidad, consumo, new Liberada(), this);
        mesas.add(nuevaMesa);

        System.out.println("Nueva mesa agregada: " + nuevaMesa.toString());
    }

    public void darDeBajaMesa() {
        System.out.println("--- Baja de Mesa ---");

      
        List<Mesa> mesasReservadas = mesasPorEstado(new Reservada());
        List<Mesa> mesasLiberadas = mesasPorEstado(new Liberada());

        if (!mesasReservadas.isEmpty() || !mesasLiberadas.isEmpty()) {
            System.out.println("Mesas reservadas:");
            for (Mesa mesa : mesasReservadas) {
                System.out.println(mesa.toString());
            }

            System.out.println("Mesas liberadas:");
            for (Mesa mesa : mesasLiberadas) {
                System.out.println(mesa.toString());
            }

            System.out.print("Ingrese el numero de la mesa que desea dar de baja: ");
            int nroMesa = sc.nextInt();
            sc.nextLine(); 

            Mesa mesaAEliminar = null;

            for (Mesa mesa : mesas) {
                if (mesa.getNro_mesa() == nroMesa) {
                    mesaAEliminar = mesa;
                    break;
                }
            }

            if (mesaAEliminar != null) {
                mesas.remove(mesaAEliminar);
                System.out.println("Mesa " + nroMesa + " dada de baja correctamente.");
            } else {
                System.out.println("Número de mesa no valido.");
            }
        } else {
            System.out.println("No hay mesas reservadas ni liberadas para dar de baja.");
        }
    }

    
    public void listarMesasPorEstado() {
        System.out.print("Ingrese el numero del estado de las mesas que desea listar (1. Liberada, 2. Reservada, 3. Ocupada): ");
        int opcion = sc.nextInt();
        sc.nextLine(); 

        Estado estado = null;

        switch (opcion) {
            case 1:
                estado = new Liberada();
                break;
            case 2:
                estado = new Reservada();
                break;
            case 3:
                estado = new Ocupada();
                break;
            default:
                System.out.println("Opción no válida");
                return;
        }

        List<Mesa> mesasEnEstado = mesasPorEstado(estado);

        if (mesasEnEstado.isEmpty()) {
            System.out.println("No hay mesas en el estado especificado.");
        } else {
            System.out.println("Mesas en estado " + estado.getClass().getSimpleName() + ":");
            for (Mesa mesa : mesasEnEstado) {
                System.out.println(mesa.toString());
            }
        }
    }
    
    public void listarMesasPorEstadoYFecha() {
        System.out.println("--- Listar Mesas por Estado y Fecha ---");

        System.out.print("Ingrese el estado de las mesas (1 = Liberada, 2 = Reservada, 3 = Ocupada): ");
        int opcionEstado = sc.nextInt();
        sc.nextLine(); 

        Estado estado = convertirNumeroAEstado(opcionEstado);

        if (estado == null) {
            System.out.println("Opcion de estado no valida.");
            return;
        }

        System.out.print("Ingrese la fecha (formato dd/MM/yyyy): ");
        String fechaStr = sc.nextLine();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = dateFormat.parse(fechaStr);

            // Obtener las mesas en el estado y fecha proporcionados
            List<Mesa> mesasEnEstadoYFecha = mesasPorEstadoYFecha(estado, fecha);

            if (mesasEnEstadoYFecha.isEmpty()) {
                System.out.println("No hay mesas en el estado especificado para la fecha dada.");
            } else {
                System.out.println("Mesas en estado " + convertirEstadoANumero(estado) + " para la fecha " + fechaStr + ":");
                for (Mesa mesa : mesasEnEstadoYFecha) {
                    System.out.println(mesa.toString());
                }
            }

        } catch (ParseException e) {
            System.out.println("Formato de fecha no valido. Intentelo de nuevo.");
        }
    }

    private Estado convertirNumeroAEstado(int numeroEstado) {
        switch (numeroEstado) {
            case 1:
                return new Liberada();
            case 2:
                return new Reservada();
            case 3:
                return new Ocupada();
            default:
                return null;
        }
    }

    private int convertirEstadoANumero(Estado estado) {
        if (estado instanceof Liberada) {
            return 1;
        } else if (estado instanceof Reservada) {
            return 2;
        } else if (estado instanceof Ocupada) {
            return 3;
        } else {
            return 0;  // Opción no válida
        }
    }
    
    public void ocuparMesa() {
        List<Mesa> mesasReservadas = mesasPorEstado(new Reservada());

        if (mesasReservadas.isEmpty()) {
            System.out.println("No hay mesas reservadas.");
        } else {
            System.out.println("--- Mesas Reservadas ---");
            for (Mesa mesa : mesasReservadas) {
                System.out.println(mesa.toString());
            }

            System.out.print("Ingrese el numero de mesa que desea ocupar: ");
            int nroMesa = sc.nextInt();
            sc.nextLine(); 

            Mesa mesaAOcupar = null;

            for (Mesa mesa : mesasReservadas) {
                if (mesa.getNro_mesa() == nroMesa) {
                    mesaAOcupar = mesa;
                    break;
                }
            }

            if (mesaAOcupar != null) {
                mesaAOcupar.ocupar();
                System.out.println("Mesa " + nroMesa + " ocupada correctamente.");
            } else {
                System.out.println("Numero de mesa no valido o la mesa no esta reservada.");
            }
        }
    }
    
    public String nombre() {
        return nombre; 
    }
}
