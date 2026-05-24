package com.terminalarrow.app.data;

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
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TerminalDao _terminalDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `connection_profiles` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `host` TEXT NOT NULL, `port` INTEGER NOT NULL, `username` TEXT NOT NULL, `password` TEXT, `keyPath` TEXT, `group` TEXT, `forwardingRules` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `snippets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `command` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '58ab50b9f7500c330d422fd8a0fe9370')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `connection_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `snippets`");
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
        final HashMap<String, TableInfo.Column> _columnsConnectionProfiles = new HashMap<String, TableInfo.Column>(9);
        _columnsConnectionProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("host", new TableInfo.Column("host", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("port", new TableInfo.Column("port", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("keyPath", new TableInfo.Column("keyPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("group", new TableInfo.Column("group", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConnectionProfiles.put("forwardingRules", new TableInfo.Column("forwardingRules", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysConnectionProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesConnectionProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoConnectionProfiles = new TableInfo("connection_profiles", _columnsConnectionProfiles, _foreignKeysConnectionProfiles, _indicesConnectionProfiles);
        final TableInfo _existingConnectionProfiles = TableInfo.read(db, "connection_profiles");
        if (!_infoConnectionProfiles.equals(_existingConnectionProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "connection_profiles(com.terminalarrow.app.data.ConnectionProfile).\n"
                  + " Expected:\n" + _infoConnectionProfiles + "\n"
                  + " Found:\n" + _existingConnectionProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsSnippets = new HashMap<String, TableInfo.Column>(3);
        _columnsSnippets.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSnippets.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSnippets.put("command", new TableInfo.Column("command", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSnippets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSnippets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSnippets = new TableInfo("snippets", _columnsSnippets, _foreignKeysSnippets, _indicesSnippets);
        final TableInfo _existingSnippets = TableInfo.read(db, "snippets");
        if (!_infoSnippets.equals(_existingSnippets)) {
          return new RoomOpenHelper.ValidationResult(false, "snippets(com.terminalarrow.app.data.Snippet).\n"
                  + " Expected:\n" + _infoSnippets + "\n"
                  + " Found:\n" + _existingSnippets);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "58ab50b9f7500c330d422fd8a0fe9370", "eec3b161be2364d2f58d4742be761236");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "connection_profiles","snippets");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `connection_profiles`");
      _db.execSQL("DELETE FROM `snippets`");
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
    _typeConvertersMap.put(TerminalDao.class, TerminalDao_Impl.getRequiredConverters());
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
  public TerminalDao terminalDao() {
    if (_terminalDao != null) {
      return _terminalDao;
    } else {
      synchronized(this) {
        if(_terminalDao == null) {
          _terminalDao = new TerminalDao_Impl(this);
        }
        return _terminalDao;
      }
    }
  }
}
