package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.dao.UtilDao;
import mx.sigmact.broker.model.AggressionEntity;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.pojo.graphs.LineGraphTwoLineElements;
import mx.sigmact.broker.repositories.BrokerAggressionRepository;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created on 28/10/16.
 */
@RestController
@RequestMapping("admin/rest/getAgrStaGraph")
public class RESTAdminAgressionStandingGraph {

    private static final int STATDAYS = 90;

    @Resource
    private UtilDao utilDao;

    @Resource
    BrokerStandingRepository standingRepository;

    @Resource
    BrokerAggressionRepository aggressionRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public LineGraphTwoLineElements[] doGetAggresionsAndStandings() {
        LineGraphTwoLineElements result[] = new LineGraphTwoLineElements[STATDAYS];
        Calendar today = utilDao.today();//Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, 1 - STATDAYS);
        //CalendarUtil.zeroTimeCalendar(today);
        List<StandingEntity> byDatetimeGreaterThanEqualStandings = standingRepository.findByDatetimeGreaterThanEqual(today);
        List<AggressionEntity> byDatetimeGreaterThanEqualAggression = aggressionRepository.findByDatetimeGreaterThanEqual(today);
        HashMap<Calendar, Integer> mapStandings = new HashMap<>();
        HashMap<Calendar, Integer> mapAggressions = new HashMap<>();
        for (StandingEntity standing : byDatetimeGreaterThanEqualStandings) {
            Calendar standing_calendar_date = CalendarUtil.zeroTimeCalendar(standing.getDatetime());
            Integer current_sum = mapStandings.get(standing_calendar_date);
            if (current_sum == null) {
                current_sum = 1;
            } else {
                current_sum++;
            }
            mapStandings.put(standing_calendar_date, current_sum);
        }
        for (AggressionEntity aggression : byDatetimeGreaterThanEqualAggression) {
            Calendar standing_calendar_date = CalendarUtil.zeroTimeCalendar(aggression.getDatetime());
            Integer current_sum = mapAggressions.get(standing_calendar_date);
            if (current_sum == null) {
                current_sum = 1;
            } else {
                current_sum++;
            }
            mapAggressions.put(standing_calendar_date, current_sum);
        }
        for (int i = 0; i < STATDAYS; i++) {
            Integer standing = mapStandings.get(today);
            Integer aggression = mapAggressions.get(today);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            LineGraphTwoLineElements lgtl = new LineGraphTwoLineElements(
                    fmt.format(today.getTime()),
                    String.valueOf(standing),
                    String.valueOf(aggression));
            result[i] = lgtl;
            today.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

}
