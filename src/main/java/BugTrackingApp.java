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
    private static Logger LOGGER = LogManager.getLogger(BugTrackingApp.class);

    private static final Marker COMMAND_HISTORY_MARKER = MarkerManager.getMarker("INPUT_HISTORY");
    private static final Marker INVALID_QUERIES_MARKER = MarkerManager.getMarker("INVALID_QUERIES");
    private static UserService userService = new UserService();
    private static TaskService taskService = new TaskService();
    private static ProjectService projectService = new ProjectService();

    private static Scanner scanner;

    public static void main(String[] args)
    {
        System.out.println("Система отслеживания задач/ошибок\n" +
                "Выберите одну из следующих комманд: \n" +
                "\t-list users - показать список всех пользователей в системе\n" +
                "\t-list projects - показать список всех проектов\n" +
                "\t-get user - выбрать пользователя\n" +
                "\t-get project - выбрать проект\n" +
                "\t-a user - добавить нового пользователя\n" +
                "\t-a project - добавить новый проект\n" +
                "\t-a task - добавить новую задачу\n" +
                "\tunload - выгрузить все данные системы в файл");

        scanner = new Scanner(System.in);
        for (; ;)
        {
            System.out.print("Введите команду: ");
            String command = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл команду {}", command);
            if (command.equals("-list users"))
            {
                userService.getAllUsers().forEach(user -> {
                    System.out.println("\tid: " + user.getId() + "\n\tимя: " + user.getName() +
                            "\n\tзадач у пользователя: " + user.getTasks().size());
                });
                continue;
            }

            if (command.equals("-list projects"))
            {
                projectService.getAllProjects().forEach(project -> {
                    System.out.println("\tid: " + project.getId() + "\n\tназвание: " + project.getName() +
                            "\n\tсрок сдачи: " + project.getDeadLine() + "\n\tзадач на проекте: " + project.getTasks().size());

                });

                continue;
            }

            if (command.equals("-get user"))
            {
                String userCommand = scanner.nextLine();
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел команду {}", userCommand);
                User user = getUser("Введите ID пользователя");
                System.out.println("Введите одну из следующих команд: \n" +
                        "\t-update - изменить пользователя\n" +
                        "\t-delete - удалить пользователя из базы\n" +
                        "\t-list tasks - получить список задач пользователя\n" +
                        "\t-get task - получить задачу по ID\n +" +
                        "\t-back - назад");
                if (userCommand.equals("-update"))
                {
                    userService.updateUser(user);
                }

                if (userCommand.equals("-delete"))
                {
                    userService.deleteUser(user);
                }
                if (userCommand.equals("-list tasks"))
                {
                    user.getTasks().forEach(task -> {
                        System.out.println("ID: " + task.getId() + "\n\ttype: " + task.getType() +
                                "\n\ttheme: " + task.getTheme() + "\n\tdescription: " + task.getDescription() +
                                "\n\tpriority: " + task.getPriority() + "\n\tназвание проекта" + task.getProject().getName());
                    });
                }
                if (userCommand.equals("-get task"))
                {
                    Task task = getTask("Введите ID задачи");
                    System.out.println("Выберите следующие действия: " +
                            "\n\t-update - изменить задачу" +
                            "\n\t-delete - удалить задачу" +
                            "\n\t-back - назад");
                    System.out.print("Введите команду: ");
                    LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел команду {}", userCommand);
                    String taskCommand = scanner.nextLine();
                    if (taskCommand.equals("-update"))
                    {
                        taskService.updateTask(task);
                    }
                    if (taskCommand.equals("-delete"))
                    {
                        taskService.deleteTask(task);
                    }
                    if (taskCommand.equals("-back")) continue;
                }
                if (userCommand.equals("-back"))
                {
                    continue;
                }
            }

            if (command.equals("-get project"))
            {
                String projectCommand = scanner.nextLine();
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел команду {}", projectCommand);
                Project project = getProject("Введите ID проекта");
                System.out.println("Введите одну из следующих команд: \n" +
                        "\t-update - изменить пользователя\n" +
                        "\t-delete - удалить пользователя из базы\n" +
                        "\t-list tasks - получить список задач пользователя\n" +
                        "\t-get task - получить задачу по ID\n +" +
                        "\t-back - назад");
                if (projectCommand.equals("-update"))
                {
                    projectService.updateProject(project);
                }

                if (projectCommand.equals("-delete"))
                {
                    projectService.deleteProject(project);
                }
                if (projectCommand.equals("-list tasks"))
                {
                    project.getTasks().forEach(task -> {
                        System.out.println("ID: " + task.getId() + "\n\ttype: " + task.getType() +
                                "\n\ttheme: " + task.getTheme() + "\n\tdescription: " + task.getDescription() +
                                "\n\tpriority: " + task.getPriority() + "\n\tназвание проекта" + task.getUser().getName());
                    });
                }
                if (projectCommand.equals("-get task"))
                {
                    Task task = getTask("Введите ID задачи");
                    System.out.println("Выберите следующие действия: " +
                            "\n\t-update - изменить задачу" +
                            "\n\t-delete - удалить задачу" +
                            "\n\t-back - назад");
                    System.out.print("Введите команду: ");
                    String taskCommand = scanner.nextLine();
                    LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввел команду {}", taskCommand);
                    if (taskCommand.equals("-update"))
                    {
                        taskService.updateTask(task);
                    }
                    if (taskCommand.equals("-delete"))
                    {
                        taskService.deleteTask(task);
                    }
                    if (taskCommand.equals("-back")) continue;
                }
                if (projectCommand.equals("-back"))
                {
                    continue;
                }
            }
            if (command.equals("-a user"))
            {
                User user = createUser("Введите имя пользователя");
                userService.saveUser(user);
            }

            if (command.equals("-a project"))
            {
                Project project = createProject();
                projectService.saveProject(project);
            }

            if (command.equals("unload"))
            {
                try {
                    System.out.println("Введите путь до файла, который хотите использовать");
                    String path = scanner.nextLine();
                    writeDataIntoFile(new File(path));
                    continue;
                }
                catch (IOException exception)
                {
                    LOGGER.error(exception);
                }
            }
            if (command.equals("-a task"))
            {
                Task task = createTask();
                taskService.saveTask(task);
            }
            if (command.equals("-q"))
            {
                break;
            }
            else {
                LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь ввел команду{}", INVALID_QUERIES_MARKER);
                System.out.println("Неизвестная команда!");
            }
        }
    }

    private static User getUser(String message)
    {
        for (; ;)
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

    private static Project getProject(String message)
    {
        for (; ;)
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

    private static User createUser(String message)
    {
        for (; ;)
        {
            System.out.println(message);
            User user = new User();

            String userName = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            user.setName(userName);
            return user;
        }
    }

    private static Project createProject()
    {
        for (; ;)
        {
            Project newProject = new Project();
            System.out.println("Введите название нового проекта");
            String name = scanner.nextLine().trim();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.println("Введите срок сдачи проекта в формате дд.ММ.гггг");
            try
            {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(scanner.nextLine());
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
                newProject.setName(name);
                newProject.setDeadLine(date);
                return newProject;
            }
            catch (ParseException e)
            {
                LOGGER.error(e);
            }

        }
    }

    private static Task createTask()
    {
        for (; ;)
        {
            Task task = new Task();
            System.out.println("Введите тему задачи");
            String theme = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.println("Введите тип задачи");
            String type = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.println("Введите описание задачи");
            String description = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.println("Задайте приоритет LOW, MEDIUM, HIGH");
            Priority priority = Priority.valueOf(scanner.nextLine());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            User user = getUser("Выберите пользователя для задачи по ID ");
            Project project = getProject("Выберите для какого проекта ставится задача");
            task.setTheme(theme);
            task.setType(type);
            task.setDescription(description);
            task.setPriority(priority);
            task.setUser(user);
            task.setProject(project);
            user.getTasks().add(task);
            project.getTasks().add(task);
            return task;
        }
    }

    private static Task getTask(String message)
    {
        for (; ;)
        {
            System.out.println(message);
            long seekTask = Long.parseLong(scanner.nextLine());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            if (taskService.findTaskById(seekTask) != null)
            {
                return taskService.findTaskById(seekTask);
            }

            LOGGER.info(INVALID_QUERIES_MARKER, "Задача не найдена {}", INVALID_QUERIES_MARKER);
            System.out.println("Такой задачи не существует");
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
