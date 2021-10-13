package ru.ipimenov.informationboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ru.ipimenov.informationboard.data.Advertisement
import ru.ipimenov.informationboard.databinding.AdvertListItemBinding

class AdvertListRVAdapter(val auth: FirebaseAuth) : RecyclerView.Adapter<AdvertListRVAdapter.AdvertHolder>() {

    lateinit var binding: AdvertListItemBinding
    val advertList = ArrayList<Advertisement>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertHolder {
        binding = AdvertListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvertHolder(binding, auth)
    }

    override fun onBindViewHolder(holder: AdvertHolder, position: Int) {
        holder.setData(advertList[position])
    }

    override fun getItemCount(): Int {
        return advertList.size
    }

    fun updateAdapter(newAdvertList: List<Advertisement>) {
        advertList.clear()
        advertList.addAll(newAdvertList)
        notifyDataSetChanged()
    }

    class AdvertHolder(val binding: AdvertListItemBinding, val auth: FirebaseAuth) : RecyclerView.ViewHolder(binding.root) {

        fun setData(advertisement: Advertisement) {
            binding.apply {
                tvName.text = advertisement.name
                tvDescription.text = advertisement.description
                tvPrice.text = advertisement.price
            }
            visibilityEditPanel(isOwner(advertisement))
        }

        private fun isOwner(advertisement: Advertisement): Boolean {
            return advertisement.uid == auth.uid
        }

        private fun visibilityEditPanel(isOwner: Boolean) {
            if (isOwner) {
                binding.editPanel.visibility = View.VISIBLE
            } else {
                binding.editPanel.visibility = View.GONE
            }
        }
    }
}