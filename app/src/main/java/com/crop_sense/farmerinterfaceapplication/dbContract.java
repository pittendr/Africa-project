package com.crop_sense.farmerinterfaceapplication;

import android.provider.BaseColumns;

/**
 * Created by Nima on 11/20/2015.
 */
public final class dbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public dbContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "test2";
        public static final String COLUMN_NAME_MAC = "mac";
        public static final String COLUMN_NAME_SERIAL = "serial";
        public static final String COLUMN_NAME_AID = "aid";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_GPS = "gps";
        public static final String COLUMN_NAME_PESTS = "pests";
    }

}