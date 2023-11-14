package entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Resto {
	private int id_resto;
	private String nombre;
	private String domicilio;
	private String localidad;
	private List<Mesa> mesas;
	private List<Reserva> reservas;
	public Resto(int id_resto, String nombre, String domicilio, String localidad) {
		super();
		this.id_resto = id_resto;
		this.nombre = nombre;
		this.domicilio = domicilio;
		this.localidad = localidad;
	}



	public Resto() {
		super();
		mesas = new ArrayList<>();
		reservas = new ArrayList<>();

	
	}



	public int getId_resto() {
		return id_resto;
	}

	public void setId_resto(int id_resto) {
		this.id_resto = id_resto;
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

	        int capacidad2personas = 2;
	        int capacidad4personas = 4;
	        int capacidad6personas = 6;

	        for (int i = 1; i <= mesas2personas; i++) {
	            mesas.add(new Mesa(i, i, capacidad2personas, 0, new Liberada()));
	        }
	        for (int i = mesas2personas + 1; i <= mesas2personas + mesas4personas; i++) {
	            mesas.add(new Mesa(i, i, capacidad4personas, 0, new Liberada()));
	        }
	        for (int i = mesas2personas + mesas4personas + 1; i <= mesas2personas + mesas4personas + mesas6personas; i++) {
	            mesas.add(new Mesa(i, i, capacidad6personas, 0, new Liberada()));
	        }
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

	public List<Mesa> mesasxestado(Estado estado) {
		List<Mesa> mesasEnEstado = new ArrayList<>();
		for (Mesa mesa : mesas) {
			if (mesa.getEstado().getClass() == estado.getClass()) {
				mesasEnEstado.add(mesa);
			}
		}
		return mesasEnEstado;
	}

	public List<Mesa> mesasxestadoyfeecha(Estado estado, Date fecha) {
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
		int mesasocupadas = 0;
		int mesaslibres = 0;
		int mesasreservadas = 0;

		for (Mesa mesa : mesas) {
			capacidadTotal += mesa.getCapacidad();
			if (mesa.getEstado() instanceof Ocupada) {
				mesasocupadas += mesa.getCapacidad();
			} else if (mesa.getEstado() instanceof Reservada) {
				mesasreservadas += mesa.getCapacidad();
			} else {
				mesaslibres += mesa.getCapacidad();
			}
		}

		System.out.println("capacidad total: " + capacidadTotal);
		System.out.println(" ocupadas: " + mesasocupadas);
		System.out.println(" libres: " + mesaslibres);
		System.out.println(" reservadas: " + mesasreservadas);


	} 

	public void agregarmesa (int nroMesa, int capacidad, int consumo) {
		Mesa nuevaMesa = new Mesa(mesas.size() + 1, nroMesa, capacidad, consumo, new Liberada());
		mesas.add(nuevaMesa);
		System.out.println("nueva mesa agregada: " + nuevaMesa.toString());
	}

	public void eliminarmesa(int nroMesa) {
		Mesa mesaAEliminar = null;

		for (Mesa mesa : mesas) {
			if (mesa.getNro_mesa() == nroMesa) {
				mesaAEliminar = mesa;
				break;
			}
		}

		if (mesaAEliminar != null) {
			if (mesaAEliminar.getEstado() instanceof Liberada) {
				mesas.remove(mesaAEliminar);
				System.out.println("Mesa " + nroMesa + " eliminada correctamente.");



			}

		}
	}
	
	
		
	}
