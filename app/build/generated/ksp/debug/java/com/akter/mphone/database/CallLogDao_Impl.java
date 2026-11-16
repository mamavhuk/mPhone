package com.akter.mphone.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
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
public final class CallLogDao_Impl implements CallLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CallLogEntity> __insertionAdapterOfCallLogEntity;

  public CallLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCallLogEntity = new EntityInsertionAdapter<CallLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `call_logs` (`id`,`callerName`,`phoneNumber`,`sipAccountName`,`direction`,`callStatus`,`startTime`,`endTime`,`durationSeconds`,`jitterMs`,`packetLossPercent`,`rtpRttMs`,`networkType`,`codecUsed`,`totalDataBytes`,`mosScore`,`qualityRating`,`recordingFilePath`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CallLogEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getCallerName());
        statement.bindString(3, entity.getPhoneNumber());
        statement.bindString(4, entity.getSipAccountName());
        statement.bindString(5, entity.getDirection());
        statement.bindString(6, entity.getCallStatus());
        statement.bindLong(7, entity.getStartTime());
        statement.bindLong(8, entity.getEndTime());
        statement.bindLong(9, entity.getDurationSeconds());
        if (entity.getJitterMs() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getJitterMs());
        }
        if (entity.getPacketLossPercent() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getPacketLossPercent());
        }
        if (entity.getRtpRttMs() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getRtpRttMs());
        }
        statement.bindString(13, entity.getNetworkType());
        statement.bindString(14, entity.getCodecUsed());
        statement.bindLong(15, entity.getTotalDataBytes());
        if (entity.getMosScore() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getMosScore());
        }
        statement.bindString(17, entity.getQualityRating());
        if (entity.getRecordingFilePath() == null) {
          statement.bindNull(18);
        } else {
          statement.bindString(18, entity.getRecordingFilePath());
        }
      }
    };
  }

  @Override
  public Object insertCallLog(final CallLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCallLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CallLogEntity>> observeAllLogs() {
    final String _sql = "SELECT * FROM call_logs ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_logs"}, new Callable<List<CallLogEntity>>() {
      @Override
      @NonNull
      public List<CallLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCallerName = CursorUtil.getColumnIndexOrThrow(_cursor, "callerName");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfSipAccountName = CursorUtil.getColumnIndexOrThrow(_cursor, "sipAccountName");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfCallStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "callStatus");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfJitterMs = CursorUtil.getColumnIndexOrThrow(_cursor, "jitterMs");
          final int _cursorIndexOfPacketLossPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "packetLossPercent");
          final int _cursorIndexOfRtpRttMs = CursorUtil.getColumnIndexOrThrow(_cursor, "rtpRttMs");
          final int _cursorIndexOfNetworkType = CursorUtil.getColumnIndexOrThrow(_cursor, "networkType");
          final int _cursorIndexOfCodecUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "codecUsed");
          final int _cursorIndexOfTotalDataBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDataBytes");
          final int _cursorIndexOfMosScore = CursorUtil.getColumnIndexOrThrow(_cursor, "mosScore");
          final int _cursorIndexOfQualityRating = CursorUtil.getColumnIndexOrThrow(_cursor, "qualityRating");
          final int _cursorIndexOfRecordingFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "recordingFilePath");
          final List<CallLogEntity> _result = new ArrayList<CallLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallLogEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCallerName;
            _tmpCallerName = _cursor.getString(_cursorIndexOfCallerName);
            final String _tmpPhoneNumber;
            _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            final String _tmpSipAccountName;
            _tmpSipAccountName = _cursor.getString(_cursorIndexOfSipAccountName);
            final String _tmpDirection;
            _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            final String _tmpCallStatus;
            _tmpCallStatus = _cursor.getString(_cursorIndexOfCallStatus);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final long _tmpEndTime;
            _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final Float _tmpJitterMs;
            if (_cursor.isNull(_cursorIndexOfJitterMs)) {
              _tmpJitterMs = null;
            } else {
              _tmpJitterMs = _cursor.getFloat(_cursorIndexOfJitterMs);
            }
            final Float _tmpPacketLossPercent;
            if (_cursor.isNull(_cursorIndexOfPacketLossPercent)) {
              _tmpPacketLossPercent = null;
            } else {
              _tmpPacketLossPercent = _cursor.getFloat(_cursorIndexOfPacketLossPercent);
            }
            final Float _tmpRtpRttMs;
            if (_cursor.isNull(_cursorIndexOfRtpRttMs)) {
              _tmpRtpRttMs = null;
            } else {
              _tmpRtpRttMs = _cursor.getFloat(_cursorIndexOfRtpRttMs);
            }
            final String _tmpNetworkType;
            _tmpNetworkType = _cursor.getString(_cursorIndexOfNetworkType);
            final String _tmpCodecUsed;
            _tmpCodecUsed = _cursor.getString(_cursorIndexOfCodecUsed);
            final long _tmpTotalDataBytes;
            _tmpTotalDataBytes = _cursor.getLong(_cursorIndexOfTotalDataBytes);
            final Float _tmpMosScore;
            if (_cursor.isNull(_cursorIndexOfMosScore)) {
              _tmpMosScore = null;
            } else {
              _tmpMosScore = _cursor.getFloat(_cursorIndexOfMosScore);
            }
            final String _tmpQualityRating;
            _tmpQualityRating = _cursor.getString(_cursorIndexOfQualityRating);
            final String _tmpRecordingFilePath;
            if (_cursor.isNull(_cursorIndexOfRecordingFilePath)) {
              _tmpRecordingFilePath = null;
            } else {
              _tmpRecordingFilePath = _cursor.getString(_cursorIndexOfRecordingFilePath);
            }
            _item = new CallLogEntity(_tmpId,_tmpCallerName,_tmpPhoneNumber,_tmpSipAccountName,_tmpDirection,_tmpCallStatus,_tmpStartTime,_tmpEndTime,_tmpDurationSeconds,_tmpJitterMs,_tmpPacketLossPercent,_tmpRtpRttMs,_tmpNetworkType,_tmpCodecUsed,_tmpTotalDataBytes,_tmpMosScore,_tmpQualityRating,_tmpRecordingFilePath);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
