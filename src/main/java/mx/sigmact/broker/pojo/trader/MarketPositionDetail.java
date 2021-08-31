package mx.sigmact.broker.pojo.trader;

import java.util.List;

public class MarketPositionDetail extends Bidding {
    List<BlockDetail> blockDetailList;
    String status;
    String message;

    public MarketPositionDetail(){}

    public MarketPositionDetail(int instrumentId, String biddingType, List<BlockDetail> blockDetailListBid, List<BlockDetail> blockDetailListOffer) {
        this.instrumentId = instrumentId;
        this.biddingType = biddingType;
        this.blockDetailList = blockDetailList;

    }
    public MarketPositionDetail(int instrumentId, String biddingType, List<BlockDetail> blockDetailList,String status, String message) {
        this.instrumentId = instrumentId;
        this.biddingType = biddingType;
        this.blockDetailList = blockDetailList;
        this.status = status;
        this.message = message;
    }

    public List<BlockDetail> getBlockDetailList() {
        return blockDetailList;
    }

    public void setBlockDetailList(List<BlockDetail> blockDetailList) {
        this.blockDetailList = blockDetailList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
