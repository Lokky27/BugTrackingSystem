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
    }

    public void updateProject(Project project)
    {
        projectDao.updateProject(project);
    }

    public void deleteProject(Project project)
    {
        projectDao.deleteProject(project);
    }
}
