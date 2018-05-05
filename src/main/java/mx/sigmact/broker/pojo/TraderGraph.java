package mx.sigmact.broker.pojo;

import java.util.List;

public class TraderGraph {

    private List<DatePoint> list;
    private Double maxYield;
    private Double minYield;

    public List<DatePoint> getList() {
        return list;
    }

    public void setList(List<DatePoint> list) {
        this.list = list;
        this.maxYield = Double.MIN_VALUE;
        this.minYield = Double.MAX_VALUE;
        for(DatePoint point: list){
            if(this.minYield > point.getYield()){
                this.minYield = point.getYield();
            }
            if(this.maxYield < point.getYield()){
                this.maxYield = point.getYield();
            }
        }
        Double range = this.maxYield - this.minYield;
        Double offset = range *0.05;
        this.maxYield += offset;
        this.minYield -= offset;
    }

    public Double getMaxYield() {
        return maxYield;
    }

    public Double getMinYield() {
        return minYield;
    }
}
