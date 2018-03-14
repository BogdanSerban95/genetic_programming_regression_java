import de.tudresden.inf.lat.jsexp.Sexp;
import de.tudresden.inf.lat.jsexp.SexpFactory;
import de.tudresden.inf.lat.jsexp.SexpParserException;

import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        ArgParser parser = ArgParser.getInstance();
        int question = Integer.parseInt(parser.getArgument(args, "-question"));
        switch (question) {
            case 1:
                String[] splitX = parser.getArgument(args, "-x").split(" ");
                int n = Integer.parseInt(parser.getArgument(args, "-n"));
                double[] x = new double[n];
                for (int i = 0; i < n; i++) {
                    x[i] = Double.parseDouble(splitX[i]);
                }
                String expressionString = parser.getArgument(args, "-expr");
                Sexp parsedExpression = null;
                try {
                    parsedExpression = SexpFactory.parse(new StringReader(expressionString));
                    ExpressionNode node = new ExpressionNode().fromExpression(parsedExpression);
                    System.out.println(node.evaluateExpression(x));
                } catch (SexpParserException | IOException e) {
                    System.out.println(0);
                    e.printStackTrace();
                }

                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
