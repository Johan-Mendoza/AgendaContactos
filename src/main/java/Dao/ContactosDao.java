package Dao;

import Config.ConexionDB;
import Models.Contactos;
import com.mysql.cj.protocol.Resultset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactosDao {

    public void crearContacto(Contactos contactos){
        String sql = "INSERT INTO contactos (nombre, apellido, telefono, email) VALUES (?, ?, ?, ?)";

        try(Connection con = ConexionDB.conexion(); PreparedStatement stmt = con.prepareStatement(sql)){
            con.setAutoCommit(false);

            stmt.setString(1, contactos.getNombre());
            stmt.setString(2, contactos.getApellido());
            stmt.setString(3, contactos.getTelefono());
            stmt.setString(4, contactos.getEmail());

            var insert = stmt.executeUpdate();
            System.out.println(insert);

            con.commit();
        }catch (SQLException e){
            System.out.println("Error al crear contactos "+e.getMessage());
        }
    }

    public List<Contactos> verContacto(){
        List<Contactos> contactos = new ArrayList<>();
        String sql = "SELECT * FROM contactos";

        try(Connection con = ConexionDB.conexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                Contactos Contactos = new Contactos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                contactos.add(Contactos);
            }

        }catch (SQLException e){
            System.out.println("Error al obtener los contactos "+e.getMessage());
        }
        return contactos;
    }

    public void modificarContacto (Contactos contactos){
        String sql = "UPDATE contactos SET nombre = ?, apellido = ?, telefono = ?, email = ? WHERE id = ?";

        try(Connection con = ConexionDB.conexion();
            PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, contactos.getNombre());
            pstmt.setString(2, contactos.getApellido());
            pstmt.setString(3, contactos.getTelefono());
            pstmt.setString(4, contactos.getEmail());
            pstmt.setInt(5, contactos.getId());

            var modificar = pstmt.executeUpdate();
            System.out.println(modificar);

        }catch (SQLException e){
            System.out.println("Error al actualizar "+e.getMessage());
        }
    }

    public void eliminarContacto (Contactos contactos){
        String sql = "DELETE FROM contactos WHERE id = ?";

        try(Connection con = ConexionDB.conexion();
            PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setInt(1,contactos.getId());

            var eliminar = pstmt.executeUpdate();
            System.out.println(eliminar);

        }catch (SQLException e){
            System.out.println("Error al eliminar: "+e.getMessage());
        }

    }

}
