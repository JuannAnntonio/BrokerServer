package mx.sigmact.broker.core.lib;

import mx.sigmact.broker.model.BankFundingEntity;
import mx.sigmact.broker.pojo.TasasDeInteresBanxicoInfo;
import mx.sigmact.broker.services.soap.DgieWSStub;
import org.apache.axis2.AxisFault;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by norberto on 21/02/2017.
 */
public class FundingRateClient {
    @Resource(name = "formatDate" )
    SimpleDateFormat banxicoDateFormat;

    @Value("${banco_de_mexico_wsdl}")
    String wsdl;

    public BankFundingEntity getFundingTimeAndObsevedValue() {
        TasasDeInteresBanxicoInfo methodResult = new TasasDeInteresBanxicoInfo();
        BankFundingEntity entity = null;
        try {
            Namespace bm = Namespace.getNamespace("bm","http://www.banxico.org.mx/structure/key_families/dgie/sie/series/compact");
            DgieWSStub stub = new DgieWSStub(wsdl);
            DgieWSStub.TasasDeInteresBanxico req = new DgieWSStub.TasasDeInteresBanxico();
            DgieWSStub.TasasDeInteresBanxicoResponse tasasDeInteresBanxicoResponse= stub.tasasDeInteresBanxico(req);
            String result = tasasDeInteresBanxicoResponse.getResult();
            System.out.println(result);
            SAXBuilder builder = new SAXBuilder();
            ByteArrayInputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
            org.jdom.Document doc = builder.build(stream);
            org.jdom.Element rootElement = doc.getRootElement();
            Element dataSet = rootElement.getChild("DataSet",bm);
            List<Element> series = dataSet.getChildren("Series",bm);
            for(Element elem: series){
                Attribute titulo = elem.getAttribute("TITULO");
                String lTitulo = titulo.getValue();
                if(lTitulo.equals("Tasa objetivo")){
                    Element obs = elem.getChild("Obs", bm);
                    Attribute time_period = obs.getAttribute("TIME_PERIOD");
                    Attribute obs_value = obs.getAttribute("OBS_VALUE");
                    entity = new BankFundingEntity();
                    try {
                        Date parse = banxicoDateFormat.parse(time_period.getValue());
                        Calendar instance = Calendar.getInstance();
                        instance.setTime(parse);
                        entity.setDate(instance);
                        entity.setRate(obs_value.getDoubleValue());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entity;
    }
}
