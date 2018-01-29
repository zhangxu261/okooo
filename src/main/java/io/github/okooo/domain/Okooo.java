package io.github.okooo.domain;

import javax.persistence.Id;

public class Okooo {

    @Id
    private Long id;
    private String serial;
    private String matchType;
    private String matchTime;
    private String hostTeam;
    private String guestTeam;
    private String zhishu;
    private String chayi;
    private String kaili;
    private String odds;
    private String matchResult;

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

    public String getHostTeam() {
        return hostTeam;
    }

    public void setHostTeam(String hostTeam) {
        this.hostTeam = hostTeam;
    }

    public String getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(String guestTeam) {
        this.guestTeam = guestTeam;
    }

    public String getZhishu() {
        return zhishu;
    }

    public void setZhishu(String zhishu) {
        this.zhishu = zhishu;
    }

    public String getChayi() {
        return chayi;
    }

    public void setChayi(String chayi) {
        this.chayi = chayi;
    }

    public String getKaili() {
        return kaili;
    }

    public void setKaili(String kaili) {
        this.kaili = kaili;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }
}
