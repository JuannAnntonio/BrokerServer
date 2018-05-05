package mx.sigmact.broker.pojo.trader;

/**
 * Created by norberto on 22/01/2017.
 */
public class Aggression {
    MarketPosition position;
    Boolean isPartial;

    public Aggression() {
    }

    public Aggression(MarketPosition position, Boolean isPartial) {
        this.position = position;
        this.isPartial = isPartial;
    }

    public MarketPosition getPosition() {
        return position;
    }

    public void setPosition(MarketPosition position) {
        this.position = position;
    }

    public Boolean getPartial() {
        return isPartial;
    }

    public void setPartial(Boolean partial) {
        isPartial = partial;
    }
}
