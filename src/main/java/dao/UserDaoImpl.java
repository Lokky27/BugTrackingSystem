package dao;

import models.Project;
import models.Task;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class UserDaoImpl implements UserDao
{

    @Override
    public User findUserById(Long id)
    {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(User.class, id);
    }

    @Override
    public List<User> getAllUsers()
    {
        return (List<User>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from User ").list();
    }

    @Override
    public void saveUser(User user)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateUser(Long idUserToUpdate, User newUser)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User userToUpdate = session.get(User.class, idUserToUpdate);
        userToUpdate.setName(newUser.getName());
        userToUpdate.setTasks(newUser.getTasks());
        session.update(userToUpdate);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteUser(Long deletedUserId)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User deletedUser = session.get(User.class, deletedUserId);
        List<Task> tasks = deletedUser.getTasks();
        deletedUser.getTasks().removeAll(tasks);
        session.delete(deletedUser);
        transaction.commit();
        session.close();
    }
}
