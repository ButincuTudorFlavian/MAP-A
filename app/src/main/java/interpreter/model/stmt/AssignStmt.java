package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.IDictionary;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;

public record AssignStmt(String id, IExp exp) implements IStmt {

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IDictionary<String, IValue> symTable = state.getSymTable();

        if (!symTable.isDefined(id)) {
            throw new StatementException(
                    "variable " + id + " is not declared"
            );
        }

        IValue value = exp.eval(symTable);
        IValue oldValue = symTable.lookup(id);

        if (!value.getType().equals(oldValue.getType())) {
            throw new StatementException(
                    "type of expression does not match type of variable " + id
            );
        }

        symTable.update(id, value);
        return state;
    }

    @Override
    public String toString() {
        return id + " = " + exp;
    }
}