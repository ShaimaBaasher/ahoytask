package com.shaima.ahoytask.ui.home.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shaima.ahoytask.R
import com.shaima.ahoytask.data.home.WeatherResponse
import com.shaima.ahoytask.databinding.ItemWeatherBinding
import com.shaima.api.repository.Utils
import com.squareup.picasso.Picasso

class HomeWeatherAdapter(private val products: MutableList<WeatherResponse>) : RecyclerView.Adapter<HomeWeatherAdapter.ViewHolder>(){
    interface OnItemTap {
        fun onTap(product: WeatherResponse)
    }

    fun setItemTapListener(l: OnItemTap) {
        onTapListener = l
    }

    private var isCel: Boolean = false
    private var onTapListener: OnItemTap? = null

    fun updateList(bool : Boolean, mProducts: List<WeatherResponse>) {
        isCel = bool
        products.clear()
        products.addAll(mProducts)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemWeatherBinding) : RecyclerView.ViewHolder(itemBinding.root){
        private var degreeVal: String? = null

        fun bind(product: WeatherResponse) {
//            val img = "http://openweathermap.org/img/w/${product.weather!![0].icon}.png"
//            Log.d("images", img)
//            Picasso.get().load(img).error(R.mipmap.ic_launcher)
//                .into(itemBinding.imgIcon);
            itemBinding.dayName.text = Utils.convertDate(product.dt_txt)
            if (isCel)
                degreeVal = "${product.main?.temp} °C"
            else degreeVal = "${product.main?.fahrenheit} °F"
            itemBinding.temp.text = degreeVal
            itemBinding.textHum.text = "${product.main?.humidity} H"
            itemBinding.tempDesc.text = product.weather!![0].main
            itemBinding.cardRoot.setOnClickListener {
                onTapListener?.onTap(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position])

    override fun getItemCount() = products.size
}