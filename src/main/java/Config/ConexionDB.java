package Config;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;


public class ConexionDB {
    private static String url = "jdbc:mysql://localhost:3306/agenda";
    private static String usuario = "root";
    private static String clave = "admin";
    private static DataSource ds;

    public static Connection conexion () throws SQLException{
        if (ds == null){
            var hikari = new HikariDataSource();
            hikari.setJdbcUrl(url);
            hikari.setUsername(usuario);
            hikari.setPassword(clave);
            ds = hikari;
        }
        return ds.getConnection();
    }
}
