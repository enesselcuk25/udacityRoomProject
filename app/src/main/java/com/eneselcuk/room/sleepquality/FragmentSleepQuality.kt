package com.eneselcuk.room.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.eneselcuk.room.R
import com.eneselcuk.room.databinding.FragmentSleepQualityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSleepQuality : Fragment() {

    val arguments: FragmentSleepQualityArgs by navArgs()

    private lateinit var binding: FragmentSleepQualityBinding
    private val viewModel: SleepQualityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_quality, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        setDefine()
    }

    fun setDefine() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            arguments.sleepData.let { key ->
                viewModel.key = key
            }

            viewModelQuality = viewModel

        }
    }

    private fun setObserver() {
        viewModel.navigateSleepTracker.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(FragmentSleepQualityDirections.actionFragmentSleepQualityToFragmentSleepTracker())
                viewModel.doneNavigating()
            }
        })
    }
}