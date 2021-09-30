package ru.ipimenov.informationboard.fragments

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.activities.EditAdsActivity
import ru.ipimenov.informationboard.databinding.ImageListItemBinding
import ru.ipimenov.informationboard.utils.ImagePicker
import ru.ipimenov.informationboard.utils.ItemTouchMoveCallBack

class SelectImageRVAdapter : RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(),
    ItemTouchMoveCallBack.ItemTouchAdapter {

    lateinit var binding: ImageListItemBinding
    val imageList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        binding = ImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding.root
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_list_item, parent, false)
        return ImageHolder(view, binding, this)
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
        imageList[startPosition] = targetItem
        notifyItemMoved(startPosition, targetPosition)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView: View, val binding: ImageListItemBinding, val adapter: SelectImageRVAdapter) :
        RecyclerView.ViewHolder(itemView) {

         val context = binding.root.context as EditAdsActivity
//         val adapter = (binding.root.context as ImageListFragment).adapter

//        lateinit var tvTitle: TextView
//        lateinit var imageItem: ImageView
//        lateinit var imageItem: ImageView

        fun setData(item: String) {

//            tvTitle = itemView.findViewById(R.id.tv_title)
//            imageItem = itemView.findViewById(R.id.rv_image_item)
            binding.tvTitle.text =
                context.resources.getStringArray(R.array.array_titles)[adapterPosition]
            binding.rvImageItem.setImageURI(Uri.parse(item))
            binding.btEditImage.setOnClickListener {
                ImagePicker.getImages(context, 1, ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGE)
                context.editImagePosition = adapterPosition
            }
            binding.btDeleteImage.setOnClickListener {
                adapter.imageList.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for (position in 0 until adapter.imageList.size) {
                    adapter.notifyItemChanged(position)
                }
            }

        }
    }

    fun updateAdapter(newImageList: List<String>, needClear: Boolean) {

        if (needClear) imageList.clear()
        imageList.addAll(newImageList)
        notifyDataSetChanged()

    }
}