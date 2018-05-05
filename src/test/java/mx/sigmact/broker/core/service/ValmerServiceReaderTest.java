package mx.sigmact.broker.core.service;

import mx.sigmact.broker.core.config.SigmactBrokerConfiguration;
import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.fail;
import org.junit.Ignore;

/**
 * Created on 01/11/16.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SigmactBrokerConfiguration.class, TestConfig.class}, loader=AnnotationConfigContextLoader.class)
public class ValmerServiceReaderTest {

    @Resource
    ValmerServiceReader vsr;

    @Resource
    BrokerValmerPriceVectorRepository bvpvr;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testVSR() {

        Calendar cal = Calendar.getInstance();
        try {
            String responseBody = vsr.getValmer(cal);
            List<ValmerPriceVectorEntity> valmerPriceVectorEntities = vsr.readCSV(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)),1);
            for (ValmerPriceVectorEntity vpv : valmerPriceVectorEntities) {
                System.out.println(vpv.getIssue() + "" + vpv.getTv());
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Ignore
    public void testVSRSave() {
        LocalDate cal = LocalDate.now();
        cal = cal.withMonthOfYear(11);
        cal = cal.withDayOfMonth(2);
        cal = cal.withYear(2016);
        LocalDate now = LocalDate.now();
        while (cal.isBefore(now)|| cal.isEqual(now)) {
            try {
                Calendar lCal = Calendar.getInstance();
                lCal.setTime(cal.toDate());
                String responseBody = vsr.getValmer(lCal);
                List<ValmerPriceVectorEntity> topId = bvpvr.findTop1ByOrderByIdValmerPriceVectorDesc();
                int topIdInt = topId.get(0).getIdValmerPriceVector();
                List<ValmerPriceVectorEntity> valmerPriceVectorEntities =
                        vsr.readCSV(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)),topIdInt);
                vsr.setMarketToVector(valmerPriceVectorEntities, 1);
                bvpvr.save(valmerPriceVectorEntities);
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
            cal = cal.plusDays(1);
        }
    }

    @Test
    @Ignore
    public void testGet() {
        Calendar cal = Calendar.getInstance();
        try {
            vsr.getValmerPlain(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
