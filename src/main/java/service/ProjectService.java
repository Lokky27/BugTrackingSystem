package service;

import dao.ProjectDao;
import dao.ProjectDaoImpl;
import models.Project;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectService
{
    private static Logger LOGGER = LogManager.getLogger(ProjectService.class);

    private ProjectDao projectDao;

    public ProjectService(ProjectDao projectDao)
    {
        this.projectDao = projectDao;
    }

    public Project findProjectById(Long id) throws SQLException
    {
        Project project = null;
        try
        {
            project = projectDao.getProjectById(id);
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при полученнии проекта {}", exception.getMessage());
            System.out.printf("Не удалось получить проект с ID %d: %s", id, exception.getMessage());
            exception.printStackTrace();
        }
        return project;
    }

    public List<Project> getAllProjects() throws SQLException
    {
        List<Project> projects = new ArrayList<>();
        try
        {
            projects = projectDao.getAllProjects();
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при получении списка всех проектов: {}",exception.getMessage());
            System.out.printf("Не удалось получить список всех проектов: %s", exception.getMessage());
            exception.printStackTrace();
        }
        return projects;
    }

    public void saveProject(Project project) throws SQLException
    {
        try
        {
            projectDao.saveProject(project);
            System.out.println("Проект" + project.getName() + " добавлен в систему");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при сохранении проекта: {}", exception.getMessage());
            System.out.printf("Не удалось сохранить проект %s: %s\n", project.getName(), exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void updateProject(Long projectIdToUpdate, Project newProject) throws SQLException
    {
        try
        {
            projectDao.updateProject(projectIdToUpdate, newProject);
            System.out.println("Проект обновлен!");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка при обновлении проекта: {}", exception.getMessage());
            System.out.printf("Не удалось обновить проект ID %d: %s\n", projectIdToUpdate, exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void deleteProject(Long projectIdToDelete) throws SQLException
    {
        try
        {
            projectDao.deleteProject(projectIdToDelete);
            System.out.println("Проект удален из системы!");
        }
        catch (Exception exception)
        {
            LOGGER.error("Ошибка удаления проекта: {}", exception.getMessage());
            System.out.printf("Не удалось удалить проект ID - %d: %s\n", projectIdToDelete, exception.getMessage());
            exception.printStackTrace();
        }
    }
}
