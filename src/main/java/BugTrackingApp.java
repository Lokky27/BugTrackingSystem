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

    private static final String PROJECT_CHOICE = "projects";
    private static final String USER_CHOICE = "users";
    private static final String UNLOAD = "file";
    private static final String BACK = "back";
    private static final String EXIT_COMMAND = "exit";

    private static final String INFO_COMMAND = "info";
    private static final String GET_COMMAND = "get";
    private static final String GET_LIST = "list";
    private static final String TASKS = "tasks";
    private static final String ADD_COMMAND = "add";
    private static final String ADD_TASK = "add-task";
    private static final String UPDATE_COMMAND = "update";
    private static final String DELETE_COMMAND = "delete";

    private static UserService userService = new UserService();
    private static TaskService taskService = new TaskService();
    private static ProjectService projectService = new ProjectService();

    private static Scanner scanner;

    public static void main(String[] args)
    {
        System.out.println("Система отслеживания задач/ошибок");

        scanner = new Scanner(System.in);
        for (; ;)
        {
            try
            {
                System.out.println("Для работы с проектами введите: " + PROJECT_CHOICE +
                        "\nДля работы с пользователями введите: " + USER_CHOICE +
                        "\nДля выгрузки данных в файл введите: " + UNLOAD +
                        "\nДля выхода из программы введите: " + EXIT_COMMAND);
                System.out.print("Введите команду: ");
                String userInput = scanner.nextLine();
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл команду {}", userInput);
                if (userInput.equals(EXIT_COMMAND))
                {
                    System.out.println("Выход");
                    break;
                }
                switch (userInput)
                {
                    case PROJECT_CHOICE:
                        System.out.println("Работа с проектами");
                        System.out.println("\tЧтобы добавить новый проект введите: " + ADD_COMMAND +
                                "\n\tДля вывода списка активных проектов введите: " + GET_LIST +
                                "\n\tДля работы с конкретным проектом введите: " + GET_COMMAND +
                                "\n\tЧтобы обновить проект введите: " + UPDATE_COMMAND +
                                "\n\tЧтобы удалить проект введите: " + DELETE_COMMAND +
                                "\n\tЧтобы вернутся в главное меню введите " + BACK);
                        System.out.print("Введите команду: ");
                        String projectCommand = scanner.nextLine();
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователя ввёл команду {} ", projectCommand);
                        switch (projectCommand)
                        {
                            case BACK : break;

                            case GET_COMMAND :
                                workWithTasksInProject();
                                break;
                            case ADD_COMMAND :
                                addProject();
                                break;
                            case GET_LIST :
                                projectService.getAllProjects().forEach(System.out::println);
                                break;
                            case UPDATE_COMMAND :
                                updateProject();
                                break;
                            case DELETE_COMMAND :
                                deleteProject();
                                break;
                            default :
                                LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь ввёл команду {} ", projectCommand);
                                System.out.println("Неизвестная команда");
                                break;
                        }
                        break;
                    case USER_CHOICE:
                        System.out.println("Работа с пользователями");
                        System.out.println("\tЧтобы добавить нового пользователя введите: " + ADD_COMMAND +
                                "\n\tДля вывода списка пользователей введите: " + GET_LIST +
                                "\n\tДля работы с конкретным пользователем введите: " + GET_COMMAND +
                                "\n\tЧтобы обновить данные пользователя введите: " + UPDATE_COMMAND +
                                "\n\tЧтобы удалить пользователя введите: " + DELETE_COMMAND +
                                "\n\tЧтобы вернутся в главное меню введите " + BACK);
                        String userCommand = scanner.nextLine();
                        LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователя ввёл команду {} ", userCommand);
                        switch (userCommand)
                        {
                            case BACK : break;

                            case GET_COMMAND :
                                workWithUserTasks();
                                break;
                            case ADD_COMMAND :
                                addNewUser();
                                break;
                            case GET_LIST :
                                userService.getAllUsers().forEach(System.out::println);
                                break;
                            case UPDATE_COMMAND :
                                updateUser();
                                break;
                            case DELETE_COMMAND :
                                deleteUser();
                                break;
                            default :
                                LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь ввёл команду {} ", userCommand);
                                System.out.println("Неизвестная команда");
                                break;
                        }
                        break;
                    case UNLOAD:
                        System.out.println("Выгрузка данных в файл");
                        System.out.print("Введите путь до файла: ");
                        File file = new File(scanner.nextLine());
                        writeDataIntoFile(file);
                        break;
                    default:
                        LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь ввёл команду {}", userInput);
                        System.out.println("Неизвестная команда!");
                        break;
                }
            }
            catch (Exception exception)
            {
                System.out.println("Что-то здесь не так: " + exception.getMessage());
                exception.printStackTrace();
                LOGGER.error(exception.getMessage());
            }
        }
    }

    private static void addProject()
    {
        for (; ;)
        {
            Project newProject = new Project();
            System.out.print("Введите название нового проекта: ");
            String name = scanner.nextLine().trim();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.print("Введите срок сдачи проекта в формате дд.ММ.гггг: ");
            try
            {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(scanner.nextLine());
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
                newProject.setName(name);
                newProject.setDeadLine(date);
                projectService.saveProject(newProject);
                break;
            }
            catch (ParseException e)
            {
                System.out.println(e.getMessage());
                LOGGER.error(e);
            }
        }
    }

    private static void updateProject() throws ParseException
    {
        for (; ;)
        {
            Project newProject = new Project();
            System.out.print("Введите новое название для проекта: ");
            String newName = scanner.nextLine();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            System.out.print("Введите новый срок сдачи: ");
            Date newDeadline = new SimpleDateFormat("dd.MM.yyyy").parse(scanner.nextLine());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", COMMAND_HISTORY_MARKER);
            newProject.setName(newName);
            newProject.setDeadLine(newDeadline);
            System.out.print("Введите ID проекта, который нужно обновить: ");
            Long updatedProjectId = Long.parseLong(scanner.nextLine());
            if (projectService.findProjectById(updatedProjectId) != null)
            {
                projectService.updateProject(updatedProjectId, newProject);
                break;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Проект c ID {} не найден", updatedProjectId);
            System.out.printf("Проект с ID: %d не существует", updatedProjectId);
        }
    }

    private static void deleteProject()
    {
        for (; ;)
        {
            System.out.print("Введите ID проекта, который нужно удалить: ");
            Long deletedProjectId = Long.parseLong(scanner.nextLine());
            if (projectService.findProjectById(deletedProjectId) != null)
            {
                projectService.deleteProject(deletedProjectId);
                break;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Проект с ID {} не найден", deletedProjectId);
            System.out.printf("Проект с ID: %d не существует", deletedProjectId);
        }
    }

    private static void workWithTasksInProject()
    {
        for (; ;)
        {
            System.out.println("Введите ID проекта: ");
            Long projectId = Long.parseLong(scanner.nextLine());
            Project project = projectService.findProjectById(projectId);
            System.out.println("Вы выбрали проект: " + project.getName() +
                        "\nВыберите действие: ");
            System.out.println("\tЧтобы просмотреть информацию о задаче введите: " + INFO_COMMAND +
                        "\n\tЧтобы посмотреть список всех задач на проекте введите: " + TASKS +
                        "\n\tЧтобы добавить новую задачу в проект введите: " + ADD_TASK +
                        "\n\tЧтобы обновить задачу на проекте введите: " + UPDATE_COMMAND +
                        "\n\tЧтобы удалить задачу из проекта введите: " + DELETE_COMMAND +
                        "\n\tДля перехода в главное меню введите: " + BACK);
            System.out.print("Введите комманду: ");
            String userInput = scanner.nextLine().trim();
            if (userInput.equals(BACK)) break;
            switch (userInput)
            {
                case INFO_COMMAND :
                    getTaskInfo();
                    break;
                case TASKS :
                    project.getTasks().forEach(System.out::println);
                    break;
                case ADD_TASK :
                    addTaskIntoProject(projectId);
                    break;
                case UPDATE_COMMAND :
                    updateTaskInProject(projectId);
                    break;
                case DELETE_COMMAND :
                    deleteTaskFromProject(projectId);
                    break;
                default :
                    LOGGER.info(INVALID_QUERIES_MARKER, "Проекта с ID {} нет в системе", projectId);
                    System.out.printf("Проекта с ID %d нет в системе\n", projectId);
            }
            break;
        }
    }

    private static void getTaskInfo()
    {
        for (; ;)
        {
            System.out.println("Введите ID задачи: ");
            Long taskId = Long.parseLong(scanner.nextLine().trim());
            if (taskService.findTaskById(taskId) != null)
            {
                Task task = taskService.findTaskById(taskId);
                System.out.println(task);
                break;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Задачи с ID {} нет в проекте", taskId);
            System.out.printf("Задача с ID %d не найдена\n", taskId);
        }
    }

    private static void addTaskIntoProject(Long projectId)
    {
        for (; ;)
        {
            Project project = projectService.findProjectById(projectId);
            Task task = createTask();
            User user = getUser();
            taskService.saveTask(task, user.getId(), projectId);
            project.getTasks().add(task);
            user.getTasks().add(task);
            break;
        }
    }

    private static void updateTaskInProject(Long projectId)
    {
        for (; ;)
        {
            Project project = projectService.findProjectById(projectId);
            if (project != null)
            {
                System.out.print("Введите ID задачи, которую нужно обновить: ");
                Long taskIdToUpdate = Long.parseLong(scanner.nextLine().trim());
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {} ID", taskIdToUpdate);
                if (taskService.findTaskById(taskIdToUpdate) != null)
                {
                    Task newTask = createTask();
                    taskService.updateTask(taskIdToUpdate, newTask);
                    break;
                }
                LOGGER.info(INVALID_QUERIES_MARKER, "Задача с ID {} не найдена", taskIdToUpdate);
                System.out.printf("Задача с ID %d не найдена", taskIdToUpdate);
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Проект с ID {} не найден", projectId);
            System.out.printf("Проект с ID %d не найден", projectId);
        }
    }

    private static void deleteTaskFromProject(Long projectId)
    {
        for (; ;)
        {
            System.out.println("Введите ID задачи, которую нужно удалить");
            Long deletedTaskId = Long.parseLong(scanner.nextLine().trim());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл ID {} ", deletedTaskId);
            if (taskService.findTaskById(deletedTaskId) != null)
            {
                Task deletedTask = taskService.findTaskById(deletedTaskId);
                Long userIdOfDeletedTask = deletedTask.getUser().getId();
                taskService.deleteTask(deletedTaskId, userIdOfDeletedTask, projectId);
                break;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Задача с ID {} не найдена!", deletedTaskId);
            System.out.printf("Задача с ID %d не найдена", deletedTaskId);
        }
    }

    private static User getUser()
    {
        for (; ;)
        {
            System.out.println("Введите ID исполнителя для задачи: ");
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

    private static void addNewUser()
    {
        for (; ;)
        {
            User newUser = new User();
            System.out.println("Введите имя для нового пользователя!");
            String userName = scanner.nextLine().trim();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {} ", userName);
            newUser.setName(userName);
            userService.saveUser(newUser);
            break;
        }
    }

    private static void updateUser()
    {
        for (; ;)
        {
            System.out.print("Введите ID пользователя для обновления: ");
            Long userIdToUpdate = Long.parseLong(scanner.nextLine().trim());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {} ", userIdToUpdate);
            if (userService.findUserById(userIdToUpdate) != null)
            {
                User newUser = new User();
                System.out.println("Введите новое имя для пользователя");
                String newName = scanner.nextLine().trim();
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {} ", newName);
                newUser.setName(newName);
                userService.updateUser(userIdToUpdate, newUser);
                break;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь с ID {} не найден", userIdToUpdate);
            System.out.printf("Пользователь с ID %d не найден", userIdToUpdate);
        }
    }

    private static void deleteUser()
    {
        for (; ;)
        {
            System.out.print("Введите ID пользователя, которого нужно удалить: ");
            Long userIdToDelete = Long.parseLong(scanner.nextLine().trim());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", userIdToDelete);
            if (userService.findUserById(userIdToDelete) != null)
            {
                userService.deleteUser(userIdToDelete);
                break;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь с ID {} не найден", userIdToDelete);
            System.out.printf("Пользователь с ID %d не найден", userIdToDelete);
        }

    }

    private static void workWithUserTasks()
    {
        for (; ;)
        {
            System.out.println("Введите ID пользователя: ");
            Long userId = Long.parseLong(scanner.nextLine());
            User user = userService.findUserById(userId);
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {}", userId);
            System.out.println("Пользователь - " + user.getName());
            System.out.println("Здесь доступны следующие команды: ");
            System.out.println("\tЧтобы просмотреть информацию о задаче введите: " + INFO_COMMAND +
                        "\n\tЧтобы посмотреть список всех задач у пользователя введите: " + TASKS +
                        "\n\tЧтобы добавить новую задачу пользователю введите: " + ADD_TASK +
                        "\n\tЧтобы обновить задачу у пользователя введите: " + UPDATE_COMMAND +
                        "\n\tЧтобы удалить задачу у пользователя введите: " + DELETE_COMMAND);
            System.out.print("Введите комманду: ");
            String userInput = scanner.nextLine().trim();
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл команду {}", userInput);
            switch (userInput)
            {
                case INFO_COMMAND :
                    getTaskInfo();
                    break;
                case TASKS :
                    user.getTasks().forEach(System.out::println);
                    break;
                case ADD_TASK :
                    addTaskToUser(userId);
                    break;

                case UPDATE_COMMAND :
                    updateUserTask(userId);
                    break;
                case DELETE_COMMAND :
                    deleteTaskFromUser(userId);
                    break;
                default :
                    LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь ввёл команду {} ", userInput);
                    System.out.println("Неизвестная команда");
                    continue;
            }
            break;
        }
    }

    private static void addTaskToUser(Long userId)
    {
        for (; ;)
        {
            Task task = createTask();
            Project project = getProject();
            taskService.saveTask(task, userId, project.getId());
            break;
        }
    }

    private static void updateUserTask(Long userId)
    {
        for (; ;)
        {
            User user = userService.findUserById(userId);
            if (user != null)
            {
                System.out.print("Введите ID задачи, которую нужно обновить: ");
                Long taskIdToUpdate = Long.parseLong(scanner.nextLine().trim());
                LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл {} ID", taskIdToUpdate);
                if (taskService.findTaskById(taskIdToUpdate) != null)
                {
                    Task newTask = createTask();
                    taskService.updateTask(taskIdToUpdate, newTask);
                    break;
                }
                LOGGER.info(INVALID_QUERIES_MARKER, "Задача с ID {} не найдена", taskIdToUpdate);
                System.out.printf("Задача с ID %d не найдена", taskIdToUpdate);
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Пользователь с ID {} не найден", userId);
            System.out.printf("Пользователь с ID %d не найден", userId);
        }
    }

    private static void deleteTaskFromUser(Long userId)
    {
        for (; ;)
        {
            System.out.println("Введите ID задачи, которую нужно удалить");
            Long deletedTaskId = Long.parseLong(scanner.nextLine().trim());
            LOGGER.info(COMMAND_HISTORY_MARKER, "Пользователь ввёл ID {} ", deletedTaskId);
            if (taskService.findTaskById(deletedTaskId) != null)
            {
                Task deletedTask = taskService.findTaskById(deletedTaskId);
                Long projectIdOfDeletedTask = deletedTask.getProject().getId();
                taskService.deleteTask(deletedTaskId, userId, projectIdOfDeletedTask);
                break;
            }
            LOGGER.info(INVALID_QUERIES_MARKER, "Задача с ID {} не найдена!", deletedTaskId);
            System.out.printf("Задача с ID %d не найдена", deletedTaskId);
        }
    }

    private static Project getProject()
    {
        for (; ;)
        {
            System.out.print("Введите ID проекта: ");
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
        String header = "id" + "," + "theme" + "," + "type" + "," +
                "description" + "," + "priority" + "," + "user_id" +
                "," + "user name" + "," + "project_id" + "," + "project_name" +
                "," + "deadline" + "\n";
        writer.write(header);
        List<Task> allTasks = taskService.findAllTasks();
        for (Task task : allTasks) {
            String itemBuilder = task.getId() + "," + task.getTheme() + "," +
                    task.getType() + "," + task.getDescription() +
                    "," + task.getPriority() + "," + task.getUser().getId() +
                    "," + task.getUser().getName() + "," + task.getProject().getId() +
                    "," + task.getProject().getName() + "," + task.getProject().getDeadLine();
            writer.write(itemBuilder);
        }
        writer.flush();
        writer.close();
        System.out.println("Записано в файл " + userFile.getPath());
    }
}
