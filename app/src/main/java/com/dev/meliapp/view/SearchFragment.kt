package com.dev.meliapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.meliapp.R
import com.dev.meliapp.model.ProductModel
import com.dev.meliapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private val  listAdapter by lazy { ProductListAdapter(arrayListOf()) }

    private val productListDataObserver = Observer<List<ProductModel>>{list ->
        list?.let {
            if (list.isNotEmpty()){
                rvProducts.visibility = View.VISIBLE
                listAdapter.updateProductList(it)
            } else {
                tvListError.text ="No products found."
                tvListError.visibility = View.VISIBLE
            }
        }
    }
    private val loadingDataObserver = Observer<Boolean>{isLoading ->
        pgLoadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading){
            tvListError.visibility = View.GONE
            rvProducts.visibility = View.GONE
        }
    }
    private val errorDataObserver = Observer<Boolean>{isError ->
        tvListError.visibility = if (isError) View.VISIBLE else View.GONE
        if (isError){
            pgLoadingView.visibility = View.GONE
            rvProducts.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btSearch.setOnClickListener{
            viewModel.retrieveProductsByName(etSearch.text.toString()?:"")
        }

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.products.observe(this, productListDataObserver)
        viewModel.loading.observe(this, loadingDataObserver)
        viewModel.loadError.observe(this, errorDataObserver)

        rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }
    }


}