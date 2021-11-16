package service;

import dao.UserDao;
import dao.UserDaoImpl;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService
{
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private UserDao userDao;

    public UserService (UserDao userDao)
    {
        this.userDao = userDao;
    }
    public User findUserById(Long id) throws SQLException
    {
        User user = null;
        try
        {
            user = userDao.findUserById(id);
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при получении пользователя по ID - {}", exception.getMessage());
            System.out.printf("Не удалось получить пользователя по ID - %d: %s\n", id, exception.getMessage());
            exception.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() throws SQLException
    {
        List<User> users = new ArrayList<>();
        try
        {
            users = userDao.getAllUsers();
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при получении всех пользователей: {}", exception.getMessage());
            System.out.println("Не удалось получить список всех пользователей: " + exception.getMessage());
            exception.printStackTrace();
        }
        return users;
    }

    public void saveUser(User user) throws SQLException
    {
        try
        {
            userDao.saveUser(user);
            System.out.println("Пользователь " + user.getName() + " добавлен в систему!");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при сохранении пользователя: {}", exception.getMessage());
            System.out.printf("Не удалось сохранить пользователя: %s - %s\n", user.getName(), exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void updateUser(Long userIdToUpdate, User newUser) throws SQLException
    {
        try
        {
            userDao.updateUser(userIdToUpdate, newUser);
            System.out.println("Пользователь обновлен!");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при обновлении пользователя: {}", exception.getMessage());
            System.out.printf("Не удалось обновить пользователя %s: %s\n", findUserById(userIdToUpdate).getName(), exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void deleteUser(Long deletedUserId) throws SQLException
    {
        try
        {
            userDao.deleteUser(deletedUserId);
            System.out.println("Пользователь удален!");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при удалении пользователя: {}", exception.getMessage());
            System.out.printf("Не удалось удалить пользователя %s: %s\n", findUserById(deletedUserId).getName(), exception.getMessage());
            exception.printStackTrace();
        }
    }
}
