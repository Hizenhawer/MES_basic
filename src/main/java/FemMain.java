public class FemMain {
    public static void main(String[] args) {
        //Matrices.initiate();

        FemGrid grid = new FemGrid();
        grid.GenerateGrid();

        //grid.print();
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

        for (Element element : grid.getElements()){
            System.out.print("Element " + element.getElementNumber() + "\n");
            element.calculateH();
            element.printH();
            System.out.print("\n");
        }
    }
}