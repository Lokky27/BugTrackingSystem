package service;

import dao.*;
import models.Task;

import java.sql.SQLException;
import java.util.List;

public class TaskService
{
    private TaskDao taskDao;
    private UserDao userDao;
    private ProjectDao projectDao;

    public TaskService(TaskDao taskDao, UserDao userDao, ProjectDao projectDao)
    {
        this.taskDao = taskDao;
        this.userDao = userDao;
        this.projectDao = projectDao;
    }

    public Task findTaskById(Long id) throws SQLException

    {
        return taskDao.findTaskById(id);
    }

    public List<Task> findAllTasks() throws SQLException
    {
        return taskDao.findAllTasks();
    }

    public void saveTask(Task task, Long userId, Long projectId) throws SQLException
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

    public void updateTask(Long taskToUpdate, Task newTask) throws SQLException

    {
        taskDao.updateTask(taskToUpdate, newTask);
        System.out.println("Задача обновлена");
    }

    public void deleteTask(Long taskId, Long userId, Long projectId) throws SQLException

    {
        taskDao.deleteTask(taskId, userId, projectId);
        System.out.println("Задача удалена");
    }
}
