import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
    private int populationSize;
    private int timeBudget;
    private int tournamentSize;
    private double mutationProbability;
    private int maxHeight;
    private DataHolder dataHolder;
    private ArrayList<ExpressionNode> population;

    public GeneticAlgorithm(int populationSize, int timeBudget, int tournamentSize, double mutationProbability, int maxHeight, DataHolder dataHolder) {
        this.populationSize = populationSize;
        this.timeBudget = timeBudget;
        this.tournamentSize = tournamentSize;
        this.mutationProbability = mutationProbability;
        this.maxHeight = maxHeight;
        this.dataHolder = dataHolder;
    }

    public void generateInitialPopulation() {
        this.population = new ArrayList<>();
        for (int i = 0; i < this.populationSize; i++) {
            ExpressionNode individual = new ExpressionNode().randomInit(this.maxHeight);
            individual.setSquareError(this.dataHolder.evaluateExpression(individual));
            individual.setSize(individual.getExpressionSize());
            this.population.add(individual);
        }
    }

    public void generateMixedInitialPopulation() {
        this.population = new ArrayList<>();
        for (int i = 0; i < this.populationSize; i++) {
            ExpressionNode individual = new ExpressionNode();
            if (i < this.populationSize / 2) {
                individual.randomInit(this.maxHeight);
            } else {
                individual.randomGrowthInit(this.maxHeight, this.maxHeight);
            }

            individual.setSquareError(this.dataHolder.evaluateExpression(individual));
            individual.setSize(individual.getExpressionSize());
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
//        this.generateInitialPopulation();
        this.generateMixedInitialPopulation();
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) / 1000.0 < this.timeBudget) {
            bestIndividual = this.getBestIndividual();
            generationsCount++;
            ArrayList<ExpressionNode> newPopulation = new ArrayList<>();
//            newPopulation.add(bestIndividual);
            for (int i = 0; i < this.populationSize; i++) {
                ExpressionNode parentX = this.tournamentSelection();
                ExpressionNode parentY = this.tournamentSelection();
                ExpressionNode offspring = this.matchedCrossover(parentX, parentY);
                offspring.mutate(this.mutationProbability);
                offspring.setSquareError(this.dataHolder.evaluateExpression(offspring));
                offspring.setSize(offspring.getExpressionSize());
                newPopulation.add(offspring);
            }
            this.population = newPopulation;
//            System.out.println(String.format("Generation count: %d; Fitness: %.4f; Square Error: %.4f; Size: %d", generationsCount, bestIndividual.getFitness(), bestIndividual.getSquareError(), bestIndividual.getSize()));
        }
        return bestIndividual;
    }
}

