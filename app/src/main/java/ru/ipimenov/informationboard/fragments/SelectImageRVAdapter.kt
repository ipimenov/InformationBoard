package ru.ipimenov.informationboard.fragments

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.activities.EditAdsActivity
import ru.ipimenov.informationboard.databinding.ImageListItemBinding
import ru.ipimenov.informationboard.utils.AdapterCallback
import ru.ipimenov.informationboard.utils.ImageManager
import ru.ipimenov.informationboard.utils.ImagePicker
import ru.ipimenov.informationboard.utils.ItemTouchMoveCallBack

class SelectImageRVAdapter(val adapterCallback: AdapterCallback) :
    RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(),
    ItemTouchMoveCallBack.ItemTouchAdapter {

    lateinit var binding: ImageListItemBinding
    val imageList = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        binding = ImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(binding, this)
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

    class ImageHolder(val binding: ImageListItemBinding, val adapter: SelectImageRVAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        val context = binding.root.context as EditAdsActivity

        fun setData(itemBitmap: Bitmap) {
            binding.tvTitle.text =
                context.resources.getStringArray(R.array.array_titles)[adapterPosition]
            ImageManager.chooseScaleType(binding.rvImageItem, itemBitmap)
            binding.rvImageItem.setImageBitmap(itemBitmap)

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
                adapter.adapterCallback.onImageDelete()
            }
        }
    }

    fun updateAdapter(newImageList: List<Bitmap>, needClear: Boolean) {
        if (needClear) imageList.clear()
        imageList.addAll(newImageList)
        notifyDataSetChanged()
    }
}