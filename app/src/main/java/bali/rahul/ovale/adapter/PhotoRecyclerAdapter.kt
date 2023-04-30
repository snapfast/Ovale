package bali.rahul.ovale.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import bali.rahul.ovale.R
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ItemPhotoCardBinding
import bali.rahul.ovale.storage.OvaleDatabase
import bali.rahul.ovale.storage.dao.PhotoDao
import bali.rahul.ovale.storage.model.PhotoStore
import bali.rahul.ovale.ui.PhotoActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoRecyclerAdapter(private var photos: List<Photo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private lateinit var photoDao: PhotoDao

    // Define a coroutine scope
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Create a suspend function that calls the DAO's insert function inside a coroutine
    private suspend fun insertItem(photoStore: PhotoStore, itemView: View) {
        withContext(Dispatchers.IO) {
            val db = OvaleDatabase.getInstance(itemView.context)
            photoDao = db.photoDao() // initialized photoDao
            val ret = photoDao.insert(photoStore)
            Log.d(">>>>>>>>>>>>>>", "insertItem: $ret")
            Log.d(">>>>>>>>>>>>>>", "item: $photoStore")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // Add an onClickListener to the CardView
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PhotoActivity::class.java)
            Log.d(">>>>>>>>>>>>>>", "intent: $intent")
            intent.putPhotoExtra("photo", photos[position])
            Log.d(">>>>>>>>>>>>>>", "opening photo activity: $photos[position]")
            startActivity(holder.itemView.context, intent, null)
        }

        // on clicking heart , save Photo data to database
        holder.itemView.findViewById<ImageView>(R.id.add_image_view).setOnClickListener {

            // write code to insert photo data to database
            val photoStore = PhotoStore(id = photos[position].id!!)

            // write code to create PhotoDao object and insert photo data to database
            photoStore.date_added = System.currentTimeMillis()
            photoStore.createdAt = photos[position].createdAt
            photoStore.updatedAt = photos[position].updatedAt
            photoStore.promotedAt = photos[position].promotedAt
            photoStore.width = photos[position].width
            photoStore.height = photos[position].height
            photoStore.color = photos[position].color
            photoStore.blurHash = photos[position].blurHash
            photoStore.description = photos[position].description
            photoStore.altDescription = photos[position].altDescription
            photoStore.url_regular = photos[position].urls?.regular
            photoStore.url_full = photos[position].urls?.full
            photoStore.link = photos[position].links?.html
            photoStore.user = photos[position].user?.name
            photoStore.username = photos[position].user?.username
            photoStore.likes = photos[position].likes

            coroutineScope.launch {
                insertItem(photoStore, holder.itemView)
                Toast.makeText(holder.itemView.context, "Saved to Favorites", Toast.LENGTH_SHORT)
                    .show()
            }

            holder.itemView.findViewById<ImageView>(R.id.add_image_view)
                .setColorFilter(R.color.white)

        }

        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(photos[position])
                Log.d(">>>>>>>>>>>>>>", "binding photot: done inside onBindViewHolder")
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun setPhotos(photoList: List<Photo>?) {
        if (photoList != null) {
            photos = photoList
        } else {
            error("RecyclerAdapter Error: Photo list is null")
        }
    }

    class PhotoViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPhotoCardBinding.bind(itemView)

        private val photoImage: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(photo: Photo) {
            binding.authorTextView.text = photo.user?.username
            binding.colorTextView.text = photo.color

            Log.d(">>>>>>>>>>>>>>", "binding done using PhotoViewHolder")

            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions)
                .load(photo.urls?.regular).transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoImage)
        }
    }
}

fun Intent.putPhotoExtra(name: String, photo: Photo) {
    val bundle = Bundle().apply {
        putParcelable(name, photo)
    }
    putExtras(bundle)
}

