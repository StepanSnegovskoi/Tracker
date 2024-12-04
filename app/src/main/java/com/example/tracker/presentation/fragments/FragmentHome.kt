package com.example.tracker.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.databinding.FragmentHomeBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.recyclerView.adapters.CardAdapter
import com.example.tracker.presentation.sealed.fragmentHome.LoadCards
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentHomeViewModel
import javax.inject.Inject


class FragmentHome : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val args by navArgs<FragmentHomeArgs>()

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: FragmentHomeViewModel

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

        FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        ).let {
            binding = it
            return it.root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CardAdapter()

        observeViewModel(adapter)

        setupRecyclerView(adapter)

        bindCardGroupName()

        setupReturningDeletedCardsClickListener()

        loadGroups()

        setupItemTouchHelper(adapter)

        setupOnCardLongClickListener(adapter)
    }

    private fun setupItemTouchHelper(adapter: CardAdapter) {
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
                val card = adapter.currentList[position]
                viewModel.deleteCardById(card.id)
            }
        }).apply {
            attachToRecyclerView(binding.recyclerViewCards)
        }
    }

    private fun observeViewModel(adapter: CardAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is LoadCards -> {
                    adapter.submitList(it.cards.toList())
                }
            }
        }
    }

    private fun setupRecyclerView(adapter: CardAdapter) {
        with(binding) {
            recyclerViewCards.layoutManager = LinearLayoutManager(activity)
            recyclerViewCards.adapter = adapter
        }
    }

    private fun loadGroups() {
        viewModel.loadCards(args.groupName)
    }

    private fun bindCardGroupName() {
        binding.textViewGroupNameMain.text = args.groupName
    }

    private fun setupReturningDeletedCardsClickListener() {
        binding.textViewReturnDeletedCards.setOnClickListener {
            viewModel.returnCards(args.groupName)
        }
    }

    private fun setupOnCardLongClickListener(adapter: CardAdapter) {
        adapter.onCardLongClickListener = {
            findNavController().navigate(
                FragmentHomeDirections.actionFragmentHomeToFragmentEditCard(
                    it
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
