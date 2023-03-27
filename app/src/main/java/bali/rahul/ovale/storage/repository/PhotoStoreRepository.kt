package bali.rahul.ovale.storage.repository

import android.content.Context
import bali.rahul.ovale.storage.OvaleDatabase
import bali.rahul.ovale.storage.dao.PhotoDao
import bali.rahul.ovale.storage.model.PhotoStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PhotoStoreRepository(context: Context) {

    private val photoDao: PhotoDao

    init {
        val database = OvaleDatabase.getInstance(context)
        photoDao = database.photoDao()
    }

    suspend fun getAllPhotos(): List<PhotoStore> {
        return withContext(Dispatchers.IO) {
            photoDao.getAllPhotos()
        }
    }

    suspend fun insertPhoto(photo: PhotoStore) {
        withContext(Dispatchers.IO) {
            photoDao.insert(photo)
        }
    }

    suspend fun deletePhoto(id: String) {
        withContext(Dispatchers.IO) {
            photoDao.delete(id)
        }
    }

}
