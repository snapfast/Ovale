package bali.rahul.ovale.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import bali.rahul.ovale.R
import bali.rahul.ovale.databinding.ItemPhotoCardBinding
import bali.rahul.ovale.storage.dao.PhotoDao
import bali.rahul.ovale.storage.model.PhotoStore
import bali.rahul.ovale.ui.PhotoActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class PhotoStoreRecyclerAdapter(private var photos: List<PhotoStore>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private lateinit var photoDao: PhotoDao

    // Define a coroutine scope
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
            val photoStore = PhotoStore(id = photos[position].id)

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
            photoStore.link = photos[position].link
            photoStore.user = photos[position].user
            photoStore.username = photos[position].username
            photoStore.likes = photos[position].likes

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

    fun setPhotoStores(photoList: List<PhotoStore>?) {
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

        fun bind(photo: PhotoStore) {
            binding.authorTextView.text = photo.username
            binding.colorTextView.text = photo.color

            Log.d(">>>>>>>>>>>>>>", "binding done using PhotoViewHolder")

            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions)
                .load(photo.link).transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView)
        }
    }
}

fun Intent.putPhotoExtra(name: String, photo: PhotoStore) {
    val bundle = Bundle().apply {
        putParcelable(name, photo)
    }
    putExtras(bundle)
}

