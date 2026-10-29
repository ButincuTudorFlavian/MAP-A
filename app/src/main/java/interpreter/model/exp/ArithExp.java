
package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record ArithExp(
        ArithOp op,
        IExp left,
        IExp right
) implements IExp {

    @Override
    public IValue eval(IDictionary<String, IValue> symTable)
            throws MyException {

        IValue leftValue = left.eval(symTable);

        if (!(leftValue instanceof IntValue n1)) {
            throw new ExpressionException(
                    "first operand is not an integer"
            );
        }

        IValue rightValue = right.eval(symTable);

        if (!(rightValue instanceof IntValue n2)) {
            throw new ExpressionException(
                    "second operand is not an integer"
            );
        }

        return switch (op) {

            case ADD -> new IntValue(
                    n1.val() + n2.val()
            );

            case SUB -> new IntValue(
                    n1.val() - n2.val()
            );

            case MUL -> new IntValue(
                    n1.val() * n2.val()
            );

            case DIV -> {
                if (n2.val() == 0) {
                    throw new ExpressionException(
                            "division by zero"
                    );
                }

                yield new IntValue(
                        n1.val() / n2.val()
                );
            }
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}
