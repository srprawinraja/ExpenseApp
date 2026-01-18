import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.expenseapp.db.record.RecordDao
import com.example.expenseapp.db.record.RecordEntity

@Database(entities = [RecordEntity::class], version = 1, exportSchema = false)
abstract class RecordDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}