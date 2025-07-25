package com.email_test.EmailCheck.Service;

import com.email_test.EmailCheck.Modal.Coin;
import com.email_test.EmailCheck.Repository.CoinRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class CoinService{
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CoinRepository coinRepository;

    public String getTop50() throws Exception{
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=1";

        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters",httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);

            return response.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public String getTradingCoins() throws Exception {
        String url = "https://api.coingecko.com/api/v3/search/trending";

        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters",httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity,String.class);

            return response.getBody();
        }
        catch (HttpClientErrorException | HttpServerErrorException e)
        {
            throw new Exception(e.getMessage());
        }
    }

    public Coin getCoinDetails(String coinId) throws Exception {
        Optional<Coin> searchCoin = coinRepository.findById(coinId);
        if(searchCoin.isPresent()) {
            return searchCoin.get();
        }
        else {
            String url = "https://api.coingecko.com/api/v3/coins/" + coinId;

            RestTemplate restTemplate = new RestTemplate();

            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                HttpEntity<String> entity = new HttpEntity<String>("parameters", httpHeaders);

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                Coin coin = new Coin();

                coin.setId(jsonNode.get("id").asText());
                coin.setName(jsonNode.get("name").asText());
                coin.setSymbol(jsonNode.get("symbol").asText());
                coin.setImage(jsonNode.get("image").get("large").asText());

                JsonNode marketData = jsonNode.get("market_data");

                coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
                coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
                coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
                coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
                coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
                coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
                coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
                coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
                coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
                coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());
                coin.setTotalSupply(marketData.get("total_supply").asLong());

                return coinRepository.save(coin);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    public Coin findById(String coinId) throws Exception{
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if(optionalCoin.isEmpty()) throw new Exception("coin not found");
        return optionalCoin.get();
    }
}
