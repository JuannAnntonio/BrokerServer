package mx.sigmact.broker.pojo.graphs;

/**
 * Created on 28/10/16.
 */

public class LineGraphOneLineElement implements GraphElements{
    private String y = null;
    private String a = null;

    public LineGraphOneLineElement() {
    }

    public LineGraphOneLineElement(String y, String a) {
        this.y = y;
        this.a = a;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

}
