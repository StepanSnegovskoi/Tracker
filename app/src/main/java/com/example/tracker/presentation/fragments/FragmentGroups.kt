package com.example.tracker.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.databinding.FragmentGroupsBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.recyclerView.adapters.CardAdapter
import com.example.tracker.presentation.recyclerView.adapters.GroupAdapter
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentGroupsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentGroups : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    lateinit var binding: FragmentGroupsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: FragmentGroupsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

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
        super.onViewCreated(view, savedInstanceState)
        val adapter = GroupAdapter()
        viewModel.listGroups.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        with(binding){
            recyclerViewGroups.layoutManager = LinearLayoutManager(activity)
            recyclerViewGroups.adapter = adapter
        }

        adapter.onGroupClick = {
            findNavController().navigate(FragmentGroupsDirections.actionFragmentGroupsToFragmentHome(it))
        }

        adapter.onImageDeleteClick = {
            Toast.makeText(
                activity,
                "За удалением группы последует удаление всех карточек с ней связанных, вы уверены?",
                Toast.LENGTH_SHORT
            ).show()
        }

        setupItemTouchHelper(adapter)

    }

    private fun setupItemTouchHelper(adapter: GroupAdapter){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val group = adapter.currentList[position]
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteGroup(group.name)
                    viewModel.loadGroups()
                }
            }
        }).apply {
            attachToRecyclerView(binding.recyclerViewGroups)
        }
    }
}
