package ru.ipimenov.informationboard.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R

class ImagesViewPagerAdapter : RecyclerView.Adapter<ImagesViewPagerAdapter.PagerViewHolder>() {

    val imageList = ArrayList<String>()

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

        fun setData(uriString: String) {
            vPImageItem = itemView.findViewById(R.id.vp_image_item)
            vPImageItem.setImageURI(Uri.parse(uriString))
        }

    }

    fun update(newImageList: ArrayList<String>) {
        imageList.clear()
        imageList.addAll(newImageList)
        notifyDataSetChanged()
    }
}