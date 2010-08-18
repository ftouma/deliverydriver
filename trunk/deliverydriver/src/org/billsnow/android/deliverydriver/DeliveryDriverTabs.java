package org.billsnow.android.deliverydriver;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class DeliveryDriverTabs extends TabActivity {
  
  private static final String PREFS = "deldrivertab";
  private static final String SHIFTS = "shifts";
  private static final String CUSTOMERS = "customers";
  private static final String LASTTAB = "last_tab";
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tabs);
    
    Resources res = getResources();
    TabHost tabhost = getTabHost();    
    
    TabHost.TabSpec shifttabspec = tabhost.newTabSpec(SHIFTS);
    TabHost.TabSpec custtabspec = tabhost.newTabSpec(CUSTOMERS);
    Intent shiftlist = new Intent(this, ShiftListActivity.class);
    Intent custlist = new Intent(this, CustomerListActivity.class);
    
    shifttabspec.setContent(shiftlist);
    shifttabspec.setIndicator("Shifts", res.getDrawable(R.drawable.shifts));
    custtabspec.setContent(custlist);
    custtabspec.setIndicator("Customers", res.getDrawable(R.drawable.customers));
    tabhost.addTab(shifttabspec);
    tabhost.addTab(custtabspec);
    
    setDefaultTab(getSharedPreferences(PREFS, MODE_PRIVATE).getInt(LASTTAB, 0));
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    SharedPreferences.Editor edit = getSharedPreferences(
                                      PREFS, MODE_PRIVATE).edit();
    edit.putInt(LASTTAB, getTabHost().getCurrentTab());
    edit.commit();
  }

}
