package dao;

import models.User;

import java.util.List;

public interface UserDao
{
    public User findUserById(Long id);

    public List<User> getAllUsers();

    public void saveUser(User user);

    public void updateUser(Long idUserToUpdate, User newUser);

    public void deleteUser(Long deletedUserId);
}
