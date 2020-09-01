package duke;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import java.util.NoSuchElementException;
import java.util.Scanner;

import exception.InvalidCommandException;
import exception.MissingInfoException;

import task.Task;

/**
 * Parser deals with making sense of the user command.
 */
public class Parser {

    private Scanner input;

    public Parser(Scanner input) {
        this.input = input;
    }

    /**
     * Sets the scanner input stored in this object.
     *
     * @param scanner The scanner to store.
     */
    public void setScanner(Scanner scanner) {
        this.input = scanner;
    }

    /**
     * Takes in the command entered by the user and execute it accordingly.
     *
     * @param taskList All the tasks in the to-do list.
     * @return String The relevant reply according to command.
     */
    public String executeCommand(TaskList taskList) {
        String command = this.input.next();

        if (command.equals("bye")) {
            return "bye";
        } else if (command.equals("list")) {
            return "Here are the tasks in your list:\n" + taskList.listTasks();
        } else if (command.equals("done")) {
            try {
                Task task = taskList.getTasks().get(this.input.nextInt() - 1);
                task.completeTask();
                return "This task has been marked as done:\n" + task.toString();
            } catch (IndexOutOfBoundsException e) {
                return "OOPS!!! Task number is invalid.";
            } catch (NoSuchElementException e) {
                if (this.input.hasNextLine()) {
                    this.input.nextLine();
                }
                return "OOPS!!! Task number must be a number.";
            }
        } else if (command.equals("delete")) {
            try {
                int taskNumber = this.input.nextInt();
                Task task = taskList.getTasks().get(taskNumber - 1);
                taskList.removeTask(taskNumber - 1);
                return "This task has been removed:\n" + task.toString()
                        + "\nNow you have " + taskList.getTasks().size() + " in the list.";
            } catch (IndexOutOfBoundsException e) {
                return "OOPS!!! Task number is invalid.";
            } catch (NoSuchElementException e) {
                if (this.input.hasNextLine()) {
                    this.input.nextLine();
                }
                return "OOPS!!! Task number must be a number.";
            }
        } else if (command.equals("find")) {
            String keyword;
            try {
                keyword = input.nextLine().substring(1);
            } catch (StringIndexOutOfBoundsException | NoSuchElementException e) {
                return "OOPS!!! Keyword cannot be empty.";
            }
            TaskList foundTasks = new TaskList(taskList.findTasks(keyword));
            return "Here are the matching tasks in your list:\n" + foundTasks.listTasks();
        } else {
            try {
                TaskType.TypeOfTask typeOfTask;

                switch (command) {
                case "todo":
                    typeOfTask = TaskType.TypeOfTask.TODO;
                    break;
                case "deadline":
                    typeOfTask = TaskType.TypeOfTask.DEADLINE;
                    break;
                case "event":
                    typeOfTask = TaskType.TypeOfTask.EVENT;
                    break;
                default:
                    if (this.input.hasNextLine()) {
                        this.input.nextLine();
                    }
                    throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

                Task newTask = getTask(command, typeOfTask, taskList);
                taskList.addTask(newTask);
                return "Got it. I've added this task:\n" + newTask.toString()
                        + "\nNow you have " + taskList.getTasks().size() + " tasks in the list.";
            } catch (InvalidCommandException e) {
                return e.getMessage();
            } catch (MissingInfoException e) {
                return e.getMessage();
            } catch (DateTimeParseException e) {
                return "OOPS!!! Date format is invalid. Make sure it is yyyy-mm-ddTHH:mm.";
            }
        }
    }

    private Task getTask(String command, TaskType.TypeOfTask typeOfTask, TaskList taskList)
            throws MissingInfoException {
        String[] commandArray;
        try {
            commandArray = this.input.nextLine().split(" ");
        } catch (NoSuchElementException e) {
            throw new MissingInfoException("OOPS!!! The description of a " + command + " cannot be empty.");
        }
        String description = "";
        LocalDateTime timing = null;

        for (int i = 1; i < commandArray.length; i++) {
            if (commandArray[i].equals("/by")) {
                timing = getTiming(command, commandArray, i + 1);
                break;
            } else if (commandArray[i].equals("/at")) {
                timing = getTiming(command, commandArray, i + 1);
                break;
            }

            if (i == 1) {
                description = commandArray[i];
            } else {
                description = description + " " + commandArray[i];
            }
        }

        if (description.isEmpty()) {
            throw new MissingInfoException("OOPS!!! The description of a " + command + " cannot be empty.");
        }

        if ((typeOfTask.equals(TaskType.TypeOfTask.DEADLINE) || typeOfTask.equals(TaskType.TypeOfTask.EVENT))
                && timing == null) {
            throw new MissingInfoException("OOPS!!! The date/time of a " + command + " cannot be empty.");
        }

        return taskList.createTask(typeOfTask, description, timing, false);
    }

    private LocalDateTime getTiming(String command, String[] commandArray, int index)
            throws MissingInfoException, DateTimeParseException {
        String timing = "";

        for (int i = index; i < commandArray.length; i++) {
            if (i == index) {
                timing = commandArray[i];
            } else {
                timing = timing + " " + commandArray[i];
            }
        }

        if (timing.isEmpty()) {
            throw new MissingInfoException("OOPS!!! The date/time of a " + command + " cannot be empty.");
        }

        try {
            return LocalDateTime.parse(timing);
        } catch (DateTimeParseException e) {
            throw e;
        }
    }
}
