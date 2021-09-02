package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.fondeo.FondeoTiie;

/**
 * Created on 08/12/16.
 */

public interface FondeoDao {

	FondeoBancario getFondeoLastRegister();

	FondeoTiie getFondeoTiieLastRegister();

	FondeoGubernamental getFondeoGubernamentalLastRegister();

	FondeoCetes getFondeoCetesLastRegister();
}
