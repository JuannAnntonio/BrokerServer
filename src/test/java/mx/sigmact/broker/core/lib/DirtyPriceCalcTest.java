package mx.sigmact.broker.core.lib;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created on 01/11/16.
 */


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath*:/test-context-xml.xml")
public class DirtyPriceCalcTest extends TestCase{


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void testVSR() {
        UdiClient udiClient = new UdiClient();
        udiClient.getUdiTimeAndObsevedValue();

    }


/*    @Resource


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testVSR() {

        Calendar cal = Calendar.getInstance();
        try {
            String responseBody = vsr.getValmer(cal);
            List<ValmerPriceVectorEntity> valmerPriceVectorEntities = vsr.readCSV(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)));
            for (ValmerPriceVectorEntity vpv : valmerPriceVectorEntities) {
                System.out.println(vpv.getIssue() + "" + vpv.getTv());
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testVSRSave() {
        Calendar cal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2010);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        while (cal.getTimeInMillis() <= today.getTimeInMillis()) {
            try {
                String responseBody = vsr.getValmer(cal);
                List<ValmerPriceVectorEntity> valmerPriceVectorEntities =
                        vsr.readCSV(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)));
                vsr.setMarketToVector(valmerPriceVectorEntities, 1);
                bvpvr.save(valmerPriceVectorEntities);
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
    */

}
