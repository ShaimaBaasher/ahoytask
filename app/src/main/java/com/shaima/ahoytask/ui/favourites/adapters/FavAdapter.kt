package com.shaima.ahoytask.ui.favourites.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shaima.ahoytask.data.home.CityResponse
import com.shaima.ahoytask.data.home.WeatherResponse
import com.shaima.ahoytask.databinding.ItemWeatherBinding
import com.shaima.ahoytask.databinding.ItemWeatherFavouriteBinding
import com.shaima.api.repository.Utils
import com.ydhnwb.cleanarchitectureexercise.domain.login.entity.WeatherEntity

class FavAdapter(private val products: MutableList<WeatherEntity>) : RecyclerView.Adapter<FavAdapter.ViewHolder>(){
    interface OnItemTap {
        fun onTap(product: WeatherEntity)
    }

    fun setItemTapListener(l: OnItemTap) {
        onTapListener = l
    }

    private var isCel: Boolean = false
    private var onTapListener: OnItemTap? = null

    fun updateList(bool : Boolean, mProducts: List<WeatherEntity>) {
        isCel = bool
        products.clear()
        products.addAll(mProducts)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemWeatherFavouriteBinding) : RecyclerView.ViewHolder(itemBinding.root){
        private var degreeVal: String? = null

        fun bind(product: WeatherEntity) {
            itemBinding.time.text = Utils.convertTime(product.list[0].dt_txt)
            itemBinding.location.text = product.city.name
            if (isCel)
                degreeVal = "${product.list[0].main?.temp} °C"
            else degreeVal = "${product.list[0].main?.fahrenheit} °F"
            itemBinding.temp.text = degreeVal
            itemBinding.cardRoot.setOnClickListener {
                onTapListener?.onTap(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemWeatherFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position])

    override fun getItemCount() = products.size
}