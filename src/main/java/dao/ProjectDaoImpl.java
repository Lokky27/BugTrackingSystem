package dao;

import models.Project;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class ProjectDaoImpl implements ProjectDao
{
    @Override
    public Project getProjectById(Long id)
    {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Project.class, id);
    }

    @Override
    public List<Project> getAllProjects()
    {
        return (List<Project>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("from Project ").list();
    }

    @Override
    public void saveProject(Project project)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(project);
        transaction.commit();
        session.close();
    }

    @Override
    public void updateProject(Project project)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(project);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteProject(Project project)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(project);
        transaction.commit();
        session.close();
    }
}
