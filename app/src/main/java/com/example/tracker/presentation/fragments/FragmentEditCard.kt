package com.example.tracker.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.tracker.R
import com.example.tracker.databinding.FragmentEditCardBinding
import com.example.tracker.domain.useCases.EditCardUseCase
import com.example.tracker.presentation.App
import com.example.tracker.presentation.sealed.fragmentEditCard.ShouldClose
import com.example.tracker.presentation.viewModels.FragmentEditCardViewModel
import javax.inject.Inject

class FragmentEditCard : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val args by navArgs<FragmentEditCardArgs>()

    private lateinit var binding: FragmentEditCardBinding

    @Inject
    lateinit var viewModel: FragmentEditCardViewModel

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

        FragmentEditCardBinding.inflate(
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

        observeViewModel()

        setupCardInfo()

        setupOnButtonClickListener()
    }

    private fun setupCardInfo() {
        with(args.card) {
            val name = name
            val description = description
            with(binding) {
                textInputEditTextCardName.setText(name)
                textInputEditTextCardDescription.setText(description)
                textInputEditTextCardDeadline.setText(deadline)
            }
        }
    }

    private fun setupOnButtonClickListener() {
        binding.buttonEditCard.setOnClickListener {
            with(args.card) {
                viewModel.editCard(
                    args.card.copy(
                        id = id,
                        name = binding.textInputEditTextCardName.text.toString(),
                        description = binding.textInputEditTextCardDescription.text.toString(),
                        deadline = binding.textInputEditTextCardDeadline.text.toString(),
                    )
                )
            }
        }
    }

    private fun observeViewModel(){
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is ShouldClose -> {
                    backPressed()
                }
            }
        }
    }

    private fun backPressed(){
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }
}