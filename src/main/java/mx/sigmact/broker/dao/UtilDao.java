package mx.sigmact.broker.dao;

import java.sql.Date;
import java.util.Calendar;

public interface UtilDao {

    Calendar today();
    Calendar todayWithTime();
    Calendar valmer_date();
    Calendar liquidation_day();
    Calendar liquidation_day1();
    Calendar Dia_Ant(String newDate, Integer NuDays);
    int Dia_Habil(String newDate);
    int updateSystemDate(Date newDate);
    
}
