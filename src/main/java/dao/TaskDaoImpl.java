package dao;

import models.Project;
import models.Task;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDaoImpl implements TaskDao
{
    private static final Logger LOGGER = LogManager.getLogger(TaskDaoImpl.class);

    @Override
    public Task findTaskById(Long id) throws SQLException
    {
        Session session = null;
        Task task = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            task = session.get(Task.class, id);

        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при поиске задачи с ID %d: %s", id, exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }

        }
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Task.class, id);
    }

    @Override
    public List<Task> findAllTasks() throws SQLException
    {
        Session session = null;
        List<Task> tasks = new ArrayList<>();
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            tasks = (List<Task>) session.createQuery("From Task").list();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при получении всех задач: %s", exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
        return tasks;
    }

    @Override
    public void saveTask(Task savingTask, Long userId, Long projectId) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            User user = session.get(User.class, userId);
            Project project = session.get(Project.class, projectId);
            savingTask.setUser(user);
            savingTask.setProject(project);
            session.save(savingTask);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка добавления задчи ID %d: %s", savingTask.getId(), exception.getMessage());
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
    public void updateTask(Long updatedTaskId, Task newTask) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(newTask);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка обновления задачи с ID %d: %s",updatedTaskId, exception.getMessage());
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
    public void deleteTask(Long taskId, Long userId, Long projectId) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("delete from Task where user_id = :userId and project_id = :projectId");
            query.setParameter("userId", userId);
            query.setParameter("projectId", projectId);
            session.beginTransaction().commit();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при удалении задачи ID %d: %s", taskId, exception.getMessage());
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
