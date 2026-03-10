package de.sysitprep.app.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import de.sysitprep.app.data.model.ExamType;
import de.sysitprep.app.data.model.Fachrichtung;
import de.sysitprep.app.data.model.Session;
import de.sysitprep.app.data.model.SessionType;
import de.sysitprep.app.data.model.UserAnswer;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SessionDao_Impl implements SessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Session> __insertionAdapterOfSession;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<UserAnswer> __insertionAdapterOfUserAnswer;

  private final EntityDeletionOrUpdateAdapter<Session> __deletionAdapterOfSession;

  private final EntityDeletionOrUpdateAdapter<Session> __updateAdapterOfSession;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSessions;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllAnswers;

  public SessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSession = new EntityInsertionAdapter<Session>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sessions` (`id`,`sessionType`,`fachrichtung`,`examType`,`lernfeld`,`totalQuestions`,`correctAnswers`,`durationSeconds`,`completedAt`,`startedAt`,`questionIds`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Session entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromSessionType(entity.getSessionType());
        statement.bindString(2, _tmp);
        final String _tmp_1 = __converters.fromFachrichtung(entity.getFachrichtung());
        statement.bindString(3, _tmp_1);
        final String _tmp_2 = __converters.fromExamType(entity.getExamType());
        if (_tmp_2 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_2);
        }
        if (entity.getLernfeld() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getLernfeld());
        }
        statement.bindLong(6, entity.getTotalQuestions());
        statement.bindLong(7, entity.getCorrectAnswers());
        statement.bindLong(8, entity.getDurationSeconds());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getCompletedAt());
        }
        statement.bindLong(10, entity.getStartedAt());
        final String _tmp_3 = __converters.fromLongList(entity.getQuestionIds());
        statement.bindString(11, _tmp_3);
      }
    };
    this.__insertionAdapterOfUserAnswer = new EntityInsertionAdapter<UserAnswer>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `user_answers` (`id`,`sessionId`,`questionId`,`selectedIndices`,`isCorrect`,`timeSpentSeconds`,`answeredAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserAnswer entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSessionId());
        statement.bindLong(3, entity.getQuestionId());
        final String _tmp = __converters.fromIntList(entity.getSelectedIndices());
        statement.bindString(4, _tmp);
        final int _tmp_1 = entity.isCorrect() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindLong(6, entity.getTimeSpentSeconds());
        statement.bindLong(7, entity.getAnsweredAt());
      }
    };
    this.__deletionAdapterOfSession = new EntityDeletionOrUpdateAdapter<Session>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sessions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Session entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSession = new EntityDeletionOrUpdateAdapter<Session>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sessions` SET `id` = ?,`sessionType` = ?,`fachrichtung` = ?,`examType` = ?,`lernfeld` = ?,`totalQuestions` = ?,`correctAnswers` = ?,`durationSeconds` = ?,`completedAt` = ?,`startedAt` = ?,`questionIds` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Session entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromSessionType(entity.getSessionType());
        statement.bindString(2, _tmp);
        final String _tmp_1 = __converters.fromFachrichtung(entity.getFachrichtung());
        statement.bindString(3, _tmp_1);
        final String _tmp_2 = __converters.fromExamType(entity.getExamType());
        if (_tmp_2 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_2);
        }
        if (entity.getLernfeld() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getLernfeld());
        }
        statement.bindLong(6, entity.getTotalQuestions());
        statement.bindLong(7, entity.getCorrectAnswers());
        statement.bindLong(8, entity.getDurationSeconds());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getCompletedAt());
        }
        statement.bindLong(10, entity.getStartedAt());
        final String _tmp_3 = __converters.fromLongList(entity.getQuestionIds());
        statement.bindString(11, _tmp_3);
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAllSessions = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sessions";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllAnswers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_answers";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final Session session, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSession.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAnswer(final UserAnswer answer,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfUserAnswer.insertAndReturnId(answer);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAnswers(final List<UserAnswer> answers,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserAnswer.insert(answers);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSessionEntity(final Session session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSession(final Session session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSession.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllSessions(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSessions.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllSessions.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllAnswers(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllAnswers.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllAnswers.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getSessionById(final long id, final Continuation<? super Session> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Session>() {
      @Override
      @Nullable
      public Session call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionType");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfCorrectAnswers = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswers");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfQuestionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "questionIds");
          final Session _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SessionType _tmpSessionType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSessionType);
            _tmpSessionType = __converters.toSessionType(_tmp);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_1);
            final ExamType _tmpExamType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfExamType);
            }
            _tmpExamType = __converters.toExamType(_tmp_2);
            final Integer _tmpLernfeld;
            if (_cursor.isNull(_cursorIndexOfLernfeld)) {
              _tmpLernfeld = null;
            } else {
              _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            }
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final int _tmpCorrectAnswers;
            _tmpCorrectAnswers = _cursor.getInt(_cursorIndexOfCorrectAnswers);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final List<Long> _tmpQuestionIds;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfQuestionIds);
            _tmpQuestionIds = __converters.toLongList(_tmp_3);
            _result = new Session(_tmpId,_tmpSessionType,_tmpFachrichtung,_tmpExamType,_tmpLernfeld,_tmpTotalQuestions,_tmpCorrectAnswers,_tmpDurationSeconds,_tmpCompletedAt,_tmpStartedAt,_tmpQuestionIds);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Session>> getRecentSessions(final int limit) {
    final String _sql = "SELECT * FROM sessions ORDER BY startedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<List<Session>>() {
      @Override
      @NonNull
      public List<Session> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionType");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfCorrectAnswers = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswers");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfQuestionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "questionIds");
          final List<Session> _result = new ArrayList<Session>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Session _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SessionType _tmpSessionType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSessionType);
            _tmpSessionType = __converters.toSessionType(_tmp);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_1);
            final ExamType _tmpExamType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfExamType);
            }
            _tmpExamType = __converters.toExamType(_tmp_2);
            final Integer _tmpLernfeld;
            if (_cursor.isNull(_cursorIndexOfLernfeld)) {
              _tmpLernfeld = null;
            } else {
              _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            }
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final int _tmpCorrectAnswers;
            _tmpCorrectAnswers = _cursor.getInt(_cursorIndexOfCorrectAnswers);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final List<Long> _tmpQuestionIds;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfQuestionIds);
            _tmpQuestionIds = __converters.toLongList(_tmp_3);
            _item = new Session(_tmpId,_tmpSessionType,_tmpFachrichtung,_tmpExamType,_tmpLernfeld,_tmpTotalQuestions,_tmpCorrectAnswers,_tmpDurationSeconds,_tmpCompletedAt,_tmpStartedAt,_tmpQuestionIds);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRecentSessionsList(final int limit,
      final Continuation<? super List<Session>> $completion) {
    final String _sql = "SELECT * FROM sessions ORDER BY startedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Session>>() {
      @Override
      @NonNull
      public List<Session> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionType");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfCorrectAnswers = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswers");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfQuestionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "questionIds");
          final List<Session> _result = new ArrayList<Session>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Session _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SessionType _tmpSessionType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSessionType);
            _tmpSessionType = __converters.toSessionType(_tmp);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_1);
            final ExamType _tmpExamType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfExamType);
            }
            _tmpExamType = __converters.toExamType(_tmp_2);
            final Integer _tmpLernfeld;
            if (_cursor.isNull(_cursorIndexOfLernfeld)) {
              _tmpLernfeld = null;
            } else {
              _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            }
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final int _tmpCorrectAnswers;
            _tmpCorrectAnswers = _cursor.getInt(_cursorIndexOfCorrectAnswers);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final List<Long> _tmpQuestionIds;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfQuestionIds);
            _tmpQuestionIds = __converters.toLongList(_tmp_3);
            _item = new Session(_tmpId,_tmpSessionType,_tmpFachrichtung,_tmpExamType,_tmpLernfeld,_tmpTotalQuestions,_tmpCorrectAnswers,_tmpDurationSeconds,_tmpCompletedAt,_tmpStartedAt,_tmpQuestionIds);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countCompletedSessions(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM sessions WHERE completedAt IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTotalStudyTimeSeconds(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(durationSeconds) FROM sessions WHERE completedAt IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAnswersForSession(final long sessionId,
      final Continuation<? super List<UserAnswer>> $completion) {
    final String _sql = "SELECT * FROM user_answers WHERE sessionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<UserAnswer>>() {
      @Override
      @NonNull
      public List<UserAnswer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfQuestionId = CursorUtil.getColumnIndexOrThrow(_cursor, "questionId");
          final int _cursorIndexOfSelectedIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedIndices");
          final int _cursorIndexOfIsCorrect = CursorUtil.getColumnIndexOrThrow(_cursor, "isCorrect");
          final int _cursorIndexOfTimeSpentSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSpentSeconds");
          final int _cursorIndexOfAnsweredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "answeredAt");
          final List<UserAnswer> _result = new ArrayList<UserAnswer>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UserAnswer _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final long _tmpQuestionId;
            _tmpQuestionId = _cursor.getLong(_cursorIndexOfQuestionId);
            final List<Integer> _tmpSelectedIndices;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSelectedIndices);
            _tmpSelectedIndices = __converters.toIntList(_tmp);
            final boolean _tmpIsCorrect;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCorrect);
            _tmpIsCorrect = _tmp_1 != 0;
            final int _tmpTimeSpentSeconds;
            _tmpTimeSpentSeconds = _cursor.getInt(_cursorIndexOfTimeSpentSeconds);
            final long _tmpAnsweredAt;
            _tmpAnsweredAt = _cursor.getLong(_cursorIndexOfAnsweredAt);
            _item = new UserAnswer(_tmpId,_tmpSessionId,_tmpQuestionId,_tmpSelectedIndices,_tmpIsCorrect,_tmpTimeSpentSeconds,_tmpAnsweredAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countCorrectForQuestion(final long questionId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM user_answers WHERE questionId = ? AND isCorrect = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, questionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countAnsweredForQuestion(final long questionId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM user_answers WHERE questionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, questionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countCorrectInSession(final long sessionId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(isCorrect) FROM user_answers WHERE sessionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getProgressForLernfeld(final int lernfeld,
      final Continuation<? super List<LernfeldProgressRaw>> $completion) {
    final String _sql = "\n"
            + "        SELECT ua.questionId, COUNT(*) as totalAnswered, SUM(ua.isCorrect) as correctCount\n"
            + "        FROM user_answers ua\n"
            + "        JOIN questions q ON ua.questionId = q.id\n"
            + "        WHERE q.lernfeld = ?\n"
            + "        GROUP BY ua.questionId\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, lernfeld);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LernfeldProgressRaw>>() {
      @Override
      @NonNull
      public List<LernfeldProgressRaw> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfQuestionId = 0;
          final int _cursorIndexOfTotalAnswered = 1;
          final int _cursorIndexOfCorrectCount = 2;
          final List<LernfeldProgressRaw> _result = new ArrayList<LernfeldProgressRaw>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LernfeldProgressRaw _item;
            final long _tmpQuestionId;
            _tmpQuestionId = _cursor.getLong(_cursorIndexOfQuestionId);
            final int _tmpTotalAnswered;
            _tmpTotalAnswered = _cursor.getInt(_cursorIndexOfTotalAnswered);
            final int _tmpCorrectCount;
            _tmpCorrectCount = _cursor.getInt(_cursorIndexOfCorrectCount);
            _item = new LernfeldProgressRaw(_tmpQuestionId,_tmpTotalAnswered,_tmpCorrectCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastSession(final Continuation<? super Session> $completion) {
    final String _sql = "SELECT * FROM sessions ORDER BY startedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Session>() {
      @Override
      @Nullable
      public Session call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionType");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfCorrectAnswers = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswers");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfQuestionIds = CursorUtil.getColumnIndexOrThrow(_cursor, "questionIds");
          final Session _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final SessionType _tmpSessionType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSessionType);
            _tmpSessionType = __converters.toSessionType(_tmp);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_1);
            final ExamType _tmpExamType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfExamType);
            }
            _tmpExamType = __converters.toExamType(_tmp_2);
            final Integer _tmpLernfeld;
            if (_cursor.isNull(_cursorIndexOfLernfeld)) {
              _tmpLernfeld = null;
            } else {
              _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            }
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            final int _tmpCorrectAnswers;
            _tmpCorrectAnswers = _cursor.getInt(_cursorIndexOfCorrectAnswers);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final List<Long> _tmpQuestionIds;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfQuestionIds);
            _tmpQuestionIds = __converters.toLongList(_tmp_3);
            _result = new Session(_tmpId,_tmpSessionType,_tmpFachrichtung,_tmpExamType,_tmpLernfeld,_tmpTotalQuestions,_tmpCorrectAnswers,_tmpDurationSeconds,_tmpCompletedAt,_tmpStartedAt,_tmpQuestionIds);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countTotalAnswered(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM user_answers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countTotalCorrect(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT SUM(isCorrect) FROM user_answers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
