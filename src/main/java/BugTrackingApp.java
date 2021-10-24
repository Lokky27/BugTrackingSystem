import models.Priority;
import models.Project;
import models.Task;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BugTrackingApp
{
    private static final Logger LOGGER = LogManager.getLogger(BugTrackingApp.class);

    private static final Marker COMMAND_HISTORY_MARKER = MarkerManager.getMarker("INPUT_HISTORY");
    private static final Marker INVALID_QUERIES_MARKER = MarkerManager.getMarker("INVALID_QUERIES");

    private static UserService userService = new UserService();
    private static TaskService taskService = new TaskService();
    private static ProjectService projectService = new ProjectService();

    private static Scanner scanner;

    public static void main(String[] args)
    {
        System.out.println("Система отслеживания задач/ошибок");

        scanner = new Scanner(System.in);
        try
        {
            for (; ; )
            {
                System.out.println("Выберите одну из следующих команд: \n" +
                        "\tuser - работа с пользователями\n" +
                        "\tproject - работа с проектами\n" +
                        "\ttask - работа с задачами\n" +
                        "\tunload - выгрузить все данные системы в файл\n" +
                        "\tquit - завершить работу программой");

                System.out.print("Введите команду: ");
                String command = scanner.nextLine();
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл команду {}", command);

                if (command.equals("user")) {
                    System.out.println("Вы выбрали работу с пользователями");
                    System.out.println("Здесь доступны следующие команды: " +
                            "\n\tget - выбрать пользователя по ID;" +
                            "\n\tlist - показать список всех пользователей;" +
                            "\n\ttasks - просмотр задач пользователя;" +
                            "\n\tadd - добавить нового пользователя;" +
                            "\n\tupdate - обновить данные пользователя (полностью);" +
                            "\n\tdelete - удалить пользователя.");
                    System.out.print("Введите команду: ");
                    String userCommand = scanner.nextLine();
                    LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел команду {}", userCommand);
                    if (userCommand.equals("get"))
                    {
                        User user = getUser("Введите ID пользователя");
                        System.out.println(
                                "\tID: " + user.getId() +
                                        "\n\tимя пользователя: " + user.getName() +
                                        "\n\tзадач у пользователя: " + user.getTasks().size());
                        continue;
                    }

                    if (userCommand.equals("list"))
                    {
                        userService.getAllUsers().forEach(user -> {
                            System.out.println("\tID: " + user.getId() +
                                    "\n\tимя пользователя: " + user.getName() +
                                    "\n\tзадач у пользователя: " + user.getTasks().size());
                        });
                        continue;
                    }

                    if (userCommand.equals("add"))
                    {
                        User user = createUser("Введите имя нового пользователя");
                        userService.saveUser(user);
                        continue;
                    }

                    if (userCommand.equals("tasks"))
                    {
                        User user = getUser("Введите ID пользователя");
                        user.getTasks().forEach(task -> {
                            System.out.println("\tID: " + task.getId() +
                                    "\n\tтип: " + task.getType() +
                                    "\n\tтема: " + task.getTheme() +
                                    "\n\tописание: " + task.getDescription() +
                                    "\n\tприоритет: " + task.getPriority() +
                                    "\n\tпользователь: " + task.getUser().getName() +
                                    "\n\tпроект: " + task.getProject().getName());
                        });
                        continue;
                    }

                    if (userCommand.equals("update"))
                    {
                        System.out.println("Введите ID пользователя для изменения");
                        long userIdToUpdate = Long.parseLong(scanner.nextLine().trim());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл ID: {}", userIdToUpdate);
                        User newUser = createUser("Введите имя нового пользователя");
                        userService.updateUser(userIdToUpdate, newUser);
                        continue;
                    }

                    if (userCommand.equals("delete"))
                    {
                        System.out.print("Введите ID исполнителя, которого нужно удалить ");
                        long idUserToDelete = Long.parseLong(scanner.nextLine().trim());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл ID пользователя {}", idUserToDelete);
                        userService.deleteUser(idUserToDelete);
                        continue;
                    }

                    if (userCommand.equals("back")) continue;
                }
                if (command.equals("project")) {
                    System.out.println("Вы выбрали работу с проектами");
                    System.out.println("Здесь доступны следующие команды: " +
                            "\n\tget - выбрать проект по ID;" +
                            "\n\tadd - добавить новый проект;" +
                            "\n\tadd-task - добавить задачу в проект;" +
                            "\n\tlist - показать все проекты;" +
                            "\n\ttasks - получить список всех задач на проекте" +
                            "\n\tupdate - обновить данные проекта (полностью);" +
                            "\n\tdelete - удалить проект;" +
                            "\n\tback - назад");

                    System.out.print("Введите команду: ");
                    String projectCommand = scanner.nextLine();
                    LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел команду {}", projectCommand);

                    if (projectCommand.equals("get"))
                    {
                        Project project = getProject("Введите ID проекта");
                        System.out.println("\tID: " + project.getId() +
                                "\n\tНазвание: " + project.getName() +
                                "\n\tСрок сдачи: " + project.getDeadLine() +
                                "\n\tЗадач в проекте: " + project.getTasks().size()
                        );
                        continue;
                    }

                    if (projectCommand.equals("add-task"))
                    {
                        Project project = getProject("Введите ID проекта");
                        User user = getUser("Задайте ID исполнителя");
                        Task task = createTask();
                        taskService.saveTask(task, project.getId(), user.getId());
                        continue;
                    }

                    if (projectCommand.equals("tasks"))
                    {
                        Project project = getProject("Введите ID проекта");
                        project.getTasks().forEach(task -> {
                            System.out.println("\tID: " + task.getId() +
                                    "\n\ttype: " + task.getType() +
                                    "\n\ttheme: " + task.getTheme() +
                                    "\n\tdescription: " + task.getDescription() +
                                    "\n\tpriority: " + task.getPriority() +
                                    "\n\tuser: " + task.getUser().getName() +
                                    "\n\tproject: " + task.getProject().getName());
                        });
                        continue;
                    }

                    if (projectCommand.equals("list"))
                    {
                        projectService.getAllProjects().forEach(project -> {
                            System.out.println("\tID: " + project.getId() +
                                    "\n\tНазвание: " + project.getName() +
                                    "\n\tСрок сдачи: " + project.getDeadLine() +
                                    "\n\tЗадач в проекте: " + project.getTasks().size()
                            );
                        });
                        continue;
                    }
                    if (projectCommand.equals("add"))
                    {
                        Project project = createProject();
                        projectService.saveProject(project);
                        continue;
                    }

                    if (projectCommand.equals("update"))
                    {
                        System.out.print("Введите ID проекта который нужно изменить: ");
                        long projectIdToUpdate = Long.parseLong(scanner.nextLine().trim());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {} ", projectIdToUpdate);
                        Project newProject = createProject();
                        projectService.updateProject(projectIdToUpdate, newProject);
                        continue;
                    }

                    if (projectCommand.equals("delete"))
                    {
                        System.out.print("Введите ID проекта, который нужно удалить: ");
                        long projectIdToDelete = Long.parseLong(scanner.nextLine().trim());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл ID {} ", projectIdToDelete);
                        projectService.deleteProject(projectIdToDelete);
                        continue;
                    }

                    if (projectCommand.equals("back")) continue;
                }

                if (command.equals("task"))
                {
                    System.out.println("Вы выбрали работу с задачами");
                    System.out.println("Здесь доступны следующие команды: " +
                            "\n\tget - выбрать задачу по ID;" +
                            "\n\tadd - добавить новую задачу;" +
                            "\n\tupdate - обновить задачу (полностью);" +
                            "\n\tdelete - удалить задачу;" +
                            "\n\tback - назад");

                    System.out.print("Введите команду: ");
                    String taskCommand = scanner.nextLine();
                    LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел команду {}", taskCommand);
                    if (taskCommand.equals("get"))
                    {
                        Task task = getTask("Введите ID задачи");
                        System.out.println("\tID: " + task.getId() +
                                "\n\ttype: " + task.getType() +
                                "\n\ttheme: " + task.getTheme() +
                                "\n\tdescription: " + task.getDescription() +
                                "\n\tpriority: " + task.getPriority() +
                                "\n\tuser: " + task.getUser().getName());
                        continue;
                    }

                    if (taskCommand.equals("add"))
                    {
                        System.out.println("Добавляем задачу");
                        System.out.print("Введите ID проекта ");
                        Long projectId = Long.parseLong(scanner.nextLine());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
                        System.out.print("Введите ID исполнителя ");
                        Long userId = Long.parseLong(scanner.nextLine());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
                        Task task = createTask();
                        taskService.saveTask(task, projectId, userId);
                        continue;
                    }

                    if (taskCommand.equals("update"))
                    {
                        Task newTask = createTask();
                        System.out.print("Введите ID задачи, которую нужно изменить: ");
                        long taskToUpdateLong = Long.parseLong(scanner.nextLine());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел ID {} ", taskToUpdateLong);
                        taskService.updateTask(taskToUpdateLong, newTask);
                        continue;
                    }

                    if (taskCommand.equals("delete"))
                    {
                        System.out.print("Введите ID задачи которую нужно удалить: ");
                        long deleteTaskId = Long.parseLong(scanner.nextLine());
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел ID {} ", deleteTaskId);
                        taskService.deleteTask(
                                deleteTaskId,
                                taskService.findTaskById(deleteTaskId).getUser().getId(),
                                taskService.findTaskById(deleteTaskId).getProject().getId()
                        );
                        continue;
                    }

                    if (taskCommand.equals("back")) continue;

                }

                if (command.equals("unload"))
                {
                    try
                    {
                        System.out.println("Введите путь до файла, который хотите использовать");
                        String path = scanner.nextLine();
                        writeDataIntoFile(new File(path));
                        continue;
                    } catch (IOException exception) {
                        LOGGER.error(exception.getStackTrace());
                    }
                }

                if (command.equals("quit"))
                {
                    break;
                }
                else
                {
                    LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь ввел команду {}", INVALID_QUERIES_MARKER);
                    System.out.println("Неизвестная команда!");
                }
            }
        }
        catch (Exception exception)
        {
            LOGGER.error(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static Task getTask(String message)
    {
        for (; ; )
        {
            System.out.print(message + " ");
            long seekTask = Long.parseLong(scanner.nextLine());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            if (taskService.findTaskById(seekTask) != null)
            {
                return taskService.findTaskById(seekTask);
            }
        }
    }

    private static User getUser(String message)
    {
        for (; ; )
        {
            System.out.println(message);
            long userId = Long.parseLong(scanner.nextLine().trim());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            User user = userService.findUserById(userId);
            if (user != null)
            {
                return user;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь {} не найден", INVALID_QUERIES_MARKER);
            System.out.println("Такого пользователя нет в системе");
        }
    }

    private static Project getProject (String message)
    {
        for (; ; )
        {
            System.out.println(message);
            long projectId = Long.parseLong(scanner.nextLine().trim());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            Project project = projectService.findProjectById(projectId);
            if (project != null)
            {
                return project;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Проект {} не найден", INVALID_QUERIES_MARKER);
            System.out.println("Такого проекта нет в системе");
        }
    }

    private static User createUser (String message)
    {
        for (; ; )
        {
            System.out.print(message);
            User user = new User();

            String userName = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            user.setName(userName);
            return user;
        }
    }

    private static Project createProject ()
    {
        for (; ; )
        {
            Project newProject = new Project();
            System.out.print("Введите название нового проекта: ");
            String name = scanner.nextLine().trim();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.print("Введите срок сдачи проекта в формате дд.ММ.гггг: ");
            try {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(scanner.nextLine());
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
                newProject.setName(name);
                newProject.setDeadLine(date);
                return newProject;
            }
            catch (ParseException e)
            {
                System.out.println(e.getMessage());
                LOGGER.error(e);
            }
        }
    }

    private static Task createTask()
    {
        for (; ; )
        {
            Task task = new Task();
            System.out.print("Введите тему задачи: ");
            String theme = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.print("Введите тип задачи: ");
            String type = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.print("Введите описание задачи: ");
            String description = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.print("Задайте приоритет LOW, MEDIUM, HIGH: ");
            Priority priority = Priority.valueOf(scanner.nextLine());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            task.setTheme(theme);
            task.setType(type);
            task.setDescription(description);
            task.setPriority(priority);
            return task;
        }
    }

    private static void writeDataIntoFile(File userFile) throws IOException
    {
        FileWriter writer = new FileWriter(userFile);
        StringBuilder header = new StringBuilder();
        header.append("id").append(",").append("theme").append(",").append("type").append(",")
                .append("description").append(",").append("priority").append(",").append("user_id").
                append(",").append("user name").append(",").append("project_id").append(",").append("project_name").
                append(",").append("deadline").append("\n");
        writer.write(header.toString());
        List<Task> allTasks = taskService.findAllTasks();
        for (Task task : allTasks) {
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
        System.out.println("Записано в файл " + userFile.getPath());
    }
}
