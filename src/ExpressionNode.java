import de.tudresden.inf.lat.jsexp.Sexp;
import de.tudresden.inf.lat.jsexp.SexpList;

import java.util.ArrayList;
import java.util.Random;

public class ExpressionNode {
    private String root;
    private ArrayList<ExpressionNode> children;
    private double fitness;

    public ExpressionNode() {
        children = new ArrayList<>();
    }

    public ExpressionNode fromStringExpression(Sexp expression) {
        if (expression.getClass() == SexpList.class) {
            this.root = expression.get(0).toString();
            for (int i = 1; i < expression.getLength(); i++) {
                this.children.add(new ExpressionNode().fromStringExpression(((SexpList) expression).get(i)));
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

    public ExpressionNode mutate(double chi) {
        Random rnd = RandomWrapper.getRandom();
        if (rnd.nextDouble() < chi) {
            return new ExpressionNode().randomInit(3);
        } else if (this.children == null) {
            return this;
        } else {
            int childIdx = rnd.nextInt(this.children.size());
            this.children.set(childIdx, this.children.get(childIdx).mutate(chi));
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
                return args.get(0) != 0 ? Math.sqrt(args.get(0)) : 0;
            case "log":
                return args.get(0) > 0 ? Math.log(args.get(0)) / Math.log(2) : 0;
            case "exp":
                return Math.exp(args.get(0));
            case "max":
                return Math.max(args.get(0), args.get(1));
            case "ifleq":
                return args.get(0) <= args.get(1) ? args.get(2) : args.get(3);
            case "data":
                return x[args.get(0).intValue() % n];
            case "diff":
                int i = args.get(0).intValue() % n;
                int j = args.get(1).intValue() % n;
                return x[i] - x[j];
            case "avg":
                int k = args.get(0).intValue() % n;
                int l = args.get(1).intValue() % n;
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
}
