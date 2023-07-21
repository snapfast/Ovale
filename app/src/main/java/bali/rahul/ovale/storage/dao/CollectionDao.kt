package bali.rahul.ovale.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import bali.rahul.ovale.storage.model.CollectionStore

@Dao
interface CollectionDao {

    @Query("SELECT * FROM ovale_collections ORDER BY date_added DESC")
    fun getSelectedCollections(): LiveData<List<CollectionStore>>

    @Query("SELECT id FROM ovale_collections")
    fun getSelectedCollectionIds(): LiveData<List<String>>

    @Query("SELECT id FROM ovale_collections LIMIT 1 OFFSET :offset")
    suspend fun getRandomCollectionId(offset: Int): String?

    @Query("SELECT COUNT(*) FROM ovale_collections")
    suspend fun getNumberOfCollections(): Int

    @Query("SELECT COUNT(*) FROM ovale_collections")
    fun getNumberOfCollectionsLiveData(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM ovale_collections WHERE id = :id")
    fun getCountById(id: String): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(collectionStore: CollectionStore)

    @Query("DELETE FROM ovale_collections WHERE id = :id")
    suspend fun delete(id: String)

    // Used for testing
    @Query("SELECT * FROM ovale_collections WHERE id = :id")
    fun getCollection(id: String): CollectionStore
}
