package com.dev.meliapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.dev.meliapp.R
import com.dev.meliapp.databinding.LayoutPagerBinding
import com.dev.meliapp.model.PicturerModel


class CustomPagerAdapter(private val context: Context, private val images: List<PicturerModel>) : PagerAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = DataBindingUtil.inflate<LayoutPagerBinding>(inflater, R.layout.layout_pager, container, false)
        view.picture = images[position]
        container.addView(view.root)
        return view.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ==  `object`
    }
}
