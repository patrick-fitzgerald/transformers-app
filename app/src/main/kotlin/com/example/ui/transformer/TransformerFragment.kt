package com.example.ui.transformer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.databinding.FragmentTransformerBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransformerFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentTransformerBinding>()
    private val transformerViewModel: TransformerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentTransformerBinding.inflate(inflater, container, false)
        viewBinding.viewModel = transformerViewModel
        viewBinding.lifecycleOwner = this
        return viewBinding.root
    }

    override fun onStart() {
        super.onStart()
        subscribeToContextEvents()
    }

    private fun subscribeToContextEvents() {
        // navigation events
        transformerViewModel.contextEventBus.subscribe { contextEvent ->
            context?.let {
                when (contextEvent) {
                    TransformerViewModel.ContextEvent.NAVIGATE_TO_HOME_FRAGMENT -> Unit
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
    }

}
