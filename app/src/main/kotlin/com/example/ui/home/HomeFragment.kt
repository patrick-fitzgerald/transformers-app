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
import com.example.ui.transformer.TransformerViewType
import com.example.util.Constants
import com.example.util.Constants.TEAM_AUTOBOT
import com.example.util.Constants.TEAM_DECEPTICON
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentHomeBinding>()
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var autoBotViewAdapter: TransformersAdapter
    private lateinit var decepticonViewAdapter: TransformersAdapter

    private val listViewClickListener = TransformerListener { transformerId ->
        navigateToTransformerFragmentView(transformerId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewBinding.viewModel = homeViewModel
        viewBinding.lifecycleOwner = this

        autoBotViewAdapter = TransformersAdapter(listViewClickListener)
        decepticonViewAdapter = TransformersAdapter(listViewClickListener)

        initListViews(viewBinding.autobotList, autoBotViewAdapter)
        initListViews(viewBinding.decepticonList, decepticonViewAdapter)

        return viewBinding.root
    }

    private fun initListViews(
        recyclerView: RecyclerView,
        transformersAdapter: TransformersAdapter
    ) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transformersAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.getTransformersRequest()

        homeViewModel.transformers.observe(
            viewLifecycleOwner,
            Observer { transformersResponse ->
                transformersResponse?.let { transformers ->
                    val autobots = transformers
                        .filter { it.team == TEAM_AUTOBOT }
                        .sortedBy { it.overallRating() }
                    val decepticons = transformers
                        .filter { it.team == TEAM_DECEPTICON }
                        .sortedBy { it.overallRating() }
                    autoBotViewAdapter.addHeaderAndSubmitList(autobots)
                    decepticonViewAdapter.addHeaderAndSubmitList(decepticons)
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
                    HomeViewModel.ContextEvent.NAVIGATE_TO_TRANSFORMER_FRAGMENT_AUTOBOT ->
                        navigateToTransformerFragmentCreate(
                            TransformerType.AUTOBOT
                        )
                    HomeViewModel.ContextEvent.NAVIGATE_TO_TRANSFORMER_FRAGMENT_DECEPTICON ->
                        navigateToTransformerFragmentCreate(
                            TransformerType.DECEPTICON
                        )
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
    }

    private fun navigateToTransformerFragmentCreate(
        transformerType: TransformerType
    ) {
        findNavController().navigate(
            R.id.action_homeFragment_to_transformerFragment,
            bundleOf(
                Constants.BUNDLE_TRANSFORMER_VIEW_TYPE to TransformerViewType.CREATE.name,
                Constants.BUNDLE_TRANSFORMER_TYPE to transformerType.name

            )
        )
    }

    private fun navigateToTransformerFragmentView(
        transformerId: String
    ) {
        findNavController().navigate(
            R.id.action_homeFragment_to_transformerFragment,
            bundleOf(
                Constants.BUNDLE_TRANSFORMER_VIEW_TYPE to TransformerViewType.VIEW.name,
                Constants.BUNDLE_TRANSFORMER_ID to transformerId
            )
        )
    }
}
