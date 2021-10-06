package service;

import dao.TaskDao;
import models.Task;

public class TaskService
{
    private TaskDao taskDao;

    public TaskService(TaskDao taskDao)
    {
        this.taskDao = taskDao;
    }

    public Task findTaskById(Long id)
    {
        return taskDao.findTaskById(id);
    }

    public void saveTask(Task task)
    {
        if (task.getProject() == null || task.getUser() == null)
        {
            throw new NullPointerException("В задаче не указан пользователь и/или проект");
        }
        else taskDao.saveTask(task);
    }

    public void updateTask(Task task)
    {
        taskDao.updateTask(task);
    }

    public void deleteTask(Task task)
    {
        taskDao.deleteTask(task);
    }
}
