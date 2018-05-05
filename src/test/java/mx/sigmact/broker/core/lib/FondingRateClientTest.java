package mx.sigmact.broker.core.lib;

import mx.sigmact.broker.core.service.TestConfig;
import mx.sigmact.broker.repositories.BrokerBankFundingRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import java.util.Calendar;
import org.junit.Ignore;

/**
 * Created by norberto on 28/02/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class}, loader=AnnotationConfigContextLoader.class)
public class FondingRateClientTest {

    @Resource
    FundingRateClient bankFondingClient;

    @Resource
    BrokerBankFundingRepository fundRepo;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testFondingRateClient() {
        Calendar cal = Calendar.getInstance();
        bankFondingClient.getFundingTimeAndObsevedValue();
    }

}