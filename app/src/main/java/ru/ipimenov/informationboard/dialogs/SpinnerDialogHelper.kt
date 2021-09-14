package ru.ipimenov.informationboard.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ipimenov.informationboard.R
import ru.ipimenov.informationboard.utils.CityHelper

class SpinnerDialogHelper {

    fun spinnerDialogShow(context: Context, list: ArrayList<String>, tvCountryCity: TextView) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_dialog, null)
        val adapter = RecyclerSpinnerDialogAdapter(dialog, tvCountryCity)
        val rvSpinnerDialog = view.findViewById<RecyclerView>(R.id.rv_spinner_dialog)
        val svSpinnerDialog = view.findViewById<SearchView>(R.id.sv_spinner_dialog)
        rvSpinnerDialog.layoutManager = LinearLayoutManager(context)
        rvSpinnerDialog.adapter = adapter
        adapter.updateAdapter(list)
        dialog.setView(view)
        setSearchViewListener(adapter, list, svSpinnerDialog)

        dialog.show()
    }

    private fun setSearchViewListener(adapter: RecyclerSpinnerDialogAdapter, list: ArrayList<String>, searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = CityHelper.filterListData(list, newText.toString().trim())
                adapter.updateAdapter(tempList)
                return true
            }
        })

    }
}