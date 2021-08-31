package mx.sigmact.broker.pojo.trader;

import java.util.List;

/**
 * Class for showing positions in the bidding table for traders. This
 * implementation does not tie the instrument to the position so it can be used
 * in a duple (idVPV, MarketPosition) This also tells if the position is from
 * the trader currently requesting the position this might change in the future
 * so it is not recommended to rely on this field. Created on 19/12/16.
 */
public class MarketPosition extends Bidding {
	private double rate;
	private int amount;
	private boolean isOwnOffer;
	private boolean isSameInstitution;
	private String userName;
	private String institucion;
	private Integer orden;
	private List<BlockDetail> blockDetailList;

	public MarketPosition() {
	}

	public MarketPosition(int instrumentId, double rate, int amount, String biddingType, Integer orden) {
		this.instrumentId = instrumentId;
		this.rate = rate;
		this.amount = amount;
		this.biddingType = biddingType;
		this.orden = orden;
	}

	public MarketPosition(int instrumentId, double rate, int amount, String biddingType, boolean isOwnOffer) {
		this.instrumentId = instrumentId;
		this.rate = rate;
		this.amount = amount;
		this.biddingType = biddingType;
		this.isOwnOffer = isOwnOffer;
	}

	public MarketPosition(int instrumentId, double rate, int amount, String biddingType,
			List<BlockDetail> blockDetailList, Integer orden) {
		this.instrumentId = instrumentId;
		this.rate = rate;
		this.amount = amount;
		this.biddingType = biddingType;
		this.blockDetailList = blockDetailList;
		this.orden = orden;
	}

	public int getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(int instrumentId) {
		this.instrumentId = instrumentId;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getBiddingType() {
		return biddingType;
	}

	public void setBiddingType(String biddingType) {
		this.biddingType = biddingType;
	}

	public boolean isOwnOffer() {
		return isOwnOffer;
	}

	public void setOwnOffer(boolean ownOffer) {
		isOwnOffer = ownOffer;
	}

	public List<BlockDetail> getBlockDetailList() {
		return blockDetailList;
	}

	public void setBlockDetailList(List<BlockDetail> blockDetailList) {
		this.blockDetailList = blockDetailList;
	}

	public boolean isSameInstitution() {
		return isSameInstitution;
	}

	public void setSameInstitution(boolean isSameInstitution) {
		this.isSameInstitution = isSameInstitution;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}
}