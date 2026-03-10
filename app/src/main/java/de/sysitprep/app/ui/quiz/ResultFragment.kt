package de.sysitprep.app.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import de.sysitprep.app.R
import de.sysitprep.app.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionId = arguments?.getLong("sessionId") ?: -1L
        viewModel.loadResult(sessionId)
        observeViewModel()
        setupClickListeners(sessionId)
    }

    private fun observeViewModel() {
        viewModel.quizResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe

            binding.tvScorePercent.text = "${result.scorePercent}%"

            if (result.passed) {
                binding.tvResultStatus.text = getString(R.string.result_passed)
                binding.tvResultStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_correct))
                binding.tvScorePercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_correct))
            } else {
                binding.tvResultStatus.text = getString(R.string.result_failed)
                binding.tvResultStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_wrong))
                binding.tvScorePercent.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_wrong))
            }

            binding.tvCorrectCount.text = result.correctAnswers.toString()
            binding.tvWrongCount.text = result.wrongAnswers.toString()

            val mins = result.timeSeconds / 60
            val secs = result.timeSeconds % 60
            binding.tvTimeTaken.text = "${mins}m ${secs}s"
        }
    }

    private fun setupClickListeners(sessionId: Long) {
        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.btnRetry.setOnClickListener {
            val args = requireArguments()
            val quizArgs = Bundle().apply {
                putString("sessionType", args.getString("sessionType", "QUICK_QUIZ"))
                putInt("lernfeld", args.getInt("lernfeld", -1))
                putString("fachrichtung", args.getString("fachrichtung", "SI"))
                putString("examType", args.getString("examType", "AP1"))
            }
            findNavController().popBackStack(R.id.quizFragment, true)
            findNavController().navigate(R.id.quizFragment, quizArgs)
        }

        binding.btnReviewAnswers.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
