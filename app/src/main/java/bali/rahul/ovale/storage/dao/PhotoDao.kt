package bali.rahul.ovale.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bali.rahul.ovale.storage.model.PhotoStore

@Dao
interface PhotoDao {

    @Query("SELECT COUNT(*) FROM ovale_photos WHERE id = :id")
    fun getCountById(id: String): LiveData<Int>

    @Query("SELECT * FROM ovale_photos")
    fun getAllPhotos(): List<PhotoStore>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photoStore: PhotoStore)

    @Query("DELETE FROM ovale_photos WHERE id = :id")
    suspend fun delete(id: String)

    // Used for testing
    @Query("SELECT * FROM ovale_photos WHERE id = :id")
    fun getPhotos(id: String): PhotoStore
}
