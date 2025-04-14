package com.example.diaryapp.ui.moment

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.diaryapp.data.local.entities.Moment
import com.example.diaryapp.databinding.FragmentEditMomentBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditMomentFragment : Fragment() {

    private var _binding: FragmentEditMomentBinding? = null
    private val binding get() = _binding!!

    private val args: EditMomentFragmentArgs by navArgs()

    private val viewModel: MomentViewModel by viewModels {
        MomentViewModel.Factory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditMomentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val momentId = args.momentId
        if (momentId != 0) {
            viewModel.loadMoment(momentId.toLong()) // Преобразуем momentId в Long
        }

        viewModel.currentMoment.observe(viewLifecycleOwner) { moment ->
            if (moment != null) {
                binding.editTitle.setText(moment.title)
                binding.editNote.setText(moment.note)
                binding.editLocation.setText(moment.location)
                binding.editType.setText(moment.type)
                binding.editDescription.setText(moment.description)
                // Форматируем дату и отображаем в поле
                val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                binding.editDate.setText(moment.dateTime.format(dateTimeFormatter)) // Преобразуем LocalDateTime в строку
            }
        }

        binding.buttonSave.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val note = binding.editNote.text.toString()
            val location = binding.editLocation.text.toString()
            val type = binding.editType.text.toString()
            val description = binding.editDescription.text.toString()
            val date = binding.editDate.text.toString()

            if (title.isBlank() || note.isBlank() || description.isBlank() || date.isBlank()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Преобразуем строку даты в LocalDateTime
            val dateTime = LocalDateTime.now()

            val moment = Moment(
                id = momentId.toLong(),
                title = title,
                description = description,
                date = date, // Дата как строка
                note = note,
                location = location,
                type = type,
                dateTime = dateTime // Сохраняем LocalDateTime
            )

            if (momentId.toLong() == 0L) viewModel.insert(moment)
            else viewModel.update(moment)

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}