import java.util.ArrayList;
import java.util.HashMap;

public class ExpressionGenerator {
    private static ExpressionGenerator instance;
    private ArrayList<String> operators;
    private static HashMap<String, Integer> operatorArity;


    private ExpressionGenerator() {
        operatorArity = new HashMap<>();
        operatorArity.put("add", 2);
        operatorArity.put("sub", 2);
        operatorArity.put("mul", 2);
        operatorArity.put("div", 2);
        operatorArity.put("pow", 2);
        operatorArity.put("sqrt", 1);
        operatorArity.put("log", 1);
        operatorArity.put("exp", 1);
        operatorArity.put("max", 2);
        operatorArity.put("ifleq", 4);
        operatorArity.put("data", 1);
        operatorArity.put("diff", 2);
        operatorArity.put("avg", 2);
        operators = new ArrayList<>(operatorArity.keySet());
    }

    public static ExpressionGenerator getInstance() {
        if (instance == null) {
            instance = new ExpressionGenerator();
        }
        return instance;
    }

    public String randomOperator() {
        int pos = RandomWrapper.getRandom().nextInt(operators.size());
        return operators.get(pos);
    }

    public int getOperatorArity(String operator) {
        return operatorArity.get(operator);
    }

    public String randomTerminal() {
        return String.valueOf(RandomWrapper.getRandom().nextGaussian() * 5);
    }
}
