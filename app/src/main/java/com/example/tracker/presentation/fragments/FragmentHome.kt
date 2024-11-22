package com.example.tracker.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tracker.R
import com.example.tracker.databinding.FragmentHomeBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.recyclerView.adapters.CardAdapter
import com.example.tracker.presentation.recyclerView.adapters.GroupAdapter
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentHomeViewModel
import javax.inject.Inject


class FragmentHome : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: FragmentHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            binding = this
            return this.root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CardAdapter()
        viewModel.listCards.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        with(binding!!){
            recyclerViewCards.layoutManager = LinearLayoutManager(activity)
            recyclerViewCards.adapter = adapter
        }
        super.onViewCreated(view, savedInstanceState)
    }
}
