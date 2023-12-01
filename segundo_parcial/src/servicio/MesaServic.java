package servicio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import entidades.AccesoBD;
import entidades.Estado;
import entidades.Liberada;
import entidades.Mesa;
import entidades.Ocupada;
import entidades.Reservada;

public class MesaServic {

    EstadoServic estadoservic = new EstadoServic();
    private ReservaServic reservaServic; 

    public List<Mesa> obtenerMesasLiberadasConCapacidadPorRestaurante(int capacidad, int idRestaurante) {
        List<Mesa> mesasLiberadas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT nro_mesa, capacidad, estado FROM mesa WHERE estado ILIKE 'liberada' AND capacidad >= ? AND idresto = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, capacidad);
                stmt.setInt(2, idRestaurante);

                try (ResultSet rs = stmt.executeQuery()) {
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

   
    

    public boolean puedeReservarMesa(int idMesa) {
        Mesa mesa = obtenerMesaPorId(idMesa);
        return mesa != null && "liberada".equals(mesa.getEstado().nombreEstado());
    }

    public void reservarMesa(int idMesa) {
        if (puedeReservarMesa(idMesa)) {
            cambiarEstadoMesa(idMesa, "reservada");
        }
    }
    public boolean puedeOcuparMesa(int idMesa) {
        Mesa mesa = obtenerMesaPorId(idMesa);
        return mesa != null && ("liberada".equals(mesa.getEstado().nombreEstado()) || "reservada".equals(mesa.getEstado().nombreEstado()));
    }


    public void ocuparMesa(int idMesa) {
        if (puedeOcuparMesa(idMesa)) {
            cambiarEstadoMesa(idMesa, "ocupada");
        }
    }

    public boolean puedeLiberarMesa(int idMesa) {
        Mesa mesa = obtenerMesaPorId(idMesa);
        return mesa != null && "ocupada".equals(mesa.getEstado().nombreEstado());
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

                    stmt.setString(1, nombreEstado);
                    stmt.setInt(2, idMesa);

                    int filasAfectadas = stmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        if ("liberada".equals(nombreEstado)) {
                            preguntarConsumoYActualizar(idMesa);
                        }
                        System.out.println("Estado de la mesa actualizado correctamente.");
                    } else {
                        System.out.println("No se encontró la mesa con el ID proporcionado.");
                    }
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
    private void preguntarConsumoYActualizar(int idMesa) {
        String consumoStr = JOptionPane.showInputDialog(null, "Ingrese el consumo para la mesa " + idMesa + ":");
        try {
            if (consumoStr != null) {
                int consumo = Integer.parseInt(consumoStr);
                if (consumo >= 0) {
                    actualizarConsumoMesa(idMesa, consumo);
                } else {
                    JOptionPane.showMessageDialog(null, "El consumo debe ser un número positivo.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un valor numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarConsumoMesa(int idMesa, int nuevoConsumo) {
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "UPDATE mesa SET consumo = consumo + ? WHERE id = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, nuevoConsumo);
                stmt.setInt(2, idMesa);

                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    System.out.println("Consumo de la mesa actualizado correctamente.");
                } else {
                    System.out.println("No se encontró la mesa con el ID proporcionado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }
    }

    public List<Mesa> obtenerTodasLasMesasLiberadasPorRestaurante(int idRestaurante) {
        List<Mesa> mesasLiberadas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM mesa WHERE LOWER(estado) = 'liberada' AND idresto = ?";
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

   

   

    public List<Mesa> obtenerMesasOrdenadasPorConsumo(int idRestaurante) {
        List<Mesa> mesasOrdenadas = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM mesa WHERE idresto = ? ORDER BY consumo DESC";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idRestaurante);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int numeroMesa = rs.getInt("nro_mesa");
                    int capacidadMesa = rs.getInt("capacidad");
                    int consumoMesa = rs.getInt("consumo");
                    String estadoNombre = rs.getString("estado");

                    Estado estado = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                    Mesa mesa = new Mesa();
                    mesa.setNro_mesa(numeroMesa);
                    mesa.setCapacidad(capacidadMesa);
                    mesa.setConsumo(consumoMesa);
                    mesa.setEstado(estado);

                    mesasOrdenadas.add(mesa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return mesasOrdenadas;
    }
    public List<Mesa> obtenerMesasPorEstado(String estado, int idRestaurante) {
        List<Mesa> mesasPorEstado = new ArrayList<>();
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT * FROM mesa WHERE estado ILIKE ? AND idresto = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, estado);
                stmt.setInt(2, idRestaurante);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int numeroMesa = rs.getInt("nro_mesa");
                    int capacidadMesa = rs.getInt("capacidad");
                    int consumoMesa = rs.getInt("consumo");
                    String estadoNombre = rs.getString("estado");

                    Estado estadoMesa = estadoservic.obtenerEstadoPorNombre(estadoNombre);

                    Mesa mesa = new Mesa();
                    mesa.setNro_mesa(numeroMesa);
                    mesa.setCapacidad(capacidadMesa);
                    mesa.setConsumo(consumoMesa);
                    mesa.setEstado(estadoMesa);

                    mesasPorEstado.add(mesa);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return mesasPorEstado;
    }
   
    public List<Mesa> obtenerMesasPorEstadolambda(String estado, int idRestaurante) {
        List<Mesa> mesasPorEstado = obtenerMesasOrdenadasPorConsumo(idRestaurante);

        return mesasPorEstado.stream()
                .filter(mesa -> mesa.getEstado().nombreEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }
    
    public List<Mesa> obtenerMesasConMayorConsumoLambda(int idRestaurante, int cantidad) {
        List<Mesa> mesasConMayorConsumo = obtenerMesasOrdenadasPorConsumo(idRestaurante);

        return mesasConMayorConsumo.stream()
                .sorted(Comparator.comparingInt(Mesa::getConsumo).reversed())
                .limit(cantidad)
                .collect(Collectors.toList());
    }



    public boolean hayReservaParaMesaEnFecha(int idMesa, Date fecha) {
        AccesoBD accesoBD = new AccesoBD();

        try {
            accesoBD.abrirConexion();

            String sql = "SELECT COUNT(*) AS count FROM reserva WHERE id_mesa = ? AND DATE(fecha) = ?";
            try (Connection conn = accesoBD.getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idMesa);
                stmt.setDate(2, fecha);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }

        return false;
    }


   

    
}
