package com.appTurnosMedicos.persistencia;

/* ESTABLECIMIENTO DE CONEXION Y CREACION DEL POOL */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class BaseDeDatos {
    private static HikariDataSource dataSource;
    private static boolean poolFallido = false;

    private static void inicializar() {
        if (dataSource == null && !poolFallido) {
            int intentos = 3;
            for (int i = 1; i <= intentos; i++) {
                try {
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl("jdbc:sqlserver://turnosmedicos.database.windows.net:1433;database=turnosMedicos;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
                    config.setUsername("administradorRodrigo@turnosmedicos");
                    config.setPassword("elAdminRodri#");
                    config.setMaximumPoolSize(10);
                    config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    dataSource = new HikariDataSource(config);
                    break;
                } catch (Exception e) {
                    if (i == intentos) {
                        poolFallido = true;
                        System.err.println("❌ No se pudo conectar luego de varios intentos.");
                    } else {
                        try {
                            Thread.sleep(1000); // Espera 1 segundos antes del siguiente intento
                        } catch (InterruptedException ignored) {}
                    }
                }
            }
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        inicializar();
        if(dataSource == null)
            throw new SQLException("No se pudo establecer conexión con la base de datos. Por favor, intente más tarde.");
        return dataSource.getConnection();
    }
    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexiones de HikariCP cerrado.");
        }
    }
}
