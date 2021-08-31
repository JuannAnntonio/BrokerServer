package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.coupon_bonde.Coupon_rate;

/**
 * Created on 27/08/21.
 */

public interface Coupon_bondeFDao {
	Coupon_rate getRate(String date, String series);
}
