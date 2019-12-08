import java.io.FileNotFoundException;
import java.util.ArrayList;

import static java.lang.StrictMath.sqrt;

class Element {
    private static final double K = 30; //steel
    private static final int SIZE = 4;
    private static final double KSI = 1. / sqrt(3.), ETA = 1. / sqrt(3.);
    private int elementNumber;
    private ArrayList<FemNode> id; // Np 1,6,7,2

    private double[][] H = new double[SIZE][SIZE];
    private double[][] Hpc1, Hpc2, Hpc3, Hpc4;
    private ArrayList<double[][]> Hpc = new ArrayList<>(4);

    private Double detJpc1, detJpc2, detJpc3, detJpc4;
    private ArrayList<Double> detJpc = new ArrayList<>(4);

    double Nx, Ny;
    private double xKsi, yEta, yKsi, xEta;

    double[] NiXpc1 = new double[4], NiXpc2 = new double[4], NiXpc3 = new double[4], NiXpc4 = new double[4];
    double[] NiYpc1 = new double[4], NiYpc2 = new double[4], NiYpc3 = new double[4], NiYpc4 = new double[4];
    private ArrayList<double[]> NiX = new ArrayList<>(4);
    private ArrayList<double[]> NiY = new ArrayList<>(4);


    Element(int elementNumber, ArrayList<FemNode> id) throws FileNotFoundException {
        this.elementNumber = elementNumber;
        this.id = id;
    }

    void calculateH(){

        calculateHpc();

        H = Matrices.multiplyByValue(
                Matrices.add(
                        Matrices.add(
                                Matrices.multiplyByValue(Hpc1,detJpc1),
                                Matrices.multiplyByValue(Hpc2,detJpc2)),
                        Matrices.add(
                                Matrices.multiplyByValue(Hpc3,detJpc3),
                                Matrices.multiplyByValue(Hpc4,detJpc4))),
                K);
    }

    private void calculateHpc() {

        calculateShapeFunctionDerivatives();

        for (int i = 0; i < 4; i++) {
            double[] NiXL = NiX.get(i);
            double[][] NiXtimesNiXT = Matrices.multiplyVectorByTransposedVector(NiXL);
            double[] NiYL = NiY.get(i);
            double[][] NiYtimesNiYT = Matrices.multiplyVectorByTransposedVector(NiYL);
            double[][] LHpc = Matrices.add(NiXtimesNiXT, NiYtimesNiYT);
            Hpc.add(LHpc);
        }
        Hpc1 = Hpc.get(0);
        Hpc2 = Hpc.get(1);
        Hpc3 = Hpc.get(2);
        Hpc4 = Hpc.get(3);
    }

    private void calculateShapeFunctionDerivatives() {

        calculateDetJ();

        int signKsi, signEta;
        for (int i = 1; i <= SIZE; i++) {
            switch (i) {
                case 1:
                    signKsi = -1;
                    signEta = -1;
                    break;
                case 2:
                    signKsi = 1;
                    signEta = -1;
                    break;
                case 3:
                    signKsi = 1;
                    signEta = 1;
                    break;
                case 4:
                    signKsi = -1;
                    signEta = 1;
                    break;
                default:
                    return;
            }
            double N1Ksi, N2Ksi, N3Ksi, N4Ksi, N1Eta, N2Eta, N3Eta, N4Eta;
            double[] NiKsi = new double[4], NiEta = new double[4];

            N1Ksi = -1. / 4. * (1 - ETA * signEta);
            NiKsi[0] = N1Ksi;
            N1Eta = -1. / 4. * (1 - KSI * signKsi);
            NiEta[0] = N1Eta;

            N2Ksi = 1. / 4. * (1 - ETA * signEta);
            NiKsi[1] = N2Ksi;
            N2Eta = -1. / 4. * (1 + KSI * signKsi);
            NiEta[1] = N2Eta;

            N3Ksi = 1. / 4. * (1 + ETA * signEta);
            NiKsi[2] = N3Ksi;
            N3Eta = 1. / 4. * (1 + KSI * signKsi);
            NiEta[2] = N3Eta;

            N4Ksi = -1. / 4. * (1 + ETA * signEta);
            NiKsi[3] = N4Ksi;
            N4Eta = 1. / 4. * (1 - KSI * signKsi);
            NiEta[3] = N4Eta;

            double[] pcNiX = new double[4];
            double[] pcNiY = new double[4];

            for (int j = 0; j < 4; j++) {
                pcNiX[j] = 1. / detJpc.get(j) * (yEta * NiEta[j] + (-yKsi * NiEta[j]));
                pcNiY[j] = 1. / detJpc.get(j) * (-xEta * NiKsi[j] + (xKsi * NiKsi[j]));
            }

            NiX.add(pcNiX);
            NiY.add(pcNiY);
        }
        NiXpc1 = NiX.get(0);
        NiYpc1 = NiY.get(0);

        NiXpc2 = NiX.get(1);
        NiYpc2 = NiY.get(1);

        NiXpc3 = NiX.get(2);
        NiYpc3 = NiY.get(2);

        NiXpc4 = NiX.get(3);
        NiYpc4 = NiY.get(3);
    }

    private void calculateDetJ() {
        int signKsi, signEta;
        for (int i = 1; i <= SIZE; i++) {
            switch (i) {
                case 1:
                    signKsi = -1;
                    signEta = -1;
                    break;
                case 2:
                    signKsi = 1;
                    signEta = -1;
                    break;
                case 3:
                    signKsi = 1;
                    signEta = 1;
                    break;
                case 4:
                    signKsi = -1;
                    signEta = 1;
                    break;
                default:
                    return;
            }

            xKsi = 1. / 4. * (signEta * ETA - 1) * id.get(0).getX() +
                    1. / 4. * (1 - ETA * signEta) * id.get(1).getX() +
                    1. / 4. * (1 + ETA * signEta) * id.get(2).getX() +
                    -(1. / 4.) * (1 + ETA * signEta) * id.get(3).getX();
            yEta = 1. / 4. * (signKsi * KSI - 1) * id.get(0).getY() +
                    -(1. / 4.) * (1 + KSI * signKsi) * id.get(1).getY() +
                    1. / 4. * (1 + KSI * signKsi) * id.get(2).getY() +
                    1. / 4. * (1 - KSI * signKsi) * id.get(3).getY();
            yKsi = 1. / 4. * (signEta * ETA - 1) * id.get(0).getY() +
                    1. / 4. * (1 - ETA * signEta) * id.get(1).getY() +
                    1. / 4. * (1 + ETA * signEta) * id.get(2).getY() +
                    -(1. / 4.) * (1 + ETA * signEta) * id.get(3).getY();
            xEta = 1. / 4. * (signKsi * KSI - 1) * id.get(0).getX() +
                    -(1. / 4.) * (1 + KSI * signKsi) * id.get(1).getX() +
                    1. / 4. * (1 + KSI * signKsi) * id.get(2).getX() +
                    1. / 4. * (1 - KSI * signKsi) * id.get(3).getX();

            detJpc.add(xKsi * yEta - (yKsi * xEta));
        }
        detJpc1 = detJpc.get(0);
        detJpc2 = detJpc.get(1);
        detJpc3 = detJpc.get(2);
        detJpc4 = detJpc.get(3);
    }

    public boolean isBorder (){
        for (FemNode node : id){
            if (node.isBc()){
                return true;
            }
        }
        return false;
    }

    int getElementNumber() {
        return elementNumber;
    }

    void print() {
        System.out.print("[");
        for (int i = 0; i < id.size(); i++) {
            FemNode numb = id.get(i);
            if (numb.isBc()) {
                if (i < id.size() - 1) {
                    System.out.print("*" + numb.getId() + "*, ");
                } else {
                    System.out.print("*" + numb.getId() + "*");
                }
            } else if (i < id.size() - 1) {
                System.out.print(numb.getId() + ", ");
            } else {
                System.out.print(numb.getId());
            }
        }
        System.out.print("]\n");
    }

    void fullPrint() {
        System.out.print("[");
        for (int i = 0; i < id.size(); i++) {
            FemNode numb = id.get(i);
            if (numb.isBc()) {
                if (i < id.size() - 1) {
                    System.out.print("*" + numb.getId() + "* x: " + numb.getX() + " y: " + numb.getY() + ", ");
                } else {
                    System.out.print("*" + numb.getId() + "* x: " + numb.getX() + " y: " + numb.getY());
                }
            } else if (i < id.size() - 1) {
                System.out.print(numb.getId() + " x: " + numb.getX() + " y: " + numb.getY() + ", ");
            } else {
                System.out.print(numb.getId() + " x: " + numb.getX() + " y: " + numb.getY());
            }
        }
        System.out.print("]\n");
    }

    void printDetJ() {
        System.out.print("detJ Pc1 = " + detJpc1 + "\n");
        System.out.print("detJ Pc2 = " + detJpc2 + "\n");
        System.out.print("detJ Pc3 = " + detJpc3 + "\n");
        System.out.print("detJ Pc4 = " + detJpc4 + "\n");
    }

    void printHpcs(){
        System.out.print("H Pc1:\n");
        Matrices.printTable(Hpc1);
        System.out.print("\n");
        System.out.print("H Pc2:\n");
        Matrices.printTable(Hpc2);
        System.out.print("\n");
        System.out.print("H Pc3:\n");
        Matrices.printTable(Hpc3);
        System.out.print("\n");
        System.out.print("H Pc4:\n");
        Matrices.printTable(Hpc4);
        System.out.print("\n");
    }

    void printH() {
        Matrices.printTable(H);
    }
}
