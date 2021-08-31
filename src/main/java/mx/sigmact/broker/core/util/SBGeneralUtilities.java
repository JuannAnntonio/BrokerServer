package mx.sigmact.broker.core.util;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.pojo.StandingType;
import mx.sigmact.broker.pojo.trader.MarketPosition;

/**
 * Created on 17/01/17.
 */
public class SBGeneralUtilities {
	static Logger log = Logger.getLogger(SBGeneralUtilities.class);

	public static final Integer calc_base = 1000000;

	public static final void compareObjects(Object a, Object b, String errorMessage) throws IllegalStateException {
		if (!a.equals(b)) {
			throw new IllegalStateException(errorMessage);
		}
	}

	public static MarketPosition mergeStandings(List<StandingEntity> standings, int standingStatus) {

		int lAmount = 0;
		if (standings.size() <= 0) {
			return null;
		}
		StandingEntity sample = standings.get(0);
		int stdTyp = sample.getFkIdStandingType();
		double lRate = sample.getValue();
		String lStandingType = SBGeneralUtilities.getStandingTypeNameFromId(stdTyp);
		int idInstrument = sample.getFkIdValmerPriceVector();
		for (StandingEntity std : standings) {
			if (std.getFkIdStandingStatus() == standingStatus) {
				lAmount += std.getCurrentAmount();
			}
		}
		return new MarketPosition(idInstrument, lRate, lAmount / SBGeneralUtilities.calc_base, lStandingType, sample.getOrden());
	}

	// TODO optimize so less operations are done in the DB
	public static List<StandingEntity> sortQueue(List<StandingEntity> standings) {

		log.info("[SBGeneralUtilities][sortQueue]");

		log.info("[SBGeneralUtilities][sortQueue] StandingType.OFFER: " + StandingType.OFFER);

		int stdType = standings.get(0).getFkIdStandingType();
		if (getStandingTypeNameFromId(stdType).equals(StandingType.OFFER))
			Collections.sort(standings, Collections.reverseOrder());
		else
			Collections.sort(standings);
		if (standings != null && standings != null) {
			Double bestRate = standings.get(0).getValue();
			for (StandingEntity std : standings) {

				log.info("[SBGeneralUtilities][sortQueue] bestRate: " + bestRate);
				log.info("[SBGeneralUtilities][sortQueue] std.getValue(): " + std.getValue());

				if (bestRate == std.getValue()) {
					log.info("[SBGeneralUtilities][sortQueue] StandingStatus.INMARKET: " + StandingStatus.INMARKET);
					std.setFkIdStandingStatus(StandingStatus.INMARKET);
				} else {
					log.info("[SBGeneralUtilities][sortQueue] StandingStatus.QUEUED: " + StandingStatus.QUEUED);
					std.setFkIdStandingStatus(StandingStatus.QUEUED);
				}
			}
		}
		return standings;
	}

	public static boolean sortQueueAndAdd(List<StandingEntity> standings, StandingEntity standing) {

		log.info("[SBGeneralUtilities][sortQueueAndAdd] (List<StandingEntity> standings, StandingEntity standing)");

		boolean retVal = false;
		if (standing != null && standings != null) {
			standings.add(standing);
			SBGeneralUtilities.sortQueue(standings);
			StandingEntity first = standings.get(0);
			if (first.getFkIdUser() == standing.getFkIdUser()) {
				retVal = true;
			}
		}
		return retVal;
	}

	public static MarketPosition mergeStandings(List<StandingEntity> standings) {

		log.info("[SBGeneralUtilities][mergeStandings] (List<StandingEntity> standings)");

		int lAmount = 0;
		if (standings.size() <= 0) {
			return null;
		}
		StandingEntity sample = standings.get(0);
		int stdTyp = sample.getFkIdStandingType();
		double lRate = sample.getValue();
		String lStandingType = SBGeneralUtilities.getStandingTypeNameFromId(stdTyp);
		int idInstrument = sample.getFkIdValmerPriceVector();
		for (StandingEntity std : standings) {
			lAmount += std.getCurrentAmount();
		}
		return new MarketPosition(idInstrument, lRate, lAmount / SBGeneralUtilities.calc_base, lStandingType, sample.getOrden());
	}

	public static MarketPosition mergeStandings(List<StandingEntity> standings, String biddingType) {

		log.info("[SBGeneralUtilities][mergeStandings] (List<StandingEntity> standings, String biddingType)");

		int lAmount = 0;
		if (standings.size() <= 0) {
			return null;
		}
		StandingEntity sample = standings.get(0);
		double lRate = sample.getValue();
		int idInstrument = sample.getFkIdValmerPriceVector();
		for (StandingEntity std : standings) {
			Integer standingType = SBGeneralUtilities.getStandingTypeIdFromName(biddingType);
			if (standingType == std.getFkIdStandingType()) {
				lAmount += std.getAmount();
			}
		}
		return new MarketPosition(idInstrument, lRate, lAmount / SBGeneralUtilities.calc_base, biddingType, sample.getOrden());
	}

	public static Integer getStandingTypeIdFromName(String biddingType) {
		switch (biddingType) {
		case StandingType.BID:
			return StandingType.BIDID;
		case StandingType.OFFER:
			return StandingType.OFFERID;
		}
		return null;
	}

	public static MarketPosition getPositionFromStanding(StandingEntity standing) {

		return new MarketPosition(standing.getFkIdValmerPriceVector(), standing.getValue(),
				standing.getAmount() / SBGeneralUtilities.calc_base,
				getStandingTypeNameFromId(standing.getFkIdStandingType()), standing.getOrden());
	}

	public static String getStandingTypeNameFromId(int id) {
		switch (id) {
		case StandingType.BIDID:
			return StandingType.BID;
		case StandingType.OFFERID:
			return StandingType.OFFER;
		}
		return null;
	}

}
