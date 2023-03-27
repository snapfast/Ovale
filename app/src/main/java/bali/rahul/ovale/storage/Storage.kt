package bali.rahul.ovale.storage

import android.app.Application
import androidx.room.Room


class Storage {

    private fun createWallpaperDatabase(application: Application) =
        Room.databaseBuilder(application, OvaleDatabase::class.java, "ovale_db")
            .fallbackToDestructiveMigration()
            .build()

}
