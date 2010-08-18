package org.billsnow.android.deliverydriver;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class CustomerProvider extends ContentProvider {

  public static final Uri CONTENT_URI = Uri.parse(
    "content://org.billsnow.android.customerprovider/customers");

  public static final String CUSTOMERDATA = "customers";
  
  public static final int NCOLUMNS = 9;
  public static final String _ID = "_id";
  public static final String PHONE = "phone";
  public static final String ADDRESS = "address";
  public static final String COMMENTS = "comments";
  public static final String TIPS = "tips";
  public static final String NRUNS = "nruns";
  public static final String TIMELAST = "timelast";
  public static final String LATITUDE = "latitude";
  public static final String LONGITUDE = "longitude";

  private static final int CUSTOMERS = 1;
  private static final int CUSTOMER_ID = 2;

  private static final UriMatcher matcher;
  static {
    matcher = new UriMatcher(UriMatcher.NO_MATCH);
    matcher.addURI("org.billsnow.android.customerprovider", "customers",
                   CUSTOMERS);
    matcher.addURI("org.billsnow.android.customerprovider", "customers/#",
                   CUSTOMER_ID);
  }

  private static class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
      super(context, CUSTOMERDATA + ".d", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table " + CUSTOMERDATA + "(" + 
                 _ID + " integer primary key autoincrement," + 
                 PHONE + " text unique," + 
                 ADDRESS + " text default ''," + 
                 COMMENTS + " text default ''," + 
                 TIPS + " integer default 0," + 
                 NRUNS + " integer default 0," + 
                 TIMELAST + " integer default 0," + 
                 LATITUDE + " real default 181," + 
                 LONGITUDE + " real default 181);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("drop table if exists " + CUSTOMERDATA);
      onCreate(db);
    }
  }

  private DatabaseHelper dbhelper;

  @Override
  public boolean onCreate() {
    dbhelper = new DatabaseHelper(getContext());
    return true;
  }

  @Override
  public String getType(Uri uri) {
    switch (matcher.match(uri)) {
    case CUSTOMERS:
      return "vnd.android.cursor.dir/vnd.billsnow.customer";
    case CUSTOMER_ID:
      return "vnd.android.cursor.item/vnd.billsnow.customer";
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int nrows = 0;
    switch (matcher.match(uri)) {
    case CUSTOMER_ID:
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      if (selection == null) {
        nrows = db.delete(CUSTOMERDATA, _ID + "=" + uri.getLastPathSegment(),
                          null);
      }
      else {
        nrows = db.delete(CUSTOMERDATA, _ID + "=" + uri.getLastPathSegment()
            + " and (" + selection + ")", selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Invalid URI: " + uri);
    }
    if (nrows > 0)
      getContext().getContentResolver().notifyChange(uri, null);
    return nrows;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) throws IllegalArgumentException {
    Uri newid = null;
    switch (matcher.match(uri)) {
    case CUSTOMERS:
      if (values.getAsString(PHONE).matches("[0-9]{10}")) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long id = db.insert(CUSTOMERDATA, PHONE, values);
        if (id >= 0) {
          newid = ContentUris.withAppendedId(uri, id);
          getContext().getContentResolver().notifyChange(newid, null);
        }
      }
      else {
        throw new IllegalArgumentException("Number must match [0-9]{10}");
      }
      break;
    default:
      throw new IllegalArgumentException("Invalid URI: " + uri);
    }
    return newid; //null indicates error
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {
    SQLiteDatabase db = dbhelper.getReadableDatabase();
    Cursor c = null;
    switch (matcher.match(uri)) {
    case CUSTOMER_ID:
      if (selection == null) {
        c = db.query(CUSTOMERDATA, projection, _ID + "="
            + uri.getLastPathSegment(), null, null, null, sortOrder, null);
      }
      else {
        c = db.query(CUSTOMERDATA, projection, _ID + "="
            + uri.getLastPathSegment() + " and (" + selection + ")",
                     selectionArgs, null, null, sortOrder, null);
      }
      break;
    case CUSTOMERS:
      if (selection == null) {
        c = db.query(CUSTOMERDATA, projection, null, null, null, null,
                     sortOrder, null);
      }
      else {
        c = db.query(CUSTOMERDATA, projection, selection, selectionArgs, null,
                     null, sortOrder, null);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    c.setNotificationUri(getContext().getContentResolver(), uri);
    return c;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {
    int nrows = 0;
    switch (matcher.match(uri)) {
    case CUSTOMER_ID:
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      if (selection == null) {
        nrows = db.update(CUSTOMERDATA, values, _ID + "="
            + uri.getLastPathSegment(), null);
      }
      else {
        nrows = db.update(CUSTOMERDATA, values, _ID + "="
            + uri.getLastPathSegment() + " and (" + selection + ")",
                          selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Invalid URI: " + uri);
    }
    if (nrows > 0)
      getContext().getContentResolver().notifyChange(uri, null);
    return nrows;
  }
}
