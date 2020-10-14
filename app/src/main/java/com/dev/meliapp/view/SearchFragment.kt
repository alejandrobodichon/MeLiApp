package com.dev.meliapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.meliapp.R
import com.dev.meliapp.model.ProductModel
import com.dev.meliapp.util.hideKeyboard
import com.dev.meliapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import org.w3c.dom.Text

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private val listAdapter by lazy { ProductListAdapter(arrayListOf()) }

    private val productListDataObserver = Observer<List<ProductModel>> { list ->
        list?.let {
            if (list.isNotEmpty()) {
                rvProducts.visibility = View.VISIBLE
                listAdapter.updateProductList(it)
            } else {
                tvListError.text = "No se encontraron productos."
                tvListError.visibility = View.VISIBLE
            }
        }
    }
    private val loadingDataObserver = Observer<Boolean> { isLoading ->
        pgLoadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            tvListError.visibility = View.GONE
            rvProducts.visibility = View.GONE
        }
    }
    private val errorDataObserver = Observer<Boolean> { isError ->
        tvListError.visibility = if (isError) View.VISIBLE else View.GONE
        if (isError) {
            tvListError.text = "Hubo un error en la carga del listado, intente nuevamente."
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
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Buscar"

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.products.observe(this, productListDataObserver)
        viewModel.loading.observe(this, loadingDataObserver)
        viewModel.loadError.observe(this, errorDataObserver)

        initialize()

        rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }
    }

    private fun initialize() {
        etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.retrieveProductsByName(etSearch.text?.toString() ?: "")
                hideKeyboard(requireActivity())
                true
            } else false
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (etSearch.text.isEmpty())
                    ivClose.visibility = View.GONE
                else
                    ivClose.visibility = View.VISIBLE
            }
        })

        ivClose.setOnClickListener {
            etSearch.text.clear()
        }
    }
}