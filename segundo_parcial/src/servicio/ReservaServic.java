package servicio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entidades.AccesoBD;
import entidades.Estado;
import entidades.Mesa;
import entidades.Reserva;

public class ReservaServic {

    private EstadoServic estadoservic;  
    public ReservaServic(EstadoServic estadoservic) {
        this.estadoservic = estadoservic;
    }
    

    public ReservaServic() {
		super();
	}


	public List<Mesa> buscarMesasDisponibles(String nombreApellido, int comensales, Date fecha, int restauranteId) {
        List<Mesa> mesasDisponibles = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();
        EstadoServic estadoservic = new EstadoServic();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT m.id, m.nro_mesa, m.capacidad, m.estado " +
                         "FROM mesa m " +
                         "LEFT JOIN reserva r ON m.id = r.id_mesa AND r.fecha = ? " +
                         "WHERE m.idresto = ? AND (r.id IS NULL OR r.fecha IS DISTINCT FROM ?)";

            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, fecha);
                stmt.setInt(2, restauranteId);
                stmt.setDate(3, fecha);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int idMesa = rs.getInt("id");
                    int numeroMesa = rs.getInt("nro_mesa");
                    int capacidadMesa = rs.getInt("capacidad");
                    String estadoNombre = rs.getString("estado");

                    Estado estado = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                    Mesa mesa = new Mesa();
                    mesa.setId_mesa(idMesa);
                    mesa.setNro_mesa(numeroMesa);
                    mesa.setCapacidad(capacidadMesa);
                    mesa.setEstado(estado);

                    mesasDisponibles.add(mesa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return mesasDisponibles;
    }

    
    public void insertarReserva(Reserva reserva, int idMesaSeleccionada) {
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "INSERT INTO reserva (nombreapellido, comensales, fecha, id_mesa) VALUES (?, ?, ?, ?)";
            try (Connection conn = accesoBD.getCon();
                 PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, reserva.getNombreApellido());
                stmt.setInt(2, reserva.getComensales());
                stmt.setDate(3, new java.sql.Date(reserva.getFecha().getTime()));
                stmt.setInt(4, idMesaSeleccionada);

                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int idReservaGenerado = generatedKeys.getInt(1);
                        reserva.setId(idReservaGenerado);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }
    }

    public List<Reserva> buscarReservasPorMesaYFecha(int idMesa, Date fecha) {
        List<Reserva> reservas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM reserva WHERE id_mesa = ? AND fecha = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idMesa);
                stmt.setDate(2, new java.sql.Date(fecha.getTime()));

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int idReserva = rs.getInt("id");
                    String nombreApellido = rs.getString("nombreapellido");
                    int comensales = rs.getInt("comensales");
                    Date fechaBD = rs.getDate("fecha");

                    Reserva reserva = new Reserva();
                    reserva.setId(idReserva);
                    reserva.setNombreApellido(nombreApellido);
                    reserva.setComensales(comensales);
                    reserva.setFecha(fechaBD);

                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return reservas;
    }
    public List<Reserva> buscarReservasPorFecha(Date fecha) {
        List<Reserva> reservas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM reserva WHERE fecha = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, fecha);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int idReserva = rs.getInt("id");
                    String nombreApellido = rs.getString("nombreapellido");
                    int comensales = rs.getInt("comensales");
                    Date fechaBD = rs.getDate("fecha");

                    Reserva reserva = new Reserva();
                    reserva.setId(idReserva);
                    reserva.setNombreApellido(nombreApellido);
                    reserva.setComensales(comensales);
                    reserva.setFecha(fechaBD);

                    reservas.add(reserva);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return reservas;
    }
    
    public boolean hayReservaParaMesaEnFecha(int idMesa, Date fecha) {
        List<Reserva> reservas = buscarReservasPorMesaYFecha(idMesa, fecha);
        return !reservas.isEmpty();
    }

    
}
