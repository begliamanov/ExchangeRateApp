package dev.begli.exchangerate.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.begli.exchangerate.databinding.ExchangeRateItemBinding

class ExchangeRatesAdapter(
    private val context: Context
    ) : RecyclerView.Adapter<ExchangeRatesAdapter.ViewHolder>() {

    private var rates: Map<String, Float> = mapOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSet(newRates: Map<String, Float>){
        rates = mapOf();
        rates = newRates
        notifyDataSetChanged();
    }

    class ViewHolder(view: ExchangeRateItemBinding): RecyclerView.ViewHolder(view.root) {
        val rateTextView = view.rateTextView
        val valueTextView = view.valueTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ExchangeRateItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get key by index
        val rateKey = rates.keys.toTypedArray()[position]
        // get value by key
        val rateValue = rates[rateKey]

        holder.apply {
            rateTextView.text = rateKey
            valueTextView.text = rateValue.toString()
        }

    }

    override fun getItemCount(): Int {
        return rates.size
    }
}