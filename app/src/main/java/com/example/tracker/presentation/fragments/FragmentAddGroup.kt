package com.example.tracker.presentation.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tracker.databinding.FragmentAddGroupBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.sealed.fragmentAddGroup.Error
import com.example.tracker.presentation.sealed.fragmentAddGroup.GroupName
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentAddGroupViewModel
import javax.inject.Inject


class FragmentAddGroup : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentAddGroupBinding

    @Inject
    lateinit var viewModel: FragmentAddGroupViewModel

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

        FragmentAddGroupBinding.inflate(
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

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is Error -> showToast(it.errorText)
                is GroupName -> {
                    showToast("Группа ${it.groupName} успешно добавлена")
                    reset()
                }
            }
            closeKeyboard()
        }
    }

    private fun setupButtonClickListener() {
        binding.buttonAddGroup.setOnClickListener {
            viewModel.addGroup(binding.textInputLayoutHint.text.toString())
        }
    }

    private fun reset() {
        binding.textInputLayoutHint.setText("")
    }

    private fun closeKeyboard(){
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
