package com.example.test.db;

import com.example.test.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class RateDb {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public boolean insert(String name, BigDecimal rate, Date date){
        String sql = "INSERT INTO rate(name,rate,date) VALUE (:exchangeName, :exchangeRate ,:date) " +
                "ON DUPLICATE KEY UPDATE rate = :exchangeRate, date =:date";
        //只會存最新一筆的rate/data
        Map<String,Object> map = new HashMap<>();
        map.put("exchangeName",name);
        map.put("exchangeRate",rate);
        map.put("date",date);
        return namedParameterJdbcTemplate.update(sql, map) > 0;
        // nameParameterJdbcTemplate會回傳int值，如果大於0則代表執行成功
    }

    public boolean delete(int exchangeId){
        String sql = "DELETE FROM rate WHERE id = :exchangeId";
        Map<String ,Object> map = new HashMap<>();
        map.put("exchangeId",exchangeId);
        return namedParameterJdbcTemplate.update(sql,map) > 0;
    }
    public boolean updateRate(int exchangeId,BigDecimal newRate) {
        String sql = "UPDATE rate SET rate = :newRate WHERE id = :id";
        Map<String, Object> map = new HashMap<>();
        map.put("newRate", newRate);
        map.put("id", exchangeId);

        return namedParameterJdbcTemplate.update(sql, map) > 0;
    }

    public List<Rate> selects(List<Integer> rateIdList) {
        String sql = "SELECT * FROM rate WHERE id IN (:rateIds)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("rateIds", rateIdList);

        return namedParameterJdbcTemplate.query(sql, paramMap, (rs, rowNum) -> {
            //rs是每行查詢到的數據，rowNum是行數
            Rate rate = new Rate();
            rate.setId(rs.getInt("id"));
            rate.setName(rs.getString("name"));
            rate.setRate(rs.getBigDecimal("rate"));
            return rate;
        });
    }
}
