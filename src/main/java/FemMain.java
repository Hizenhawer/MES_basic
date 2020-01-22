import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FemMain {
    public static void main(String[] args) {
        //Matrices.initiate();

        int nH = 0;
        int nW = 0;
        try (InputStream input = new FileInputStream(Globals.CONFIG_RELATIVE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);

            nH = Integer.parseInt(prop.getProperty("nH"));
            nW = Integer.parseInt(prop.getProperty("nW"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FemGrid grid = new FemGrid();
        grid.GenerateGrid();

        double[][] HGlobal = fillHglobal(nH * nW, grid);
        double[][] CGlobal = fillCglobal(nH * nW, grid);


//        grid.print();
        //grid.printElementByElementNumber(5);

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

//        for (Element element : grid.getElements()){
//            System.out.print("Element " + element.getElementNumber() + "\n"+"Is border element: "+element.isBorder()+"\n");
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

        Matrices.printTable(CGlobal);
    }

    private static double[][] fillCglobal(int size, FemGrid grid) {
        double[][] result = new double[size][size];
        for (Element element : grid.getElements()){
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