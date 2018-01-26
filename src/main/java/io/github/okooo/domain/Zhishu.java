package io.github.okooo.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
public class Zhishu {

    @Id
    private Long id;

    @Column(length = 10)
    private String serial;

    @Column(length = 20)
    private String matchType;

    @Column(length = 20)
    private String matchTime;

    @Column(length = 40)
    private String hostTeam;

    @Column(length = 40)
    private String guestTeam;

    @Column(length = 100)
    private String remind;

    @Column(length = 10)
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

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

}
