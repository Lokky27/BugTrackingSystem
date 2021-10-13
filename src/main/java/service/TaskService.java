package service;

import dao.*;
import models.Project;
import models.Task;

import java.util.List;

public class TaskService
{
    private TaskDao taskDao = new TaskDaoImpl();
    private UserDao userDao = new UserDaoImpl();
    private ProjectDao projectDao = new ProjectDaoImpl();

    public Task findTaskById(Long id)

    {
        return taskDao.findTaskById(id);
    }

    public List<Task> findAllTasks()
    {
        return taskDao.findAllTasks();
    }

    public void saveTask(Task task)
    {
        if (task.getUser() == null || userDao.findUserById(task.getUser().getId()) == null)
        {
            throw new NullPointerException("Невозможно сохранить. Задача не назначена пользователю");
        }
        if (task.getProject() == null || projectDao.getProjectById(task.getProject().getId()) == null)
        {
            throw new NullPointerException("Невозможно сохранить. Задача ссылается на несуществующий прокет");
        }
        taskDao.saveTask(task);
        System.out.println("Задача добавлена!");
    }

    public void updateTask(Task task)

    {
        taskDao.updateTask(task);
        System.out.println("Задача обновлена");
    }

    public void deleteTask(Task task)

    {
        taskDao.deleteTask(task);
        System.out.println("Задача удалена");
    }
}
