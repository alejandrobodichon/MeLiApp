package com.dev.meliapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.dev.meliapp.R
import com.dev.meliapp.databinding.ItemProductBinding
import com.dev.meliapp.model.ProductModel

class ProductListAdapter(private val productList: ArrayList<ProductModel>):
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>(), ProductClickListener {

    class ProductViewHolder(var view: ItemProductBinding): RecyclerView.ViewHolder(view.root)

    fun updateProductList(newProductList: List<ProductModel>){
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemProductBinding>(inflater, R.layout.item_product,parent,false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.view.product = productList[position]
        holder.view.listener = this
    }

    override fun getItemCount() = productList.size

    override fun onClick(view: View) {
        productList.forEach { productModel ->
            if (view.tag == productModel.id){
                productModel.id?.let {
                    val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(it)
                    Navigation.findNavController(view).navigate(action) }
            }
        }
    }
}