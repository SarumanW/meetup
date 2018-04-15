package com.meetup.meetup.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

    public class OracleDAOFactory {
        public static final String DRIVER =
                "COM.cloudscape.core.RmiJdbcDriver";
        private static final String DBURL =
                "jdbc:cloudscape:rmi://localhost:1099/CoreJ2EEDB";
        private static final String DBUSERNAME = "";
        private static final String DBPASS = "";

        // метод для создания соединений к Cloudscape
        public static Connection createConnection() {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(DBURL, DBUSERNAME, DBPASS);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
            // Использовать DRIVER и DBURL для создания соединения
            // Рекомендовать реализацию/использование пула соединений
        }

        public UserDAO getOrderDAO() {
            return new OracleUserDAO();
        }

    }

