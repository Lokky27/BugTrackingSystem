package dao;

import models.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao
{
    public User findUserById(Long id) throws SQLException;

    public List<User> getAllUsers() throws SQLException;

    public void saveUser(User user) throws SQLException;

    public void updateUser(Long idUserToUpdate, User newUser) throws SQLException;

    public void deleteUser(Long deletedUserId) throws SQLException;
}
