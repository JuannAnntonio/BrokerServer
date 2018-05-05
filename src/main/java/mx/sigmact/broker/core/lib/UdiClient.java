package mx.sigmact.broker.core.lib;

import mx.sigmact.broker.pojo.UdisBanxicoInfo;
import mx.sigmact.broker.services.soap.DgieWSStub;
import org.apache.axis2.AxisFault;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created on 16/11/16.
 */

public class UdiClient {

    @Value("${banco_de_mexico_wsdl}")
    String wsdl;

    public UdisBanxicoInfo getUdiTimeAndObsevedValue() {
        UdisBanxicoInfo methodResult = new UdisBanxicoInfo();
        try {
            DgieWSStub stub = new DgieWSStub(wsdl);
            DgieWSStub.UdisBanxico req = new DgieWSStub.UdisBanxico();
            DgieWSStub.UdisBanxicoResponse udisBanxicoResponse = stub.udisBanxico(req);
            String result = udisBanxicoResponse.getResult();
            SAXBuilder builder = new SAXBuilder();
            ByteArrayInputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
            org.jdom.Document doc = builder.build(stream);
            org.jdom.Element rootElement = doc.getRootElement();
            List children = rootElement.getChildren();
            org.jdom.Element dataset = (org.jdom.Element) children.get(1);
            children = dataset.getChildren();
            org.jdom.Element serie = (org.jdom.Element) children.get(1);
            children = serie.getChildren();
            org.jdom.Element obs = (org.jdom.Element) children.get(0);
            methodResult.setDate(obs.getAttribute("TIME_PERIOD").getValue());
            methodResult.setUdiValue(obs.getAttribute("OBS_VALUE").getValue());
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return methodResult;

    }
}
