package service;

import dao.UserDao;
import dao.UserDaoImpl;
import models.User;

import java.util.List;

public class UserService
{
    private UserDao userDao = new UserDaoImpl();

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
    }

    public void updateUser(User user)
    {
        userDao.updateUser(user);
    }

    public void deleteUser(User user)
    {
        userDao.deleteUser(user);
    }
}
