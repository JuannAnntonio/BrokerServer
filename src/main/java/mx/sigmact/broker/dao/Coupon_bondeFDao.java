package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.DatePoint;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;
import mx.sigmact.broker.pojo.coupon_bondeF.Coupon_rate;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 27/08/21.
 */

public interface Coupon_bondeFDao {
	Coupon_rate getRate(String date, String series);
}
