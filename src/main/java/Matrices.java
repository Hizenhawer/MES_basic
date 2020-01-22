public class Matrices {

    static void printTable(double[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            for (double[] doubles : tab) {
                System.out.printf("%.4f", doubles[i]);
                System.out.print("|");
            }
            System.out.println();
        }
    }

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
}
