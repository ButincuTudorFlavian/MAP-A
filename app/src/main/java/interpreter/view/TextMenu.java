
package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.Repository;

import java.util.Scanner;

public class TextMenu {

    private final Scanner scanner = new Scanner(System.in);
    private IStmt selectedProgram;

    public void run() {
        while (true) {
            System.out.println("\n===== TOY INTERPRETER =====");
            System.out.println("1. Select a program");
            System.out.println("2. Run selected program");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> selectProgram();
                case "2" -> executeProgram();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println(
                        "Invalid option. Try again."
                );
            }
        }
    }

    private void selectProgram() {
        System.out.println("\nAvailable programs:");
        System.out.println("1. " + Examples.EX1);
        System.out.println("2. " + Examples.EX2);
        System.out.println("3. " + Examples.EX3);
        System.out.print("Select program: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> selectedProgram = Examples.EX1;
            case "2" -> selectedProgram = Examples.EX2;
            case "3" -> selectedProgram = Examples.EX3;
            default -> {
                System.out.println("Invalid program.");
                return;
            }
        }

        System.out.println("Program selected successfully!");
    }

    private void executeProgram() {
        if (selectedProgram == null) {
            System.out.println("Select a program first!");
            return;
        }

        PrgState state = new PrgState(
                new MyStack<>(),
                new MyDictionary<>(),
                new MyList<>(),
                selectedProgram
        );

        Controller controller = new Controller(
                new Repository(state),
                true
        );

        try {
            System.out.println("\nExecuting program...");
            controller.allStep();

            System.out.println("Execution finished!");
            System.out.println("Final output: " + state.getOut());

        } catch (MyException exception) {
            System.out.println(
                    "Execution error: " + exception.getMessage()
            );
        }
    }
}
