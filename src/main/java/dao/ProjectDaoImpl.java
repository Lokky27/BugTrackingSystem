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
    public void updateProject(Long projectIdToUpdate, Project newProject)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Project projectToUpdate = session.get(Project.class, projectIdToUpdate);
        projectToUpdate.setName(newProject.getName());
        projectToUpdate.setDeadLine(newProject.getDeadLine());
        projectToUpdate.setTasks(newProject.getTasks());
        session.update(projectToUpdate);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteProject(Long projectIdToDelete)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Project projectToDelete = session.get(Project.class, projectIdToDelete);
        session.delete(projectToDelete);
        transaction.commit();
        session.close();
    }
}
