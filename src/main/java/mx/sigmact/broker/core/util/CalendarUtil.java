package mx.sigmact.broker.core.util;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.dao.JdbcAdminDao;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Utility for working with Calendars
 * Created on 09/11/16.
 */

public class CalendarUtil {

    /**
     * This method sets the time of the day to zero
     * @param cal
     * @return
     */
    public static Calendar zeroTimeCalendar(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }


    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Integer calcDiffDays(Calendar d1, Calendar d2){
        Calendar ld1 = (Calendar) d1.clone();
        Calendar ld2 = (Calendar) d2.clone();
        zeroTimeCalendar(ld1);
        zeroTimeCalendar(ld2);
        long diff = ld1.getTimeInMillis() - ld2.getTimeInMillis();
        Double daysDouble = (diff/86400000.0);
        Long roundDays = Math.round(daysDouble);
        return roundDays.intValue();
    }

    public static Double calcDiffSeconds(Calendar d1, Calendar d2){
        Calendar ld1 = (Calendar) d1.clone();
        Calendar ld2 = (Calendar) d2.clone();
        long diff = ld1.getTimeInMillis() - ld2.getTimeInMillis();
        Double diffSec = diff/1000.0;
        return diffSec;
    }

    public static Calendar today(){
        return CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
    }
}
