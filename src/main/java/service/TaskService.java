package service;

import dao.*;
import models.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskService
{
    private static Logger LOGGER = LogManager.getLogger(TaskService.class);

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
        Task task = null;
        try
        {
            task = taskDao.findTaskById(id);

        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при получении задачи по ID {}", exception.getMessage());
            System.out.printf("Ошибка при получении задачи по ID %d: %s\n", id, exception.getMessage());
            exception.printStackTrace();
        }
        return task;
    }

    public List<Task> findAllTasks() throws SQLException
    {
        List<Task> tasks = new ArrayList<>();
        try
        {
            tasks = taskDao.findAllTasks();
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при получении всех задач: {}", exception.getMessage());
            System.out.printf("Ошибка при получении всех задач: %s\n", exception.getMessage());
            exception.printStackTrace();
        }
        return tasks;
    }

    public void saveTask(Task task, Long userId, Long projectId) throws SQLException
    {
        try
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
            System.out.println("Задача добавлена!");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при сохранении задачи: {}", exception.getMessage());
            System.out.printf("Не удалось сохранить задачу: %s\n", exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void updateTask(Long taskToUpdate, Task newTask) throws SQLException
    {
        try
        {
            taskDao.updateTask(taskToUpdate, newTask);
            System.out.println("Задача обновлена");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при обновлении задачи: {}", exception.getMessage());
            System.out.printf("Не удалось обновить задачу: %s\n", exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void deleteTask(Long taskId, Long userId, Long projectId) throws SQLException

    {
        try
        {
            taskDao.deleteTask(taskId, userId, projectId);
            System.out.println("Задача удалена");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при удалении задачи: {}", exception.getMessage());
            System.out.printf("Не удалось удалить задачу: %s\n", exception.getMessage());
            exception.printStackTrace();
        }
    }
}
