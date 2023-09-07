package com.example.test.service;

import com.example.test.Rate;
import com.example.test.db.RateDb;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RateService {
    @Autowired
    private RateDb rateDb;

    @Autowired
    private RestTemplate restTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public void getData() {
        String url = "https://openapi.taifex.com.tw/v1/DailyForeignExchangeRates";
        byte[] fileBytes = restTemplate.getForObject(url, byte[].class);
        //讀api取得的data
        String jsonStr = new String(fileBytes);
        //把二進位檔變成jsonStr
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map<String, String>> dataList = objectMapper.readValue(jsonStr, new TypeReference<List<Map<String, String>>>() {});
            for (Map<String, String> data : dataList) {
                String dateString = data.get("Date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Date date = dateFormat.parse(dateString);

                for (String key : data.keySet()) {//把所有key都拿出來跑for，回傳list string
                    if(!key.equals("Date")){
                        insert(key,new BigDecimal(data.get(key)),date);
                        //data.get(key)取得key的value
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insert(String name, BigDecimal rate,Date date){
        return rateDb.insert(name,rate,date);
    }
    public boolean delete(Integer id){
        return rateDb.delete(id);
    }
    public boolean update(Integer id,BigDecimal rate){
        return rateDb.updateRate(id,rate);
    }

    public List<Rate> selects (List<Integer> id){
        return rateDb.selects(id);
    }
}