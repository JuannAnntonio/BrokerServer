package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.DatePoint;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.instrument.Instrument;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import mx.sigmact.broker.pojo.udi.Udi;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 08/12/16.
 */

public interface InstrumentDao {
	Instrument getInstrument(String tv);
}