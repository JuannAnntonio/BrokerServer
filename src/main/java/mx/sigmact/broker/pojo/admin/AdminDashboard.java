package mx.sigmact.broker.pojo.admin;

import mx.sigmact.broker.pojo.graphs.LineGraphTwoLineElements;

import java.util.List;

/**
 * Created on 16/11/16.
 */
public class AdminDashboard {
    private List<LineGraphTwoLineElements> lastQuarterActivityStandings;
    private List<LineGraphTwoLineElements> lastQuarterTradedAmount;

    public List<LineGraphTwoLineElements> getLastQuarterActivityStandings() {
        return lastQuarterActivityStandings;
    }

    public void setLastQuarterActivityStandings(List<LineGraphTwoLineElements> lastQuarterActivityStandings) {
        this.lastQuarterActivityStandings = lastQuarterActivityStandings;
    }

    public List<LineGraphTwoLineElements> getLastQuarterTradedAmount() {
        return lastQuarterTradedAmount;
    }

    public void setLastQuarterTradedAmount(List<LineGraphTwoLineElements> lastQuarterTradedAmount) {
        this.lastQuarterTradedAmount = lastQuarterTradedAmount;
    }
}
