package ru.ipimenov.informationboard.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.utils.ItemTouchMoveCallBack

class ImageListFragment(private val closeFragment: CloseFragment, private val newList: ArrayList<String>) : Fragment() {

    val adapter = SelectImageRVAdapter()
    val dragCallback = ItemTouchMoveCallBack(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btBack = view.findViewById<Button>(R.id.btBack)
        val rvImageList = view.findViewById<RecyclerView>(R.id.rv_image_list)
        touchHelper.attachToRecyclerView(rvImageList)
        rvImageList.layoutManager = LinearLayoutManager(activity)
        rvImageList.adapter = adapter
        val updateList = ArrayList<ImageItem>()
        for (n in 0 until newList.size) {
            updateList.add(ImageItem(n.toString(), newList[n]))
        }
        adapter.updateAdapter(updateList)
        btBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.remove(this)
                ?.commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        closeFragment.onCloseFragment()
        Log.d("MyLog", "Title 0: ${adapter.imageList[0].title}")
        Log.d("MyLog", "Title 1: ${adapter.imageList[1].title}")
        Log.d("MyLog", "Title 2: ${adapter.imageList[2].title}")
    }
}