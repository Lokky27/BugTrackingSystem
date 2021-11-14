package service;

import dao.UserDao;
import dao.UserDaoImpl;
import models.User;

import java.sql.SQLException;
import java.util.List;

public class UserService
{
    private UserDao userDao;

    public UserService (UserDao userDao)
    {
        this.userDao = userDao;
    }
    public User findUserById(Long id) throws SQLException
    {
        return userDao.findUserById(id);
    }

    public List<User> getAllUsers() throws SQLException
    {
        return userDao.getAllUsers();
    }

    public void saveUser(User user) throws SQLException
    {
        userDao.saveUser(user);
        System.out.println("Пользователь " + user.getName() + " добавлен в систему!");
    }

    public void updateUser(Long userIdToUpdate, User newUser) throws SQLException
    {
        userDao.updateUser(userIdToUpdate, newUser);
        System.out.println("Пользователь обновлен!");
    }

    public void deleteUser(Long deletedUserId) throws SQLException
    {
        userDao.deleteUser(deletedUserId);
        System.out.println("Пользователь удален!");
    }
}
