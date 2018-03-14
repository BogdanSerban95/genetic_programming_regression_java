import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    private int populationSize;
    private int timeBudget;
    private int tournamentSize;
    private double mutationRate;
    private int maxHeight;
    private DataHolder dataHolder;
    private ArrayList<ExpressionNode> population;

    public GeneticAlgorithm(int populationSize, int timeBudget, int tournamentSize, double mutationRate, int maxHeight, DataHolder dataHolder) {
        this.populationSize = populationSize;
        this.timeBudget = timeBudget;
        this.tournamentSize = tournamentSize;
        this.mutationRate = mutationRate;
        this.maxHeight = maxHeight;
        this.dataHolder = dataHolder;
    }

    public void generateInitialPopulation() {
        this.population = new ArrayList<>();
        for (int i = 0; i < this.populationSize; i++) {
            ExpressionNode individual = new ExpressionNode().randomInit(this.maxHeight);
            individual.setFitness(this.dataHolder.evaluateExpression(individual));
            this.population.add(individual);
        }
    }

    public ExpressionNode tournamentSelection() {
        Random rnd = RandomWrapper.getRandom();
        ExpressionNode winner = this.population.get(0);
        ArrayList<ExpressionNode> contestants = new ArrayList<>();
        contestants.add(winner);
        for (int i = 0; i < this.tournamentSize; i++) {
            int contestantIdx = rnd.nextInt(this.populationSize);
            ExpressionNode contestant = this.population.get(contestantIdx);
            if (contestant.getFitness() < winner.getFitness()) {
                winner = contestant;
                contestants.clear();
                contestants.add(contestant);
            } else if (contestant.getFitness() == winner.getFitness()) {
                contestants.add(contestant);
            }
        }
        return contestants.get(rnd.nextInt(contestants.size()));
    }

    public ExpressionNode matchedCrossover(ExpressionNode parentX, ExpressionNode parentY) throws CloneNotSupportedException {
        Random rnd = RandomWrapper.getRandom();
        ExpressionNode offspringX = (ExpressionNode) parentX.clone();
        ExpressionNode offspringY = (ExpressionNode) parentY.clone();
        ArrayList<ArrayList<ExpressionNode>> commonBranches = this.getCommonBranches(offspringX, offspringY);
        ArrayList<ExpressionNode> branchesX = commonBranches.get(0);
        ArrayList<ExpressionNode> branchesY = commonBranches.get(1);
        int switchRootIdx = rnd.nextInt(branchesX.size());
        int switchChildIdx = rnd.nextInt(
                Math.min(
                        branchesX.get(switchRootIdx).getChildrenCount(),
                        branchesY.get(switchRootIdx).getChildrenCount()
                )
        );
        ExpressionNode aux = branchesX.get(switchRootIdx).getChild(switchChildIdx);
        branchesX.get(switchRootIdx).setChildAt(switchChildIdx, branchesY.get(switchRootIdx).getChild(switchChildIdx));
        branchesY.get(switchRootIdx).setChildAt(switchChildIdx, aux);
        return offspringX;
    }

    public ArrayList<ArrayList<ExpressionNode>> getCommonBranches(ExpressionNode nodeX, ExpressionNode nodeY) {
        ArrayList<ExpressionNode> commonX = new ArrayList<>();
        ArrayList<ExpressionNode> commonY = new ArrayList<>();
        ArrayList<ArrayList<ExpressionNode>> results = new ArrayList<>();
        results.add(commonX);
        results.add(commonY);
        commonX.add(nodeX);
        commonY.add(nodeY);

        if (!nodeX.hasChildren() || !nodeY.hasChildren()) {
            commonX.clear();
            commonY.clear();
            return results;
        }

        if (nodeX.getChildrenCount() == nodeY.getChildrenCount()) {

            for (int i = 0; i < nodeX.getChildrenCount(); i++) {
                ArrayList<ArrayList<ExpressionNode>> commonChildren = this.getCommonBranches(nodeX.getChild(i), nodeY.getChild(i));
                commonX.addAll(commonChildren.get(0));
                commonY.addAll(commonChildren.get(1));
            }
        }
        return results;
    }

    public ExpressionNode getBestIndividual() {
        ExpressionNode bestIndividual = new ExpressionNode();
        for (ExpressionNode individual : this.population) {
            if (individual.getFitness() < bestIndividual.getFitness()) {
                bestIndividual = individual;
            }
        }

        return bestIndividual;
    }

    public ExpressionNode run() throws CloneNotSupportedException {
        ExpressionNode bestIndividual = null;
        int generationsCount = 0;
        this.generateInitialPopulation();
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) / 1000.0 < this.timeBudget) {
            bestIndividual = this.getBestIndividual();
            generationsCount++;
            ArrayList<ExpressionNode> newPopulation = new ArrayList<>();
            newPopulation.add(bestIndividual);
            for (int i = 1; i < this.populationSize; i++) {
                ExpressionNode parentX = this.tournamentSelection();
                ExpressionNode parentY = this.tournamentSelection();
                ExpressionNode offspring = this.matchedCrossover(parentX, parentY);
                offspring.mutate(this.mutationRate);
                offspring.setFitness(this.dataHolder.evaluateExpression(offspring));
                newPopulation.add(offspring);
            }
            this.population = newPopulation;
        }
        System.out.println(String.format("Generation count: %d; Best fitness: %.4f", generationsCount, bestIndividual.getFitness()));
        return bestIndividual;
    }
}

