package com.example.ui.transformer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.findNavController
import com.example.R
import com.example.data.TransformerType
import com.example.databinding.FragmentTransformerBinding
import com.example.ui.base.BaseFragment
import com.example.util.autoCleared
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.transformer_seek_bar.view.*
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

        // Parse transformer type from bundle
        arguments?.let {
            val safeArgs = TransformerFragmentArgs.fromBundle(it)
            when (safeArgs.transformerType) {
                TransformerType.AUTOBOT.name -> {
                    transformerViewModel.transformerTeam = "A"
                    viewBinding.title.text = getString(R.string.new_autobot)
                }
                TransformerType.DECEPTICON.name -> {
                    transformerViewModel.transformerTeam = "D"
                    viewBinding.title.text = getString(R.string.new_decepticon)
                }
            }
        }

        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        subscribeToContextEvents()
        initSeekBarChangeListeners()
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
