package com.example.tracker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tracker.R
import com.example.tracker.databinding.FragmentAddCardBinding
import com.example.tracker.databinding.FragmentAddGroupBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.sealed.fragmentAddCard.AddCard
import com.example.tracker.presentation.sealed.fragmentAddCard.Error
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentAddCardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FragmentAddCard : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var viewModel: FragmentAddCardViewModel

    private var binding: FragmentAddCardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        FragmentAddCardBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            binding = this
            return this.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding!!) {
            buttonAddCard.setOnClickListener {
                val name = textInputEditTextCardName.text.toString()
                val description = textInputEditTextCardDescription.text.toString()
                val deadline = textInputEditTextCardDeadline.text.toString()
                val groupName = textInputEditTextCardGroupName.text.toString()
                lifecycleScope.launch {
                    viewModel.addCard(
                        name = name,
                        description = description,
                        deadline = deadline,
                        groupName = groupName,
                    )
                }
            }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    AddCard -> {
                        Toast.makeText(
                            activity,
                            "Карточка успешно добавлена",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Error -> {
                        Toast.makeText(
                            activity,
                            it.text,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            super.onViewCreated(view, savedInstanceState)
        }
    }
}