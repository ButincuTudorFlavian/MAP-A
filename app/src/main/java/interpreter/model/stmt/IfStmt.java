package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;

public record IfStmt(
        IExp exp,
        IStmt thenStmt,
        IStmt elseStmt
) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue conditionValue = exp.eval(state.getSymTable());

        if (!(conditionValue instanceof BoolValue condition)) {
            throw new StatementException(
                    "if condition is not boolean"
            );
        }

        if (condition.val()) {
            state.getExeStack().push(thenStmt);
        } else {
            state.getExeStack().push(elseStmt);
        }

        return state;
    }

    @Override
    public String toString() {
        return "if (" + exp + ") then (" + thenStmt + ") else (" + elseStmt + ")";
    }
}