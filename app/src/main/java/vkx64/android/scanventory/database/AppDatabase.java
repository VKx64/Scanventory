package vkx64.android.scanventory.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {
                TableGroups.class,
                TableItems.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoGroups daoGroups();
    public abstract DaoItems daoItems();
}
