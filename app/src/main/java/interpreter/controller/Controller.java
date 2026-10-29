
package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {

    private final IRepository repo;
    private final boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public PrgState oneStep(PrgState state) throws MyException {

        IStack<IStmt> stack = state.getExeStack();

        if (stack.isEmpty()) {
            throw new StatementException(
                    "program state stack is empty"
            );
        }

        IStmt currentStatement = stack.pop();

        return currentStatement.execute(state);
    }

    public void allStep() throws MyException {

        PrgState program = repo.getCrtPrg();

        if (displayFlag) {
            display(program);
        }

        while (!program.getExeStack().isEmpty()) {

            program = oneStep(program);

            if (displayFlag) {
                display(program);
            }
        }
    }

    private void display(PrgState state) {

        System.out.println("--------------------------");
        System.out.println(state);
        System.out.println("--------------------------");
    }
}
