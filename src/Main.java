import de.tudresden.inf.lat.jsexp.Sexp;
import de.tudresden.inf.lat.jsexp.SexpFactory;
import de.tudresden.inf.lat.jsexp.SexpParserException;
import de.tudresden.inf.lat.jsexp.SexpString;

import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        String testStr = "(min (max 2 3) (log (max 2 2)))";
        try {
            Sexp parsedExpression = SexpFactory.parse(new StringReader(testStr));
            System.out.println(parsedExpression);
        } catch (SexpParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
