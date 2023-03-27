package bali.rahul.ovale.adapter

// write recyclerview adapter to have image with textview below it

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import bali.rahul.ovale.PhotoActivity
import bali.rahul.ovale.R
import bali.rahul.ovale.dataModel.Photo
import bali.rahul.ovale.databinding.ItemPhotoCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


class RecyclerAdapter(private var photos: List<Photo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder = PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_card, parent, false)
        )

        viewHolder.heartImageView.setOnClickListener {
            Toast.makeText(parent.context, "Heart Loved", Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // Add an onClickListener to the CardView
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PhotoActivity::class.java)
            intent.putPhotoExtra("photo", photos[position])
            startActivity(holder.itemView.context, intent, null)
        }

        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(photos[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun setPhotos(photoList: MutableList<Photo>?) {
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

        val binding = ItemPhotoCardBinding.bind(itemView)

        private val photoImage: ImageView = itemView.findViewById(R.id.imageView)
        val heartImageView: ImageView = itemView.findViewById(R.id.heart_image_view)

        fun bind(photo: Photo) {
            binding.authorTextView.text = photo.user?.username
            binding.colorTextView.text = photo.color

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(photo.urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
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

