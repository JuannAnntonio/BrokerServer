package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.DatePoint;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;
import mx.sigmact.broker.pojo.coupon_bonde.Coupon_rate;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 08/12/16.
 */

public interface Coupon_bondeDao {
	Coupon_rate getRate(String date, String series);
}
