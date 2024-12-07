package com.example.tracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tracker.databinding.FragmentAddCardBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.sealed.fragmentAddCard.AddCard
import com.example.tracker.presentation.sealed.fragmentAddCard.Error
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentAddCardViewModel
import javax.inject.Inject

class FragmentAddCard : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: FragmentAddCardViewModel

    private lateinit var binding: FragmentAddCardBinding

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

        FragmentAddCardBinding.inflate(
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

        setupButtonClickListener()

        observeViewModel()
    }

    private fun setupButtonClickListener() {
        with(binding) {
            buttonAddCard.setOnClickListener {
                val name = textInputEditTextCardName.text.toString()
                val description = textInputEditTextCardDescription.text.toString()
                val deadline = textInputEditTextCardDeadline.text.toString()
                val groupName = textInputEditTextCardGroupName.text.toString()
                viewModel.addCard(
                    name = name,
                    description = description,
                    deadline = deadline,
                    groupName = groupName,
                )
            }
        }
    }

    private fun observeViewModel(){
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is AddCard -> {
                    Toast.makeText(
                        activity,
                        "Карточка успешно добавлена",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Error -> {
                    Toast.makeText(
                        activity,
                        it.errorText,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}