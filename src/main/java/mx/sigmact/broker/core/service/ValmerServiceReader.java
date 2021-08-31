package mx.sigmact.broker.core.service;

import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created on 01/11/16.
 */
@Service
public class ValmerServiceReader {

    @Value("${valmer_price_vector_service}")
    private String valmerPriceVectorService;

    private final String USER_AGENT = "Mozilla/5.0";

    private static final Logger log = LoggerFactory.getLogger(ValmerServiceReader.class);

    public ValmerServiceReader() {
        super();
    }

    public List<ValmerPriceVectorEntity> readCSV(InputStream is, Integer id) {
        List<ValmerPriceVectorEntity> result = null;
        final Reader in = new InputStreamReader(new BOMInputStream(is));
        try {
            final CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withHeader(
                    "Fecha", "Emision", "TV", "Emisora", "Serie", "Tipo Instrumento", "Sobretasa de Mercado",
                    "Sobretasa de colocacion", "ISR", "SobretasaTotal", "Rendimiento", "Tasa Cupon", "Dias Plazo",
                    "Fecha de Colocacion", "Fecha de Vencimiento", "Dias por Vencer", "Curva de Descuento",
                    "Periodo de Cupon", "FITCH", "S&P", "Moodys", "HR", "Cve.", "Inicio de Cupon",
                    "Fin de Cupon", "Precio Sucio MD", "Precio Limpio MD", "Intereses", "Duracion Ajustada.",
                    "Convexidad", "Valor Nominal Actualizado", "Valores Inscritos", "Monto enCirculacion",
                    "Representante Comun", "Agente Colocador", "Cupones por vencer", "Cupones vencidos",
                    "Días por vencer alCC", "Sector", "Isin", "VERUM").withSkipHeaderRecord());
            result = new ArrayList<>();
            int j = 0;
            for (final CSVRecord record : parser) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date formatDate = null;
                Calendar cal = null;
                String tmp;
                ValmerPriceVectorEntity vpv = new ValmerPriceVectorEntity();
                vpv.setIdValmerPriceVector(id + j + 1);
                j++;
                for (int i = 0; i < record.size(); i++) {
                    switch (i) {
                        case 0:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                formatDate = sdf.parse(record.get(i));
                                cal = Calendar.getInstance();
                                cal.setTime(formatDate);
                                vpv.setDate(cal);
                            }
                            break;
                        case 1:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setIssue(record.get(i));
                            }
                            break;
                        case 2:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setTv(record.get(i));
                            }
                            break;
                        case 3:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setIssuingCompany(record.get(i));
                            }
                            break;
                        case 4:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setSeries(record.get(i));
                            }
                            break;
                        case 5:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setInstrumentType(record.get(i));
                            }
                            break;
                        case 6:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setMarketSurcharge(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 7:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setPlacementSurcharge(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 8:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setIncomeTax(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 9:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setTotalSurcharge(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 10:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setYield(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 11:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setCouponRate(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 12:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setTermDays(Integer.parseInt(record.get(i)));
                            }
                            break;
                        case 13:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                formatDate = sdf.parse(record.get(i));
                                cal = Calendar.getInstance();
                                cal.setTime(formatDate);
                                vpv.setPlacementDate(cal);
                            }
                            break;
                        case 14:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                formatDate = sdf.parse(record.get(i));
                                cal = Calendar.getInstance();
                                cal.setTime(formatDate);
                                vpv.setExpirationDate(cal);
                            }
                            break;
                        case 15:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setDaysToExpire(Integer.parseInt(record.get(i)));
                            }
                            break;
                        case 16:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setDiscountCurve(record.get(i));
                            }
                            break;
                        case 17:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setCouponPeriod(record.get(i));
                            }
                            break;
                        case 18:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setFitch(record.get(i));
                            }
                            break;
                        case 19:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setSp(record.get(i));
                            }
                            break;
                        case 20:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setMoodis(record.get(i));
                            }
                            break;
                        case 21:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setHr(record.get(i));
                            }
                            break;
                        case 22:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setCve(Integer.parseInt(record.get(i)));
                            }
                            break;
                        case 23:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                formatDate = sdf.parse(record.get(i));
                                cal = Calendar.getInstance();
                                cal.setTime(formatDate);
                                vpv.setCouponStart(cal);
                            }
                            break;
                        case 24:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                formatDate = sdf.parse(record.get(i));
                                cal = Calendar.getInstance();
                                cal.setTime(formatDate);
                                vpv.setCouponEnd(cal);
                            }
                            break;
                        case 25:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setDirtyPrice(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 26:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setCleanPrice(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 27:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setInterest(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 28:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setAdjustedTerm(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 29:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setConvexity(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 30:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setUpdatedNominalValue(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 31:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setSignedValue(Long.parseLong(record.get(i)));
                            }
                            break;
                        case 32:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setOustandingAmount(Double.parseDouble(record.get(i)));
                            }
                            break;
                        case 33:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setCommonRepresentative(record.get(i));
                            }
                            break;
                        case 34:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setPlacementAgent(record.get(i));
                            }
                            break;
                        case 35:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setCouponsDueToExpire(Integer.parseInt(record.get(i)));
                            }
                            break;
                        case 36:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setExpiredCoupons(Integer.parseInt(record.get(i)));
                            }
                            break;
                        case 37:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setDaysToContractClosing(Integer.parseInt(record.get(i)));
                            }
                            break;
                        case 38:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setSector(record.get(i));
                            }
                            break;
                        case 39:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setIsin(record.get(i));
                            }
                            break;
                        case 40:
                            tmp = record.get(i);
                            if (tmp != null && !tmp.isEmpty()) {
                                vpv.setVerum(record.get(i));
                            }
                            break;
                    }
                }
                result.add(vpv);
            }
        } catch (IOException e) {
            log.error("IOException");
            log.error(e.getMessage());
        } catch (ParseException e) {
            log.error("Parser excpetion");
            log.error(e.getMessage());
        }
        return result;
    }

    public List<ValmerPriceVectorEntity> readCSV(File file, Integer id) {
        try {
            return readCSV(new FileInputStream(file), id);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public List<ValmerPriceVectorEntity> setMarketToVector(List<ValmerPriceVectorEntity> list, Integer marketId) {
        for (ValmerPriceVectorEntity vpv : list) {
            vpv.setFkIdMarketType(marketId);
        }
        return list;
    }

    public String getValmer(Calendar day) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(day.getTime());
        StringBuilder sb = new StringBuilder();
        sb.append(valmerPriceVectorService).append(format);
        String responseBody;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(sb.toString());
            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseBody = httpclient.execute(httpget, responseHandler);
        }
        return responseBody;

    }

    public String getValmerPlain(Calendar day) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(day.getTime());
        StringBuilder sb = new StringBuilder();
        sb.append(valmerPriceVectorService).append(format);
        String url = sb.toString();

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            //log.info(inputLine);
            response.append(inputLine+"\n");
        }
        in.close();
        return response.toString();

    }


}
