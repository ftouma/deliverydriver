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

public class ShiftProvider extends ContentProvider {
  public static final Uri CONTENT_URI = Uri
      .parse("content://org.billsnow.android.shiftprovider/shifts");

  public static final String SHIFTDATA = "shifts";
  public static final String _ID = "_id";
  public static final String START = "start";
  public static final String END = "end";
  public static final String WAGE = "hourlywage";
  public static final String NRUNS = "nruns";
  public static final String TIPSC = "tipscash";
  public static final String TIPSCC = "tipsnoncash";
  public static final String BONUS = "compensation";
  public static final String MILES = "milage";

  private static final int SHIFTS = 1;
  private static final int SHIFT_ID = 2;

  private static final UriMatcher matcher;
  static {
    matcher = new UriMatcher(UriMatcher.NO_MATCH);
    matcher.addURI("org.billsnow.android.shiftprovider", "shifts",
        SHIFTS);
    matcher.addURI("org.billsnow.android.shiftprovider", "shifts/#",
        SHIFT_ID);
  }

  private static class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
      super(context, SHIFTDATA + ".d", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table " + SHIFTDATA + "(" +
          _ID + " integer primary key autoincrement," +
          START + " integer," +
          END + " integer," +
          WAGE + " integer," +
          NRUNS + " integer default 0," +
          TIPSC + " integer default 0," +
          TIPSCC + " integer default 0," +
          BONUS + " integer default 0," +
          MILES + " real default 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("drop table if exists " + SHIFTDATA);
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
    case SHIFTS:
      return "vnd.android.cursor.dir/vnd.billsnow.shift";
    case SHIFT_ID:
      return "vnd.android.cursor.item/vnd.billsnow.shift";
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    Uri newid = null;
    switch (matcher.match(uri)) {
    case SHIFTS:
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      long id = db.insert(SHIFTDATA, START, values);
      if (id >= 0) {
        newid = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(newid, null);
      }
      break;
    default:
      throw new IllegalArgumentException("Invalid URI: " + uri);
    }
    return newid;
  }
  
  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int nrows = 0;
    SQLiteDatabase db = dbhelper.getWritableDatabase();
    switch (matcher.match(uri)) {
    case SHIFT_ID:      
      if (selection == null) {
        nrows = db.delete(SHIFTDATA, _ID + "=" + uri.getLastPathSegment(), null);
      }
      else {
        nrows = db.delete(SHIFTDATA, _ID + "=" + uri.getLastPathSegment() +
                          " and (" + selection + ")", selectionArgs);
      }
      break;
    case SHIFTS:
      nrows = db.delete(SHIFTDATA, selection, selectionArgs);
      break;
    default:
      throw new IllegalArgumentException("Invalid URI: " + uri);
    }
    if (nrows > 0)
      getContext().getContentResolver().notifyChange(uri, null);
    return nrows;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {
    int nrows = 0;
    switch (matcher.match(uri)) {
    case SHIFT_ID:
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      if (selection == null) {
        nrows = db.update(SHIFTDATA, values, _ID + "=" + uri.getLastPathSegment(), null);
      }
      else {
        nrows = db.update(SHIFTDATA, values, _ID + "=" + uri.getLastPathSegment() +
                          " and (" + selection + ")", selectionArgs);
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
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {
    SQLiteDatabase db = dbhelper.getReadableDatabase();
    Cursor c = null;
    switch (matcher.match(uri)) {
    case SHIFT_ID:
      if (selection == null) {
        c = db.query(SHIFTDATA, projection, _ID + "=" + uri.getLastPathSegment(),
                      null, null, null, sortOrder, null);
      }
      else {
        c = db.query(SHIFTDATA, projection, _ID + "=" + uri.getLastPathSegment() +
                     " and (" + selection + ")", selectionArgs, null, null, sortOrder, null);
      }
      break;
    case SHIFTS:
      if (selection == null) {
        c = db.query(SHIFTDATA, projection, null, null, null, 
                     null, sortOrder, null);
      }
      else {
        c = db.query(SHIFTDATA, projection, selection, selectionArgs, 
                     null, null, sortOrder, null);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    c.setNotificationUri(getContext().getContentResolver(), uri);
    return c;
  }
}
