package dev.rao.rockmarket.country.presentation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.rao.rockmarket.core.domain.model.Country
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
            animateItemSelection(it)
            onCountrySelected(country)
        }
    }

    private fun animateItemSelection(view: View) {
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f)
        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f)

        scaleDownX.duration = 100
        scaleDownY.duration = 100
        scaleUpX.duration = 200
        scaleUpY.duration = 200

        scaleUpX.interpolator = OvershootInterpolator(1.5f)
        scaleUpY.interpolator = OvershootInterpolator(1.5f)

        val scaleDown = AnimatorSet()
        scaleDown.play(scaleDownX).with(scaleDownY)

        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleDown).before(scaleUp)
        animatorSet.start()
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

            country.flagUrl.let { url ->
                Glide.with(binding.root)
                    .load(url)
                    .circleCrop()
                    .into(binding.flagImageView)
            }

            // Cambiar estilo si está seleccionado
            if (isSelected) {
                binding.root.elevation = 8f
            } else {
                binding.root.elevation = 4f
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