package interpreter.model.stmt;

import interpreter.model.PrgState;

public class NopStmt implements IStmt {

    @Override
    public PrgState execute(PrgState state) {
        return state;
    }

    @Override
    public String toString() {
        return "nop";
    }
}