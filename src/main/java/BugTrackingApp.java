import models.Priority;
import models.Project;
import models.Task;
import models.User;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BugTrackingApp
{
    private static UserService userService = new UserService();
    private static TaskService taskService = new TaskService();
    private static ProjectService projectService = new ProjectService();

    public static void main(String[] args) throws ParseException
    {
        Set<Task> tasks = new HashSet<>();
        List<Task> projectTasks = new ArrayList<>();

        User user = createUser("Peter Jackson");
        User user1 = createUser("James Cameron");

        Project project = createProject("Hobbit", new SimpleDateFormat("dd.MM.yyyy").parse("22.12.2018"));
        Project project1 = createProject("Termitanor 5", new SimpleDateFormat("dd.MM.yyyy").parse("31.12.2018"));

        Task task = createTask("Make movie", "Fantastic", Priority.HIGH, "Make a great movie");
        Task task1 = createTask("Assembling", "Fantasy", Priority.MEDIUM, "Make an assembling of movie");
        Task task2 = createTask("Promotion", "Fantasy", Priority.HIGH, "To promote the movie");

        tasks.add(task);
        tasks.add(task1);
        tasks.add(task2);

        task.setUser(user);
        task.setProject(project);

        task1.setUser(user1);
        task1.setProject(project1);

        task2.setUser(user);
        task2.setProject(project);

        taskService.saveTask(task);
        taskService.saveTask(task1);
        taskService.saveTask(task2);

        userService.saveUser(user);
        userService.saveUser(user1);

        projectService.saveProject(project);
        projectService.saveProject(project1);
    }

    private static User createUser(String name)
    {
        User user = new User();
        user.setName(name);
        return user;
    }

    private static Project createProject(String name, Date deadLine)
    {
        Project project = new Project();
        project.setName(name);
        project.setDeadLine(deadLine);
        return project;
    }

    private static Task createTask(String type, String theme, Priority priority, String description)
    {
        Task task = new Task();
        task.setType(type);
        task.setDescription(description);
        task.setTheme(theme);
        task.setPriority(priority);
        return task;
    }

}
