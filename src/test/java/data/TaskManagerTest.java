package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

import static data.TaskManager.addTask;
import static data.TaskManager.updateTask;
import static data.TaskManager.deleteAllTasksOnDate;
import static org.junit.jupiter.api.Assertions.assertEquals;


class TaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManager();
    }

    @AfterEach
    void resetTaskManager() {
        LocalDate date = LocalDate.now();
        deleteAllTasksOnDate(taskManager, date);
    }

    @Test
    void addTodo_validInput_addsTask() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        String taskDescription = "Test Todo";

        // Act
        Task testTask = new Task(taskDescription);
        TaskType testTaskType = TaskType.TODO;
        String[] dummyTestDates = new String[]{null};
        String[] dummyTestTimes = new String[]{null};

        addTask(date, taskDescription, testTaskType, dummyTestDates,dummyTestTimes);
        Task addedTask = taskManager.getTasksForDate(date).get(0);

        // Assert
        assertEquals(testTask.getName(), addedTask.getName());
    }

    @Test
    void updateTodo_validInput_updatesTask() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        String initialTaskDescription = "Initial todo";
        String updatedTaskDescription = "Updated todo";
        TaskType testTaskType = TaskType.TODO;
        String[] dummyTestDates = new String[]{null};
        String[] dummyTestTimes = new String[]{null};

        Scanner scanner = new Scanner(System.in);

        addTask(date, initialTaskDescription, testTaskType, dummyTestDates, dummyTestTimes);

        // Act
        updateTask(date, 0, updatedTaskDescription, scanner);

        // Assert
        assertEquals(updatedTaskDescription, taskManager.getTasksForDate(date).get(0).getName());
    }

    @Test
    void getTasksForDate_validDate_returnsTasks() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        String taskDescription = "Test todo task";
        TaskType testTaskType = TaskType.TODO;
        String[] dummyTestDates = new String[]{null};
        String[] dummyTestTimes = new String[]{null};

        addTask(date, taskDescription, testTaskType, dummyTestDates,dummyTestTimes);

        // Act
        List<Task> tasksForDate = taskManager.getTasksForDate(date);
        Task createdTask = tasksForDate.get(0);

        // Assert
        assertEquals(createdTask, tasksForDate.get(0));
    }

    @Test
    void addTodoFromFile_validInput_addsTasks() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        Map<LocalDate, List<Task>> tasksFromFile = new HashMap<>();
        String taskDescription = "Test todo task";
        Task testTodoTask = new Todo(taskDescription);
        tasksFromFile.put(date, List.of(testTodoTask));

        // Act
        taskManager.addTasksFromFile(tasksFromFile);

        // Assert
        assertEquals(testTodoTask.getName() ,taskManager.getTasksForDate(date).get(0).getName());
    }
    @Test
    void addEvent_validInput_addsTask() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        String taskDescription = "Test Event";
        TaskType testTaskType = TaskType.EVENT;
        String[] testDates = new String[]{"01/01/2023", "02/01/2023"};
        String[] testTimes = new String[]{"10:00", "12:00"};

        // Act
        TaskManager.addTask(date, taskDescription, testTaskType, testDates, testTimes);
        Task addedTask = taskManager.getTasksForDate(date).get(0);

        // Assert
        assertEquals(taskDescription, addedTask.getName());
    }

    @Test
    void addDeadline_validInput_addsTask() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        String taskDescription = "Test Deadline";
        TaskType testTaskType = TaskType.DEADLINE;
        String[] testDates = new String[]{"01/01/2023"};
        String[] testTimes = new String[]{"10:00"};

        // Act
        TaskManager.addTask(date, taskDescription, testTaskType, testDates, testTimes);
        Task addedTask = taskManager.getTasksForDate(date).get(0);

        // Assert
        assertEquals(taskDescription, addedTask.getName());
    }

    @Test
    void updateEvent_noInput_updatesTask() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        String initialTaskDescription = "Initial event";
        String updatedTaskDescription = "Updated event";
        TaskType testTaskType = TaskType.EVENT;
        String[] initialDates = new String[]{"01/01/2023", "02/01/2023"};
        String[] initialTimes = new String[]{"10:00", "12:00"};

        String simulatedUserInput = "no\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        addTask(date, initialTaskDescription, testTaskType, initialDates, initialTimes);

        // Act
        updateTask(date, 0, updatedTaskDescription, new Scanner(System.in));

        // Assert
        assertEquals(updatedTaskDescription, taskManager.getTasksForDate(date).get(0).getName());
    }

    @Test
    void updateDeadline_noInput_updatesTask() throws TaskManagerException {
        // Arrange
        LocalDate date = LocalDate.now();
        String initialTaskDescription = "Initial deadline";
        String updatedTaskDescription = "Updated deadline";
        TaskType testTaskType = TaskType.DEADLINE;
        String[] initialDates = new String[]{"01/01/2023"};
        String[] initialTimes = new String[]{"10:00"};

        String simulatedUserInput = "no\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        addTask(date, initialTaskDescription, testTaskType, initialDates, initialTimes);

        // Act
        updateTask(date, 0, updatedTaskDescription, new Scanner(System.in));

        // Assert
        assertEquals(updatedTaskDescription, taskManager.getTasksForDate(date).get(0).getName());
    }
}
