package ru.ipimenov.informationboard.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ru.ipimenov.informationboard.MainActivity
import ru.ipimenov.informationboard.activities.EditAdsActivity
import ru.ipimenov.informationboard.model.Advertisement
import ru.ipimenov.informationboard.databinding.AdvertListItemBinding

class AdvertListRVAdapter(val mainActivity: MainActivity) :
    RecyclerView.Adapter<AdvertListRVAdapter.AdvertHolder>() {

    lateinit var binding: AdvertListItemBinding
    val advertList = ArrayList<Advertisement>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertHolder {
        binding = AdvertListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvertHolder(binding, mainActivity)
    }

    override fun onBindViewHolder(holder: AdvertHolder, position: Int) {
        holder.setData(advertList[position])
    }

    override fun getItemCount(): Int {
        return advertList.size
    }

    fun updateAdapter(newAdvertList: List<Advertisement>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(advertList, newAdvertList))
        diffResult.dispatchUpdatesTo(this)
        advertList.clear()
        advertList.addAll(newAdvertList)
    }

    class AdvertHolder(val binding: AdvertListItemBinding, val mainActivity: MainActivity) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(advertisement: Advertisement) = with(binding) {
            tvName.text = advertisement.name
            tvDescription.text = advertisement.description
            tvPrice.text = advertisement.price
            visibilityEditPanel(isOwner(advertisement))
            btEditAdvert.setOnClickListener(onClickEdit(advertisement))
            btDeleteAdvert.setOnClickListener {
                mainActivity.onDeleteMyAdvert(advertisement)
            }
        }

        private fun onClickEdit(advertisement: Advertisement): View.OnClickListener {
            return View.OnClickListener {
                val intent = Intent(mainActivity, EditAdsActivity::class.java).apply {
                    putExtra(EditAdsActivity.EDIT_STATE, true)
                    putExtra(EditAdsActivity.ADVERT_DATA, advertisement)
                }
                mainActivity.startActivity(intent)
            }
        }

        private fun isOwner(advertisement: Advertisement): Boolean {
            return advertisement.uid == mainActivity.myAuth.uid
        }

        private fun visibilityEditPanel(isOwner: Boolean) {
            if (isOwner) {
                binding.editPanel.visibility = View.VISIBLE
            } else {
                binding.editPanel.visibility = View.GONE
            }
        }
    }

    interface DeleteMyAdvertListener {
        fun onDeleteMyAdvert(advertisement: Advertisement)
    }
}