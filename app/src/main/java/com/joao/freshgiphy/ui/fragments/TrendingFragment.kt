package com.joao.freshgiphy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.joao.freshgiphy.R
import com.joao.freshgiphy.di.AppInjector
import com.joao.freshgiphy.ui.adapters.TrendingPagedAdapter
import com.joao.freshgiphy.utils.Constants
import com.joao.freshgiphy.utils.extensions.addTextWatcherDebounce
import com.joao.freshgiphy.viewmodel.TrendingViewModel
import kotlinx.android.synthetic.main.fragment_trending.*

class TrendingFragment : Fragment() {

    private val viewModel by lazy {
        val factory = AppInjector.getTrendingViewModelFactory()
        ViewModelProvider(this, factory).get(TrendingViewModel::class.java)
    }

    private val trendingAdapter by lazy {
        TrendingPagedAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        val layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }

        recyclerView.apply {
            this.adapter = trendingAdapter
            this.layoutManager = layoutManager //TODO
        }

        searchEdt.addTextWatcherDebounce(Constants.EDIT_TEXT_DEBOUNCE_TIME) { viewModel.search(it) }
    }

    private fun setupObservers() {
        viewModel.getGifs().observe(this, Observer { trendingAdapter.submitList(it) })
        viewModel.getNetworkState().observe(this, Observer { trendingAdapter.setNetworkState(it) })
    }

}