package duke;

/**
 * Ui deals with interactions with the user.
 */
public class Ui {

    /**
     * Formats the given input and prints out the text with relevant indentations and enclosed it with lines.
     *
     * @param text The String to be formatted
     */
    public String printReply(String text) {
        return "\n\t\t" + text.replaceAll("\\n", "\n\t\t") + "\n";
    }

    /**
     * Prints out the standard greeting message when the bot is first started.
     */
    public String greet() {
        return printReply("Hello! I'm Duke\nWhat can I do for you?");
    }

    /**
     * Prints out the standard message when the bot is being ended.
     */
    public String bye() {
        return printReply("Bye. Hope to see you again soon!");
    }
}