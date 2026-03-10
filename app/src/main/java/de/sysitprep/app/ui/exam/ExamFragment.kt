package de.sysitprep.app.ui.exam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.sysitprep.app.R
import de.sysitprep.app.data.model.ExamType
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.SessionType
import de.sysitprep.app.databinding.FragmentExamBinding

class ExamFragment : Fragment() {

    private var _binding: FragmentExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStartAp1.setOnClickListener {
            showStartConfirmation(
                title = getString(R.string.exam_ap1_title),
                message = getString(R.string.exam_confirm_message, "AP1", "90 Minuten"),
                examType = ExamType.AP1,
                fachrichtung = Fachrichtung.SI
            )
        }

        binding.btnStartAp2Si.setOnClickListener {
            showStartConfirmation(
                title = getString(R.string.exam_ap2_si_title),
                message = getString(R.string.exam_confirm_message, "AP2 Systemintegration", "90 Minuten"),
                examType = ExamType.AP2,
                fachrichtung = Fachrichtung.SI
            )
        }

        binding.btnStartAp2Ae.setOnClickListener {
            showStartConfirmation(
                title = getString(R.string.exam_ap2_ae_title),
                message = getString(R.string.exam_confirm_message, "AP2 Anwendungsentwicklung", "90 Minuten"),
                examType = ExamType.AP2,
                fachrichtung = Fachrichtung.AE
            )
        }
    }

    private fun showStartConfirmation(title: String, message: String, examType: ExamType, fachrichtung: Fachrichtung) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.exam_start_now)) { _, _ ->
                startExam(examType, fachrichtung)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun startExam(examType: ExamType, fachrichtung: Fachrichtung) {
        findNavController().navigate(
            R.id.quizFragment,
            Bundle().apply {
                putString("sessionType", SessionType.EXAM_SIMULATION.name)
                putInt("lernfeld", -1)
                putString("fachrichtung", fachrichtung.name)
                putString("examType", examType.name)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
