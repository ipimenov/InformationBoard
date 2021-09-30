package ru.ipimenov.informationboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.databinding.FragmentImageListBinding
import ru.ipimenov.informationboard.utils.ImagePicker
import ru.ipimenov.informationboard.utils.ItemTouchMoveCallBack
import java.text.ParsePosition

class ImageListFragment(private val closeFragment: CloseFragment, private val newList: ArrayList<String>) : Fragment() {

    lateinit var binding: FragmentImageListBinding
    val adapter = SelectImageRVAdapter()
    private val dragCallback = ItemTouchMoveCallBack(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        touchHelper.attachToRecyclerView(binding.rvImageList)
        binding.rvImageList.layoutManager = LinearLayoutManager(activity)
        binding.rvImageList.adapter = adapter
        adapter.updateAdapter(newList, true)
    }

    override fun onDetach() {
        super.onDetach()
        closeFragment.onCloseFragment(adapter.imageList)
    }

    private fun setUpToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_choose_images)
        val deleteImages = binding.toolbar.menu.findItem(R.id.id_delete_images)
        val addImages = binding.toolbar.menu.findItem(R.id.id_add_images)

        binding.toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }

        deleteImages.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            true
        }

        addImages.setOnMenuItemClickListener {
            val imageCounter = ImagePicker.MAX_IMAGE_COUNTER - adapter.imageList.size
            ImagePicker.getImages(activity as AppCompatActivity, imageCounter, ImagePicker.REQUEST_CODE_GET_IMAGES)
            true
        }
    }

    fun updateAdapter(newList: ArrayList<String>) {
        adapter.updateAdapter(newList, false)
    }

    fun setSingleImage(imageUriString: String, position: Int) {

        adapter.imageList[position] = imageUriString
        adapter.notifyDataSetChanged()

    }
}