import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class FemMain {
    public static void main(String[] args) {
        //Matrices.initiate();

        int nH = 0;
        int nW = 0;
        double simTime = 0.;
        double dT = 0.;
        double t0 = 0.;
        try (InputStream input = new FileInputStream(Globals.CONFIG_RELATIVE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);

            nH = Integer.parseInt(prop.getProperty("nH"));
            nW = Integer.parseInt(prop.getProperty("nW"));
            simTime = Double.parseDouble((prop.getProperty("simTime")));
            dT = Double.parseDouble((prop.getProperty("deltaT")));
            t0 = Double.parseDouble((prop.getProperty("t0")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int numberOfTimeSteps = (int) (simTime / dT);

        FemGrid grid = new FemGrid();
        grid.GenerateGrid();

        double[][] HGlobal = fillHglobal(nH * nW, grid);
        double[][] CGlobal = fillCglobal(nH * nW, grid);
        double[] PGlobal = fillPglobal(nH * nW, grid, CGlobal, dT, t0);

        double[][] H_plus_C_over_deltaT = HGlobal.clone();
        for (int i = 0; i < numberOfTimeSteps; i++) {
            H_plus_C_over_deltaT = calculateH_plus_C_over_deltaT(H_plus_C_over_deltaT, CGlobal, dT);
            System.out.print("Iteretion " + i + " [H] + ([C] / dT)\n");
            Matrices.printTable(H_plus_C_over_deltaT);
            System.out.print("\n\n");
        }


//        grid.print();
//        grid.printElementByElementNumber(5);

        //System.out.printf("Eta Table:\n");
        //Matrices.printTable(Matrices.getEtaTableValue());
        //System.out.printf("\nKsi Table:\n");
        //Matrices.printTable(Matrices.getKsiTableValue());
        //System.out.printf("\nShape function Table:\n");
        //Matrices.printTable(Matrices.getShapeFunctionTableValue());

        //Matrices.transpose(Matrices.getShapeFunctionTableValue());
        //System.out.printf("\nShape function Table transposed:\n");
        //Matrices.printTable(Matrices.getShapeFunctionTableValue());
        //System.out.printf("\nMatrix multiply test:\n");
        //Matrices.printTable(Matrices.multiply(Matrices.getEtaTableValue(), Matrices.getKsiTableValue()));
        //System.out.printf("\nMatrix add test:\n");
        //Matrices.printTable(Matrices.add(Matrices.getEtaTableValue(),Matrices.getKsiTableValue()));

//        for (Element element : grid.getElements()) {
//            System.out.print("Element " + element.getElementNumber() + "\n");
//            element.calculateDetJ();
//            element.printDetJ();
//            System.out.print("\n");
//        }

//        for (Element element : grid.getElements()){
//            System.out.print("Element " + element.getElementNumber() + "\n");
//            element.calculateHpc();
//            element.printHpcs();
//            System.out.printf("\n");
//        }

//        double[][] m1 = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,16}};
//        Matrices.printTable(Matrices.multiplyByValue(m1,5));

//        for (Element element : grid.getElements()) {
//            System.out.print("Element " + element.getElementNumber() + "\n" + "Is border element: " + element.isBorder() + "\n");
//            element.calculateH();
//            element.printH();
//            System.out.print("\n");
//        }

//        for (Element element : grid.getElements()) {
//            System.out.print("Element " + element.getElementNumber() + "\n" + "Is border element: " + element.isBorder() + "\n");
//            element.calculateC();
//            element.printC();
//            System.out.print("\n");
//        }

//        Matrices.printTable(HGlobal);
        Matrices.printTable(CGlobal);
        // Matrices.printVector(PGlobal);
    }

    private static double[][] calculateH_plus_C_over_deltaT(double[][] HGlobal, double[][] CGlobal, double dT) {
        double[][] tempCGlob = new double[CGlobal.length][];
        for(int i=0;i<CGlobal.length;i++)
            tempCGlob[i] = CGlobal[i].clone();

        return Matrices.add(
                HGlobal,
                Matrices.multiplyByValue(
                        tempCGlob,
                        1. / dT
                )
        );
    }


    private static double[] fillPglobal(int size, FemGrid grid, double[][] CGlobal, double dT, double t0) {
        double[] result = new double[size];
        for (Element element : grid.getElements()) {
            element.calculateP();
            for (int i = 0; i < 4; i++) {
                result[element.getId().get(i).getId() - 1] += element.getP()[i];
            }
        }
        double[] tt0 = new double[size];
        for (int i = 0; i < size; i++) {
            tt0[i] = t0;
        }
        result = Matrices.addVectors(
                result,
                Matrices.multiplyMatrixByVector(
                        Matrices.multiplyByValue(
                                CGlobal,
                                1. / dT
                        ),
                        tt0
                )
        );
        return result;
    }

    private static double[][] fillCglobal(int size, FemGrid grid) {
        double[][] result = new double[size][size];
        for (Element element : grid.getElements()) {
            element.calculateC();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    result[element.getId().get(i).getId() - 1][element.getId().get(j).getId() - 1] += element.getC()[i][j];
                }
            }
        }
        return result;
    }

    private static double[][] fillHglobal(int size, FemGrid grid) {
        double[][] result = new double[size][size];
        for (Element element : grid.getElements()) {
            element.calculateH();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    result[element.getId().get(i).getId() - 1][element.getId().get(j).getId() - 1] += element.getH()[i][j];
                }
            }
        }
        return result;
    }
}