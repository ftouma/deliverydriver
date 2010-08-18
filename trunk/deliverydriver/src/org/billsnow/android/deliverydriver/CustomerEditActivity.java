package org.billsnow.android.deliverydriver;

import java.util.Date;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomerEditActivity extends Activity {
  
  private Customer customer;
  
  private Customer getCustomer() {
    Cursor c = managedQuery(getIntent().getData(), null, null, null, null);
    c.moveToFirst();
    if (c.getLong(c.getColumnIndex(CustomerProvider.TIMELAST)) < 0) {
      return new Customer(c.getString(c.getColumnIndex(CustomerProvider.PHONE)),
                          c.getString(c.getColumnIndex(CustomerProvider.ADDRESS)),
                          c.getString(c.getColumnIndex(CustomerProvider.COMMENTS)),
                          c.getInt(c.getColumnIndex(CustomerProvider.TIPS)),
                          c.getInt(c.getColumnIndex(CustomerProvider.NRUNS)),
                          null,
                          c.getDouble(c.getColumnIndex(CustomerProvider.LATITUDE)),
                          c.getDouble(c.getColumnIndex(CustomerProvider.LONGITUDE)));
    }
    else {      
      return new Customer(c.getString(c.getColumnIndex(CustomerProvider.PHONE)),
                          c.getString(c.getColumnIndex(CustomerProvider.ADDRESS)),
                          c.getString(c.getColumnIndex(CustomerProvider.COMMENTS)),
                          c.getInt(c.getColumnIndex(CustomerProvider.TIPS)),
                          c.getInt(c.getColumnIndex(CustomerProvider.NRUNS)),
                          new Date(c.getLong(c.getColumnIndex(CustomerProvider.TIMELAST))),
                          c.getDouble(c.getColumnIndex(CustomerProvider.LATITUDE)),
                          c.getDouble(c.getColumnIndex(CustomerProvider.LONGITUDE)));
    }
  }
  private void readCustomer() {
    customer.setAddress(((EditText)getWindow().findViewById(
               R.id.cust_editaddress)).getText().toString());
    customer.setAddress(addresswidget.getText().toString());
    customer.setComments(commentswidget.getText().toString());    
  }
  private int updateCustomer() {
    ContentValues row = new ContentValues();
    //row.put(CustomerProvider.PHONE, customer.getPhone());
    row.put(CustomerProvider.ADDRESS, customer.getAddress());
    row.put(CustomerProvider.COMMENTS, customer.getComments());
    //row.put(CustomerProvider.NRUNS, customer.getNRuns());
    //row.put(CustomerProvider.TIPS, customer.getTips());
    //row.put(CustomerProvider.TIMELAST, customer.getTimeLast());
    row.put(CustomerProvider.LATITUDE, customer.getLatitude());
    row.put(CustomerProvider.LONGITUDE, customer.getLongitude());
    return getContentResolver().update(getIntent().getData(), row, null, null);
  }
  
  private void writeWidgets() {
    phonewidget.setText(customer.getPhone());
    addresswidget.setText(customer.getAddress());
    commentswidget.setText(customer.getComments());
    nrunswidget.setText(customer.getNRunsReadable());
    tipavgwidget.setText("$" + customer.getAvgTip());
    timelastwidget.setText(customer.getTimeLastReadable());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.customeredit);
    customer = getCustomer();
    writeWidgets();
    savewidget.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        readCustomer();
        updateCustomer();
      }
    });
    loadwidget.setOnClickListener(new View.OnClickListener() {      
      @Override
      public void onClick(View v) {
        writeWidgets();
      }
    });
  }
}
