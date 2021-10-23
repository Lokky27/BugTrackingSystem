package dao;

import models.Task;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class TaskDaoImpl implements TaskDao
{

    @Override
    public Task findTaskById(Long id)
    {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Task.class, id);
    }

    @Override
    public List<Task> findAllTasks()
    {
        return (List<Task>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Task").list();
    }

    @Override
    public void updateTask(Task newTask)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(newTask);
        transaction.commit();
        session.close();
    }

    @Override
    public void saveTask(Task savingTask)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(savingTask);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteTask(Task task)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(task);
        transaction.commit();
        session.close();
    }
}
