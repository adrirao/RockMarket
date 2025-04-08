package dev.rao.rockmarket.country.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.rao.rockmarket.country.domain.model.Country
import dev.rao.rockmarket.databinding.ItemCountryBinding

class CountryAdapter(
    private val onCountrySelected: (Country) -> Unit
) : ListAdapter<Country, CountryAdapter.CountryViewHolder>(CountryDiffCallback()) {

    private var selectedCountry: Country? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = getItem(position)
        holder.bind(country, country == selectedCountry)
        holder.itemView.setOnClickListener {
            onCountrySelected(country)
        }
    }

    fun setSelectedCountry(country: Country) {
        val oldSelectedCountry = selectedCountry
        selectedCountry = country

        oldSelectedCountry?.let { old ->
            notifyItemChanged(currentList.indexOf(old))
        }
        notifyItemChanged(currentList.indexOf(country))
    }

    class CountryViewHolder(
        private val binding: ItemCountryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Country, isSelected: Boolean) {
            binding.countryNameTextView.text = country.name
            binding.selectedImageView.isVisible = isSelected

            country.flag.let { url ->
                Glide.with(binding.root)
                    .load(url)
                    .circleCrop()
                    .into(binding.flagImageView)
            }
        }
    }
}

private class CountryDiffCallback : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}