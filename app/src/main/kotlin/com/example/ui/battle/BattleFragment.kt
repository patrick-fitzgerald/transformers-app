package com.example.ui.battle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.databinding.FragmentBattleBinding
import com.example.ui.base.BaseFragment
import com.example.ui.battle.BattleViewModel
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import org.koin.androidx.viewmodel.ext.android.viewModel

class BattleFragment : BaseFragment() {

    private var viewBinding by autoCleared<FragmentBattleBinding>()
    private val battleViewModel: BattleViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentBattleBinding.inflate(inflater, container, false)
        viewBinding.viewModel = battleViewModel
        viewBinding.lifecycleOwner = this
        return viewBinding.root
    }

    override fun onStart() {
        super.onStart()
        subscribeToContextEvents()
    }

    private fun subscribeToContextEvents() {
        // navigation events
        battleViewModel.contextEventBus.subscribe { contextEvent ->
            context?.let {
                when (contextEvent) {
                    BattleViewModel.ContextEvent.NAVIGATE_TO_HOME_FRAGMENT -> Unit //navigateToHomeFragment()
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
    }

//    private fun navigateToHomeFragment() {
//        findNavController().navigate(
//            R.id.action_battleFragment_to_homeFragment,
//            null,
//            NavOptions.Builder().setPopUpTo(R.id.battleFragment, true).build()
//        )
//    }
}