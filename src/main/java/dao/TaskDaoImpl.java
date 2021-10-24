package dao;

import models.Project;
import models.Task;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class TaskDaoImpl implements TaskDao
{
    private static final Logger LOGGER = LogManager.getLogger(TaskDaoImpl.class);

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
    public void updateTask(Long updatedTaskId, Task newTask)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Task taskToUpdate = session.get(Task.class, updatedTaskId);
        taskToUpdate.setTheme(newTask.getTheme());
        taskToUpdate.setType(newTask.getType());
        taskToUpdate.setDescription(newTask.getDescription());
        taskToUpdate.setPriority(newTask.getPriority());
        taskToUpdate.setUser(newTask.getUser());
        taskToUpdate.setProject(newTask.getProject());
        session.update(newTask);
        transaction.commit();
        session.close();
    }

    @Override
    public void saveTask(Task savingTask, Long userId, Long projectId)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, userId);
        Project project = session.get(Project.class, projectId);
        savingTask.setUser(user);
        savingTask.setProject(project);
        session.save(savingTask);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteTask(Long taskId, Long userId, Long projectId)
    {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, userId);
        Project project = session.get(Project.class, projectId);
        Task task = session.get(Task.class, taskId);
        user.getTasks().remove(task);
        project.getTasks().remove(task);
        session.delete(task);
        transaction.commit();
        session.close();
    }
}
