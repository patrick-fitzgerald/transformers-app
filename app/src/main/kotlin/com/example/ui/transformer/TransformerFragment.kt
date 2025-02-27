package com.example.ui.transformer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.data.TransformerType
import com.example.databinding.FragmentTransformerBinding
import com.example.ui.base.BaseFragment
import com.example.util.Constants.TEAM_AUTOBOT
import com.example.util.Constants.TEAM_DECEPTICON
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.transformer_seek_bar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

enum class TransformerViewType {
    CREATE,
    VIEW
}

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

        parseSafeArgs()

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        subscribeToContextEvents()
        initSeekBarChangeListeners()
    }

    // parse bundle arguments
    private fun parseSafeArgs() {
        arguments?.let {
            val safeArgs = TransformerFragmentArgs.fromBundle(it)

            // Parse transformer view type
            when (safeArgs.transformerViewType) {
                TransformerViewType.CREATE.name -> {
                    transformerViewModel.transformerViewType = TransformerViewType.CREATE
                    parseTransformerType(safeArgs)
                }
                TransformerViewType.VIEW.name -> {
                    transformerViewModel.transformerViewType = TransformerViewType.VIEW
                    parseTransformerId(safeArgs)
                }
            }
        }
    }

    // Parse transformer type
    private fun parseTransformerType(safeArgs: TransformerFragmentArgs) {

        when (safeArgs.transformerType) {
            TransformerType.AUTOBOT.name -> {
                transformerViewModel.transformerTeam = TEAM_AUTOBOT
                transformerViewModel.viewTitle.value = getString(R.string.new_autobot)
            }
            TransformerType.DECEPTICON.name -> {
                transformerViewModel.transformerTeam = TEAM_DECEPTICON
                transformerViewModel.viewTitle.value = getString(R.string.new_decepticon)
            }
        }
    }

    // Parse transformer id
    private fun parseTransformerId(safeArgs: TransformerFragmentArgs) {
        transformerViewModel.getTransformerRequest(safeArgs.transformerId)
    }

    private fun initSeekBarChangeListeners() {

        // Maps each seek bar to a mutable live data value
        val seekBarViewsMap = mapOf(
            viewBinding.seekBarStrength to transformerViewModel.transformerStrength,
            viewBinding.seekBarIntelligence to transformerViewModel.transformerIntelligence,
            viewBinding.seekBarSpeed to transformerViewModel.transformerSpeed,
            viewBinding.seekBarEndurance to transformerViewModel.transformerEndurance,
            viewBinding.seekBarRank to transformerViewModel.transformerRank,
            viewBinding.seekBarCourage to transformerViewModel.transformerCourage,
            viewBinding.seekBarFirepower to transformerViewModel.transformerFirepower,
            viewBinding.seekBarSkill to transformerViewModel.transformerSkill
        )

        for ((transformerSeekBar, mutableLiveData) in seekBarViewsMap) {

            // SeekBar change listener
            transformerSeekBar.seekBar?.setOnSeekBarChangeListener(object :
                    SeekBar.OnSeekBarChangeListener {

                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        // Update mutableLiveData when seekbar progress is updated
                        mutableLiveData.value = progress
                        transformerSeekBar.seekBarLabel?.text =
                            "${transformerSeekBar.seekBarLabelText}: $progress"
                        if (progress < 1) {
                            // SeekBar Attribute min is only used in API level 26 and higher
                            // Min value set to 1 for API less than 26
                            seekBar.progress = 1
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    }
                })

            // Update seekbar progress when mutableLiveData is updated
            mutableLiveData.observe(
                viewLifecycleOwner,
                Observer { value ->
                    transformerSeekBar.seekBar?.progress = value
                }
            )
        }
    }

    private fun subscribeToContextEvents() {
        // navigation events
        transformerViewModel.contextEventBus.subscribe { contextEvent ->
            context?.let {
                when (contextEvent) {
                    TransformerViewModel.ContextEvent.NAVIGATE_TO_HOME_FRAGMENT -> navigateToHomeFragment()
                    else -> Unit
                }
            }
        }.addTo(compositeDisposable)
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            R.id.action_transformerFragment_to_homeFragment
        )
    }
}
