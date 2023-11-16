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
import entidades.Mesa;
import entidades.Reserva;

public class ReservaServic {
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



    public List<Reserva> buscarMesasDisponibles(String nombreApellido, int comensales, Date fecha, int restauranteId) {
        List<Reserva> mesasDisponibles = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();
        MesaServic mesaServic = new MesaServic();

        try {
            accesoBD.abrirConexion();

            List<Mesa> mesasLiberadas = mesaServic.obtenerMesasLiberadasConCapacidadPorRestaurante(comensales, restauranteId);

            String sql = "SELECT id_mesa FROM reserva WHERE fecha = ?";

            try (Connection conn = accesoBD.getCon();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, fecha);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int idMesaOcupada = rs.getInt("id_mesa");
                    mesasLiberadas.removeIf(mesa -> mesa.getId_mesa() == idMesaOcupada);
                }
            }

            for (Mesa mesa : mesasLiberadas) {
                Reserva reserva = new Reserva();
                reserva.setFecha(fecha);
                reserva.setNombreApellido(nombreApellido);
                reserva.setComensales(comensales);
                reserva.setMesa(mesaServic.obtenerMesaPorId(mesa.getId_mesa())); // 

                mesasDisponibles.add(reserva);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return mesasDisponibles;
    }

    public List<Reserva> buscarReservasPorMesaYFecha(int idMesa, Date fecha) {
        List<Reserva> reservas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM reserva WHERE id_mesa = ? AND fecha = ?";
            try (Connection conn = accesoBD.getCon();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idMesa);
                stmt.setDate(2, new java.sql.Date(fecha.getTime()));

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int idReserva = rs.getInt("id");
                    String nombreApellido = rs.getString("nombreapellido");
                    int comensales = rs.getInt("comensales");

                    Reserva reserva = new Reserva();
                    reserva.setId(idReserva);
                    reserva.setNombreApellido(nombreApellido);
                    reserva.setComensales(comensales);
                    reserva.setFecha(fecha); 

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
}
