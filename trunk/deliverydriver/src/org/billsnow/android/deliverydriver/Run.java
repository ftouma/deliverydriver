package org.billsnow.android.deliverydriver;

import java.util.Date;

public class Run extends Customer {
  private int bonus;
  private int tipcash;
  private int tipnoncash;
  private Date start;
  private Date end;
    
  public Run(String phone) {
    super(phone);
    start = new Date();
  }
  public void complete(int bonus, int tipcash, int tipnoncash) {
    this.bonus = bonus;
    this.tipcash = tipcash;
    this.tipnoncash = tipnoncash;
    end = new Date();
  }
  public boolean ongoing() {
    return (end == null);
  }
  public int getBonus() {
    return bonus;
  }
  public int getTipCash() {
    return tipcash;
  }
  public int getTipNonCash() {
    return tipnoncash;
  }
  public String getTip() {
    int tip = tipcash + tipnoncash;
    return String.valueOf(tip/100) + "." + String.valueOf(tip%100);
  }
  public long getStart() {
    return start.getTime();
  }
  public long getEnd() {
    return end.getTime();
  }
  private int deltime() {
    // returns delivery time in seconds 
    return (int) (getEnd()-getStart()) / 1000;
  }
  public String getDeliveryTime() {
    return String.valueOf(deltime() / 60) + "min, " + String.valueOf(deltime() % 60) + "sec";
  }
}
