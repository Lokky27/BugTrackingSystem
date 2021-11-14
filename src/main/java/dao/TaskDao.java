package dao;

import models.Task;

import java.sql.SQLException;
import java.util.List;

public interface TaskDao
{
//    Найти задачу по Id
    public Task findTaskById(Long id) throws SQLException;

//    Найти все задачи
    public List<Task> findAllTasks() throws SQLException;

//    Обновление задачи
    public void updateTask(Long updatedTaskId, Task newTask) throws SQLException;
//    Сохранить задачу
    public void saveTask(Task savingTask, Long userId, Long projectId) throws SQLException;
//    Удалить задачу
    public void deleteTask(Long taskId, Long userId, Long projectId) throws SQLException;

}
