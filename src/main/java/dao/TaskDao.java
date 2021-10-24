package dao;

import models.Task;

import java.util.List;

public interface TaskDao
{
//    Найти задачу по Id
    public Task findTaskById(Long id);

//    Найти все задачи
    public List<Task> findAllTasks();

//    Обновление задачи
    public void updateTask(Task newTask, Long userId, Long projectId);
//    Сохранить задачу
    public void saveTask(Task savingTask, Long userId, Long projectId);
//    Удалить задачу
    public void deleteTask(Task task, Long userId, Long projectId);

}
