package dao;

import models.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import utils.HibernateSessionFactoryUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoImpl implements ProjectDao
{
    private static final Logger LOGGER = LogManager.getLogger(ProjectDaoImpl.class);

    @Override
    public Project getProjectById(Long id) throws SQLException
    {
        Session session = null;
        Project project = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            project = session.get(Project.class, id);
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.println("Ошибка при пополучении проекта по ID: " + exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
        return project;
    }

    @Override
    public List<Project> getAllProjects() throws SQLException
    {
        Session session = null;
        List projects = new ArrayList<>();
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            projects = (List<Project>) session.createQuery("from Project").list();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.println("Ошибка при получении списка всех проектов: " + exception.getMessage());
        }
        finally
        {
            if (session != null && session.isOpen())
            {
                session.close();
            }
        }
        return projects;
    }

    @Override
    public void saveProject(Project project) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(project);
            session.getTransaction().commit();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.println("Ошибка при сохранении проекта: " + exception.getMessage());
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
    public void updateProject(Long projectIdToUpdate, Project newProject)
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(newProject);
            session.getTransaction().commit();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка обновления объекта с ID %d: %s", projectIdToUpdate, exception.getMessage());
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
    public void deleteProject(Long projectIdToDelete) throws SQLException
    {
        Session session = null;
        try
        {
            session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Project projectToDelete = session.get(Project.class, projectIdToDelete);
            session.delete(projectToDelete);
            session.beginTransaction().commit();
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            System.out.printf("Ошибка при удалении объекта %d: %s", projectIdToDelete, exception.getMessage());
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
