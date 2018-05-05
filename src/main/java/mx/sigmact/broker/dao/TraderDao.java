package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.DatePoint;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;

import java.util.Calendar;
import java.util.List;

/**
 * Created on 08/12/16.
 */

public interface TraderDao {
    List<TraderInstrumentView> getTraderInstruments(Integer userId);
    List<TraderTicketView> getTraderTickets(Integer userId, Calendar calendar );
    List<TraderActivityView> getTraderActivity(Integer userId, Calendar calendar);
    List<DatePoint> getGraphDataPoints(Integer days, String instrumentIdentifier);
    List<DatePoint> getGraphDataPointsCurrentDay(String instrumentIdentifier);
    List<String> getCommonWorkbench(Integer aggressor, Integer bidder, Integer idInstrument);
}
