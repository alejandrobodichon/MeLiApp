package com.dev.meliapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.meliapp.R
import com.dev.meliapp.databinding.FragmentDetailBinding
import com.dev.meliapp.model.ProductModel
import com.dev.meliapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding: FragmentDetailBinding
    private val attributeListAdapter by lazy { AttributeListAdapter(arrayListOf()) }


    private val productDetailDataObserver = Observer<ProductModel>{ product ->
        product?.let {
            renderView(it)
        }
    }

    private val loadingDataObserver = Observer<Boolean>{ isLoading ->
        pgLoadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading){
//            tvListError.visibility = View.GONE
//            rvProducts.visibility = View.GONE
        }
    }

    private val errorDataObserver = Observer<Boolean>{ isError ->
//        tvListError.visibility = if (isError) View.VISIBLE else View.GONE
        if (isError){
            pgLoadingView.visibility = View.GONE
//            rvProducts.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Detalle"

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.productDetail.observe(this, productDetailDataObserver)
        viewModel.loading.observe(this, loadingDataObserver)
        viewModel.loadError.observe(this, errorDataObserver)

        rvAttributes.apply {
            adapter = attributeListAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
            isNestedScrollingEnabled = false
        }


        arguments?.let {
            val productId = DetailFragmentArgs.fromBundle(it).productId
            viewModel.retrieveProductDetail(productId)
        }
    }

    private fun renderView(product: ProductModel){
        dataBinding.product = product
        product.pictures?.let {
            vPager.adapter = CustomPagerAdapter(requireContext(), product.pictures)
        }
        product.attributes?.let {
            attributeListAdapter.updateAttributeList(it)
        }
    }
}