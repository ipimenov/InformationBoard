package ru.ipimenov.informationboard.fragments

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.utils.ItemTouchMoveCallBack

class SelectImageRVAdapter : RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(), ItemTouchMoveCallBack.ItemTouchAdapter {

    val imageList = ArrayList<ImageItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_list_item, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onMove(startPosition: Int, targetPosition: Int) {
        val targetItem = imageList[targetPosition]
        imageList[targetPosition] = imageList[startPosition]
        val startItemTitle = imageList[targetPosition].title
        imageList[targetPosition].title = targetItem.title
        imageList[startPosition] = targetItem
        imageList[startPosition].title = startItemTitle
        notifyItemMoved(startPosition, targetPosition)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tvTitle: TextView
        lateinit var imageItem: ImageView

        fun setData(item: ImageItem) {

            tvTitle = itemView.findViewById(R.id.tv_title)
            imageItem = itemView.findViewById(R.id.rv_image_item)
            tvTitle.text = item.title
            imageItem.setImageURI(Uri.parse(item.imageUriString))

        }
    }

    fun updateAdapter(newImageList: List<ImageItem>, needClear: Boolean) {

        if (needClear) imageList.clear()
        imageList.addAll(newImageList)
        notifyDataSetChanged()

    }
}