package bali.rahul.ovale.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bali.rahul.ovale.R
import bali.rahul.ovale.databinding.ItemPhotostoreCardBinding
import bali.rahul.ovale.storage.model.PhotoStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


class PhotoStoreRecyclerAdapter(private var photoStores: List<PhotoStore>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photostore_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(photoStores[position])
                Log.d(">>>>>>>>>>>>>>", "binding photot: done inside onBindViewHolder")
            }
        }
    }

    override fun getItemCount(): Int {
        return photoStores.size
    }

    fun setPhotoStores(photoList: List<PhotoStore>?) {
        if (photoList != null) {
            photoStores = photoList
        } else {
            error("RecyclerAdapter Error: Photo list is null")
        }
    }

    class PhotoViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        //        private val binding = ItemPhotoCardBinding.bind(itemView)
        private val binding = ItemPhotostoreCardBinding.bind(itemView)

        fun bind(photo: PhotoStore) {
            binding.authorTextView.text = photo.username

            Log.d(">>>>>>>>>>>>>>", "binding done using PhotoViewHolder")
            Log.d(">>>>>>>>>>>>>>", "${photo.link}")

            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions)
                .load(photo.url_regular).transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageView)
        }
    }
}
