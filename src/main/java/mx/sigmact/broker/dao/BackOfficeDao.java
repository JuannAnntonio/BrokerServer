package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.backoffice.BackOfficeMainView;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCalculadoraPrecios;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCalculadoraPreciosDetalles;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCalculadoraPreciosDetalles_list;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCartaConfirmacion;
import java.util.Date;
import java.util.List;
/**
 * Dao for accessing information about the BackOffice
 * Created by norberto on 09/09/2017.
 */
public interface BackOfficeDao {
    List<BackOfficeMainView> getBackOfficeMainView(Integer institucion, String date, String start, String end);
    List<BackOfficeReporteCartaConfirmacion> getBackOfficeReporteCartaConfirmacion(Integer institucion, String date, String start, String end);
    List<BackOfficeReporteCalculadoraPrecios> getBackOfficeReporteCalculadoraPrecios(Integer institucion, String date, String tv);
    BackOfficeReporteCalculadoraPreciosDetalles_list getBackOfficeReporteCalculadoraPreciosDetalles(Integer institucion, String date, String id_valmer);
}
