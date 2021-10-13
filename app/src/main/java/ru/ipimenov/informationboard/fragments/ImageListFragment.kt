package ru.ipimenov.informationboard.fragments

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.activities.EditAdsActivity
import ru.ipimenov.informationboard.databinding.FragmentImageListBinding
import ru.ipimenov.informationboard.dialogs.ProgressDialog
import ru.ipimenov.informationboard.utils.AdapterCallback
import ru.ipimenov.informationboard.utils.ImageManager
import ru.ipimenov.informationboard.utils.ImagePicker
import ru.ipimenov.informationboard.utils.ItemTouchMoveCallBack

class ImageListFragment(
    private val closeFragment: CloseFragment,
    private val newList: ArrayList<String>?
) : BaseAdsFragment(), AdapterCallback {

    lateinit var binding: FragmentImageListBinding

    val adapter = SelectImageRVAdapter(this)
    private val dragCallback = ItemTouchMoveCallBack(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var addImages: MenuItem? = null
    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentImageListBinding.inflate(inflater)
        adView = binding.adView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        with(binding) {
            touchHelper.attachToRecyclerView(rvImageList)
            rvImageList.layoutManager = LinearLayoutManager(activity)
            rvImageList.adapter = adapter
        }
        if (newList != null) resizeSelectedImages(newList, true)
    }

    override fun onImageDelete() {
        addImages?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>) {
        adapter.updateAdapter(bitmapList, true)

    }

    override fun onDetach() {
        super.onDetach()
        closeFragment.onCloseFragment(adapter.imageList)
        job?.cancel()
    }

    override fun onClose() {
        super.onClose()
        activity?.supportFragmentManager?.beginTransaction()
            ?.remove(this@ImageListFragment)
            ?.commit()
    }

    private fun resizeSelectedImages(newList: ArrayList<String>, needClear: Boolean) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity as Activity)
            val bitmapList = ImageManager.resizeImages(newList)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if (adapter.imageList.size > 2) addImages?.isVisible = false
        }
    }

    private fun setUpToolbar() {

        with(binding) {

            toolbar.inflateMenu(R.menu.menu_choose_images)
            val deleteImages = toolbar.menu.findItem(R.id.id_delete_images)
            addImages = toolbar.menu.findItem(R.id.id_add_images)

            toolbar.setNavigationOnClickListener {

                showInterstitialAd()

            }

            deleteImages.setOnMenuItemClickListener {
                adapter.updateAdapter(ArrayList(), true)
                addImages?.isVisible = true
                true
            }

            addImages?.setOnMenuItemClickListener {
                val imageCounter = ImagePicker.MAX_IMAGE_COUNTER - adapter.imageList.size
                val editAdsActivity = activity as EditAdsActivity
                ImagePicker.launch(
                    editAdsActivity,
                    editAdsActivity.launcherMultiSelectImages,
                    imageCounter
                )
                true
            }
        }
    }

    fun updateAdapter(newList: ArrayList<String>) {
        resizeSelectedImages(newList, false)
    }

    fun setSingleImage(imageUriString: String, position: Int) {
        val progressBarItem =
            binding.rvImageList[position].findViewById<ProgressBar>(R.id.progress_bar_item)
        job = CoroutineScope(Dispatchers.Main).launch {
            progressBarItem.visibility = View.VISIBLE
            val bitmapList = ImageManager.resizeImages(listOf(imageUriString))
            adapter.imageList[position] = bitmapList[0]
            progressBarItem.visibility = View.GONE
            adapter.notifyItemChanged(position)
        }
    }
}