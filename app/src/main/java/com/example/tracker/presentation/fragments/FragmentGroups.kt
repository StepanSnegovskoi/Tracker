package com.example.tracker.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tracker.R
import com.example.tracker.databinding.FragmentGroupsBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.recyclerView.adapters.GroupAdapter
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentGroupsViewModel
import javax.inject.Inject

class FragmentGroups : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private var binding: FragmentGroupsBinding? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: FragmentGroupsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        FragmentGroupsBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            binding = this
            return this.root
        }
    }

    // OnCreateView -> binding = this
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = GroupAdapter()
        viewModel.listGroups.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        with(binding!!){
            recyclerViewGroups.layoutManager = LinearLayoutManager(activity)
            recyclerViewGroups.adapter = adapter
        }

        adapter.onGroupClick = {
            findNavController().navigate(FragmentGroupsDirections.actionFragmentGroupsToFragmentHome(it))
        }
        
        super.onViewCreated(view, savedInstanceState)
    }
}
