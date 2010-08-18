package org.billsnow.android.deliverydriver;

import java.util.Date;

public class Customer {
  private String phone;  //must be 10 digits to ensure unique customers
  private String address;
  private String comments;
  private int tips;
  private int nruns;
  private Date timelast;
  private double latitude; //values from -180 to 180 are valid 
  private double longitude; //values from -180 to 180 are valid
    
  public Customer(String phone) {
    this(phone, "", "", 0, 0, 0, 181, 181);
  }
  public Customer(String phone, String address, String comments,
                  int tips, int nruns, long timelast, 
                  double latitude, double longitude) {
    this.phone = phone;
    this.address = address;
    this.comments = comments;
    this.tips = tips;
    this.nruns = nruns;
    this.timelast = new Date(timelast);
    this.latitude = latitude;
    this.longitude = longitude;
  }
  public void addRun(int tip) {
    tips += tip;
    nruns++;
    timelast = new Date();
  }
  public void setLocation(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public void setComments(String comments) {
    this.comments = comments;
  }
  public String getPhoneReadable() {
    return "(" + phone.substring(0, 3) + ") "
               + phone.substring(3, 6) + "-"
               + phone.substring(6, 10);
  }
  public String getPhone() {
	return phone;
  }
  public boolean hasLocation() {
    return (latitude != 181);
  }
  public double getLatitude() {
    return latitude;
  }
  public double getLongitude() {
    return longitude;
  }
  public String getAddress() {
	return address;
  }
  public String getComments() {
	return comments;
  }
  public int getNRuns() {
    return nruns;
  }
  public int getTips() {
    return tips;
  }
  public long getTimeLast() {
    return timelast.getTime();
  }
  public String getNRunsReadable() {
    return String.valueOf(nruns);
  }
  public String getAvgTip() {
    if (nruns>0)
      return String.valueOf(tips/nruns/100) + "." + String.valueOf(tips/nruns%100);
    else
      return "N/A";
  }
  public String getTimeLastReadable() {
    if (getTimeLast() > 0) {
      return timelast.toString();
    }
    else {
      return "N/A";
    }
  }
}
