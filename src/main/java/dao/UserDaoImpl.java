package dao;

import models.Task;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import utils.HibernateSessionFactoryUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao
{
    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);

    @Override
    public User findUserById(Long id) throws SQLException
    {
        Session session = null;
        User user = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            user = session.get(User.class, id);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при получении пользователя с ID %d: %s", id, exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() throws SQLException
    {
        Session session = null;
        List<User> users = new ArrayList<>();
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            users = (List<User>) session.createQuery("from User").list();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при получении всех пользователей: %s", exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
        return users;
    }

    @Override
    public void saveUser(User user) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при добавлении пользователя %s: %s", user.getName(), exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
    }

    @Override
    public void updateUser(Long idUserToUpdate, User newUser) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(newUser);
            session.getTransaction().commit();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при обновлении пользователя с ID %d: %s", idUserToUpdate, exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
    }

    @Override
    public void deleteUser(Long deletedUserId) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            User userToDelete = session.get(User.class, deletedUserId);
            session.delete(userToDelete);
            session.beginTransaction().commit();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при удалении пользователя с ID %d: %s", deletedUserId, exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }

    }
}