package bali.rahul.ovale.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import bali.rahul.ovale.R
import bali.rahul.ovale.dataModel.Collection
import bali.rahul.ovale.databinding.ItemCollectionCardBinding
import bali.rahul.ovale.ui.CollectionActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

class CollectionRecyclerAdapter(private var collections: List<Collection>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_collection_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CollectionActivity::class.java)
            intent.putCollectionExtra("collection", collections[position])
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }

        when (holder) {
            is PhotoViewHolder -> {
                holder.bind(collections[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    fun setPhotos(collectionList: List<Collection>?) {
        if (collectionList != null) {
            collections = collectionList
        } else {
            error("RecyclerAdapter Error: Photo list is null")
        }
    }

    class PhotoViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemCollectionCardBinding.bind(itemView)

        fun bind(collection: Collection) {
            binding.title.text = collection.title
            binding.numberPhotos.text = buildString {
                append("Number: ")
                append(collection.totalPhotos.toString())
            }

            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context).applyDefaultRequestOptions(requestOptions)
                .load(collection.coverPhoto?.urls?.regular)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image)
        }
    }

}

fun Intent.putCollectionExtra(key: String, value: Collection) {
    val bundle = Bundle().apply {
        putParcelable(key, value)
    }
    putExtras(bundle)
}





