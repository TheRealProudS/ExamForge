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
import de.sysitprep.app.data.model.Question;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
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
public final class QuestionDao_Impl implements QuestionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Question> __insertionAdapterOfQuestion;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<Question> __insertionAdapterOfQuestion_1;

  private final EntityDeletionOrUpdateAdapter<Question> __updateAdapterOfQuestion;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfSetBookmark;

  public QuestionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuestion = new EntityInsertionAdapter<Question>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `questions` (`id`,`questionKey`,`title`,`questionText`,`options`,`correctAnswerIndices`,`explanation`,`lernfeld`,`fachrichtung`,`examType`,`year`,`difficulty`,`isBookmarked`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Question entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQuestionKey());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getQuestionText());
        final String _tmp = __converters.fromStringList(entity.getOptions());
        statement.bindString(5, _tmp);
        final String _tmp_1 = __converters.fromIntList(entity.getCorrectAnswerIndices());
        statement.bindString(6, _tmp_1);
        statement.bindString(7, entity.getExplanation());
        statement.bindLong(8, entity.getLernfeld());
        final String _tmp_2 = __converters.fromFachrichtung(entity.getFachrichtung());
        statement.bindString(9, _tmp_2);
        final String _tmp_3 = __converters.fromExamType(entity.getExamType());
        if (_tmp_3 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_3);
        }
        statement.bindLong(11, entity.getYear());
        statement.bindLong(12, entity.getDifficulty());
        final int _tmp_4 = entity.isBookmarked() ? 1 : 0;
        statement.bindLong(13, _tmp_4);
        statement.bindLong(14, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfQuestion_1 = new EntityInsertionAdapter<Question>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `questions` (`id`,`questionKey`,`title`,`questionText`,`options`,`correctAnswerIndices`,`explanation`,`lernfeld`,`fachrichtung`,`examType`,`year`,`difficulty`,`isBookmarked`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Question entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQuestionKey());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getQuestionText());
        final String _tmp = __converters.fromStringList(entity.getOptions());
        statement.bindString(5, _tmp);
        final String _tmp_1 = __converters.fromIntList(entity.getCorrectAnswerIndices());
        statement.bindString(6, _tmp_1);
        statement.bindString(7, entity.getExplanation());
        statement.bindLong(8, entity.getLernfeld());
        final String _tmp_2 = __converters.fromFachrichtung(entity.getFachrichtung());
        statement.bindString(9, _tmp_2);
        final String _tmp_3 = __converters.fromExamType(entity.getExamType());
        if (_tmp_3 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_3);
        }
        statement.bindLong(11, entity.getYear());
        statement.bindLong(12, entity.getDifficulty());
        final int _tmp_4 = entity.isBookmarked() ? 1 : 0;
        statement.bindLong(13, _tmp_4);
        statement.bindLong(14, entity.getCreatedAt());
      }
    };
    this.__updateAdapterOfQuestion = new EntityDeletionOrUpdateAdapter<Question>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `questions` SET `id` = ?,`questionKey` = ?,`title` = ?,`questionText` = ?,`options` = ?,`correctAnswerIndices` = ?,`explanation` = ?,`lernfeld` = ?,`fachrichtung` = ?,`examType` = ?,`year` = ?,`difficulty` = ?,`isBookmarked` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Question entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQuestionKey());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getQuestionText());
        final String _tmp = __converters.fromStringList(entity.getOptions());
        statement.bindString(5, _tmp);
        final String _tmp_1 = __converters.fromIntList(entity.getCorrectAnswerIndices());
        statement.bindString(6, _tmp_1);
        statement.bindString(7, entity.getExplanation());
        statement.bindLong(8, entity.getLernfeld());
        final String _tmp_2 = __converters.fromFachrichtung(entity.getFachrichtung());
        statement.bindString(9, _tmp_2);
        final String _tmp_3 = __converters.fromExamType(entity.getExamType());
        if (_tmp_3 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_3);
        }
        statement.bindLong(11, entity.getYear());
        statement.bindLong(12, entity.getDifficulty());
        final int _tmp_4 = entity.isBookmarked() ? 1 : 0;
        statement.bindLong(13, _tmp_4);
        statement.bindLong(14, entity.getCreatedAt());
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM questions";
        return _query;
      }
    };
    this.__preparedStmtOfSetBookmark = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE questions SET isBookmarked = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<Question> questions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfQuestion.insert(questions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insert(final Question question, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfQuestion_1.insertAndReturnId(question);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Question question, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfQuestion.handle(question);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setBookmark(final long id, final boolean bookmarked,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetBookmark.acquire();
        int _argIndex = 1;
        final int _tmp = bookmarked ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfSetBookmark.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super Question> $completion) {
    final String _sql = "SELECT * FROM questions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Question>() {
      @Override
      @Nullable
      public Question call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "questionKey");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfQuestionText = CursorUtil.getColumnIndexOrThrow(_cursor, "questionText");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfCorrectAnswerIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswerIndices");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Question _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQuestionKey;
            _tmpQuestionKey = _cursor.getString(_cursorIndexOfQuestionKey);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpQuestionText;
            _tmpQuestionText = _cursor.getString(_cursorIndexOfQuestionText);
            final List<String> _tmpOptions;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfOptions);
            _tmpOptions = __converters.toStringList(_tmp);
            final List<Integer> _tmpCorrectAnswerIndices;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfCorrectAnswerIndices);
            _tmpCorrectAnswerIndices = __converters.toIntList(_tmp_1);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final int _tmpLernfeld;
            _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_2);
            final ExamType _tmpExamType;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfExamType);
            }
            final ExamType _tmp_4 = __converters.toExamType(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'de.sysitprep.app.data.model.ExamType', but it was NULL.");
            } else {
              _tmpExamType = _tmp_4;
            }
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpDifficulty;
            _tmpDifficulty = _cursor.getInt(_cursorIndexOfDifficulty);
            final boolean _tmpIsBookmarked;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_5 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Question(_tmpId,_tmpQuestionKey,_tmpTitle,_tmpQuestionText,_tmpOptions,_tmpCorrectAnswerIndices,_tmpExplanation,_tmpLernfeld,_tmpFachrichtung,_tmpExamType,_tmpYear,_tmpDifficulty,_tmpIsBookmarked,_tmpCreatedAt);
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
  public Object getByKey(final String key, final Continuation<? super Question> $completion) {
    final String _sql = "SELECT * FROM questions WHERE questionKey = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, key);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Question>() {
      @Override
      @Nullable
      public Question call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "questionKey");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfQuestionText = CursorUtil.getColumnIndexOrThrow(_cursor, "questionText");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfCorrectAnswerIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswerIndices");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final Question _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQuestionKey;
            _tmpQuestionKey = _cursor.getString(_cursorIndexOfQuestionKey);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpQuestionText;
            _tmpQuestionText = _cursor.getString(_cursorIndexOfQuestionText);
            final List<String> _tmpOptions;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfOptions);
            _tmpOptions = __converters.toStringList(_tmp);
            final List<Integer> _tmpCorrectAnswerIndices;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfCorrectAnswerIndices);
            _tmpCorrectAnswerIndices = __converters.toIntList(_tmp_1);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final int _tmpLernfeld;
            _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_2);
            final ExamType _tmpExamType;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfExamType);
            }
            final ExamType _tmp_4 = __converters.toExamType(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'de.sysitprep.app.data.model.ExamType', but it was NULL.");
            } else {
              _tmpExamType = _tmp_4;
            }
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpDifficulty;
            _tmpDifficulty = _cursor.getInt(_cursorIndexOfDifficulty);
            final boolean _tmpIsBookmarked;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_5 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new Question(_tmpId,_tmpQuestionKey,_tmpTitle,_tmpQuestionText,_tmpOptions,_tmpCorrectAnswerIndices,_tmpExplanation,_tmpLernfeld,_tmpFachrichtung,_tmpExamType,_tmpYear,_tmpDifficulty,_tmpIsBookmarked,_tmpCreatedAt);
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
  public Object getByLernfeldAndFachrichtung(final int lernfeld, final Fachrichtung fachrichtung,
      final int limit, final Continuation<? super List<Question>> $completion) {
    final String _sql = "SELECT * FROM questions WHERE lernfeld = ? AND (fachrichtung = ? OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, lernfeld);
    _argIndex = 2;
    final String _tmp = __converters.fromFachrichtung(fachrichtung);
    _statement.bindString(_argIndex, _tmp);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "questionKey");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfQuestionText = CursorUtil.getColumnIndexOrThrow(_cursor, "questionText");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfCorrectAnswerIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswerIndices");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQuestionKey;
            _tmpQuestionKey = _cursor.getString(_cursorIndexOfQuestionKey);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpQuestionText;
            _tmpQuestionText = _cursor.getString(_cursorIndexOfQuestionText);
            final List<String> _tmpOptions;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfOptions);
            _tmpOptions = __converters.toStringList(_tmp_1);
            final List<Integer> _tmpCorrectAnswerIndices;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfCorrectAnswerIndices);
            _tmpCorrectAnswerIndices = __converters.toIntList(_tmp_2);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final int _tmpLernfeld;
            _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_3);
            final ExamType _tmpExamType;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfExamType);
            }
            final ExamType _tmp_5 = __converters.toExamType(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'de.sysitprep.app.data.model.ExamType', but it was NULL.");
            } else {
              _tmpExamType = _tmp_5;
            }
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpDifficulty;
            _tmpDifficulty = _cursor.getInt(_cursorIndexOfDifficulty);
            final boolean _tmpIsBookmarked;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_6 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Question(_tmpId,_tmpQuestionKey,_tmpTitle,_tmpQuestionText,_tmpOptions,_tmpCorrectAnswerIndices,_tmpExplanation,_tmpLernfeld,_tmpFachrichtung,_tmpExamType,_tmpYear,_tmpDifficulty,_tmpIsBookmarked,_tmpCreatedAt);
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
  public Object getByExamType(final ExamType examType, final Fachrichtung fachrichtung,
      final int limit, final Continuation<? super List<Question>> $completion) {
    final String _sql = "SELECT * FROM questions WHERE examType = ? AND (fachrichtung = ? OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    final String _tmp = __converters.fromExamType(examType);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromFachrichtung(fachrichtung);
    _statement.bindString(_argIndex, _tmp_1);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "questionKey");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfQuestionText = CursorUtil.getColumnIndexOrThrow(_cursor, "questionText");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfCorrectAnswerIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswerIndices");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQuestionKey;
            _tmpQuestionKey = _cursor.getString(_cursorIndexOfQuestionKey);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpQuestionText;
            _tmpQuestionText = _cursor.getString(_cursorIndexOfQuestionText);
            final List<String> _tmpOptions;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfOptions);
            _tmpOptions = __converters.toStringList(_tmp_2);
            final List<Integer> _tmpCorrectAnswerIndices;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfCorrectAnswerIndices);
            _tmpCorrectAnswerIndices = __converters.toIntList(_tmp_3);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final int _tmpLernfeld;
            _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_4);
            final ExamType _tmpExamType;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfExamType);
            }
            final ExamType _tmp_6 = __converters.toExamType(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'de.sysitprep.app.data.model.ExamType', but it was NULL.");
            } else {
              _tmpExamType = _tmp_6;
            }
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpDifficulty;
            _tmpDifficulty = _cursor.getInt(_cursorIndexOfDifficulty);
            final boolean _tmpIsBookmarked;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_7 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Question(_tmpId,_tmpQuestionKey,_tmpTitle,_tmpQuestionText,_tmpOptions,_tmpCorrectAnswerIndices,_tmpExplanation,_tmpLernfeld,_tmpFachrichtung,_tmpExamType,_tmpYear,_tmpDifficulty,_tmpIsBookmarked,_tmpCreatedAt);
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
  public Flow<List<Question>> getBookmarked() {
    final String _sql = "SELECT * FROM questions WHERE isBookmarked = 1 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"questions"}, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "questionKey");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfQuestionText = CursorUtil.getColumnIndexOrThrow(_cursor, "questionText");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfCorrectAnswerIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswerIndices");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQuestionKey;
            _tmpQuestionKey = _cursor.getString(_cursorIndexOfQuestionKey);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpQuestionText;
            _tmpQuestionText = _cursor.getString(_cursorIndexOfQuestionText);
            final List<String> _tmpOptions;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfOptions);
            _tmpOptions = __converters.toStringList(_tmp);
            final List<Integer> _tmpCorrectAnswerIndices;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfCorrectAnswerIndices);
            _tmpCorrectAnswerIndices = __converters.toIntList(_tmp_1);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final int _tmpLernfeld;
            _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_2);
            final ExamType _tmpExamType;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfExamType);
            }
            final ExamType _tmp_4 = __converters.toExamType(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'de.sysitprep.app.data.model.ExamType', but it was NULL.");
            } else {
              _tmpExamType = _tmp_4;
            }
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpDifficulty;
            _tmpDifficulty = _cursor.getInt(_cursorIndexOfDifficulty);
            final boolean _tmpIsBookmarked;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_5 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Question(_tmpId,_tmpQuestionKey,_tmpTitle,_tmpQuestionText,_tmpOptions,_tmpCorrectAnswerIndices,_tmpExplanation,_tmpLernfeld,_tmpFachrichtung,_tmpExamType,_tmpYear,_tmpDifficulty,_tmpIsBookmarked,_tmpCreatedAt);
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
  public Object getRandomByFachrichtung(final Fachrichtung fachrichtung, final int limit,
      final Continuation<? super List<Question>> $completion) {
    final String _sql = "SELECT * FROM questions WHERE (fachrichtung = ? OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromFachrichtung(fachrichtung);
    _statement.bindString(_argIndex, _tmp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "questionKey");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfQuestionText = CursorUtil.getColumnIndexOrThrow(_cursor, "questionText");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfCorrectAnswerIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswerIndices");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQuestionKey;
            _tmpQuestionKey = _cursor.getString(_cursorIndexOfQuestionKey);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpQuestionText;
            _tmpQuestionText = _cursor.getString(_cursorIndexOfQuestionText);
            final List<String> _tmpOptions;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfOptions);
            _tmpOptions = __converters.toStringList(_tmp_1);
            final List<Integer> _tmpCorrectAnswerIndices;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfCorrectAnswerIndices);
            _tmpCorrectAnswerIndices = __converters.toIntList(_tmp_2);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final int _tmpLernfeld;
            _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_3);
            final ExamType _tmpExamType;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfExamType);
            }
            final ExamType _tmp_5 = __converters.toExamType(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'de.sysitprep.app.data.model.ExamType', but it was NULL.");
            } else {
              _tmpExamType = _tmp_5;
            }
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpDifficulty;
            _tmpDifficulty = _cursor.getInt(_cursorIndexOfDifficulty);
            final boolean _tmpIsBookmarked;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_6 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Question(_tmpId,_tmpQuestionKey,_tmpTitle,_tmpQuestionText,_tmpOptions,_tmpCorrectAnswerIndices,_tmpExplanation,_tmpLernfeld,_tmpFachrichtung,_tmpExamType,_tmpYear,_tmpDifficulty,_tmpIsBookmarked,_tmpCreatedAt);
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
  public Object countByLernfeld(final int lernfeld, final Fachrichtung fachrichtung,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM questions WHERE lernfeld = ? AND (fachrichtung = ? OR fachrichtung = 'GEMEINSAM')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, lernfeld);
    _argIndex = 2;
    final String _tmp = __converters.fromFachrichtung(fachrichtung);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1;
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
  public Object countAll(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM questions";
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
  public Object getByDifficulty(final int difficulty, final Fachrichtung fachrichtung,
      final int limit, final Continuation<? super List<Question>> $completion) {
    final String _sql = "SELECT * FROM questions WHERE difficulty = ? AND (fachrichtung = ? OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, difficulty);
    _argIndex = 2;
    final String _tmp = __converters.fromFachrichtung(fachrichtung);
    _statement.bindString(_argIndex, _tmp);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Question>>() {
      @Override
      @NonNull
      public List<Question> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQuestionKey = CursorUtil.getColumnIndexOrThrow(_cursor, "questionKey");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfQuestionText = CursorUtil.getColumnIndexOrThrow(_cursor, "questionText");
          final int _cursorIndexOfOptions = CursorUtil.getColumnIndexOrThrow(_cursor, "options");
          final int _cursorIndexOfCorrectAnswerIndices = CursorUtil.getColumnIndexOrThrow(_cursor, "correctAnswerIndices");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLernfeld = CursorUtil.getColumnIndexOrThrow(_cursor, "lernfeld");
          final int _cursorIndexOfFachrichtung = CursorUtil.getColumnIndexOrThrow(_cursor, "fachrichtung");
          final int _cursorIndexOfExamType = CursorUtil.getColumnIndexOrThrow(_cursor, "examType");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<Question> _result = new ArrayList<Question>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Question _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQuestionKey;
            _tmpQuestionKey = _cursor.getString(_cursorIndexOfQuestionKey);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpQuestionText;
            _tmpQuestionText = _cursor.getString(_cursorIndexOfQuestionText);
            final List<String> _tmpOptions;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfOptions);
            _tmpOptions = __converters.toStringList(_tmp_1);
            final List<Integer> _tmpCorrectAnswerIndices;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfCorrectAnswerIndices);
            _tmpCorrectAnswerIndices = __converters.toIntList(_tmp_2);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final int _tmpLernfeld;
            _tmpLernfeld = _cursor.getInt(_cursorIndexOfLernfeld);
            final Fachrichtung _tmpFachrichtung;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfFachrichtung);
            _tmpFachrichtung = __converters.toFachrichtung(_tmp_3);
            final ExamType _tmpExamType;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfExamType)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfExamType);
            }
            final ExamType _tmp_5 = __converters.toExamType(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'de.sysitprep.app.data.model.ExamType', but it was NULL.");
            } else {
              _tmpExamType = _tmp_5;
            }
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpDifficulty;
            _tmpDifficulty = _cursor.getInt(_cursorIndexOfDifficulty);
            final boolean _tmpIsBookmarked;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp_6 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new Question(_tmpId,_tmpQuestionKey,_tmpTitle,_tmpQuestionText,_tmpOptions,_tmpCorrectAnswerIndices,_tmpExplanation,_tmpLernfeld,_tmpFachrichtung,_tmpExamType,_tmpYear,_tmpDifficulty,_tmpIsBookmarked,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
