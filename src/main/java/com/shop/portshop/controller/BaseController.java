package com.shop.portshop.controller;

import com.shop.portshop.commons.APOD;
import com.shop.portshop.commons.APODClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Controller
public class BaseController {

    @GetMapping("/")
    public String moveMain(){
        return "/member_templates/index";
    }

    @GetMapping("/index")
    public String moveMain2(){
        return "/member_templates/index";
    }

    @GetMapping("/feature")
    public String moveFeature(){
        return "/aranoz/feature";
    }

    @GetMapping("/apod")
    public String moveTodayAPOD(Model model){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        APODClient apodClient = retrofit.create(APODClient.class);
        CompletableFuture<APOD> response = apodClient.getApod("DEMO_KEY");
        APOD apod = null;
        try {
            apod = response.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, String> info = new HashMap<>();
        info.put("title", apod.title);
        info.put("date", apod.date);
        info.put("explanation", apod.explanation);
        info.put("url", apod.url);

        model.addAttribute("info", info);
        return "/commons/apod";
    }




}
