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
import entidades.Liberada;
import entidades.Mesa;
import entidades.Ocupada;
import entidades.Reservada;

public class MesaServic {

    EstadoServic estadoservic = new EstadoServic();

    public List<Mesa> obtenerMesasLiberadasConCapacidadPorRestaurante(int capacidad, int idRestaurante) {
        List<Mesa> mesasLiberadas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM mesa WHERE estado = 'Liberada' AND capacidad >= ? AND idresto = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, capacidad);
                stmt.setInt(2, idRestaurante);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int numeroMesa = rs.getInt("nro_mesa");
                    int capacidadMesa = rs.getInt("capacidad");
                    String estadoNombre = rs.getString("estado");

                    Estado estado = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                    Mesa mesa = new Mesa();
                    mesa.setNro_mesa(numeroMesa);
                    mesa.setCapacidad(capacidadMesa);
                    mesa.setEstado(estado);

                    mesasLiberadas.add(mesa);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return mesasLiberadas;
    }

    public Mesa obtenerMesaPorId(int idMesa) {
        Mesa mesa = null;
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM mesa WHERE id = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idMesa);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int numeroMesa = rs.getInt("nro_mesa");
                        int capacidadMesa = rs.getInt("capacidad");
                        String estadoNombre = rs.getString("estado");

                        Estado estado = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                        mesa = new Mesa();
                        mesa.setNro_mesa(numeroMesa);
                        mesa.setCapacidad(capacidadMesa);
                        mesa.setEstado(estado);
                    } else {
                        mesa = new Mesa();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return mesa;
    }

    public List<Mesa> obtenerTodasLasMesasLiberadasPorRestaurante(int idRestaurante) {
        List<Mesa> mesasLiberadas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT mesa.* FROM mesa LEFT JOIN reserva ON mesa.id = reserva.id_mesa "
                    + "WHERE mesa.estado = 'Liberada' AND mesa.idresto = ? AND reserva.id_mesa IS NULL";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idRestaurante);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int numeroMesa = rs.getInt("nro_mesa");
                    int capacidadMesa = rs.getInt("capacidad");
                    String estadoNombre = rs.getString("estado");

                    Estado estado = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                    Mesa mesa = new Mesa();
                    mesa.setNro_mesa(numeroMesa);
                    mesa.setCapacidad(capacidadMesa);
                    mesa.setEstado(estado);

                    mesasLiberadas.add(mesa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return mesasLiberadas;
    }

    public void insertarMesa(Mesa mesa) {
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "INSERT INTO mesa (capacidad, consumo, estado, idresto) VALUES (?, ?, ?, ?)";
            try (Connection conn = accesoBD.getCon();
                    PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setInt(1, mesa.getCapacidad());
                stmt.setInt(2, mesa.getConsumo());
                stmt.setString(3, mesa.getEstado().nombreEstado());
                stmt.setInt(4, mesa.getResto().getIdResto());

                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int nroMesaGenerado = generatedKeys.getInt(1);
                        mesa.setNro_mesa(nroMesaGenerado);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }
    }

    public void darDeBajaMesa(int numeroMesa) {
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "DELETE FROM mesa WHERE nro_mesa = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, numeroMesa);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }
    }

    public List<Mesa> obtenerTodasLasMesasPorRestaurante(int idRestaurante) {
        List<Mesa> todasLasMesas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM mesa WHERE idresto = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idRestaurante);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int numeroMesa = rs.getInt("nro_mesa");
                    int capacidadMesa = rs.getInt("capacidad");
                    String estadoNombre = rs.getString("estado");

                    Estado estado = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                    Mesa mesa = new Mesa();
                    mesa.setNro_mesa(numeroMesa);
                    mesa.setCapacidad(capacidadMesa);
                    mesa.setEstado(estado);

                    todasLasMesas.add(mesa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return todasLasMesas;
    }

    public static Mesa obtenerMesaPorId(Connection connection, int mesaId) {
        Mesa mesa = null;
        EstadoServic estadoServic = new EstadoServic();

        try {
            String sql = "SELECT * FROM mesa WHERE id_mesa = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, mesaId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int id_mesa = resultSet.getInt("id_mesa");
                        int nro_mesa = resultSet.getInt("nro_mesa");
                        int capacidad = resultSet.getInt("capacidad");
                        int consumo = resultSet.getInt("consumo");
                        String estadoNombre = resultSet.getString("estado");

                        Estado estado = estadoServic.obtenerEstadoPorNombre(estadoNombre);

                        mesa = new Mesa();
                        mesa.setId_mesa(id_mesa);
                        mesa.setNro_mesa(nro_mesa);
                        mesa.setCapacidad(capacidad);
                        mesa.setEstado(estado);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mesa;
    }

    public List<Mesa> obtenerMesasDisponiblesParaFecha(int idRestaurante, Date fecha) {
        List<Mesa> mesasDisponibles = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT m.* FROM mesa m " + "LEFT JOIN reserva r ON m.id = r.id_mesa AND r.fecha = ? "
                    + "WHERE m.idresto = ? AND r.id_mesa IS NULL";

            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDate(1, fecha);
                stmt.setInt(2, idRestaurante);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int numeroMesa = rs.getInt("nro_mesa");
                    int capacidadMesa = rs.getInt("capacidad");
                    String estadoNombre = rs.getString("estado");

                    Estado estado = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                    Mesa mesa = new Mesa();
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

    public boolean esIdMesaValido(int idMesa) {
        return idMesa > 0 && existeMesaConId(idMesa);
    }

    public boolean existeMesaConId(int idMesa) {
        Mesa mesa = obtenerMesaPorId(idMesa);
        return mesa != null;
    }

    public boolean puedeReservarMesa(int idMesa) {
        Mesa mesa = obtenerMesaPorId(idMesa);
        Estado estadoLiberada = estadoservic.obtenerEstadoPorNombre("liberada");

        return mesa != null && mesa.getEstado() != null && estadoLiberada.equals(mesa.getEstado());
    }

    public void reservarMesa(int idMesa) {
        if (puedeReservarMesa(idMesa)) {
            cambiarEstadoMesa(idMesa, "reservada");
        }
    }

    public boolean puedeOcuparMesa(int idMesa) {
        Mesa mesa = obtenerMesaPorId(idMesa);
        return mesa != null && mesa.getEstado().equals(estadoservic.obtenerEstadoPorNombre("reservada"));
    }

    public void ocuparMesa(int idMesa) {
        if (puedeOcuparMesa(idMesa)) {
            cambiarEstadoMesa(idMesa, "ocupada");
        }
    }

    public boolean puedeLiberarMesa(int idMesa) {
        Mesa mesa = obtenerMesaPorId(idMesa);
        return mesa != null && mesa.getEstado().equals(estadoservic.obtenerEstadoPorNombre("ocupada"));
    }

    public void liberarMesa(int idMesa) {
        if (puedeLiberarMesa(idMesa)) {
            cambiarEstadoMesa(idMesa, "liberada");
        }
    }

   

    public void cambiarEstadoMesa(int idMesa, String nombreEstado) {
        if (nombreEstado != null) {
            AccesoBD accesoBD = new AccesoBD();

            try {
                accesoBD.abrirConexion();

                String sql = "UPDATE mesa SET estado = ? WHERE id = ?";
                try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, nombreEstado.toLowerCase()); // Cambiar a minúsculas
                    stmt.setInt(2, idMesa);

                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                accesoBD.cerrarConexion();
            }
        } else {
            System.out.println("El nuevo estado es nulo. No se puede cambiar el estado de la mesa.");
        }
    }

    public void cambiarEstadoMesaPorNombre(int idMesa, String nombreEstado) {
        if (nombreEstado != null) {
            AccesoBD accesoBD = new AccesoBD();

            try {
                accesoBD.abrirConexion();

                String sql = "UPDATE mesa SET estado = ? WHERE id = ?";
                try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, nombreEstado.toLowerCase()); 
                    stmt.setInt(2, idMesa);

                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                accesoBD.cerrarConexion();
            }
        } else {
            System.out.println("El nuevo estado es nulo. No se puede cambiar el estado de la mesa.");
        }
    }

}
