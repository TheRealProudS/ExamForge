package de.sysitprep.app.ui.quiz

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.sysitprep.app.R
import de.sysitprep.app.data.model.ExamType
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.Question
import de.sysitprep.app.data.model.SessionType
import de.sysitprep.app.databinding.FragmentQuizBinding
import java.util.Locale

open class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by viewModels()

    private var isExamMode = false
    private val optionButtons = mutableListOf<MaterialButton>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionTypeStr = arguments?.getString("sessionType") ?: SessionType.QUICK_QUIZ.name
        val sessionType = SessionType.valueOf(sessionTypeStr)
        val lernfeld = arguments?.getInt("lernfeld") ?: -1
        val fachrichtungStr = arguments?.getString("fachrichtung") ?: Fachrichtung.SI.name
        val fachrichtung = Fachrichtung.valueOf(fachrichtungStr)
        val examTypeStr = arguments?.getString("examType") ?: ExamType.AP1.name
        val examType = ExamType.valueOf(examTypeStr)

        isExamMode = sessionType == SessionType.EXAM_SIMULATION

        if (isExamMode) {
            binding.tvTimer.visibility = View.VISIBLE
        }

        setupClickListeners()
        observeViewModel()
        handleBackPress()

        viewModel.init(sessionType, lernfeld, fachrichtung, examType, isExamMode)
    }

    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener { confirmExit() }
        binding.btnBookmark.setOnClickListener { viewModel.toggleBookmark() }
        binding.btnSkip.setOnClickListener { viewModel.skipQuestion() }
        binding.btnCheckNext.setOnClickListener {
            if (viewModel.isAnswerChecked.value == true) {
                viewModel.nextQuestion()
            } else {
                if (optionButtons.any { it.isSelected || it.tag == "selected" }) {
                    viewModel.checkAnswer()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            question?.let { renderQuestion(it) }
        }

        viewModel.questions.observe(viewLifecycleOwner) { questions ->
            if (questions.isNotEmpty()) {
                val total = questions.size
                val current = (viewModel.currentIndex.value ?: 0) + 1
                binding.tvQuestionCounter.text = "Frage $current von $total"
                binding.quizProgressBar.progress = (current * 100 / total)
            }
        }

        viewModel.currentIndex.observe(viewLifecycleOwner) { index ->
            val total = viewModel.questions.value?.size ?: 0
            binding.tvQuestionCounter.text = "Frage ${index + 1} von $total"
            binding.quizProgressBar.progress = if (total == 0) 0 else ((index + 1) * 100 / total)
        }

        viewModel.isAnswerChecked.observe(viewLifecycleOwner) { checked ->
            if (checked) {
                binding.cardExplanation.visibility = View.VISIBLE
                if (!isExamMode) {
                    binding.cardExplanation.visibility = View.VISIBLE
                }
                binding.btnCheckNext.text = getString(R.string.quiz_next)
                binding.btnCheckNext.setIconResource(R.drawable.ic_arrow_right)
                applyAnswerColors()
            } else {
                binding.cardExplanation.visibility = View.GONE
                binding.btnCheckNext.text = getString(R.string.quiz_check_answer)
                binding.btnCheckNext.setIconResource(R.drawable.ic_check)
            }
        }

        viewModel.selectedIndices.observe(viewLifecycleOwner) { selected ->
            if (viewModel.isAnswerChecked.value != true) {
                optionButtons.forEachIndexed { idx, btn ->
                    val isSelected = idx in selected
                    btn.tag = if (isSelected) "selected" else null
                    btn.isSelected = isSelected
                    if (isSelected) {
                        btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.surface_overlay))
                        btn.setStrokeColorResource(R.color.brand_primary)
                        btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.brand_primary))
                    } else {
                        btn.setBackgroundColor(Color.TRANSPARENT)
                        btn.setStrokeColorResource(R.color.border_light)
                        btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                    }
                }
                binding.btnCheckNext.isEnabled = selected.isNotEmpty()
            }
        }

        viewModel.quizFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                navigateToResult()
            }
        }

        viewModel.timerSeconds.observe(viewLifecycleOwner) { seconds ->
            if (isExamMode) {
                val mins = seconds / 60
                val secs = seconds % 60
                binding.tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
                if (seconds <= 600) { // Last 10 minutes
                    binding.tvTimer.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_wrong))
                }
            }
        }
    }

    private fun renderQuestion(question: Question) {
        binding.tvQuestionText.text = question.questionText
        binding.tvExplanation.text = question.explanation
        binding.tvLernfeldBadge.text = "LF ${question.lernfeld}"
        binding.tvDifficultyBadge.text = when (question.difficulty) {
            1 -> "Leicht"
            2 -> "Mittel"
            else -> "Schwer"
        }
        binding.tvDifficultyBadge.setTextColor(
            when (question.difficulty) {
                1 -> ContextCompat.getColor(requireContext(), R.color.difficulty_easy)
                2 -> ContextCompat.getColor(requireContext(), R.color.difficulty_medium)
                else -> ContextCompat.getColor(requireContext(), R.color.difficulty_hard)
            }
        )

        if (question.correctAnswerIndices.size > 1) {
            binding.tvMultipleChoiceHint.visibility = View.VISIBLE
        } else {
            binding.tvMultipleChoiceHint.visibility = View.GONE
        }

        binding.answerOptionsContainer.removeAllViews()
        optionButtons.clear()
        binding.btnCheckNext.isEnabled = false

        val optionLabels = listOf("A", "B", "C", "D", "E")
        val spacing = resources.getDimensionPixelSize(R.dimen.option_spacing)
        val radiusPx = resources.getDimensionPixelSize(R.dimen.option_corner_radius)
        val padH = resources.getDimensionPixelSize(R.dimen.option_padding_h)
        val padV = resources.getDimensionPixelSize(R.dimen.option_padding_v)

        question.options.forEachIndexed { index, optionText ->
            val label = optionLabels.getOrElse(index) { index.toString() }
            val btn = MaterialButton(requireContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
                text = "  $label    $optionText"
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                textSize = 15f
                setPadding(padH, padV, padH, padV)
                cornerRadius = radiusPx
                strokeWidth = 2
                setStrokeColorResource(R.color.border_light)
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, spacing) }
                setOnClickListener { viewModel.toggleOption(index) }
            }
            optionButtons.add(btn)
            binding.answerOptionsContainer.addView(btn)
        }

        binding.cardExplanation.visibility = View.GONE
        binding.btnCheckNext.text = getString(R.string.quiz_check_answer)
    }

    private fun applyAnswerColors() {
        val question = viewModel.currentQuestion.value ?: return
        val correctIndices = question.correctAnswerIndices.toSet()
        val selectedIndices = viewModel.selectedIndices.value ?: emptySet()

        optionButtons.forEachIndexed { index, btn ->
            btn.isEnabled = false
            when {
                index in correctIndices && index in selectedIndices -> {
                    btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_correct_light))
                    btn.setStrokeColorResource(R.color.color_correct)
                    btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_correct))
                }
                index in correctIndices -> {
                    btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_correct_light))
                    btn.setStrokeColorResource(R.color.color_correct)
                    btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_correct))
                }
                index in selectedIndices -> {
                    btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_wrong_light))
                    btn.setStrokeColorResource(R.color.color_wrong)
                    btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_wrong))
                }
            }
        }
    }

    private fun navigateToResult() {
        val sessionId = viewModel.sessionId.value ?: -1L
        val args = arguments
        findNavController().navigate(
            R.id.resultFragment,
            Bundle().apply {
                putLong("sessionId", sessionId)
                putString("sessionType", args?.getString("sessionType") ?: "QUICK_QUIZ")
                putInt("lernfeld", args?.getInt("lernfeld") ?: -1)
                putString("fachrichtung", args?.getString("fachrichtung") ?: "SI")
                putString("examType", args?.getString("examType") ?: "AP1")
            }
        )
    }

    private fun confirmExit() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Quiz verlassen?")
            .setMessage("Dein Fortschritt in dieser Sitzung geht verloren.")
            .setPositiveButton("Verlassen") { _, _ -> findNavController().popBackStack() }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun handleBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            confirmExit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
