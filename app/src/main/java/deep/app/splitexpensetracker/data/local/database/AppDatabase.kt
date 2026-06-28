package deep.app.splitexpensetracker.data.local.database

import android.content.Context
import androidx.room.*
import deep.app.splitexpensetracker.data.local.dao.*
import deep.app.splitexpensetracker.data.local.entity.*

@Database(
    entities = [
        GroupEntity::class,
        MemberEntity::class,
        ExpenseEntity::class,
        ExpenseParticipantEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupDao(): GroupDao

    abstract fun memberDao(): MemberDao

    abstract fun expenseDao(): ExpenseDao

    abstract fun expenseParticipantDao():
            ExpenseParticipantDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "split_expense_db"
                    ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}