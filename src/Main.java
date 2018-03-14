import de.tudresden.inf.lat.jsexp.Sexp;
import de.tudresden.inf.lat.jsexp.SexpFactory;
import de.tudresden.inf.lat.jsexp.SexpParserException;
import de.tudresden.inf.lat.jsexp.SexpString;

import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
//        String testStr = "(avg 3 1)";
//        try {
//            Sexp parsedExpression = SexpFactory.parse(new StringReader(testStr));
//            ExpressionNode node = new ExpressionNode().fromStringExpression(parsedExpression);
//
//            double[] x = new double[]{1, 2, 3, 4, 5};
//            System.out.println(node.evaluateExpression(x));
//
//
//        } catch (SexpParserException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ExpressionNode node = new ExpressionNode().randomInit(3);
        System.out.println(node.toExpressionString());
    }
}
