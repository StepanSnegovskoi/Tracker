package com.example.tracker.presentation.fragments

import android.content.ClipData.Item
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.databinding.FragmentHomeBinding
import com.example.tracker.domain.entities.Card
import com.example.tracker.presentation.App
import com.example.tracker.presentation.activities.MainActivity
import com.example.tracker.presentation.recyclerView.adapters.CardAdapter
import com.example.tracker.presentation.recyclerView.adapters.GroupAdapter
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentHomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FragmentHome : Fragment() {

    private val deleted = mutableListOf<Card>()

    private var binding: FragmentHomeBinding? = null

    private val args by navArgs<FragmentHomeArgs>()

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
        with(binding!!) {
            recyclerViewCards.layoutManager = LinearLayoutManager(activity)
            recyclerViewCards.adapter = adapter
        }

        binding!!.textViewGroupNameMain.text = args.groupName

        viewModel.listDeleted.observe(viewLifecycleOwner) {
            it.forEach {
                deleted.add(it)
            }
        }
/*
TODO ПОСЛЕДНИЙ ЭЛЕМЕНТ ПОЧЕМУ ТО ПРИ УДАЛЕНИИ СВАЙПОМ СТАНОВИТСЯ НЕВИДИМЫМ, ПОТОМ ВИДИМЫМ И ТОЛЬКО ПОТОМ СДВИИГАЕТСЯ, НУЖНО РЕШИТЬ ЭТУ ПРОБЛЕМУ
 */


        binding!!.textViewReturnDeletedCards.setOnClickListener {
            val job = lifecycleScope.launch {
                deleted.forEach {
                    viewModel.returnCard(it)
                }
            }
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    job.join()
                    viewModel.getCardsByName(args.groupName)
                    deleted.clear()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getCardsByName(args.groupName)
        }

        setupItemTouchHelper(adapter)

        super.onViewCreated(view, savedInstanceState)
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
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteCardById(card.id)
                    viewModel.getCardsByName(args.groupName)
                }
            }
        }).apply {
            attachToRecyclerView(binding!!.recyclerViewCards)
        }
    }
}
