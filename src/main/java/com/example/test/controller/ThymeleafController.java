package com.example.test.controller;

import com.example.test.Rate;
import com.example.test.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;

@Controller
public class ThymeleafController {

    @Autowired
    private RateService rateService;

    @GetMapping("/refresh")
    public void getRate(){
        rateService.getData();
        //每一次刷新都會重置DB資料
    }
    @GetMapping("/homepage")
    public String home(Model model){
        List<Rate> rateList = rateService.selects(Arrays.asList(558, 559, 565));
        //取得DB中id為558,559,565的資料
        model.addAttribute("rateList",rateList);
        return "mainpage";
    }
}
