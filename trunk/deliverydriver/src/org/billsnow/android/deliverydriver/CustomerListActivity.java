package org.billsnow.android.deliverydriver;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerListActivity extends ListActivity {
  
  private class CustomerAdapter extends ArrayAdapter<Customer> {
    private Activity context;
    CustomerAdapter(Activity context, ArrayList<Customer> customers) {
      super(context, R.layout.customer, customers);
      this.context = context;
    }
    public View getView(int position, View item, ViewGroup parent) {
      Customer curr = getItem(position);
      View row;
      //TextView phone, timelast, address, runs;
      if (item == null)
        row = context.getLayoutInflater().inflate(R.layout.customer, parent);
      else
        row = item;
      ((TextView)row.findViewById(R.id.cust_phone)).setText(curr.getPhoneReadable());
      ((TextView)row.findViewById(R.id.cust_timelast)).setText(curr.getTimeLastReadable());
      ((TextView)row.findViewById(R.id.cust_address)).setText(curr.getAddress());
      ((TextView)row.findViewById(R.id.cust_runs)).setText(curr.getNRunsReadable());
      return row;
    }
  }
  
  private static ArrayList<Customer> customers;
  
  private static final String[] PROJECTION = new String[] {
    //CustomerProvider._ID,
    CustomerProvider.PHONE,
    CustomerProvider.ADDRESS,
    CustomerProvider.COMMENTS,
    CustomerProvider.TIPS,
    CustomerProvider.NRUNS,
    CustomerProvider.TIMELAST,
    CustomerProvider.LATITUDE,
    CustomerProvider.LONGITUDE
  };
  
  private Customer getCustomer(Cursor c) {
    return new Customer(c.getString(
                        c.getColumnIndex(CustomerProvider.PHONE)),
            c.getString(c.getColumnIndex(CustomerProvider.ADDRESS)),
            c.getString(c.getColumnIndex(CustomerProvider.COMMENTS)),
            c.getInt(c.getColumnIndex(CustomerProvider.TIPS)),
            c.getInt(c.getColumnIndex(CustomerProvider.NRUNS)),
            c.getLong(c.getColumnIndex(CustomerProvider.TIMELAST)),
            c.getDouble(c.getColumnIndex(CustomerProvider.LATITUDE)),
            c.getDouble(c.getColumnIndex(CustomerProvider.LONGITUDE)));
  }
  
  private void addPhone() {
    
  }
    
  @Override
  protected Dialog onCreateDialog(int id, Bundle args) {
    super.onCreateDialog(id);
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Enter Phone:");
    EditText phone = new EditText(this);
    phone.setInputType(InputType.TYPE_CLASS_PHONE);
    dialog.setView(phone);
    dialog.setPositiveButton("Insert", new DialogInterface.OnClickListener() {      
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Uri uri;
        try {
          uri = insertPhone(phone.getText().toString());
        }
        catch(IllegalArgumentException e) {
          
        }
      }
    });
    return dialog.show();
  }
  
  private Uri insertPhone(String phone) throws IllegalArgumentException {
    ContentValues row = new ContentValues();
    row.put(CustomerProvider.PHONE, phone);
    return getContentResolver().insert(CustomerProvider.CONTENT_URI, row);
  }
  
  private long getID(String phone) {
    Cursor c = managedQuery(CustomerProvider.CONTENT_URI, 
              new String[] {CustomerProvider._ID}, null, null, null);
    if (c.moveToFirst())
      return -1;
    else
      return c.getLong(c.getColumnIndex(CustomerProvider._ID)); 
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Cursor c = managedQuery(CustomerProvider.CONTENT_URI, PROJECTION,
                                 null, null, CustomerProvider.PHONE);
    customers = new ArrayList<Customer>(c.getCount());
    for (int i=0; c.moveToPosition(i); i++)
      customers.add(i, getCustomer(c));
    
    CustomerAdapter adapter = new CustomerAdapter(this, customers);
    
    setListAdapter(adapter);
    registerForContextMenu(getListView());
  }
  
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Intent intent = new Intent(Intent.ACTION_EDIT, ContentUris.withAppendedId(
                     CustomerProvider.CONTENT_URI, getID(
                            customers.get(position).getPhone()))); 
    startActivity(intent);
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.custmenu, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
    Intent intent;
    switch (item.getItemId()) {
    case R.id.addcust:
      addPhone();
      return true;
    case R.id.custsettings:
      intent = new Intent(Intent.ACTION_EDIT).addCategory(Intent.CATEGORY_PREFERENCE);
      startActivity(intent);
      return true;
    default:
      return false;
    }
  }
}
