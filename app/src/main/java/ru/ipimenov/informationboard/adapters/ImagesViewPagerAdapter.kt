package ru.ipimenov.informationboard.adapters

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R

class ImagesViewPagerAdapter : RecyclerView.Adapter<ImagesViewPagerAdapter.PagerViewHolder>() {

    val imageList = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_pager_item, parent, false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.setData(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var vPImageItem: ImageView

        fun setData(imageBitmap: Bitmap) {
            vPImageItem = itemView.findViewById(R.id.vp_image_item)
            vPImageItem.setImageBitmap(imageBitmap)
        }

    }

    fun update(newImageList: List<Bitmap>) {
        imageList.clear()
        imageList.addAll(newImageList)
        notifyDataSetChanged()
    }
}