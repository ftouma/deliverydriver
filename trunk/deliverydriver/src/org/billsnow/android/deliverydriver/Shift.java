package org.billsnow.android.deliverydriver;

import java.util.Date;

public class Shift {
  private Date start;
  private Date end;
  private int nruns;
  private int tipscash;
  private int tipsnoncash;
  private int compensation;
  private int hourlywage;
  private float distance; //in miles
  
  public Shift(int hourlywage) {
    start = new Date();
    nruns = 0;
    compensation = 0;
    this.hourlywage = hourlywage;
    distance = 0;
    tipscash = 0;
    tipsnoncash = 0;
  }
  public Shift(long start, long end, int hourlywage, int nruns, 
               int tipscash, int tipsnoncash, int compensation, 
               float distance) {
    this.start = new Date(start);
    this.end = new Date(end);
    this.hourlywage = hourlywage;
    this.nruns = nruns;
    this.compensation = compensation;
    this.distance = distance;
    this.tipscash = tipscash;
    this.tipsnoncash = tipsnoncash;
  }
  public void endShift(int compperrun, int bonus, float distance) {
    end = new Date();
    this.distance = distance;
    compensation += compperrun*nruns + bonus;
  }
  public void addRun(int tipscash, int tipsnoncash, int bonus) {
    nruns++;
    this.tipscash += tipscash;
    this.tipsnoncash += tipsnoncash;
    this.compensation += bonus;
  }
  public long getStart() {
    return start.getTime();
  }
  public long getEnd() {
    return end.getTime();
  }
  public int getNRuns() {
    return nruns;
  }
  public int getTipsCash() {
    return tipscash;
  }
  public int getTipsNonCash() {
    return tipsnoncash;    
  }
  public int getHourlyWage() {
    return hourlywage;
  }
  public double getMileage() {
    return distance;
  }
  public int getCompensation() {
    return compensation;
  }
  public String getShiftTime() {
    return start.toString();
  }
  public String getNRunsReadable() {
    return String.valueOf(nruns);
  }
  private float shiftlen() {
    //returns hours cutoff to 3 decimal points 
    return ((getEnd()-getStart()) / 3600) * 0.001f;
  }
  public String getShiftLength() {
    return String.valueOf(shiftlen()) + " hrs";
  }
  public String getTipsNonCashReadable() {
    return String.valueOf(tipsnoncash/100) + "." + String.valueOf(tipsnoncash%100);
  }
  public String getTakeHome() {
    int total = tipscash+tipsnoncash+compensation;
    return String.valueOf(total/100 + ((total%100+50)/100));
  }
  public String getWageReadable() {
    return String.valueOf(getWage()/100) + "." + String.valueOf(getWage()%100);
  }
  public int getWage() {
    return (int) (hourlywage * shiftlen());
  }
}
