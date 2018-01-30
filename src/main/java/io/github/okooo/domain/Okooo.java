package io.github.okooo.domain;

import javax.persistence.Id;

public class Okooo {

    @Id
    private Long id;
    private String serial;
    private String matchType;
    private String matchTime;
    private String host;
    private String guest;
    private String idx;
    private String biff;
    private String giff;
    private String kelly;
    private String odds;
    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getBiff() {
        return biff;
    }

    public void setBiff(String biff) {
        this.biff = biff;
    }

    public String getGiff() {
        return giff;
    }

    public void setGiff(String giff) {
        this.giff = giff;
    }

    public String getKelly() {
        return kelly;
    }

    public void setKelly(String kelly) {
        this.kelly = kelly;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
