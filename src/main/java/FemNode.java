class FemNode {
    private int id;
    private double x;
    private double y;
    private boolean bc;

    int getId() {
        return id;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    boolean isBc() {
        return bc;
    }

    FemNode(int id, double x, double y, boolean bc) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.bc = bc;
    }
}
