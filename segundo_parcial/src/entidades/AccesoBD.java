package entidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccesoBD {
	Connection con;
	Statement st;
	ResultSet rs;
	
	public void abrirConexion() {
		try {
			String userName = "postgres";
			String password = "sabalero";
			String url = "jdbc:postgresql://localhost/parcial?useTimezone=true&serverTimezone=UTC";
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("Conexion a la BD");
		} catch (Exception e) {
			System.out.println("Error en conexion");
			System.out.println(e.getMessage());
		}
	}

	// Para cerrar la conexión una vez terminadas las consultas
	public void cerrarConexion() {
		try {
			if (con != null) {
				con.close();
				System.out.println("Conexion cerrada");
			}
		} catch (SQLException e) {
			System.out.println("Error al cerrar conexion");
			e.printStackTrace();
		}
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Statement getSt() {
		return st;
	}

	public void setSt(Statement st) {
		this.st = st;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	
}


