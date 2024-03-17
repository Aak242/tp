/*package seedu.duke;
import Time.DateUtils;
import Time.WeekView;
import data.TaskManager;
import data.TaskManagerException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static data.TaskManager.addManager;
import static data.TaskManager.deleteManager;

public class Main {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = DateUtils.getStartOfWeek(today);
        WeekView weekView = new WeekView(startOfWeek, dateFormatter);
        TaskManager taskManager = new TaskManager();

        boolean printWeek = true; // Flag to control printing of the week view
        boolean inMonthView = false; // Flag to indicate if we are in month view mode

        while (true) {
            if (printWeek) {
                if (!inMonthView) {
                    weekView.printWeekView(taskManager);
                } else {
                    weekView.printMonthView(taskManager);
                }
            }
            printWeek = true; // Reset flag for the next iteration
            System.out.println("Enter 'next' for next week, 'prev' for previous week, 'add' to add a task, " +
                    "'delete' to delete a task, 'month' to display the month view, " +
                    "or 'quit' to quit:");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
            case "next":
                if (inMonthView) {
                    weekView.nextMonth();
                } else {
                    weekView.nextWeek();
                }
                break;
            case "prev":
                if (inMonthView) {
                    weekView.previousMonth();
                } else {
                    weekView.previousWeek();
                }
                break;
            case "add":
                addManager(scanner, weekView, taskManager); // Use the correct method
                break;
            case "delete":
            case "delete":
                deleteManager(scanner, weekView, taskManager); // Use the correct method
                break;
            case "month":
                weekView.printMonthView(taskManager);
                inMonthView = !inMonthView; // Toggle month view mode
                printWeek = false;
                break;
            case "week":
                inMonthView = false;
                //weekView.printWeekView(taskManager);
                break;
            case "quit":
                System.out.println("Exiting Calendar...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
*/

package seedu.duke;

import Time.DateUtils;
import Time.WeekView;
import data.TaskManager;
import data.TaskManagerException;

import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static Storage.Storage.createNewFile;
import static data.TaskManager.addManager;
import static data.TaskManager.deleteManager;

public class Main {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = DateUtils.getStartOfWeek(today);
        WeekView weekView = new WeekView(startOfWeek, dateFormatter);
        TaskManager taskManager = new TaskManager();

        boolean printWeek = true; // Flag to control printing of the week view
        boolean inMonthView = false; // Flag to indicate if we are in month view mode

        createNewFile(); //Creates directory and tasks.txt file if it does not exist

        while (true) {
            if (printWeek) {
                if (!inMonthView) {
                    weekView.printWeekView(taskManager);
                } else {
                    weekView.printMonthView(taskManager);
                }
            }
            printWeek = true; // Reset flag for the next iteration
            System.out.println("Enter 'next' for next week, 'prev' for previous week, 'add' to add a task, " +
                    "'delete' to delete a task, 'month' to display the month view, " +
                    "or 'quit' to quit:");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
            case "next":
                if (inMonthView) {
                    weekView.nextMonth();
                } else {
                    weekView.nextWeek();
                }
                break;
            case "prev":
                if (inMonthView) {
                    weekView.previousMonth();
                } else {
                    weekView.previousWeek();
                }
                break;
            case "add":
                try {
                    addManager(scanner, weekView, taskManager, inMonthView);
                } catch (TaskManagerException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "delete":
                try {
                    deleteManager(scanner, weekView, taskManager);
                } catch (TaskManagerException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "month":
                weekView.printMonthView(taskManager);
                inMonthView = !inMonthView; // Toggle month view mode
                printWeek = false;
                break;
            case "week":
                inMonthView = false;
                //weekView.printWeekView(taskManager);
                break;
            case "quit":
                System.out.println("Exiting Calendar...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input. Please try again.");
            }
        }
    }
}
