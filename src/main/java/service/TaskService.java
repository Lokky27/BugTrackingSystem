package service;

import dao.*;
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

    public void saveTask(Task task, Long userId, Long projectId)
    {
        if (userDao.findUserById(userId) == null)
        {
            throw new NullPointerException("Невозможно сохранить. Задача не назначена пользователю");
        }
        if (projectDao.getProjectById(projectId) == null)
        {
            throw new NullPointerException("Невозможно сохранить. Задача ссылается на несуществующий прокет");
        }
        taskDao.saveTask(task, userId, projectId);
        userDao.findUserById(userId).getTasks().add(task);
        projectDao.getProjectById(projectId).getTasks().add(task);
        System.out.println("Задача добавлена!");
    }

    public void updateTask(Task task, Long userId, Long projectId)

    {
        taskDao.updateTask(task, userId, projectId);
        System.out.println("Задача обновлена");
    }

    public void deleteTask(Task task, Long userId, Long projectId)

    {
        taskDao.deleteTask(task, userId, projectId);
        System.out.println("Задача удалена");
    }
}
