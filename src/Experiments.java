import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Experiments {
    public static void main(String[] args) throws CloneNotSupportedException {
        ArgParser parser = ArgParser.getInstance();
        int experiment = Integer.parseInt(parser.getArgument(args, "-experiment"));

        ArrayList<ArrayList<String>> results = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        DataHolder holder = new DataHolder(11, 350);
        holder.loadData(new File(".").getAbsolutePath().replace(".", "..\\") + "./data/data.txt");
        switch (experiment) {
            case 1:
//                Population size experiment
                for (int i = 50; i <= 950; i += 100) {
                    System.out.println("Pop size: " + i);
                    ArrayList<String> expResults = new ArrayList<>();
                    GeneticAlgorithm ga = new GeneticAlgorithm(i, 15, 3, 0.2, 5, holder);
                    labels.add(String.valueOf(i));
                    for (int j = 0; j < 100; j++) {
                        if ((j + 1) % 10 == 0) {
                            System.out.println("Step: " + (j + 1));
                        }
                        expResults.add(String.valueOf(ga.run().getSquareError()));
                    }
                    results.add(expResults);
                }
                writeResultsToCsv("..\\results\\population_size_50.csv", results, labels);
                break;
            case 2:
//                Mutation probability experiment
                for (double i = 0.05; i <= 0.6; i += 0.05) {
                    System.out.println("Mutation probability: " + i);
                    ArrayList<String> expResults = new ArrayList<>();
                    GeneticAlgorithm ga = new GeneticAlgorithm(400, 5, 3, i, 5, holder);
                    labels.add(String.valueOf(i));
                    for (int j = 0; j < 100; j++) {
                        if ((j + 1) % 10 == 0) {
                            System.out.println("Step: " + (j + 1));
                        }
                        expResults.add(String.valueOf(ga.run().getSquareError()));
                    }
                    results.add(expResults);
                }
                writeResultsToCsv("..\\results\\mutation_probability.csv", results, labels);
                break;
            case 3:
//                Max height experiment
                for (int i = 2; i <= 6; i += 1) {
                    System.out.println("Max height: " + i);
                    ArrayList<String> expResults = new ArrayList<>();
                    GeneticAlgorithm ga = new GeneticAlgorithm(400, 2, 3, 0.2, i, holder);
                    labels.add(String.valueOf(i));
                    for (int j = 0; j < 100; j++) {
                        if ((j + 1) % 10 == 0) {
                            System.out.println("Step: " + (j + 1));
                        }
                        expResults.add(String.valueOf(ga.run().getSquareError()));
                    }
                    results.add(expResults);
                }
                writeResultsToCsv("..\\results\\max_height.csv", results, labels);
                break;
            case 4:
//                Tournament size experiment
                for (int i = 2; i <= 5; i += 1) {
                    System.out.println("Tournament size: " + i);
                    ArrayList<String> expResults = new ArrayList<>();
                    GeneticAlgorithm ga = new GeneticAlgorithm(400, 15, i, 0.2, 5, holder);
                    labels.add(String.valueOf(i));
                    for (int j = 0; j < 100; j++) {
                        if ((j + 1) % 10 == 0) {
                            System.out.println("Step: " + (j + 1));
                        }
                        expResults.add(String.valueOf(ga.run().getSquareError()));
                    }
                    results.add(expResults);
                }
                writeResultsToCsv("..\\results\\tournament_size.csv", results, labels);
                break;
        }
    }

    private static void writeResultsToCsv(String fileName, ArrayList<ArrayList<String>> results, ArrayList<String> labels) {
        try (FileWriter fr = new FileWriter(fileName)) {
            try (BufferedWriter br = new BufferedWriter(fr)) {
                for (int i = 0; i < results.size(); i++) {
                    br.write(labels.get(i) + "," + String.join(",", results.get(i)));
                    br.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
