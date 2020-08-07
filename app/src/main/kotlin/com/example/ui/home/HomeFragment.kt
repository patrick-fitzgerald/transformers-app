package com.example.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding.FragmentHomeBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentHomeBinding>()
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var autoBotViewAdapter: TransformersAdapter
    private lateinit var decepticonViewAdapter: TransformersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.viewModel = homeViewModel
        viewBinding.lifecycleOwner = this

        autoBotViewAdapter = TransformersAdapter(TransformerType.AUTOBOT)
        decepticonViewAdapter = TransformersAdapter(TransformerType.DECEPTICON)

        initListViews(viewBinding.autobotList, autoBotViewAdapter)
        initListViews(viewBinding.decepticonList, decepticonViewAdapter)

        homeViewModel.getTransformersRequest()

        return viewBinding.root
    }

    private fun initListViews(recyclerView: RecyclerView, transformersAdapter: TransformersAdapter) {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = transformersAdapter
        }
    }

    override fun onStart() {
        super.onStart()

        homeViewModel.transformers.observe(
            viewLifecycleOwner,
            Observer { transformers ->
                transformers?.let {
                    autoBotViewAdapter.addHeaderAndSubmitList(it)
                    decepticonViewAdapter.addHeaderAndSubmitList(it)
                }
            }
        )

        // click events
        homeViewModel.contextEventBus.subscribe { contextEvent ->
            context?.let {
                when (contextEvent) {
                    HomeViewModel.ContextEvent.GET_REPOS_BUTTON_CLICKED -> Timber.d("HomeFragment - getRepoButtonClicked")
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
    }
}
