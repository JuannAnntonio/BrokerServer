package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.CalendarEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

/**
 * Created on 31/10/16.
 */
@Repository
public interface BrokerCalendarRespository extends CrudRepository<CalendarEntity, Integer>{
    List<CalendarEntity> findByDateEqualsAndFkIdMarketTypeEquals(Calendar date, Integer fkIdMarketType);
    List<CalendarEntity> findTop1ByDateLessThanEqualAndIsHolidayEqualsOrderByDateDesc(Calendar date, Byte isHoliday);
}
