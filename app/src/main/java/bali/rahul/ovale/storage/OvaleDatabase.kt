package bali.rahul.ovale.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import bali.rahul.ovale.storage.dao.CollectionDao
import bali.rahul.ovale.storage.dao.PhotoDao
import  bali.rahul.ovale.storage.model.CollectionStore
import  bali.rahul.ovale.storage.model.PhotoStore

@Database(
    entities = [
        CollectionStore::class,
        PhotoStore::class,
    ], version = 2, exportSchema = true
)
abstract class OvaleDatabase : RoomDatabase() {

    abstract fun collectionDao(): CollectionDao
    abstract fun photoDao(): PhotoDao

    companion object {

        @Volatile
        private var instance: OvaleDatabase? = null

        fun getInstance(context: Context): OvaleDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext, OvaleDatabase::class.java, "ovale_db"
                ).build()
                instance = newInstance
                newInstance
            }
        }

        /**
         * Migrate from:
         * version 1 - using Room where the {@link AutoWallpaperCollection#id} is an Int
         * to
         * version 2 - using Room where the {@link AutoWallpaperCollection#id} is a String
         *
         * SQLite supports a limited operations for ALTER. Changing the type of a column is not
         * directly supported, so this is what we need to do:
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE ovale_collections_new (
                        id TEXT NOT NULL, 
                        title TEXT, 
                        user_name TEXT, 
                        cover_photo TEXT,
                        date_added INTEGER, 
                        PRIMARY KEY(id)
                    )
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    INSERT INTO ovale_collections_new (
                        id, title, user_name, cover_photo, date_added
                    )
                    SELECT id, title, user_name, cover_photo, date_added 
                    FROM auto_wallpaper_collections
                    """.trimIndent()
                )
                database.execSQL("DROP TABLE ovale_collections")
                database.execSQL(
                    """
                    ALTER TABLE ovale_collections_new
                    RENAME TO ovale_collections
                    """.trimIndent()
                )
                database.execSQL(
                    """
                    CREATE INDEX index_ovale_collections_date_added 
                    ON ovale_collections (date_added)
                    """.trimIndent()
                )
            }
        }
    }
}
