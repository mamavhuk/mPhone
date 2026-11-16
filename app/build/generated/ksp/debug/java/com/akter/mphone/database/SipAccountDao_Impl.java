package com.akter.mphone.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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
public final class SipAccountDao_Impl implements SipAccountDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SipAccountEntity> __insertionAdapterOfSipAccountEntity;

  private final EntityDeletionOrUpdateAdapter<SipAccountEntity> __deletionAdapterOfSipAccountEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRegistrationState;

  public SipAccountDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSipAccountEntity = new EntityInsertionAdapter<SipAccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sip_accounts` (`id`,`accountName`,`username`,`domain`,`port`,`transport`,`outboundProxy`,`authId`,`displayName`,`stunServer`,`registrationExpiry`,`isEnabled`,`isDefaultOutgoing`,`autoRecordIncoming`,`autoRecordOutgoing`,`lastRegistrationStatus`,`lastErrorDetail`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SipAccountEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getAccountName());
        statement.bindString(3, entity.getUsername());
        statement.bindString(4, entity.getDomain());
        statement.bindLong(5, entity.getPort());
        statement.bindString(6, entity.getTransport());
        statement.bindString(7, entity.getOutboundProxy());
        statement.bindString(8, entity.getAuthId());
        statement.bindString(9, entity.getDisplayName());
        statement.bindString(10, entity.getStunServer());
        statement.bindLong(11, entity.getRegistrationExpiry());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(12, _tmp);
        final int _tmp_1 = entity.isDefaultOutgoing() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        final int _tmp_2 = entity.getAutoRecordIncoming() ? 1 : 0;
        statement.bindLong(14, _tmp_2);
        final int _tmp_3 = entity.getAutoRecordOutgoing() ? 1 : 0;
        statement.bindLong(15, _tmp_3);
        statement.bindString(16, entity.getLastRegistrationStatus());
        statement.bindString(17, entity.getLastErrorDetail());
        statement.bindLong(18, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfSipAccountEntity = new EntityDeletionOrUpdateAdapter<SipAccountEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sip_accounts` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SipAccountEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateRegistrationState = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sip_accounts SET lastRegistrationStatus = ?, lastErrorDetail = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAccount(final SipAccountEntity account,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSipAccountEntity.insert(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAccount(final SipAccountEntity account,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSipAccountEntity.handle(account);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRegistrationState(final String id, final String status, final String error,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRegistrationState.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindString(_argIndex, error);
        _argIndex = 3;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdateRegistrationState.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SipAccountEntity>> observeAllAccounts() {
    final String _sql = "SELECT * FROM sip_accounts ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sip_accounts"}, new Callable<List<SipAccountEntity>>() {
      @Override
      @NonNull
      public List<SipAccountEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountName = CursorUtil.getColumnIndexOrThrow(_cursor, "accountName");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfDomain = CursorUtil.getColumnIndexOrThrow(_cursor, "domain");
          final int _cursorIndexOfPort = CursorUtil.getColumnIndexOrThrow(_cursor, "port");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfOutboundProxy = CursorUtil.getColumnIndexOrThrow(_cursor, "outboundProxy");
          final int _cursorIndexOfAuthId = CursorUtil.getColumnIndexOrThrow(_cursor, "authId");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfStunServer = CursorUtil.getColumnIndexOrThrow(_cursor, "stunServer");
          final int _cursorIndexOfRegistrationExpiry = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationExpiry");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfIsDefaultOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefaultOutgoing");
          final int _cursorIndexOfAutoRecordIncoming = CursorUtil.getColumnIndexOrThrow(_cursor, "autoRecordIncoming");
          final int _cursorIndexOfAutoRecordOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "autoRecordOutgoing");
          final int _cursorIndexOfLastRegistrationStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRegistrationStatus");
          final int _cursorIndexOfLastErrorDetail = CursorUtil.getColumnIndexOrThrow(_cursor, "lastErrorDetail");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SipAccountEntity> _result = new ArrayList<SipAccountEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SipAccountEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAccountName;
            _tmpAccountName = _cursor.getString(_cursorIndexOfAccountName);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpDomain;
            _tmpDomain = _cursor.getString(_cursorIndexOfDomain);
            final int _tmpPort;
            _tmpPort = _cursor.getInt(_cursorIndexOfPort);
            final String _tmpTransport;
            _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            final String _tmpOutboundProxy;
            _tmpOutboundProxy = _cursor.getString(_cursorIndexOfOutboundProxy);
            final String _tmpAuthId;
            _tmpAuthId = _cursor.getString(_cursorIndexOfAuthId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpStunServer;
            _tmpStunServer = _cursor.getString(_cursorIndexOfStunServer);
            final int _tmpRegistrationExpiry;
            _tmpRegistrationExpiry = _cursor.getInt(_cursorIndexOfRegistrationExpiry);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final boolean _tmpIsDefaultOutgoing;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDefaultOutgoing);
            _tmpIsDefaultOutgoing = _tmp_1 != 0;
            final boolean _tmpAutoRecordIncoming;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfAutoRecordIncoming);
            _tmpAutoRecordIncoming = _tmp_2 != 0;
            final boolean _tmpAutoRecordOutgoing;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfAutoRecordOutgoing);
            _tmpAutoRecordOutgoing = _tmp_3 != 0;
            final String _tmpLastRegistrationStatus;
            _tmpLastRegistrationStatus = _cursor.getString(_cursorIndexOfLastRegistrationStatus);
            final String _tmpLastErrorDetail;
            _tmpLastErrorDetail = _cursor.getString(_cursorIndexOfLastErrorDetail);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SipAccountEntity(_tmpId,_tmpAccountName,_tmpUsername,_tmpDomain,_tmpPort,_tmpTransport,_tmpOutboundProxy,_tmpAuthId,_tmpDisplayName,_tmpStunServer,_tmpRegistrationExpiry,_tmpIsEnabled,_tmpIsDefaultOutgoing,_tmpAutoRecordIncoming,_tmpAutoRecordOutgoing,_tmpLastRegistrationStatus,_tmpLastErrorDetail,_tmpCreatedAt);
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
  public Object getEnabledAccounts(final Continuation<? super List<SipAccountEntity>> $completion) {
    final String _sql = "SELECT * FROM sip_accounts WHERE isEnabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SipAccountEntity>>() {
      @Override
      @NonNull
      public List<SipAccountEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAccountName = CursorUtil.getColumnIndexOrThrow(_cursor, "accountName");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfDomain = CursorUtil.getColumnIndexOrThrow(_cursor, "domain");
          final int _cursorIndexOfPort = CursorUtil.getColumnIndexOrThrow(_cursor, "port");
          final int _cursorIndexOfTransport = CursorUtil.getColumnIndexOrThrow(_cursor, "transport");
          final int _cursorIndexOfOutboundProxy = CursorUtil.getColumnIndexOrThrow(_cursor, "outboundProxy");
          final int _cursorIndexOfAuthId = CursorUtil.getColumnIndexOrThrow(_cursor, "authId");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfStunServer = CursorUtil.getColumnIndexOrThrow(_cursor, "stunServer");
          final int _cursorIndexOfRegistrationExpiry = CursorUtil.getColumnIndexOrThrow(_cursor, "registrationExpiry");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isEnabled");
          final int _cursorIndexOfIsDefaultOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "isDefaultOutgoing");
          final int _cursorIndexOfAutoRecordIncoming = CursorUtil.getColumnIndexOrThrow(_cursor, "autoRecordIncoming");
          final int _cursorIndexOfAutoRecordOutgoing = CursorUtil.getColumnIndexOrThrow(_cursor, "autoRecordOutgoing");
          final int _cursorIndexOfLastRegistrationStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastRegistrationStatus");
          final int _cursorIndexOfLastErrorDetail = CursorUtil.getColumnIndexOrThrow(_cursor, "lastErrorDetail");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SipAccountEntity> _result = new ArrayList<SipAccountEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SipAccountEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAccountName;
            _tmpAccountName = _cursor.getString(_cursorIndexOfAccountName);
            final String _tmpUsername;
            _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            final String _tmpDomain;
            _tmpDomain = _cursor.getString(_cursorIndexOfDomain);
            final int _tmpPort;
            _tmpPort = _cursor.getInt(_cursorIndexOfPort);
            final String _tmpTransport;
            _tmpTransport = _cursor.getString(_cursorIndexOfTransport);
            final String _tmpOutboundProxy;
            _tmpOutboundProxy = _cursor.getString(_cursorIndexOfOutboundProxy);
            final String _tmpAuthId;
            _tmpAuthId = _cursor.getString(_cursorIndexOfAuthId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpStunServer;
            _tmpStunServer = _cursor.getString(_cursorIndexOfStunServer);
            final int _tmpRegistrationExpiry;
            _tmpRegistrationExpiry = _cursor.getInt(_cursorIndexOfRegistrationExpiry);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final boolean _tmpIsDefaultOutgoing;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDefaultOutgoing);
            _tmpIsDefaultOutgoing = _tmp_1 != 0;
            final boolean _tmpAutoRecordIncoming;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfAutoRecordIncoming);
            _tmpAutoRecordIncoming = _tmp_2 != 0;
            final boolean _tmpAutoRecordOutgoing;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfAutoRecordOutgoing);
            _tmpAutoRecordOutgoing = _tmp_3 != 0;
            final String _tmpLastRegistrationStatus;
            _tmpLastRegistrationStatus = _cursor.getString(_cursorIndexOfLastRegistrationStatus);
            final String _tmpLastErrorDetail;
            _tmpLastErrorDetail = _cursor.getString(_cursorIndexOfLastErrorDetail);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SipAccountEntity(_tmpId,_tmpAccountName,_tmpUsername,_tmpDomain,_tmpPort,_tmpTransport,_tmpOutboundProxy,_tmpAuthId,_tmpDisplayName,_tmpStunServer,_tmpRegistrationExpiry,_tmpIsEnabled,_tmpIsDefaultOutgoing,_tmpAutoRecordIncoming,_tmpAutoRecordOutgoing,_tmpLastRegistrationStatus,_tmpLastErrorDetail,_tmpCreatedAt);
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
