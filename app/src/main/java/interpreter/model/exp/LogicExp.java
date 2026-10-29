
package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.BoolValue;

public record LogicExp(
        LogicOp op,
        IExp left,
        IExp right
) implements IExp {

    @Override
    public IValue eval(IDictionary<String, IValue> symTable)
            throws MyException {

        IValue leftValue = left.eval(symTable);

        if (!(leftValue instanceof BoolValue b1)) {
            throw new ExpressionException(
                    "first operand is not a boolean"
            );
        }

        IValue rightValue = right.eval(symTable);

        if (!(rightValue instanceof BoolValue b2)) {
            throw new ExpressionException(
                    "second operand is not a boolean"
            );
        }

        return switch (op) {

            case AND -> new BoolValue(
                    b1.val() && b2.val()
            );

            case OR -> new BoolValue(
                    b1.val() || b2.val()
            );
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}
