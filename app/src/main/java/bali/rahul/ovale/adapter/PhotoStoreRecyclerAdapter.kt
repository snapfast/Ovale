package bali.rahul.ovale.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bali.rahul.ovale.R
import bali.rahul.ovale.databinding.ItemPhotostoreCardBinding
import bali.rahul.ovale.storage.model.PhotoStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class PhotoStoreRecyclerAdapter(private var photoStores: List<PhotoStore>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoStoreViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photostore_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is PhotoStoreViewHolder -> {
                holder.bind(photoStores[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return photoStores.size
    }

    fun setPhotoStores(photoStoreList: List<PhotoStore>?) {
        if (photoStoreList != null) {
            photoStores = photoStoreList
        } else {
            error("RecyclerAdapter Error: Photo list is null")
        }
    }

    class PhotoStoreViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemPhotostoreCardBinding.bind(itemView)

        fun bind(photoStore: PhotoStore) {
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions)
                .load(photoStore.url_regular).transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
                .into(binding.imageView)
        }
    }


}