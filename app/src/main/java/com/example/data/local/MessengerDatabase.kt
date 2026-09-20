package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ConversationEntity::class, MessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MessengerDatabase : RoomDatabase() {
    abstract fun messengerDao(): MessengerDao

    companion object {
        @Volatile
        private var INSTANCE: MessengerDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MessengerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessengerDatabase::class.java,
                    "all_messenger_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDatabase(database.messengerDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        if (database.messengerDao().getConversationCount() == 0) {
                            populateInitialDatabase(database.messengerDao())
                        }
                    }
                }
            }

            private suspend fun populateInitialDatabase(dao: MessengerDao) {
                dao.insertConversations(InitialData.getInitialConversations())
                dao.insertMessages(InitialData.getInitialMessages())
            }
        }
    }
}
