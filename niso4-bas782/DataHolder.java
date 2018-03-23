import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DataHolder {
    private int n;
    private int m;
    private double[] y;
    private double[][] x;

    public DataHolder(int n, int m) {
        this.n = n;
        this.m = m;
        this.y = new double[m];
        this.x = new double[m][n];
    }

    public void loadData(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                int lineCount = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] splitLine = line.split("\t");
                    for (int i = 0; i < n; i++) {
                        this.x[lineCount][i] = Double.parseDouble(splitLine[i]);
                    }
                    this.y[lineCount] = Double.parseDouble(splitLine[n]);
                    lineCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double evaluateExpression(ExpressionNode expression) {
        double errorSum = 0;
        for (int i = 0; i < this.m; i++) {
            double expressionValue = expression.evaluateExpression(this.x[i]);
            errorSum += Math.pow(this.y[i] - expressionValue, 2);
        }
        return errorSum / this.m;
    }
}
