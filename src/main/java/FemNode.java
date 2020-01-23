class FemNode {
    private int id;
    private double x;
    private double y;
    private boolean bc;
    private double t;

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

    FemNode(int id, double x, double y, boolean bc,double t) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.bc = bc;
        this.t=t;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }
}
