package data;

import storage.Storage;
import time.WeekView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import static data.TaskManagerException.checkIfDateHasTasks;
import static data.TaskManagerException.checkIfDateInCurrentWeek;
import static data.TaskManagerException.checkIfDateInCurrentMonth;
import static storage.Storage.saveTasksToFile;

/**
 * Manages tasks by providing functionalities to add, delete, and update tasks.
 */
public class TaskManager {
    private static final Map<LocalDate, List<Task>> tasks = new HashMap<>();

    /**
     * Adds a task for a specific date.
     * @param date The date for the task.
     * @param taskDescription The description of the task.
     */
    public static void addTask(LocalDate date, String taskDescription) {
        Task task = new Task(taskDescription);
        tasks.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
    }

    /**
     * Deletes a task for a specific date and task index.
     * @param date The date of the task.
     * @param taskIndex The index of the task to delete.
     */
    public void deleteTask(LocalDate date, int taskIndex) {
        List<Task> dayTasks = tasks.get(date);
        if (dayTasks != null && taskIndex >= 0 && taskIndex < dayTasks.size()) {
            dayTasks.remove(taskIndex);
            if (dayTasks.isEmpty()) {
                tasks.remove(date);
            }
        }
    }

    /**
     * Updates a task for a specific date and task index.
     * @param date The date of the task.
     * @param taskIndex The index of the task to update.
     * @param newTaskDescription The updated description of the task.
     * @throws IndexOutOfBoundsException If the task index is out of bounds.
     */
    public static void updateTask(LocalDate date, int taskIndex, String newTaskDescription)
            throws IndexOutOfBoundsException {
        try {
            List<Task> dayTasks = tasks.get(date);
            boolean dayHasTasks = dayTasks != null;
            boolean taskIndexExists = taskIndex >= 0 && taskIndex < Objects.requireNonNull(dayTasks).size();
            if (dayHasTasks && taskIndexExists) {
                Task task = new Task(newTaskDescription);
                dayTasks.set(taskIndex, task);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves tasks for a specific date.
     * @param date The date to retrieve tasks for.
     * @return A list of tasks for the given date.
     */
    public List<Task> getTasksForDate(LocalDate date) {
        return tasks.getOrDefault(date, new ArrayList<>());
    }

    /**
     * Adds a task from user input along with the date.
     *
     * @param scanner Scanner object to get user input.
     * @param weekView WeekView object to validate the date.
     * @param inMonthView A boolean indicating whether the view is in month view or not.
     * @throws TaskManagerException If there is an error in managing tasks.
     */
    public static void addManager(Scanner scanner, WeekView weekView, boolean inMonthView)
            throws TaskManagerException, DateTimeParseException {
        System.out.println("Enter the date for the task (dd/MM/yyyy):");
        LocalDate date = parseInputDate(scanner);

        if (inMonthView) {
            checkIfDateInCurrentMonth(date);
        } else {
            checkIfDateInCurrentWeek(date, weekView);
        }

        System.out.println("Enter the task description:");
        String taskDescription = scanner.nextLine().trim();

        addTask(date, taskDescription);
        saveTasksToFile(tasks, Storage.FILE_PATH); // Updates tasks from hashmap into tasks.txt file
        System.out.println("Task added.");
    }

    /**
     * Prompts user for updated task description
     *
     * @param scanner User input
     * @param weekView Current week being viewed
     * @param inMonthView Whether month is being viewed
     * @param taskManager The taskManager class being used
     * @throws TaskManagerException Throws exception when not in correct week/month view
     */
    public static void updateManager(Scanner scanner, WeekView weekView, boolean inMonthView,TaskManager taskManager)
            throws TaskManagerException, DateTimeParseException {
        System.out.println("Enter the date for the task you wish to update (dd/MM/yyyy):");
        LocalDate date = parseInputDate(scanner);


        if (inMonthView) {
            checkIfDateInCurrentMonth(date);
        } else {
            checkIfDateInCurrentWeek(date, weekView);
        }

        listTasksAtDate(taskManager, date, "Enter the task number of the task you wish to update:");

        int taskNumber;
        String updatedDescription;

        try {
            taskNumber = Integer.parseInt(scanner.nextLine().trim());
            assert taskNumber != 0 : "Task Number is invalid!";

            System.out.println("Enter the updated task description:");
            updatedDescription = scanner.nextLine().trim();

            updateTask(date, taskNumber - 1, updatedDescription);
            saveTasksToFile(tasks,Storage.FILE_PATH); //Update tasks.txt file
            System.out.println("Task updated.");
        } catch (NumberFormatException e) {
            System.out.println("Task number should be an integer value. Please try again.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("The task number you have entered does not exist. Please try again.");
        }

    }

    /**
     * Adds tasks from a file to the TaskManager.
     * @param tasksFromFile A map containing tasks read from a file.
     */
    public void addTasksFromFile(Map<LocalDate, List<Task>> tasksFromFile) {
        for (Map.Entry<LocalDate, List<Task>> entry : tasksFromFile.entrySet()) {
            LocalDate date = entry.getKey();
            List<Task> taskList = entry.getValue();
            for (Task task : taskList) {
                String taskDescription = task.getName();
                addTask(date, taskDescription);
            }
        }
    }

    /**
     * Lists task of the input date
     *
     * @param taskManager Hashmap of tasks
     * @param date Date that's prompted by user
     * @param message Message to be prompted to the user
     * @throws TaskManagerException Throws exception when not in correct week/month view
     */
    private static void listTasksAtDate(TaskManager taskManager, LocalDate date, String message)
            throws TaskManagerException {
        List<Task> dayTasks = taskManager.getTasksForDate(date);
        checkIfDateHasTasks(dayTasks);

        System.out.println(message);
        for (int i = 0; i < dayTasks.size(); i++) {
            System.out.println((i + 1) + ". " + dayTasks.get(i).getName());
        }
    }

    /**
     * Prompts user for task description and deletes task from hashmap and tasks.txt file
     *
     * @param scanner User input
     * @param weekView Current week being viewed
     * @param inMonthView Whether month is being viewed
     * @param taskManager The taskManager class being used
     * @throws TaskManagerException Throws exception when not in correct week/month view
     */
    public static void deleteManager(Scanner scanner, WeekView weekView, boolean inMonthView, TaskManager taskManager)
            throws DateTimeParseException, TaskManagerException {

        System.out.println("Enter the date for the task to delete (dd/MM/yyyy):");
        LocalDate date = parseInputDate(scanner);

        if (inMonthView) {
            checkIfDateInCurrentMonth(date);
        } else {
            checkIfDateInCurrentWeek(date, weekView);
        }

        listTasksAtDate(taskManager, date, "Enter the task number to delete:");

        int taskNumber;

        try {
            taskNumber = Integer.parseInt(scanner.nextLine().trim());
            taskManager.deleteTask(date, taskNumber - 1);
            System.out.println("Task deleted.");
            saveTasksToFile(tasks, Storage.FILE_PATH); // Update tasks.txt file
        } catch (NumberFormatException e) {
            System.out.println("Invalid task number. Please try again.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("The task number you have entered does not exist. Please try again.");
        }
    }

    /**
     * Function to delete all tasks on a specified date.
     * Currently only used to complement JUnit testing.
     *
     * @param taskManager The taskManager class in use.
     * @param specifiedDate The date on which all tasks are to be deleted.
     */

    public static void deleteAllTasksOnDate (TaskManager taskManager, LocalDate specifiedDate) {
        List<Task> dayTasks = tasks.get(specifiedDate);
        int numOfTasks = dayTasks.size();
        for (int i = numOfTasks; i >= 0; i--) {
            taskManager.deleteTask(specifiedDate, i - 1);
        }
    }

    // to abstract as Parser/UI function

    /**
     * Parses user input into date time format
     *
     * @param scanner User Input
     * @return Formatted date time from user input
     * @throws DateTimeParseException Throws exception is user input is not in correct format
     */
    private static LocalDate parseInputDate(Scanner scanner) throws DateTimeParseException {
        String dateString = scanner.nextLine().trim();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid date format. Please use the format dd/MM/yyyy.", dateString, 0);
        }
        return date;
    }
}
