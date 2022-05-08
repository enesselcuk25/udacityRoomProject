package com.eneselcuk.room.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.eneselcuk.room.R
import com.eneselcuk.room.databinding.FragmentSleepTrackerBinding
import com.eneselcuk.room.sleeptracker.adapter.SleepNightAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentSleepTracker : Fragment() {

    private val viewModel: SleepTrackerViewModel by activityViewModels()
    private lateinit var adapter: SleepNightAdapter

    private lateinit var binding: FragmentSleepTrackerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_tracker,
                container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            sleppTrackerViewModel = viewModel

        }
        setObserver()
    }

    private fun setObserver() {
        /*
            navigate eder ve night id diğer fragmente götürür
            gittikten sonra doneNavigating() oluyor yani viewModeli null yapıyoruz.
         */
        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner, { night ->
            night?.let {
                this.findNavController()
                    .navigate(FragmentSleepTrackerDirections
                        .actionFragmentSleepTrackerToFragmentSleepQuality(night.nightId))
            }
        })
        // viewModeli null yapıp tamamlıyor
        viewModel.doneNavigating()

        // snackbar silme işlemini yapıyor
        viewModel.showSnacbarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT).show()
            }
            // snackbarı kapatıyor yani viewModeli kapatıyor
            viewModel.doneShowingSnackbar()
        })

        // Rcyclerview observer
        viewModel.nights.observe(viewLifecycleOwner, { listData ->
            adapter.addHeaderAndSubmitList(listData)
//            adapter.submitList(listData)
        })

        adapter = SleepNightAdapter(SleepNightAdapter.SleepNightListener { sleepId ->
            viewModel.onSleepNightClicked(sleepId)
        })

        val manager = GridLayoutManager(requireContext(), 3)
        binding.sleepList.layoutManager = manager
        binding.sleepList.adapter = adapter


        // navigation işlemleri bu step de navigation yapacağımız yere datayı paslıyoruz.
        viewModel.navigateToSleepDataQuality.observe(viewLifecycleOwner, { data ->
            data?.let {
                this.findNavController().navigate(FragmentSleepTrackerDirections
                    .actionFragmentSleepTrackerToSleepDetail(data))

            }
            viewModel.onSleepDataQualityNavigated()
        })

    }
}