package mx.sigmact.broker.services.rest;

import java.lang.reflect.Type;
import java.util.Scanner;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import mx.sigmact.broker.dao.JdbcAdminDao;
import mx.sigmact.broker.pojo.MarketMessage;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.repositories.BrokerInstitutionRepository;
import mx.sigmact.broker.repositories.BrokerInstitutionWorkbenchPriorityRepository;
import mx.sigmact.broker.repositories.BrokerRolesEntity;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import mx.sigmact.broker.repositories.BrokerUserxInstrumentRepository;

/**
 * Created on 02/11/16.
 */
@RestController
@RequestMapping("socket/test/")
public class RESTTest {

	@Resource
	JdbcAdminDao dao;
	@Resource
	BrokerUserRepository userRepo;
	@Resource
	BrokerInstitutionRepository instRepo;
	@Resource
	BrokerRolesEntity roleRepo;
	@Resource
	BrokerUserxInstrumentRepository uxiRepo;
	@Resource
	BrokerInstitutionWorkbenchPriorityRepository iwpRepo;

	private static final Logger log = LoggerFactory.getLogger(RESTTest.class);

	/**
	 * Pages the Institution list starting with page 1.
	 *
	 * @param page
	 * @param pageSize
	 * @return
	 */
	// TODO CREATE a CACHE to return the id form the catalogs *********

	/*
	 * getMatrix se encarga de llamar al dao.findAdminMatrix para llamar los datos
	 * de la tabla institutuin_instruments Se usa "institution" como buscador para
	 * localizar los datos unicos de la institucion seleccionada Llama los datos
	 * id_commision, id_institution1, name1_institucion, instrument, surcharge,
	 * id_institution2, name2_institution
	 */

	@RequestMapping(value = "getSocket", method = RequestMethod.GET, produces = "application/json")
	public int doGetSocket() {

		log.info("[RESTAdmin][doGetSocket]");

		StompSession session = null;

		try {

			StompClient stomp_client = new StompClient();

			session = stomp_client.connect();

			StompHeaders headers = new StompHeaders();

			// int instrumentId = 1223601;
			// int instrumentId = 1224920;
			int instrumentId = 1227280;
			double rate = 10;
			int amount = 10;

			MarketPosition marketPosition = new MarketPosition(instrumentId, rate, amount, "Offer",0);// TODO: ?

			Gson gson = new Gson();
			String json = gson.toJson(marketPosition);

			log.info("[RESTAdmin][doGetSocket] json: " + json);
			log.info("[RESTAdmin][doGetSocket] json.getBytes(): " + json.getBytes());

			// headers.setDestination("/BBBroker/aggress");
			headers.setDestination("/BBBroker/position");
			headers.set("user-name", "bbfixed_t01");
			session.send(headers, json.getBytes());

			session.subscribe("/market/announce", new StompFrameHandler() {

				public void handleFrame(StompHeaders stompHeaders, Object o) {

					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame]");

					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] Received: " + o.toString());

					MarketMessage marketMessage = (MarketMessage) o;

					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketMessage.getMessage(): "
							+ marketMessage.getMessage());
					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketMessage.getStatus(): "
							+ marketMessage.getStatus());
					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketMessage.getCode(): "
							+ marketMessage.getCode());
					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketMessage.getData(): "
							+ marketMessage.getData());

					Gson g = new Gson();
					MarketPosition marketPosition = g.fromJson(marketMessage.getData().toString(),
							MarketPosition.class);

					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketPosition.getAmount(): "
							+ marketPosition.getAmount());
					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketPosition.getBiddingType(): "
							+ marketPosition.getBiddingType());
					log.info(
							"[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketPosition.getInstrumentId(): "
									+ marketPosition.getInstrumentId());
					log.info("[RESTAdmin][doGetSocket][/market/announce][handleFrame] marketPosition.getRate(): "
							+ marketPosition.getRate());

				}

				@Override
				public Type getPayloadType(StompHeaders headers) {
					// TODO Auto-generated method stub

					log.info("[RESTAdmin][doGetSocket][/market/announce][getPayloadType] Headers");

					log.info("[RESTAdmin][doGetSocket][/market/announce][getPayloadType] headers: "
							+ headers.toString());

					return MarketMessage.class;
				}

			});

			new Scanner(System.in).nextLine(); // Don't close immediately.

		} catch (Exception e) {

			log.info("[RESTAdmin][doGetSocket] ERROR: " + e.getMessage());

		} finally {
			if (session != null) {
				// session.disconnect();
			}
		}

		return 0;

	}// fin getSocket

}