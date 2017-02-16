package com.uc3m.trippy.db;

import android.provider.BaseColumns;

public class TripContract {
    public static final String DB_NAME = "com.uc3m.trippy.db";
    public static final int DB_VERSION = 1;

    public class TripEntry implements BaseColumns {
        public static final String TABLE = "trips";

        public static final String COL_TRIP_TITLE = "title";
    }
}
