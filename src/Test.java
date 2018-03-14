import de.tudresden.inf.lat.jsexp.SexpFactory;
import de.tudresden.inf.lat.jsexp.SexpParserException;

public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {
        DataHolder holder = new DataHolder(11, 506);
        holder.loadData("./data/data.txt");
        GeneticAlgorithm ga = new GeneticAlgorithm(1000, 20, 3, 0.5, 5, holder);
//        ga.generateInitialPopulation();
//        ExpressionNode node1 = ga.tournamentSelection();
//        ExpressionNode node2 = ga.tournamentSelection();
//        ExpressionNode node3 = ga.matchedCrossover(node1, node2);
        ExpressionNode best = ga.run();
        System.out.println(best.toExpressionString());

//        ExpressionNode node = new ExpressionNode().randomGrowthInit(3, 3);
        System.out.println("Done...");

//        for(int i = 0; i< 100; i++) {
//            ExpressionNode node = new ExpressionNode().randomInit(5);
//            node.setSquareError(holder.evaluateExpression(node));
//
//            System.out.println(node.getSquareError());
//        }
//        ExpressionNode node = null;
//        try {
//            node = new ExpressionNode().fromExpression(SexpFactory.parse("(div (div (div (sub (div 8.139924900620342 -5.098384242465786) (pow 0.9218369903510897 4.345734730536749)) (div (max 4.294164935432524 1.7568358002653852) (exp 3.9717843423840242))) (sqrt (add (sub 2.738612323683365 4.0370663658367905) (data -5.6997799958436115)))) (mul (data (mul (sub -7.779574415670831 -2.181117818951145) (div -0.7303312176532695 -2.620707430624053))) (sqrt (sqrt (mul 4.9116816577049915 -0.20338549960005253)))))"));
//        } catch (SexpParserException e) {
//            e.printStackTrace();
//        }
    }
}
