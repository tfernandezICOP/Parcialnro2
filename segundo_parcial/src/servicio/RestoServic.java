package servicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entidades.AccesoBD;
import entidades.Resto;

public class RestoServic {
    private AccesoBD accesoBD;

    public RestoServic() {
        accesoBD = new AccesoBD();
    }

    public List<Resto> obtenerTodosLosRestaurantes() {
        List<Resto> restaurantes = new ArrayList<>();
        try {
            accesoBD.abrirConexion();
            Connection con = accesoBD.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM public.resto");

            while (rs.next()) {
                int idResto = rs.getInt("idresto");
                String nombre = rs.getString("nombre");
                String domicilio = rs.getString("domicilio");
                String localidad = rs.getString("localidad");

                Resto resto = new Resto(idResto, nombre, domicilio, localidad);
                restaurantes.add(resto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            accesoBD.cerrarConexion();
        }
        return restaurantes;
    }
}
