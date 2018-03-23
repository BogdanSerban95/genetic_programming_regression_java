import de.tudresden.inf.lat.jsexp.SexpFactory;
import de.tudresden.inf.lat.jsexp.SexpParserException;

public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {
//        DataHolder holder = new DataHolder(11, 506);
//        holder.loadData("./data/data.txt");
//        GeneticAlgorithm ga = new GeneticAlgorithm(1000, 20, 3, 0.5, 5, holder);
//        ga.generateInitialPopulation();
//        ExpressionNode node1 = ga.tournamentSelection();
//        ExpressionNode node2 = ga.tournamentSelection();
//        ExpressionNode node3 = ga.matchedCrossover(node1, node2);
//        ExpressionNode best = ga.run();
//        System.out.println(best.toExpressionString());

//        ExpressionNode node = new ExpressionNode().randomGrowthInit(3, 3);
//        System.out.println("Done...");

//        for(int i = 0; i< 100; i++) {
//            ExpressionNode node = new ExpressionNode().randomInit(5);
//            node.setSquareError(holder.evaluateExpression(node));
//
//            System.out.println(node.getSquareError());
//        }
        ExpressionNode node = null;
        try {
            node = new ExpressionNode().fromExpression(
                    SexpFactory.parse("(ifleq (add -1.3380208262 1.60648333558) 1.89541358247 (diff -0.860472547611 0.734637114803) 1.74150349214)")
            );
        } catch (SexpParserException e) {
            e.printStackTrace();
        }
        double[] x = new double[] {0.827627074139, 0.262790784083, -1.25456718259, 0.416556688649, -0.641856095987, 1.88517200234, 1.01923198347, 0.743329448121, 0.14758173541, 0.304279942623};
        System.out.println(node.evaluateExpression(x));
    }
}
