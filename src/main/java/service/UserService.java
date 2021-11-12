package service;

import dao.UserDao;
import dao.UserDaoImpl;
import models.User;

import java.util.List;

public class UserService
{
    private final UserDao userDao;

    public UserService ()
    {
        this.userDao = new UserDaoImpl();
    }
    public User findUserById(Long id)
    {
        return userDao.findUserById(id);
    }

    public List<User> getAllUsers()
    {
        return userDao.getAllUsers();
    }

    public void saveUser(User user)
    {
        userDao.saveUser(user);
        System.out.println("Пользователь " + user.getName() + " добавлен в систему!");
    }

    public void updateUser(Long userIdToUpdate, User newUser)
    {
        userDao.updateUser(userIdToUpdate, newUser);
        System.out.println("Пользователь обновлен!");
    }

    public void deleteUser(Long deletedUserId)
    {
        userDao.deleteUser(deletedUserId);
        System.out.println("Пользователь удален!");
    }
}
