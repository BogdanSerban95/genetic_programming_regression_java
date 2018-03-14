import de.tudresden.inf.lat.jsexp.Sexp;
import de.tudresden.inf.lat.jsexp.SexpList;

import java.util.ArrayList;
import java.util.Random;

public class ExpressionNode implements Cloneable {
    private String root;
    private ArrayList<ExpressionNode> children;
    private double squareError;
    private int size;

    public ExpressionNode() {
        this.squareError = Double.MAX_VALUE;
        this.children = new ArrayList<>();
        this.size = 0;
    }

    public void setSquareError(double squareError) {
        this.squareError = squareError;
    }

    public double getSquareError() {
        return squareError;
    }

    public double getFitness() {
        return 0.25 * this.size + this.squareError;
//        Math.sqrt(this.size) +
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    public ExpressionNode getChild(int index) {
        return this.children.get(index);
    }

    public void setChildAt(int index, ExpressionNode child) {
        this.children.set(index, child);
    }

    public void setSize(int expressionSize) {
        this.size = expressionSize;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setChildren(ArrayList<ExpressionNode> children) {
        this.children = children;
    }

    public int getSize() {
        return size;
    }

    public ExpressionNode fromExpression(Sexp expression) {
        if (expression.getClass() == SexpList.class) {
            this.root = expression.get(0).toString();
            for (int i = 1; i < expression.getLength(); i++) {
                this.children.add(new ExpressionNode().fromExpression(expression.get(i)));
            }
        } else {
            this.root = expression.toString();
            this.children = null;
        }
        return this;
    }

    public ExpressionNode randomInit(int maxHeight) {
        ExpressionGenerator generator = ExpressionGenerator.getInstance();
        if (maxHeight != 0) {
            this.root = generator.randomOperator();
            for (int i = 0; i < generator.getOperatorArity(this.root); i++) {
                this.children.add(new ExpressionNode().randomInit(maxHeight - 1));
            }
        } else {
            this.root = generator.randomTerminal();
            this.children = null;
        }
        return this;
    }

    public ExpressionNode randomGrowthInit(int maxHeight, int currentHeight) {
        Random rnd = RandomWrapper.getRandom();
        ExpressionGenerator generator = ExpressionGenerator.getInstance();
        if (currentHeight == maxHeight) {
            this.root = generator.randomOperator();
            for (int i = 0; i < generator.getOperatorArity(this.root); i++) {
                this.children.add(new ExpressionNode().randomGrowthInit(maxHeight, currentHeight - 1));
            }
        } else if (currentHeight != 0) {
            if (rnd.nextDouble() < 0.5) {
                this.root = generator.randomOperator();
                for (int i = 0; i < generator.getOperatorArity(this.root); i++) {
                    this.children.add(new ExpressionNode().randomGrowthInit(maxHeight, currentHeight - 1));
                }
            } else {
                this.root = generator.randomTerminal();
                this.children = null;
            }
        } else {
            this.root = generator.randomTerminal();
            this.children = null;
        }
        return this;
    }

    public ExpressionNode mutate(double mutationProbability) {
        Random rnd = RandomWrapper.getRandom();
        if (rnd.nextDouble() < mutationProbability) {
            return new ExpressionNode().randomInit(3);
        } else if (this.children == null) {
            return this;
        } else {
            int childIdx = rnd.nextInt(this.children.size());
            this.children.set(childIdx, this.children.get(childIdx).mutate(mutationProbability));
        }
        return this;
    }

    public String toExpressionString() {
        if (this.children == null) {
            return this.root;
        } else {
            ArrayList<String> childStrings = new ArrayList<>();
            for (int i = 0; i < this.children.size(); i++) {
                childStrings.add(this.children.get(i).toExpressionString());
            }
            return "(" + this.root + " " + String.join(" ", childStrings) + ")";
        }
    }

    public double evaluateExpression(double[] x) {
        if (this.children == null) {
            return Double.parseDouble(this.root);
        } else {
            ArrayList<Double> args = new ArrayList<>();
            for (ExpressionNode ex : this.children) {
                args.add(ex.evaluateExpression(x));
            }
            return this.evaluateOperator(x, args);
        }
    }

    public int getExpressionSize() {
        if (this.children == null) {
            return 1;
        } else {
            int sum = 1;
            for (ExpressionNode exp : this.children) {
                sum += exp.getExpressionSize();
            }
            return sum;
        }
    }

    public double evaluateOperator(double[] x, ArrayList<Double> args) {
        int n = x.length;
        switch (this.root) {
            case "add":
                return args.get(0) + args.get(1);
            case "sub":
                return args.get(0) - args.get(1);
            case "mul":
                return args.get(0) * args.get(1);
            case "div":
                return args.get(1) == 0 ? 0 : args.get(0) / args.get(1);
            case "pow":
                return args.get(0) < 0 && args.get(1).intValue() != args.get(1) || args.get(0) == 0 ? 0 : Math.pow(args.get(0), args.get(1));
            case "sqrt":
                return args.get(0) >= 0 ? Math.sqrt(args.get(0)) : 0;
            case "log":
                return args.get(0) > 0 ? Math.log(args.get(0)) / Math.log(2) : 0;
            case "exp":
                return Math.exp(args.get(0));
            case "max":
                return Math.max(args.get(0), args.get(1));
            case "ifleq":
                return args.get(0) <= args.get(1) ? args.get(2) : args.get(3);
            case "data":
                return x[Math.floorMod(args.get(0).intValue(), n)];
            case "diff":
                int i = Math.floorMod(args.get(0).intValue(), n);
                int j = Math.floorMod(args.get(1).intValue(), n);
                return x[i] - x[j];
            case "avg":
                int k = Math.floorMod(args.get(0).intValue(), n);
                int l = Math.floorMod(args.get(1).intValue(), n);
                int size = Math.abs(k - l);
                size = size != 0 ? size : 1;
                double sum = 0;
                for (int idx = Math.min(k, l); idx < Math.max(k, l); idx++) {
                    sum += x[idx];
                }
                return sum / size;
        }
        return 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ExpressionNode node = new ExpressionNode();
        node.setRoot(this.root);
        ArrayList<ExpressionNode> cloneChildren = null;
        if (this.children != null) {
            cloneChildren = new ArrayList<>();
            for (ExpressionNode child : this.children) {
                cloneChildren.add((ExpressionNode) child.clone());
            }
        }
        node.setChildren(cloneChildren);
        return node;
    }

    public boolean hasChildren() {
        return this.children != null;
    }
}
