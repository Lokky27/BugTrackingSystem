import models.Priority;
import models.Project;
import models.Task;
import models.User;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BugTrackingApp
{
    private static UserService userService = new UserService();
    private static TaskService taskService = new TaskService();
    private static ProjectService projectService = new ProjectService();

    private static Scanner scanner;

    public static void main(String[] args)
    {
        System.out.println("Система отслеживания задач/ошибок\n" +
                "Выберите одну из следующих комманд: \n" +
                "\tshow_users - показать список всех пользователей в системе\n" +
                "\tshow_projects - показать список всех проектов\n" +
                "\tadd_user - добавить нового пользователя\n" +
                "\tadd_project - добавить новый проект\n" +
                "\tdownload - выгрузить все данные системы в файл");
        scanner = new Scanner(System.in);
        for (; ;)
        {
            String command = scanner.nextLine();
            if (command.equals("show_users"))
            {
                List<User> users = userService.getAllUsers();
                for (User user : users)
                {
                    System.out.println("id: " + user.getId() + "\n\tимя: " + user.getName() +
                            "\n\tзадач у пользователя: " + user.getTasks().size());
                }
            }
            if (command.equals("show_projects"))
            {
                List<Project> projects = projectService.getAllProjects();
                for (Project project : projects)
                {
                    System.out.println("id: " + project.getId() + "\n\tназвание: " + project.getName() +
                            "\n\tсрок сдачи: " + project.getDeadLine() + "\n\tзадач на проекте: " + project.getTasks().size());
                }
            }
            if (command.equals("download"))
            {
                try {
                    System.out.println("Введите путь до файла, который хотите использовать");
                    String path = scanner.nextLine();
                    writeDataIntoFile(new File(path));
                }
                catch (IOException exception)
                {
                    exception.printStackTrace();
                }
            }
            if (command.equals("-q"))
            {
                break;
            }

        }
    }

    private static User getUser(String message)
    {
        for (; ;)
        {
            System.out.println(message);
            long userId = Long.parseLong(scanner.nextLine());
            User user = userService.findUserById(userId);
            if (user != null)
            {
                return user;
            }

            System.out.println("Такого пользователя нет в системе");
        }
    }

    private static Project getProject(String message)
    {
        for (; ;)
        {
            System.out.println(message);
            long projectId = Long.parseLong(scanner.nextLine());
            Project project = projectService.findProjectById(projectId);
            if (project != null)
            {
                return project;
            }
            System.out.println("Такого проекта нет в системе");
        }
    }

    private static void writeDataIntoFile(File file) throws IOException
    {
        FileWriter writer = new FileWriter(file);
        StringBuilder header = new StringBuilder();
        header.append("id").append(",").append("theme").append(",").append("type").append(",")
                .append("description").append(",").append("priority").append(",").append("user_id").
                append(",").append("user name").append(",").append("project_id").append(",").append("project_name").
                append(",").append("deadline").append("\n");
        writer.write(header.toString());
        List<Task> allTasks = taskService.findAllTasks();
        for (Task task : allTasks)
        {
            StringBuilder itemBuilder = new StringBuilder();
            itemBuilder.append(task.getId()).append(",").append(task.getTheme()).append(",")
                    .append(task.getType()).append(",").append(task.getDescription())
                    .append(",").append(task.getPriority()).append(",").append(task.getUser().getId())
                    .append(",").append(task.getUser().getName()).append(",").append(task.getProject().getId())
                    .append(",").append(task.getProject().getName()).append(",").append(task.getProject().getDeadLine());
            writer.write(itemBuilder.toString());
        }
        writer.flush();
        writer.close();
        System.out.println("Записано в файл " + file.getPath());
    }

}
