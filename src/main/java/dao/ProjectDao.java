package dao;

import models.Project;

import java.util.List;

public interface ProjectDao
{
    public Project getProjectById(Long id);

    public List<Project> getAllProjects();

    public void saveProject(Project project);

    public void updateProject(Project project);

    public void deleteProject(Project project);
}
