package com.example.tracker.presentation.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tracker.databinding.FragmentAddCardBinding
import com.example.tracker.presentation.App
import com.example.tracker.presentation.alarmReceiver.AlarmReceiver
import com.example.tracker.presentation.sealed.fragmentAddCard.AddCard
import com.example.tracker.presentation.sealed.fragmentAddCard.Error
import com.example.tracker.presentation.viewModelFactories.ViewModelFactory
import com.example.tracker.presentation.viewModels.FragmentAddCardViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.util.Calendar
import java.util.Locale
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

    private val timePicker by lazy {
        MaterialTimePicker.Builder()
            .setTitleText("Select date")
            .setTimeFormat(CLOCK_24H)
            .setHour(12)
            .setMinute(30)
            .build()
    }

    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
    }

    private val alarmManager by lazy {
        requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
    }

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

        setupDatePicker()
        setupTimePicker()
    }

    private fun setupButtonClickListener() {
        with(binding) {
            buttonAddCard.setOnClickListener {
                val name = textInputEditTextCardName.text.toString()
                val description = textInputEditTextCardDescription.text.toString()
                val deadline =
                    "${textInputEditTextDate.text.toString()} ${textInputEditTextTime.text.toString()}"
                val groupName = textInputEditTextCardGroupName.text.toString()
                viewModel.addCard(
                    name = name,
                    description = description,
                    deadline = deadline,
                    groupName = groupName,
                )
                closeKeyboard()

                sendNotification()
            }
        }
    }

    private fun sendNotification(){
        val selectedDateMillis = datePicker.selection ?: 0L
        val calendar = Calendar.getInstance().apply {
            timeInMillis = selectedDateMillis
            set(Calendar.HOUR_OF_DAY, timePicker.hour)
            set(Calendar.MINUTE, timePicker.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val intent = AlarmReceiver.newIntent(requireActivity())
        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 100, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is AddCard -> {
                    Toast.makeText(
                        activity,
                        "Карточка успешно добавлена",
                        Toast.LENGTH_SHORT
                    ).show()
                    reset()
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

    private fun reset() {
        with(binding) {
            textInputEditTextCardName.setText("")
            textInputEditTextCardDescription.setText("")
            textInputEditTextCardGroupName.setText("")
            textInputEditTextDate.setText("")
            textInputEditTextTime.setText("")
        }
    }

    private fun closeKeyboard() {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupTimePicker() {
        binding.buttonSetTime.setOnClickListener {
            timePicker.show(requireActivity().supportFragmentManager, "tag")
        }

        timePicker.addOnPositiveButtonClickListener {
            binding.textInputEditTextTime.setText(getStringTime())
        }
    }

    private fun setupDatePicker() {
        datePicker.addOnPositiveButtonClickListener {
            Calendar.getInstance().apply {
                timeInMillis = datePicker.selection as Long
            }.let {
                val day = it.get(Calendar.DAY_OF_MONTH)
                val month = it.get(Calendar.MONTH) + 1
                val year = it.get(Calendar.YEAR)
                binding.textInputEditTextDate.setText(getStringDate(day, month, year))
            }
        }

        binding.buttonSetDate.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, "tag");
        }
    }

    private fun getStringDate(day: Int, month: Int, year: Int): String {
        return String.format(
            Locale.ENGLISH,
            "%02d-%02d-%04d",
            day,
            month,
            year
        )
    }

    private fun getStringTime(): String {
        return String.format(
            Locale.ENGLISH,
            "%02d:%02d",
            timePicker.hour,
            timePicker.minute
        )
    }
}