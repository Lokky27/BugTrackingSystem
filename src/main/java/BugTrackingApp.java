import dao.TaskDaoImpl;
import models.Priority;
import models.Project;
import models.Task;
import models.User;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BugTrackingApp
{
    private static TaskService service = new TaskService();
    private static UserService userService = new UserService();
    private static ProjectService projectService = new ProjectService();

    public static void main(String[] args) throws ParseException {
        Task task = createTask("Bug fixes", "Bug Tracking application", "This task about bug fixing",
                "John Dow", Priority.HIGH, "Daily");
        Task task1 = createTask("Create new feature", "Bug Tracking application",
                "This is task about creating of new features", "John Gold", Priority.MEDIUM, "New features");
        service.saveTask(task);
        service.saveTask(task1);

        User admin1 = createUser("John Dow", "Bug fixing tasks", "Bug Tracking application");
        User admin2 = createUser("Just Tommy", "Create new feature, test this feature", "New project");

        userService.saveUser(admin1);
        userService.saveUser(admin2);

        Project project = createProject("Bug Tracking Application", new SimpleDateFormat("dd.MM.yyyy").parse("31.12.2021"),
                                        "Create new feature", "Some programmer");

        Project project1 = createProject("ToDo list on Spring Boot", new SimpleDateFormat("dd.MM.yyyy").parse("30.11.2021"),
                                        "1. Start project; 2. Create plan", "John Gold");

        projectService.saveProject(project);
        projectService.saveProject(project1);
    }

    private static Task createTask(String theme, String project, String description,
                                   String user, Priority priority, String type)
    {
       Task task = new Task();
       task.setTheme(theme);
       task.setProject(project);
       task.setDescription(description);
       task.setUser(user);
       task.setPriority(priority);
       task.setType(type);
       return task;
    }

    private static User createUser(String name, String task, String project)
    {
        User admin = new User();
        admin.setName(name);
        admin.setTasks(task);
        admin.setProject(project);
        return admin;
    }

    private static Project createProject(String name, Date deadLine, String tasks, String users)
    {
        Project project = new Project();
        project.setName(name);
        project.setDeadLine(deadLine);
        project.setTasks(tasks);
        project.setUsers(users);
        return project;
    }
}
