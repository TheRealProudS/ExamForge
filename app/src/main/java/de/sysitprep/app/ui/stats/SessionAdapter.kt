package de.sysitprep.app.ui.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.sysitprep.app.R
import de.sysitprep.app.data.model.Session
import de.sysitprep.app.data.model.SessionType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionAdapter : ListAdapter<Session, SessionAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSessionType: TextView = view.findViewById(R.id.tv_session_type)
        val tvSessionDate: TextView = view.findViewById(R.id.tv_session_date)
        val tvSessionScore: TextView = view.findViewById(R.id.tv_session_score)
        val tvSessionDuration: TextView = view.findViewById(R.id.tv_session_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = getItem(position)

        holder.tvSessionType.text = when (session.sessionType) {
            SessionType.LERNFELD -> "LF ${session.lernfeld} Training"
            SessionType.EXAM_SIMULATION -> "Prüfungssimulation"
            SessionType.QUICK_QUIZ -> "Schnell-Quiz"
            SessionType.BOOKMARK_REVIEW -> "Lesezeichen"
        }

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY)
        holder.tvSessionDate.text = sdf.format(Date(session.startedAt))

        val total = session.totalQuestions
        val correct = session.correctAnswers
        if (total > 0) {
            val pct = correct * 100 / total
            holder.tvSessionScore.text = "$correct/$total ($pct%)"
        } else {
            holder.tvSessionScore.text = "–"
        }

        val dur = session.durationSeconds ?: 0
        val mins = dur / 60
        val secs = dur % 60
        holder.tvSessionDuration.text = if (mins > 0) "${mins}m ${secs}s" else "${secs}s"
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Session>() {
            override fun areItemsTheSame(old: Session, new: Session) = old.id == new.id
            override fun areContentsTheSame(old: Session, new: Session) = old == new
        }
    }
}
