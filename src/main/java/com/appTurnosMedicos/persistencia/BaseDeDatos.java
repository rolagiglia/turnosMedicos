/* ESTABLECIMIENTO DE CONEXION Y CREACION DEL POOL */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseDeDatos {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlserver://turnosmedicos.database.windows.net:1433;database=turnosMedicos;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        config.setUsername("administradorRodrigo@turnosmedicos");
        config.setPassword("elAdminRodri#");
        config.setMaximumPoolSize(10);
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        dataSource = new HikariDataSource(config);
    }

    public static Connection obtenerConexion() throws SQLException {
        return dataSource.getConnection();
    }
}
