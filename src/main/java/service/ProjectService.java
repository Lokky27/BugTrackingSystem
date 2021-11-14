package service;

import dao.ProjectDao;
import dao.ProjectDaoImpl;
import models.Project;

import java.sql.SQLException;
import java.util.List;

public class ProjectService
{
    private ProjectDao projectDao;

    public ProjectService(ProjectDao projectDao)
    {
        this.projectDao = projectDao;
    }

    public Project findProjectById(Long id) throws SQLException
    {
        return projectDao.getProjectById(id);
    }

    public List<Project> getAllProjects() throws SQLException
    {
        return projectDao.getAllProjects();
    }

    public void saveProject(Project project) throws SQLException
    {
        projectDao.saveProject(project);
        System.out.println("Проект" + project.getName() + " добавлен в систему");
    }

    public void updateProject(Long projectIdToUpdate, Project newProject) throws SQLException
    {
        projectDao.updateProject(projectIdToUpdate, newProject);
        System.out.println("Проект обновлен!");
    }

    public void deleteProject(Long projectIdToDelete) throws SQLException
    {
        projectDao.deleteProject(projectIdToDelete);
        System.out.println("Проект удален из системы!");
    }
}
