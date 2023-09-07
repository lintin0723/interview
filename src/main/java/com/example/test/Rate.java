package com.example.test;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Rate {
    private int id;
    private String name;
    private BigDecimal rate;
    //對應到DB的資料類型，為了將DB資料取出
}
