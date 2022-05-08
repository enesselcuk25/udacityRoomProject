package com.eneselcuk.room.sleepDetail

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
import com.eneselcuk.room.databinding.FragmentSleepDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SleepDetailFragment : Fragment() {

    private lateinit var binding: FragmentSleepDetailBinding
    private val viewModel: SleepDetailViewModel by activityViewModels()
    val arguments: SleepDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_detail,
                container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            sleepDetailViewModel = viewModel
        }
        setObserver()
    }


    private fun setObserver() {
        arguments.sleepData.let { key ->
            viewModel.sleepNightKey = key
        }
        viewModel.navigateToSleepTracker.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(SleepDetailFragmentDirections
                        .actionSleepDetailToFragmentSleepTracker())
            }
            viewModel.doneNavigating()
        })


    }


}