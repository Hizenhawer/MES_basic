import static java.lang.Math.sqrt;

public class Matrices {
    private static final double[] DIFFERENTIAL_POINTS = {-1. / sqrt(3.), 1. / sqrt(3.)};
    private static final double[] DIFFERENTIAL_POINTS_WEIGHTS = {1, 1};
    private static final int NUMBER_OF_DIFFERENTIAL_POINTS = DIFFERENTIAL_POINTS.length * DIFFERENTIAL_POINTS_WEIGHTS.length;

//    private static final double[][] ksiTableValue = new double[NUMBER_OF_DIFFERENTIAL_POINTS][NUMBER_OF_DIFFERENTIAL_POINTS];
//    private static final double[][] etaTableValue = new double[NUMBER_OF_DIFFERENTIAL_POINTS][NUMBER_OF_DIFFERENTIAL_POINTS];
//    private static final double[][] shapeFunctionTableValue = new double[NUMBER_OF_DIFFERENTIAL_POINTS][NUMBER_OF_DIFFERENTIAL_POINTS];
//
//    private static void ksiFill() {
//        for (int i = 0; i < NUMBER_OF_DIFFERENTIAL_POINTS; i++) {
//            int position;
//            if (i < 2) {
//                position = 1;
//            } else {
//                position = 0;
//            }
//            ksiTableValue[0][i] = -0.25 * (1 - ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//            ksiTableValue[1][i] = 0.25 * (1 - ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//            ksiTableValue[2][i] = 0.25 * (1 + ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//            ksiTableValue[3][i] = -0.25 * (1 + ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//        }
//    }
//
//    private static void etaFill() {
//        for (int i = 0; i < NUMBER_OF_DIFFERENTIAL_POINTS; i++) {
//            int position;
//            if ((i == 0) || (i == 3)) {
//                position = 1;
//            } else {
//                position = 0;
//            }
//            etaTableValue[0][i] = -0.25 * (1 - ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//            etaTableValue[1][i] = 0.25 * (1 - ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//            etaTableValue[2][i] = 0.25 * (1 + ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//            etaTableValue[3][i] = -0.25 * (1 + ((position * (-1)) * DIFFERENTIAL_POINTS[1]));
//        }
//    }
//
//    private static void shapeFill() {
//        for (int i = 0; i < NUMBER_OF_DIFFERENTIAL_POINTS; i++) {
//            int ksiSign, etaSign;
//            if (i == 0) {
//                ksiSign = 1;
//                etaSign = 1;
//            } else if (i == 1) {
//                ksiSign = 0;
//                etaSign = 1;
//            } else if (i == 2) {
//                ksiSign = 1;
//                etaSign = 0;
//            } else if (i == 3) {
//                ksiSign = 0;
//                etaSign = 0;
//            } else {
//                return;
//            }
//            shapeFunctionTableValue[0][i] = 0.25 * (1 - ((ksiSign * (-1)) * DIFFERENTIAL_POINTS[1])) * ((etaSign * (-1)) * DIFFERENTIAL_POINTS[1]);
//            shapeFunctionTableValue[1][i] = 0.25 * (1 + ((ksiSign * (-1)) * DIFFERENTIAL_POINTS[1])) * ((etaSign * (-1)) * DIFFERENTIAL_POINTS[1]);
//            shapeFunctionTableValue[2][i] = 0.25 * (1 + ((ksiSign * (-1)) * DIFFERENTIAL_POINTS[1])) * ((etaSign * (-1)) * DIFFERENTIAL_POINTS[1]);
//            shapeFunctionTableValue[3][i] = 0.25 * (1 - ((ksiSign * (-1)) * DIFFERENTIAL_POINTS[1])) * ((etaSign * (-1)) * DIFFERENTIAL_POINTS[1]);
//        }
//    }

    static void printTable(double[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab.length; j++) {
                System.out.printf("%.4f", tab[j][i]);
                System.out.print("|");
            }
            System.out.println();
        }
    }

//    public static void transpose(double[][] matrix) {
//        double[][] transpose = new double[NUMBER_OF_DIFFERENTIAL_POINTS][NUMBER_OF_DIFFERENTIAL_POINTS];
//
//        //Code to transpose a matrix
//        for (int i = 0; i < NUMBER_OF_DIFFERENTIAL_POINTS; i++) {
//            for (int j = 0; j < NUMBER_OF_DIFFERENTIAL_POINTS; j++) {
//                transpose[i][j] = matrix[j][i];
//            }
//        }
//        System.arraycopy(transpose, 0, matrix, 0, NUMBER_OF_DIFFERENTIAL_POINTS);
//    }

    static double[][] multiplyVectorByTransposedVector(double[] v) {
        double[][] vM = new double[1][4];
        vM[0][0] = v[0];
        vM[0][1] = v[1];
        vM[0][2] = v[2];
        vM[0][3] = v[3];

        double[][] vTM = new double[4][1];
        vTM[0][0] = v[0];
        vTM[1][0] = v[1];
        vTM[2][0] = v[2];
        vTM[3][0] = v[3];

        double[][] result = new double[4][4];

        for (int i = 0; i < 4; i++) { // A rows
            for (int j = 0; j < 4; j++) { // B columns
                for (int k = 0; k < 1; k++) { // A columns
                    result[i][j] += vTM[i][k] * vM[k][j];
                }
            }
        }
        return result;
    }

    static double[][] multiplyByValue(double[][] matrix, double value) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] *= value;
            }
        }
        return matrix;
    }

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
            }
        }
        return result;
    }

    private static double multiplyMatricesCell(double[][] firstMatrix, double[][] secondMatrix, int row, int col) {
        double cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }
        return cell;
    }

    static double[][] add(double[][] a, double[][] b) {
        int rows = a.length;
        int columns = a[0].length;
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = a[i][j] + b[i][j];
            }
        }
        return result;
    }

//    public static double[][] getKsiTableValue() {
//        return ksiTableValue;
//    }
//
//    public static double[][] getEtaTableValue() {
//        return etaTableValue;
//    }
//
//    public static double[][] getShapeFunctionTableValue() {
//        return shapeFunctionTableValue;
//    }
//
//    static void initiate() {
//        ksiFill();
//        etaFill();
//        shapeFill();
//    }
}
