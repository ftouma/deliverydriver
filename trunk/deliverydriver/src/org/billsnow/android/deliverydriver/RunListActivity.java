package org.billsnow.android.deliverydriver;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class RunListActivity extends ListActivity {
  
  private static final String PREFS_FILE = "settings";
  /* Key values defined in res/xml/settings.xml */
  private static String AREACODE = "default_area_code";
  private static String
  
  private static ArrayList<Run> completeruns;
  private static ArrayList<Run> pendingruns;
  private static Shift current;
  
  
  private boolean phoneok(String phone) {
    
  }
  private void addRun(String phone) {
    if (phoneok(phone)) {
      
      pendingruns.add(new Run(phone));
      updateView();
    }
    else {
      
    }
  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    SharedPreferences settings = getSharedPreferences(null, 0);
    settings.
    current = new Shift()
    completeruns = new ArrayList<Run>();
    pendingruns = new ArrayList<Run>();
  }
}
