package de.sysitprep.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import de.sysitprep.app.ExamForgeApp
import de.sysitprep.app.R
import de.sysitprep.app.data.model.ExamType
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.SessionType
import de.sysitprep.app.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGreeting()
        setupClickListeners()
        observeViewModel()
        updateFachrichtungChip()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshStats()
        updateFachrichtungChip()
    }

    private fun updateFachrichtungChip() {
        lifecycleScope.launch {
            val prefs = (requireActivity().application as ExamForgeApp).repository.getPreferences().first()
            binding.chipFachrichtung.text = prefs.fachrichtung.displayName
        }
    }

    private fun setupGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.tvGreeting.text = when {
            hour < 12 -> getString(R.string.home_greeting_morning)
            hour < 17 -> getString(R.string.home_greeting_afternoon)
            else -> getString(R.string.home_greeting_evening)
        }
    }

    private fun setupClickListeners() {
        binding.cardQuickQuiz.setOnClickListener {
            findNavController().navigate(
                R.id.quizFragment,
                Bundle().apply {
                    putString("sessionType", SessionType.QUICK_QUIZ.name)
                    putString("fachrichtung", Fachrichtung.SI.name)
                    putString("examType", ExamType.AP1.name)
                    putInt("lernfeld", -1)
                }
            )
        }

        binding.cardExamSimulation.setOnClickListener {
            findNavController().navigate(R.id.examFragment)
        }

        binding.cardBookmarks.setOnClickListener {
            findNavController().navigate(
                R.id.quizFragment,
                Bundle().apply {
                    putString("sessionType", SessionType.BOOKMARK_REVIEW.name)
                    putString("fachrichtung", Fachrichtung.SI.name)
                    putString("examType", ExamType.AP1.name)
                    putInt("lernfeld", -1)
                }
            )
        }
    }

    private fun observeViewModel() {
        viewModel.stats.observe(viewLifecycleOwner) { stats ->
            binding.tvTotalAnswered.text = stats.totalAnswered.toString()
            binding.tvAccuracy.text = "${stats.accuracyPercent}%"
            binding.tvStreakValue.text = "🔥 ${stats.streakDays}"
        }

        viewModel.bookmarkCount.observe(viewLifecycleOwner) { count ->
            binding.tvBookmarksCount.text = "$count Fragen"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
