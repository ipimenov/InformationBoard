package ru.ipimenov.informationboard.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.databinding.FragmentImageListBinding
import ru.ipimenov.informationboard.utils.ImagePicker
import ru.ipimenov.informationboard.utils.ItemTouchMoveCallBack

class ImageListFragment(private val closeFragment: CloseFragment, private val newList: ArrayList<String>) : Fragment() {

    lateinit var binding: FragmentImageListBinding
    val adapter = SelectImageRVAdapter()
    val dragCallback = ItemTouchMoveCallBack(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        touchHelper.attachToRecyclerView(binding.rvImageList)
        binding.rvImageList.layoutManager = LinearLayoutManager(activity)
        binding.rvImageList.adapter = adapter
        val updateList = ArrayList<ImageItem>()
        for (n in 0 until newList.size) {
            updateList.add(ImageItem(n.toString(), newList[n]))
        }
        adapter.updateAdapter(updateList, true)
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
            ImagePicker.getImages(activity as AppCompatActivity, imageCounter)
            true
        }
    }

    fun updateAdapter(newList: ArrayList<String>) {
        val updateList = ArrayList<ImageItem>()
        for (n in adapter.imageList.size until newList.size + adapter.imageList.size) {
            updateList.add(ImageItem(n.toString(), newList[n - adapter.imageList.size]))
        }
        adapter.updateAdapter(updateList, false)
    }
}