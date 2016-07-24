package sample;


public class BarProgress {
    double fin;
    int in = 0;
    public BarProgress(){

    }

    public double getFin() {
        return fin;
    }
    public void setFin(double x) {
        this.fin = fin + x;
    }

    public int getIn() {
        return in;
    }
    public void setIn(int x) {
        this.in = in + x;
    }
}
