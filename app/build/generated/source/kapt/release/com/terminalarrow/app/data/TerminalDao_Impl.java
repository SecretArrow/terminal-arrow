package com.terminalarrow.app.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
public final class TerminalDao_Impl implements TerminalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ConnectionProfile> __insertionAdapterOfConnectionProfile;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<Snippet> __insertionAdapterOfSnippet;

  private final EntityDeletionOrUpdateAdapter<ConnectionProfile> __deletionAdapterOfConnectionProfile;

  private final EntityDeletionOrUpdateAdapter<Snippet> __deletionAdapterOfSnippet;

  public TerminalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfConnectionProfile = new EntityInsertionAdapter<ConnectionProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `connection_profiles` (`id`,`name`,`host`,`port`,`username`,`password`,`keyPath`,`group`,`forwardingRules`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConnectionProfile entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getHost() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getHost());
        }
        statement.bindLong(4, entity.getPort());
        if (entity.getUsername() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUsername());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPassword());
        }
        if (entity.getKeyPath() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getKeyPath());
        }
        if (entity.getGroup() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getGroup());
        }
        final String _tmp = __converters.fromList(entity.getForwardingRules());
        if (_tmp == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp);
        }
      }
    };
    this.__insertionAdapterOfSnippet = new EntityInsertionAdapter<Snippet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `snippets` (`id`,`name`,`command`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Snippet entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getCommand() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCommand());
        }
      }
    };
    this.__deletionAdapterOfConnectionProfile = new EntityDeletionOrUpdateAdapter<ConnectionProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `connection_profiles` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConnectionProfile entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfSnippet = new EntityDeletionOrUpdateAdapter<Snippet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `snippets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Snippet entity) {
        statement.bindLong(1, entity.getId());
      }
    };
  }

  @Override
  public Object insertProfile(final ConnectionProfile profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfConnectionProfile.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSnippet(final Snippet snippet, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSnippet.insert(snippet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProfile(final ConnectionProfile profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfConnectionProfile.handle(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSnippet(final Snippet snippet, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSnippet.handle(snippet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ConnectionProfile>> getAllProfiles() {
    final String _sql = "SELECT * FROM connection_profiles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"connection_profiles"}, new Callable<List<ConnectionProfile>>() {
      @Override
      @NonNull
      public List<ConnectionProfile> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfHost = CursorUtil.getColumnIndexOrThrow(_cursor, "host");
          final int _cursorIndexOfPort = CursorUtil.getColumnIndexOrThrow(_cursor, "port");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfKeyPath = CursorUtil.getColumnIndexOrThrow(_cursor, "keyPath");
          final int _cursorIndexOfGroup = CursorUtil.getColumnIndexOrThrow(_cursor, "group");
          final int _cursorIndexOfForwardingRules = CursorUtil.getColumnIndexOrThrow(_cursor, "forwardingRules");
          final List<ConnectionProfile> _result = new ArrayList<ConnectionProfile>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ConnectionProfile _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpHost;
            if (_cursor.isNull(_cursorIndexOfHost)) {
              _tmpHost = null;
            } else {
              _tmpHost = _cursor.getString(_cursorIndexOfHost);
            }
            final int _tmpPort;
            _tmpPort = _cursor.getInt(_cursorIndexOfPort);
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpKeyPath;
            if (_cursor.isNull(_cursorIndexOfKeyPath)) {
              _tmpKeyPath = null;
            } else {
              _tmpKeyPath = _cursor.getString(_cursorIndexOfKeyPath);
            }
            final String _tmpGroup;
            if (_cursor.isNull(_cursorIndexOfGroup)) {
              _tmpGroup = null;
            } else {
              _tmpGroup = _cursor.getString(_cursorIndexOfGroup);
            }
            final List<ForwardingRule> _tmpForwardingRules;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfForwardingRules)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfForwardingRules);
            }
            _tmpForwardingRules = __converters.fromString(_tmp);
            _item = new ConnectionProfile(_tmpId,_tmpName,_tmpHost,_tmpPort,_tmpUsername,_tmpPassword,_tmpKeyPath,_tmpGroup,_tmpForwardingRules);
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
  public Flow<List<Snippet>> getAllSnippets() {
    final String _sql = "SELECT * FROM snippets";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"snippets"}, new Callable<List<Snippet>>() {
      @Override
      @NonNull
      public List<Snippet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCommand = CursorUtil.getColumnIndexOrThrow(_cursor, "command");
          final List<Snippet> _result = new ArrayList<Snippet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Snippet _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpCommand;
            if (_cursor.isNull(_cursorIndexOfCommand)) {
              _tmpCommand = null;
            } else {
              _tmpCommand = _cursor.getString(_cursorIndexOfCommand);
            }
            _item = new Snippet(_tmpId,_tmpName,_tmpCommand);
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
