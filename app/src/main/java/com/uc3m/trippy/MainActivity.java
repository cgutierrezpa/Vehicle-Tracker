package com.uc3m.trippy;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.uc3m.trippy.db.TripContract;
import com.uc3m.trippy.db.TripDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TripDbHelper mHelper;
    private ListView mTripListView;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new TripDbHelper(this);
        mTripListView = (ListView) findViewById(R.id.list_trip);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_trip:
                final EditText tripEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(tripEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String trip = String.valueOf(tripEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TripContract.TripEntry.COL_TRIP_TITLE, trip);
                                db.insertWithOnConflict(TripContract.TripEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView tripTextView = (TextView) parent.findViewById(R.id.trip_title);
        String trip = String.valueOf(tripTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TripContract.TripEntry.TABLE,
                TripContract.TripEntry.COL_TRIP_TITLE + " = ?",
                new String[]{trip});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> tripTitle = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TripContract.TripEntry.TABLE,
                new String[]{TripContract.TripEntry._ID, TripContract.TripEntry.COL_TRIP_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TripContract.TripEntry.COL_TRIP_TITLE);
            tripTitle.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_trip,
                    R.id.trip_title,
                    tripTitle);
            mTripListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(tripTitle);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
}
