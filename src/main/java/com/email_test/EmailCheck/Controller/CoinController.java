package com.email_test.EmailCheck.Controller;

import com.email_test.EmailCheck.Service.CoinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("coins")
public class CoinController {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CoinService coinService;

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws Exception {
        String coin = coinService.getTop50();
        JsonNode jsonNode = objectMapper.readTree(coin);
        return new ResponseEntity<>(jsonNode, HttpStatus.OK);
    }

    @GetMapping("/trending")
    public ResponseEntity<JsonNode> getTreadingCoin() throws Exception {
        String coin = coinService.getTradingCoins();
        JsonNode jsonNode = objectMapper.readTree(coin);

        return new ResponseEntity<>(jsonNode,HttpStatus.OK);
    }

    @GetMapping("coin/{coinId}")
    public ResponseEntity<?> getDetails(String id) throws Exception{
        return new ResponseEntity<>(coinService.getCoinDetails(id), HttpStatus.OK);
    }
}
