package com.akter.mphone.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MPhoneDatabase_Impl extends MPhoneDatabase {
  private volatile SipAccountDao _sipAccountDao;

  private volatile CallLogDao _callLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `sip_accounts` (`id` TEXT NOT NULL, `accountName` TEXT NOT NULL, `username` TEXT NOT NULL, `domain` TEXT NOT NULL, `port` INTEGER NOT NULL, `transport` TEXT NOT NULL, `outboundProxy` TEXT NOT NULL, `authId` TEXT NOT NULL, `displayName` TEXT NOT NULL, `stunServer` TEXT NOT NULL, `registrationExpiry` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `isDefaultOutgoing` INTEGER NOT NULL, `autoRecordIncoming` INTEGER NOT NULL, `autoRecordOutgoing` INTEGER NOT NULL, `lastRegistrationStatus` TEXT NOT NULL, `lastErrorDetail` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `call_logs` (`id` TEXT NOT NULL, `callerName` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `sipAccountName` TEXT NOT NULL, `direction` TEXT NOT NULL, `callStatus` TEXT NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `durationSeconds` INTEGER NOT NULL, `jitterMs` REAL, `packetLossPercent` REAL, `rtpRttMs` REAL, `networkType` TEXT NOT NULL, `codecUsed` TEXT NOT NULL, `totalDataBytes` INTEGER NOT NULL, `mosScore` REAL, `qualityRating` TEXT NOT NULL, `recordingFilePath` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2b50c4efe7329179583b254a43e53ec7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `sip_accounts`");
        db.execSQL("DROP TABLE IF EXISTS `call_logs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSipAccounts = new HashMap<String, TableInfo.Column>(18);
        _columnsSipAccounts.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("accountName", new TableInfo.Column("accountName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("domain", new TableInfo.Column("domain", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("port", new TableInfo.Column("port", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("transport", new TableInfo.Column("transport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("outboundProxy", new TableInfo.Column("outboundProxy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("authId", new TableInfo.Column("authId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("stunServer", new TableInfo.Column("stunServer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("registrationExpiry", new TableInfo.Column("registrationExpiry", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("isEnabled", new TableInfo.Column("isEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("isDefaultOutgoing", new TableInfo.Column("isDefaultOutgoing", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("autoRecordIncoming", new TableInfo.Column("autoRecordIncoming", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("autoRecordOutgoing", new TableInfo.Column("autoRecordOutgoing", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("lastRegistrationStatus", new TableInfo.Column("lastRegistrationStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("lastErrorDetail", new TableInfo.Column("lastErrorDetail", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSipAccounts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSipAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSipAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSipAccounts = new TableInfo("sip_accounts", _columnsSipAccounts, _foreignKeysSipAccounts, _indicesSipAccounts);
        final TableInfo _existingSipAccounts = TableInfo.read(db, "sip_accounts");
        if (!_infoSipAccounts.equals(_existingSipAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "sip_accounts(com.akter.mphone.database.SipAccountEntity).\n"
                  + " Expected:\n" + _infoSipAccounts + "\n"
                  + " Found:\n" + _existingSipAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsCallLogs = new HashMap<String, TableInfo.Column>(18);
        _columnsCallLogs.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("callerName", new TableInfo.Column("callerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("sipAccountName", new TableInfo.Column("sipAccountName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("direction", new TableInfo.Column("direction", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("callStatus", new TableInfo.Column("callStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("durationSeconds", new TableInfo.Column("durationSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("jitterMs", new TableInfo.Column("jitterMs", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("packetLossPercent", new TableInfo.Column("packetLossPercent", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("rtpRttMs", new TableInfo.Column("rtpRttMs", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("networkType", new TableInfo.Column("networkType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("codecUsed", new TableInfo.Column("codecUsed", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("totalDataBytes", new TableInfo.Column("totalDataBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("mosScore", new TableInfo.Column("mosScore", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("qualityRating", new TableInfo.Column("qualityRating", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallLogs.put("recordingFilePath", new TableInfo.Column("recordingFilePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCallLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCallLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCallLogs = new TableInfo("call_logs", _columnsCallLogs, _foreignKeysCallLogs, _indicesCallLogs);
        final TableInfo _existingCallLogs = TableInfo.read(db, "call_logs");
        if (!_infoCallLogs.equals(_existingCallLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "call_logs(com.akter.mphone.database.CallLogEntity).\n"
                  + " Expected:\n" + _infoCallLogs + "\n"
                  + " Found:\n" + _existingCallLogs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2b50c4efe7329179583b254a43e53ec7", "6d18e6e3d3fd177b4ed8419dc33c851a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sip_accounts","call_logs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `sip_accounts`");
      _db.execSQL("DELETE FROM `call_logs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SipAccountDao.class, SipAccountDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CallLogDao.class, CallLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SipAccountDao sipAccountDao() {
    if (_sipAccountDao != null) {
      return _sipAccountDao;
    } else {
      synchronized(this) {
        if(_sipAccountDao == null) {
          _sipAccountDao = new SipAccountDao_Impl(this);
        }
        return _sipAccountDao;
      }
    }
  }

  @Override
  public CallLogDao callLogDao() {
    if (_callLogDao != null) {
      return _callLogDao;
    } else {
      synchronized(this) {
        if(_callLogDao == null) {
          _callLogDao = new CallLogDao_Impl(this);
        }
        return _callLogDao;
      }
    }
  }
}
