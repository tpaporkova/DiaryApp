package com.example.diaryapp.ui.moment

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.diaryapp.data.local.entities.Moment
import com.example.diaryapp.databinding.FragmentEditMomentBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class EditMomentFragment : Fragment() {

    private var _binding: FragmentEditMomentBinding? = null
    private val binding get() = _binding!!

    private val args: EditMomentFragmentArgs by navArgs()

    private val viewModel: MomentViewModel by viewModels {
        MomentViewModel.Factory(requireActivity().application)
    }

    private val eventTypes = listOf("Свидание", "Кино", "Театр", "Путешествие", "Сюрприз", "Цветы")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditMomentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка Spinner для выбора типа события
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editType.adapter = adapter

        // Преобразование momentId в Long
        val momentId = args.momentId.toLong()

        if (momentId != 0L) {
            viewModel.loadMoment(momentId)
        }

        viewModel.currentMoment.observe(viewLifecycleOwner) { moment ->
            moment?.let {
                binding.editNote.setText(moment.note)
                binding.editLocation.setText(moment.location)
                binding.editType.setSelection(eventTypes.indexOf(moment.type))
                binding.editDate.setText(moment.date)
            }
        }

        // Открытие календаря при клике на поле с датой
        binding.editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    binding.editDate.setText(selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                },
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
        }

        // Обработка нажатия кнопки сохранения
        binding.buttonSave.setOnClickListener {
            val note = binding.editNote.text.toString()
            val location = binding.editLocation.text.toString()
            val type = binding.editType.selectedItem.toString()
            val date = binding.editDate.text.toString()

            if (note.isBlank() || location.isBlank() || type.isBlank() || date.isBlank()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val moment = Moment(
                id = momentId, // Теперь используем momentId как Long
                note = note,
                location = location,
                type = type,
                date = date // Дату сохраняем как строку
            )

            // Вставка или обновление момента с обработкой ошибок
            if (momentId == 0L) {
                try {
                    viewModel.insert(moment)
                } catch (e: Exception) {
                    Log.e("EditMomentFragment", "Error inserting moment", e)
                    Toast.makeText(requireContext(), "Error saving moment", Toast.LENGTH_SHORT).show()
                }
            } else {
                try {
                    viewModel.update(moment)
                } catch (e: Exception) {
                    Log.e("EditMomentFragment", "Error updating moment", e)
                    Toast.makeText(requireContext(), "Error updating moment", Toast.LENGTH_SHORT).show()
                }
            }

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
