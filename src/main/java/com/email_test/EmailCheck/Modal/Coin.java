package com.email_test.EmailCheck.Modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Coin {

    @Id
    @JsonProperty("id")
    public String id;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("name")
    public String name;

    @JsonProperty("image")
    public String image;

    @JsonProperty("current_price")
    public double currentPrice;

    @JsonProperty("market_cap")
    public long marketCap;

    @JsonProperty("market_cap_rank")
    public double marketCapRank;

    @JsonProperty("fully_diluted_valuation")
    public long fullyDilutedValuation;

    @JsonProperty("total_volume")
    public long totalVolume;

    @JsonProperty("high_24h")
    public double high24h;

    @JsonProperty("low_24h")
    public double low24h;

    @JsonProperty("price_change_24h")
    public double priceChange24h;

    @JsonProperty("price_change_percentage_24h")
    public double priceChangePercentage24h;

    @JsonProperty("market_cap_change_24h")
    public long marketCapChange24h;

    @JsonProperty("market_cap_change_percentage_24h")
    public double marketCapChangePercentage24h;

    @JsonProperty("circulating_supply")
    public long circulatingSupply;

    @JsonProperty("total_supply")
    public long totalSupply;

    @JsonProperty("max_supply")
    public long maxSupply;

    @JsonProperty("ath")
    public long ath;

    @JsonProperty("ath_change_percentage")
    public double athChangePercentage;

    @JsonProperty("ath_date")
    public Date athDate;

    @JsonProperty("atl")
    public double atl;

    @JsonProperty("atl_change_percentage")
    public double atlChangePercentage;

    @JsonProperty("atl_date")
    public Date atlDate;

    @JsonProperty("roi")
    @JsonIgnore
    public String roi;

    @JsonProperty("last_updated")
    public Date lastUpdated;
}