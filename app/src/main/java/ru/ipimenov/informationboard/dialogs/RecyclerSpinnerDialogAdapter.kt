package ru.ipimenov.informationboard.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.activities.EditAdsActivity

class RecyclerSpinnerDialogAdapter(private val dialog: AlertDialog, private val tvCountryCity: TextView): RecyclerView.Adapter<RecyclerSpinnerDialogAdapter.SpinnerViewHolder>() {

    private val mainList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spinner_item, parent, false)
        return SpinnerViewHolder(view, dialog, tvCountryCity)
    }

    override fun onBindViewHolder(holder: SpinnerViewHolder, position: Int) {
        holder.setData(mainList[position])
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    class SpinnerViewHolder(itemView: View, private val dialog: AlertDialog, private val tvCountryCity: TextView) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var itemText = ""

        fun setData(string: String) {
            val tvSpinnerItem = itemView.findViewById<TextView>(R.id.tv_spinner_item)
            tvSpinnerItem.text = string
            itemText = string
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            tvCountryCity.text = itemText
            dialog.dismiss()
        }
    }

    fun updateAdapter(list: ArrayList<String>) {
        mainList.clear()
        mainList.addAll(list)
        notifyDataSetChanged()
    }
}