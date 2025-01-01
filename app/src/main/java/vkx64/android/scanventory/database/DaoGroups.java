package vkx64.android.scanventory.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoGroups {

    @Query("SELECT * FROM groups")
    List<TableGroups> getAllGroups();

    @Insert
    void insertGroup(TableGroups group);
}
