package gov.nih.nlm.ncbi.model;

import android.provider.BaseColumns;

public final class Contract {

    private Contract() {

    }

    public static abstract class Summary implements BaseColumns {

        /**
         * The table name
         */
        public static final String TABLE_NAME = "summary";

        /**
         * Column name for the data of the summary
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_DATA = "data";

        /**
         * Column name for the offline of the summary (0 = false, 1 = true)
         * <P>Type: INTEGER</P>
         */
        public static final String COLUMN_NAME_OFFLINE = "offline";
    }

    public static abstract class Content implements BaseColumns {

        /**
         * The table name
         */
        public static final String TABLE_NAME = "content";

        /**
         * Column name for the data of the content
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_DATA = "data";
    }
}