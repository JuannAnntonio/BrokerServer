package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.DatePoint;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;
import mx.sigmact.broker.pojo.aggression.Aggression;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 08/12/16.
 */

public interface AggressionDao {
	int addAggression(Aggression aggression);
}
