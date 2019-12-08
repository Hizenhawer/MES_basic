import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

class FemGrid {
    private static final String CONFIG_RELATIVE_PATH = "./src/main/resources/config.properties";

    private ArrayList<FemNode> nodes;
    private ArrayList<Element> elements;

    void GenerateGrid() {
        GenerateNodes();
        GenerateElements();
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    void print() {
        for (Element element : elements) {
            System.out.print(element.getElementNumber() + " Element ID: ");
            element.print();
        }
    }

    void printElementByElementNumber(int numb) {
        for (Element el : elements) {
            if (el.getElementNumber() == numb) {
                el.fullPrint();
                return;
            }
        }
    }

    private void GenerateNodes() {
        ArrayList<FemNode> result = new ArrayList<>();
        try (InputStream input = new FileInputStream(CONFIG_RELATIVE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);

            double H = Double.parseDouble(prop.getProperty("H"));
            double W = Double.parseDouble(prop.getProperty("W"));

            int nH = Integer.parseInt(prop.getProperty("nH"));
            int nW = Integer.parseInt(prop.getProperty("nW"));

            double dx = W / (nW - 1);
            double dy = H / (nH - 1);

            int master = 1;
            for (int i = 0; i < nW; i++) {
                for (int j = 0; j < nH; j++) {
                    if (j == 0 || i == 0 || i == nW - 1 || j == nH - 1) {
                        result.add(new FemNode(master, i * dx, j * dy, true));
                    } else {
                        result.add(new FemNode(master, i * dx, j * dy, false));
                    }
                    master++;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.nodes = result;
    }

    private void GenerateElements() {
        ArrayList<Element> result = new ArrayList<>();
        try (InputStream input = new FileInputStream(CONFIG_RELATIVE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);

            int nH = Integer.parseInt(prop.getProperty("nH"));
            int nW = Integer.parseInt(prop.getProperty("nW"));

            for (int i = 0, j = 1, h = 0; h < (nH - 1) * (nW - 1); i++, j++, h++) {

                int ID1 = i + 1;
                int ID2 = ID1 + nH;
                int ID3 = ID2 + 1;
                int ID4 = ID1 + 1;
                ArrayList<Integer> IDs = new ArrayList<>();
                IDs.add(ID1);
                IDs.add(ID2);
                IDs.add(ID3);
                IDs.add(ID4);

                ArrayList<FemNode> ns = new ArrayList<>();

                for (int id : IDs) {
                    double x = this.nodes.get(id - 1).getX();
                    double y = this.nodes.get(id - 1).getY();
                    boolean b = this.nodes.get(id - 1).isBc();
                    FemNode n = new FemNode(id, x, y, b);
                    ns.add(n);
                }

                result.add(new Element(h + 1, ns));

                if (j == nH - 1) {
                    i++;
                    j = 0;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.elements = result;
    }
}
