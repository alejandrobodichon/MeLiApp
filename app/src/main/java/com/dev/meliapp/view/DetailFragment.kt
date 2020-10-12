package com.dev.meliapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dev.meliapp.R
import com.dev.meliapp.databinding.FragmentDetailBinding
import com.dev.meliapp.model.ProductModel


class DetailFragment : Fragment() {

    var product = ProductModel()
    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            product = DetailFragmentArgs.fromBundle(it).product
        }
        dataBinding.product = product
    }
}