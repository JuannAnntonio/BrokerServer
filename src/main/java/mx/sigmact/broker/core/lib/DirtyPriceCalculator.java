package mx.sigmact.broker.core.lib;

import mx.sigmact.broker.core.enums.ValueType;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.model.CalendarEntity;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.UdiValueEntity;
import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import mx.sigmact.broker.repositories.BrokerCalendarRespository;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerUdiValueRepository;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;
import mx.sigmact.broker.services.ExternalServices;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * Calculadora para el precio sucio dependiendo el tipo de valor
 * Created on 15/11/16.
 */
public class DirtyPriceCalculator {

    @Resource
    BrokerCalendarRespository daoCalendar;

    @Resource
    BrokerValmerPriceVectorRepository dao;

    @Resource
    BrokerStandingRepository daoStanding;

    @Resource
    BrokerUdiValueRepository udiRepo;

    @Resource
    ExternalServices extSer;

    public DirtyPriceCalculator() {
        super();
    }


    public Double calcDirtyPrice(StandingEntity standing, Calendar reportDate){
        return calcDirtyPrice(standing, reportDate, null);
    }

    public Double calcDirtyPrice(StandingEntity standing, Calendar reportDate, Double overrideRate){
        Double result = 0.0;
        int fkIdValmerPriceVector = standing.getFkIdValmerPriceVector();
        ValmerPriceVectorEntity priceVectorData = dao.findOne(fkIdValmerPriceVector);
        double rate = standing.getValue();
        if(overrideRate != null){
            rate = overrideRate;
        }
        String tv = priceVectorData.getTv();
        String cp = null;
        Integer couponPeriod = null;
        switch (tv){
            case "BI":
                result = calcBi(rate,reportDate,priceVectorData.getExpirationDate());
                break;
            case "M":
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0,cp.length()-4));
                result = dirtPriceM(priceVectorData.getCouponStart(),
                        reportDate,
                        rate,
                        couponPeriod,
                        priceVectorData.getUpdatedNominalValue().intValue(),
                        priceVectorData.getCouponRate(),
                        priceVectorData.getCouponsDueToExpire());
                break;
            case "S":
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0,cp.length()-4));
                result = dirtPriceM(priceVectorData.getCouponStart(),
                        reportDate,
                        rate,
                        couponPeriod,
                        priceVectorData.getUpdatedNominalValue().intValue(),
                        priceVectorData.getCouponRate(),
                        priceVectorData.getCouponsDueToExpire());
                java.sql.Date date = new Date(
                        CalendarUtil.zeroTimeCalendar(Calendar.getInstance()).getTimeInMillis());
                UdiValueEntity udiToday = udiRepo.findOneByUdiDate(date);
                if(udiToday == null){
                    udiToday = extSer.loadUdi();
                }
                result *= udiToday.getUdiValue();
                break;
        }
        return result;
    }

    public Double calcDirtyPrice(Integer standingId, Calendar reportDate){
        StandingEntity standing = daoStanding.findOne(standingId);
        return calcDirtyPrice(standing, reportDate);
    }


    public Double interst(ValueType vt, Double standingRate, Double couponRate, Calendar couponStart, Calendar couponEnd, Calendar expirationDate, Calendar reportDate, String couponPeriod, Double yield, Double marketSurcharge, Integer updatedNominalValue){
        CalendarUtil.zeroTimeCalendar(reportDate);
        CalendarUtil.zeroTimeCalendar(couponEnd);
        Integer couponPeriodInt = Integer.parseInt(couponPeriod.substring(0,couponPeriod.length()-4));
        Integer elapsedDays = couponPeriodInt - ((Long)TimeUnit.DAYS.convert(couponEnd.getTime().getTime() - reportDate.getTime().getTime(), TimeUnit.MILLISECONDS)).intValue();
        Double interest = elapsedDays * couponRate * updatedNominalValue / 36000.0;
        return null;
    }



    public Double calcBi(Double standingRate, Calendar reportDate, Calendar expirationDate){
        return 10.0/(1+standingRate*(CalendarUtil.calcDiffDays(expirationDate,reportDate))/36000.0);
    }

/*    public Double calcM(Double standingRate, Double couponRate, Calendar couponStart, Calendar couponEnd, Calendar expirationDate, Calendar reportDate, String couponPeriod, Double yield, Double marketSurcharge, Integer updatedNominalValue){
        Integer couponPeriodInt = Integer.parseInt(couponPeriod.substring(0,couponPeriod.length()-4));
        Integer elapsedDays = couponPeriodInt - CalendarUtil.calcDiffDays(couponEnd,reportDate);
        Calendar lCouponEnd;
        Calendar lCouponStart = (Calendar) couponStart.clone();
        Integer drag = 0;
        Integer newDays= 0;
        Integer[] data;
        Double couponValue;
        Integer newCoupon = 0;
        Double price = 0.0;

        do {
            lCouponEnd = ((Calendar)lCouponStart.clone());
            lCouponEnd.add(Calendar.DAY_OF_YEAR, couponPeriodInt);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            drag = 0;
            data  = calcDays(lCouponStart, lCouponEnd);
            if(data[1] != null){
                drag = data[1];
            }
            newDays = data[0];
            if(lCouponEnd.equals(expirationDate)){
                couponValue = updatedNominalValue.doubleValue();
            }else{
                couponValue = 0.0;
            }
            if(elapsedDays >= 182 && newCoupon == 1){
                lCouponStart = lCouponEnd;
                newCoupon=1;
            }else{
                price += (newDays*couponRate/360 + couponValue)
                        /
                        Math.pow(1+standingRate*couponPeriodInt/36000,
                                ((CalendarUtil.calcDiffDays(lCouponEnd,reportDate)/(couponPeriodInt*1.0))));
                lCouponStart.equals(lCouponEnd);
                newCoupon=1;
            }


        }while(lCouponStart.getTime().getTime() < expirationDate.getTime().getTime());
    }*/


    /**
     *
     *
     Private Function CalculaDays(PdFhIni As Date, PdFhFin As Variant, PnArrastre As Integer, Optional PnFlu As Integer) As Integer
     Dim Fh_Original     As Date
     Dim nb_Fecha        As String
     On Error GoTo ErrCalculaDays

     Fh_Original = PdFhFin
     nb_Fecha = PdFhFin
     If Application.WorksheetFunction.VLookup(CDbl(PdFhFin), Range("Calendario"), 3, 0) = 1 Then

     PdFhFin = Application.WorksheetFunction.WorkDay(PdFhFin, PnFlu)
     PnArrastre = Fh_Original - PdFhFin

     End If
     CalculaDays = PdFhFin - PdFhIni

     * @param startCoupon
     * @param endCoupon
     * @return
     */

    public Integer[] calcDays(Calendar startCoupon, Calendar endCoupon){
        Calendar origianlDate;
        Calendar lEndCoupon;
        Integer result[] = new Integer[2];
        origianlDate = (Calendar) endCoupon.clone();
        List<CalendarEntity> byDateEquals = daoCalendar.findByDateEqualsAndFkIdMarketTypeEquals(endCoupon,1);
        boolean isHolidayIn = false;
        if (endCoupon.get(Calendar.DAY_OF_WEEK) == 7 || endCoupon.get(Calendar.DAY_OF_WEEK) == 1){
            isHolidayIn = true;
            if(endCoupon.get(Calendar.DAY_OF_WEEK) == 7 ){
                result[0] = CalendarUtil.calcDiffDays(endCoupon, startCoupon)-1;
                result[1] = 1;
            }else if(endCoupon.get(Calendar.DAY_OF_WEEK) == 1 ){
                result[0] = CalendarUtil.calcDiffDays(endCoupon, startCoupon)-2;
                result[1] = 2;
            }
            return result;
        }else if(byDateEquals.size()>0 && byDateEquals.get(0) != null){
            CalendarEntity calendarEntity = byDateEquals.get(0);
            isHolidayIn = calendarEntity.getIsHoliday()==0?false:true;
        }else{
            result[0] = CalendarUtil.calcDiffDays(endCoupon, startCoupon);
            result[1] = 0;
            return result;
        }
        if(isHolidayIn){
            lEndCoupon = (daoCalendar.findTop1ByDateLessThanEqualAndIsHolidayEqualsOrderByDateDesc(endCoupon,(byte)1))
                    .get(0).getDate();
            result[1] = CalendarUtil.calcDiffDays(origianlDate,lEndCoupon);
        }else{
            result[1] = 0;
        }
        result[0] = CalendarUtil.calcDiffDays(endCoupon,startCoupon);
        return result;
    }

    public Double dirtPriceM(Calendar couponStrart,
                            Calendar valuationDay,
                            Double standingRate,
                            Integer couponPeriod,
                            Integer nominalValue,
                            Double couponRate,
                            Integer couponsDueToExpire){
        Calendar lCouponEnd = (Calendar)couponStrart.clone();
        Calendar lCouponStart = (Calendar)couponStrart.clone();
        Integer lCouponPeriod = couponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        for(int i = 0;i < couponsDueToExpire;i++){
            lCouponEnd.add(Calendar.DAY_OF_YEAR, lCouponPeriod);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            lCouponPeriod = daysDrag[0];
            drag = daysDrag[1];
            final Double fi = calcFi(nominalValue, couponRate, lCouponPeriod, i+1, couponsDueToExpire);
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            price += fi/
                    Math.pow(1.0+(standingRate*(182.0/36000.0)),
                            diffDays/182.0);
            lCouponStart = (Calendar)lCouponEnd.clone();
        }
        return price;

    }

    //TODO use calc days to change
    public Double calcFi(Integer nominalValue, Double couponRate, Integer couponDays, Integer cycle, Integer totalCouponsToPay){
        Double result = null;
        if(cycle == totalCouponsToPay){
            result = nominalValue+ (nominalValue * couponDays * couponRate)/(36000);
        }else{
            result = (nominalValue * couponDays * couponRate)/(36000);
        }
        return result;
    }

}
