package dao;

import models.Project;

import java.util.List;

public interface ProjectDao
{
    public Project getProjectById(Long id);

    public List<Project> getAllProjects();

    public void saveProject(Project project);

    public void updateProject(Long projectIdToUpdate, Project newProject);

    public void deleteProject(Long projectIdToDelete);
}
