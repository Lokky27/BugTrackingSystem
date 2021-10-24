package service;

import dao.ProjectDao;
import dao.ProjectDaoImpl;
import models.Project;

import java.util.List;

public class ProjectService
{
    private ProjectDao projectDao = new ProjectDaoImpl();

    public Project findProjectById(Long id)
    {
        return projectDao.getProjectById(id);
    }

    public List<Project> getAllProjects()
    {
        return projectDao.getAllProjects();
    }

    public void saveProject(Project project)
    {
        projectDao.saveProject(project);
        System.out.println("Проект" + project.getName() + " добавлен в систему");
    }

    public void updateProject(Long projectIdToUpdate, Project newProject)
    {
        projectDao.updateProject(projectIdToUpdate, newProject);
        System.out.println("Проект обновлен!");
    }

    public void deleteProject(Long projectIdToDelete)
    {
        projectDao.deleteProject(projectIdToDelete);
        System.out.println("Проект удален из системы!");
    }
}
