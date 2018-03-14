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
                n = Integer.parseInt(parser.getArgument(args, "-n"));
                int m = Integer.parseInt(parser.getArgument(args, "-m"));
                String fileName = parser.getArgument(args, "-data");
                expressionString = parser.getArgument(args, "-expr");
                try {
                    parsedExpression = SexpFactory.parse(expressionString);
                    ExpressionNode node = new ExpressionNode().fromExpression(parsedExpression);
                    DataHolder holder = new DataHolder(n, m);
                    holder.loadData(fileName);
                    System.out.println(holder.evaluateExpression(node));
                } catch (SexpParserException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                int popSize = Integer.parseInt(parser.getArgument(args, "-lambda"));
                n = Integer.parseInt(parser.getArgument(args, "-n"));
                m = Integer.parseInt(parser.getArgument(args, "-m"));
                int time_budget = Integer.parseInt(parser.getArgument(args, "-time_budget"));
                fileName = parser.getArgument(args, "-data");
                DataHolder dataHolder = new DataHolder(n, m);
                dataHolder.loadData(fileName);
                GeneticAlgorithm ga = new GeneticAlgorithm(popSize, time_budget, 2, 0.1, 5, dataHolder);
                try {
                    ExpressionNode result = ga.run();
                    System.out.println(result.toExpressionString());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    System.out.println("()");
                }
                break;
        }
    }
}
