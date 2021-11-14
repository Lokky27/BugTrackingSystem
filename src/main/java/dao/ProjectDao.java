package dao;

import models.Project;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDao
{
    public Project getProjectById(Long id) throws SQLException;

    public List<Project> getAllProjects() throws SQLException;

    public void saveProject(Project project) throws SQLException;

    public void updateProject(Long projectIdToUpdate, Project newProject) throws SQLException;

    public void deleteProject(Long projectIdToDelete) throws SQLException;
}
