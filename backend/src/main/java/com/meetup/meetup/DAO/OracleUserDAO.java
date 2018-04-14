package com.meetup.meetup.DAO;

import com.meetup.meetup.rest.model.User;

public class OracleUserDAO implements UserDAO {
    public OracleUserDAO(){}
    // Следующие методы по необходимости могут использовать
    // CloudscapeDAOFactory.createConnection()
    // для получения соединения

    public int insertUser(User user) {
        // Реализовать здесь операцию добавления клиента.
        // Возвратить номер созданного клиента
        // или -1 при ошибке
        return 0;
    }

    public boolean deleteUser(User user) {
        // Реализовать здесь операцию удаления клиента.
        // Возвратить true при успешном выполнении, false при ошибке
        return false;
    }

    public User findUserByUserName(String byUserName) {
        // Реализовать здесь операцию поиска клиента по нику, используя
        // предоставленные значения аргументов в качестве критерия поиска.
        // Возвратить объект Transfer Object при успешном поиске,
        // null или ошибку, если клиент не найден.
        return null;
    }

    public User findUserByMail(String byMail){
        // Реализовать здесь операцию поиска клиента по почте, используя
        // предоставленные значения аргументов в качестве критерия поиска.
        // Возвратить объект Transfer Object при успешном поиске,
        // null или ошибку, если клиент не найден.
        return null;
    }
}
