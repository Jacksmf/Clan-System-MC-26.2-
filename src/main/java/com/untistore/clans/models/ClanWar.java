package com.untistore.clans.models;

public class ClanWar {
    private String clanIdA;
    private String clanIdB;
    private int pointsA;
    private int pointsB;

    private int betAmount;
    private String betCurrencyType;

    private long endTime;
    private boolean isActive;

    public void addKill(String clanId) {
        if (clanId.equals(clanIdA)) {
            pointsA++;
        } else if (clanId.equals(clanIdB)) {
            pointsB++;
        }
    }

    public int getBetAmount() {
        return betAmount;
    }
    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public String getBetCurrencyType() {
        return betCurrencyType;
    }
    public void setBetCurrencyType(String betCurrencyType) {
        this.betCurrencyType = betCurrencyType;
    }

    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getPointsA() {
        return  pointsA;
    }
    public int getPointsB() {
        return  pointsB;
    }

    public String getClanIdA() {
        return clanIdA;
    }
    public String getClanIdB() {
        return clanIdB;
    }

    public void setActive(boolean b) {
        this.isActive = b;
    }
}
