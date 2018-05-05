package mx.sigmact.broker.pojo.graphs;

/**
 * Created on 28/10/16.
 */

public class LineGraphTwoLineElements implements GraphElements {
    private String y = null;
    private String a = null;
    private String b = null;

    public LineGraphTwoLineElements(String y, String a, String b) {
        this.y = y;
        this.a = a;
        this.b = b;
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

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}
