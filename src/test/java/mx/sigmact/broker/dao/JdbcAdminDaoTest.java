package mx.sigmact.broker.dao;

import mx.sigmact.broker.core.config.SigmactBrokerConfiguration;
import mx.sigmact.broker.core.service.TestConfig;
import mx.sigmact.broker.pojo.admin.UserInstruments;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;
import org.junit.Ignore;

/**
 * Created on 01/11/16.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SigmactBrokerConfiguration.class, TestConfig.class}, loader=AnnotationConfigContextLoader.class)
public class JdbcAdminDaoTest {

    @Resource
    JdbcAdminDao dao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testJdbcDao() {
        UserInstruments instruments = dao.findCurrentUserInstruments(2);
        instruments.getActiveInstruments();

    }

}
