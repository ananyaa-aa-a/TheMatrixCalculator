import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class MatrixCalculator {
    private double[][] myMatrix;
    private int rows;
    private int cols;
    private JTextField[][] inputFields;

    private MatrixCalculator() {
        createMatrix();
    }

    private void createMatrix() {
        String rowsStr = JOptionPane.showInputDialog("Enter the number of rows:");
        String colsStr = JOptionPane.showInputDialog("Enter the number of columns:");

        if (isValidNumber(rowsStr) && isValidNumber(colsStr)) {
            rows = Integer.parseInt(rowsStr);
            cols = Integer.parseInt(colsStr);
            myMatrix = new double[rows][cols];
            inputFields = new JTextField[rows][cols];

            JPanel inputPanel = new JPanel(new GridLayout(rows, cols, 5, 5);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    inputFields[i][j] = new JTextField(6);
                    inputPanel.add(inputFields[i][j]);
                }
            }

            int option = JOptionPane.showConfirmDialog(null, inputPanel, "Enter Matrix Elements", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                fillMatrixFromInputFields();
                showMatrix(myMatrix, "Your Matrix");
                performMatrixOperations();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter valid row and column numbers.");
        }
    }

    private boolean isValidNumber(String str) {
        return str != null && str.matches("\\d+");
    }

    private void fillMatrixFromInputFields() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String input = inputFields[i][j].getText();
                if (isValidDouble(input)) {
                    myMatrix[i][j] = Double.parseDouble(input);
                }
            }
        }
    }

    private boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showMatrix(double[][] matrix, String title) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            JOptionPane.showMessageDialog(null, "The matrix is empty!");
            return;
        }

        StringBuilder matrixStr = new StringBuilder(title + ":\n");
        DecimalFormat df = new DecimalFormat("#.##");

        for (double[] row : matrix) {
            for (double val : row) {
                matrixStr.append(df.format(val)).append("\t");
            }
            matrixStr.append("\n");
        }

        JTextArea textArea = new JTextArea(matrixStr.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, "Matrix Viewer", JOptionPane.INFORMATION_MESSAGE);
    }

    private void performMatrixOperations() {
        String[] options = {"Matrix Multiplication", "Matrix Transpose", "Matrix Determinant", "Matrix Inverse", "Matrix Rank", "Solve System of Linear Equations"};
        String choice = (String) JOptionPane.showInputDialog(null, "Select an operation", "Matrix Operations", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice != null) {
            switch (choice) {
                case "Matrix Multiplication":
                    performMatrixMultiplication();
                    break;
                case "Matrix Transpose":
                    performMatrixTransposition();
                    break;
                case "Matrix Determinant":
                    calculateDeterminant();
                    break;
                case "Matrix Inverse":
                    calculateMatrixInverse();
                    break;
                case "Matrix Rank":
                    calculateMatrixRank();
                    break;
                case "Solve System of Linear Equations":
                    solveSystemOfLinearEquations();
                    break;
                default:
                    break;
            }
        }
    }

    private void performMatrixMultiplication() {
        double[][] secondMatrix = getMatrix("Enter Second Matrix", cols, rows);

        if (secondMatrix != null) {
            if (cols != secondMatrix.length) {
                JOptionPane.showMessageDialog(null, "Matrix multiplication is not possible. " +
                        "Number of columns in the first matrix must be equal to the number of rows in the second matrix.");
            } else {
                double[][] resultMatrix = multiplyMatrices(myMatrix, secondMatrix);
                showMatrix(resultMatrix, "Result Matrix");
            }
        }
    }

    private void performMatrixTransposition() {
        double[][] resultMatrix = transposeMatrix(myMatrix);
        showMatrix(resultMatrix, "Transposed Matrix");
    }

    private void calculateDeterminant() {
        if (rows != cols) {
            JOptionPane.showMessageDialog(null, "Determinant can only be calculated for a square matrix.");
        } else {
            double determinant = calculateMatrixDeterminant(myMatrix);
            JOptionPane.showMessageDialog(null, "Determinant: " + determinant);
        }
    }

    private void calculateMatrixInverse() {
        if (rows != cols) {
            JOptionPane.showMessageDialog(null, "Matrix inverse can only be calculated for a square matrix.");
        } else {
            double[][] inverseMatrix = calculateInverse(myMatrix);
            showMatrix(inverseMatrix, "Inverse Matrix");
        }
    }

    private void calculateMatrixRank() {
        int rank = calculateMatrixRank(myMatrix);
        JOptionPane.showMessageDialog(null, "Matrix Rank: " + rank);
    }

    private double[][] getMatrix(String title, int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        inputFields = new JTextField[rows][cols];
        JPanel inputPanel = new JPanel(new GridLayout(rows, cols, 5, 5));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                inputFields[i][j] = new JTextField(6);
                inputPanel.add(inputFields[i][j]);
            }
        }

        int option = JOptionPane.showConfirmDialog(null, inputPanel, title, JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            fillMatrixFromInputFields();
            return matrix;
        }

        return null;
    }

    private double[][] multiplyMatrices(double[][] matrix1, double[][] matrix2) {
        int m1Rows = matrix1.length;
        int m1Cols = matrix1[0].length;
        int m2Cols = matrix2[0].length;

        double[][] resultMatrix = new double[m1Rows][m2Cols];

        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                double sum = 0;
                for (int k = 0; k < m1Cols; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                resultMatrix[i][j] = sum;
            }
        }

        return resultMatrix;
    }

    private double[][] transposeMatrix(double[][] matrix) {
        int mRows = matrix.length;
        int mCols = matrix[0].length;

        double[][] resultMatrix = new double[mCols][mRows];

        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mCols; j++) {
                resultMatrix[j][i] = matrix[i][j];
            }
        }

        return resultMatrix;
    }

    private double calculateMatrixDeterminant(double[][] matrix) {
        int n = matrix.length;
        if (n != matrix[0].length) {
            return Double.NaN; // Not a square matrix
        }
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        double determinant = 0.0;
        for (int col = 0; col < n; col++) {
            determinant += matrix[0][col] * cofactor(matrix, 0, col);
        }
        return determinant;
    }

    private double cofactor(double[][] matrix, int row, int col) {
        int n = matrix.length;
        double[][] minor = new double[n - 1][n - 1];
        int minorRow = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) {
                continue;
            }
            int minorCol = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) {
                    continue;
                }
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return Math.pow(-1, row + col) * calculateMatrixDeterminant(minor);
    }

    private double[][] calculateInverse(double[][] matrix) {
        int n = matrix.length;
        if (n != matrix[0].length) {
            return null; // Not a square matrix
        }

        double determinant = calculateMatrixDeterminant(matrix);
        if (determinant == 0) {
            return null; // Matrix is not invertible
        }

        double[][] adjugate = new double[n][n];

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                adjugate[col][row] = cofactor(matrix, row, col);
            }
        }

        double inverseDeterminant = 1.0 / determinant;

        double[][] inverse = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = adjugate[i][j] * inverseDeterminant;
            }
        }

        return inverse;
    }

    private int calculateMatrixRank(double[][] matrix) {
        int rank = 0;
        int m = matrix.length;
        int n = matrix[0].length;
        boolean[] rowMarked = new boolean[m];
        boolean[] colMarked = new boolean[n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != 0 && !rowMarked[i] && !colMarked[j]) {
                    rank++;
                    rowMarked[i] = true;
                    colMarked[j] = true;
                }
            }
        }

        return rank;
    }

    private void solveSystemOfLinearEquations() {
        double[] solution = gaussianElimination(myMatrix);

        if (solution != null) {
            StringBuilder solutionStr = new StringBuilder("Solution:\n");
            DecimalFormat df = new DecimalFormat("#.##");

            for (int i = 0; i < solution.length; i++) {
                solutionStr.append("x").append(i + 1).append(": ").append(df.format(solution[i])).append("\n");
            }

            JTextArea textArea = new JTextArea(solutionStr.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            
            JOptionPane.showMessageDialog(null, scrollPane, "System of Linear Equations Solution", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "The system of linear equations has no unique solution.");
        }
    }

    private double[] gaussianElimination(double[][] matrix) {
        int n = matrix.length;
        
        for (int i = 0; i < n; i++) {
            // Find the pivot row (the row with the largest absolute value in the current column)
            int pivotRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[pivotRow][i])) {
                    pivotRow = j;
                }
            }
            
            // Swap the current row with the pivot row
            double[] temp = matrix[i];
            matrix[i] = matrix[pivotRow];
            matrix[pivotRow] = temp;
            
            // Eliminate the values below the pivot
            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                for (int k = i; k < n + 1; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }
        
        // Back substitution
        double[] solution = new double[n];
        
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * solution[j];
            }
            solution[i] = (matrix[i][n] - sum) / matrix[i][i];
        }
        
        return solution;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MatrixCalculator());
    }
}
