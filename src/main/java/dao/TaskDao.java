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
    public void updateTask(Task newTask);
//    Сохранить задачу
    public void saveTask(Task savingTask);
//    Удалить задачу
    public void deleteTask(Task task);

}
