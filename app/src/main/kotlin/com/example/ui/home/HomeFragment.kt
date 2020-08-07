package com.example.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.R
import com.example.data.TransformerType
import com.example.databinding.FragmentHomeBinding
import com.example.ui.base.BaseFragment
import com.example.util.Constants
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        autoBotViewAdapter = TransformersAdapter()
        decepticonViewAdapter = TransformersAdapter()

        initListViews(viewBinding.autobotList, autoBotViewAdapter)
        initListViews(viewBinding.decepticonList, decepticonViewAdapter)

        homeViewModel.getTransformersRequest()

        return viewBinding.root
    }

    private fun initListViews(recyclerView: RecyclerView, transformersAdapter: TransformersAdapter) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transformersAdapter
        }
    }

    override fun onStart() {
        super.onStart()

        homeViewModel.transformers.observe(
            viewLifecycleOwner,
            Observer { transformersResponse ->
                transformersResponse?.let { transformers ->
                    autoBotViewAdapter.addHeaderAndSubmitList(transformers.filter { it.team == "A" })
                    decepticonViewAdapter.addHeaderAndSubmitList(transformers.filter { it.team == "D" })
                }
            }
        )
        subscribeToContextEvents()
    }


    private fun subscribeToContextEvents() {

        // click events
        homeViewModel.contextEventBus.subscribe { contextEvent ->
            context?.let {
                when (contextEvent) {
                    HomeViewModel.ContextEvent.NAVIGATE_TO_TRANSFORMER_FRAGMENT_AUTOBOT -> navigateToTransformerFragment(
                        TransformerType.AUTOBOT)
                    HomeViewModel.ContextEvent.NAVIGATE_TO_TRANSFORMER_FRAGMENT_DECEPTICON -> navigateToTransformerFragment(TransformerType.DECEPTICON)
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
    }

    private fun navigateToTransformerFragment(transformerType: TransformerType) {
        findNavController().navigate(
            R.id.action_homeFragment_to_transformerFragment,
            bundleOf(Constants.BUNDLE_TRANSFORMER_TYPE to transformerType.name)
        )
    }
}
