package bali.rahul.ovale.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import bali.rahul.ovale.storage.dao.CollectionDao
import bali.rahul.ovale.storage.dao.PhotoDao
import bali.rahul.ovale.storage.model.CollectionStore
import bali.rahul.ovale.storage.model.PhotoStore

@Database(
    entities = [
        CollectionStore::class,
        PhotoStore::class,
    ], version = 2, exportSchema = false
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
    }
}
