package vkx64.android.scanventory;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import vkx64.android.scanventory.adapter.MainAdapter;
import vkx64.android.scanventory.database.AppClient;
import vkx64.android.scanventory.database.TableGroups;
import vkx64.android.scanventory.dialog.AddGroupDialogFragment;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvItemList;
    private SwipeRefreshLayout srItemList;
    private ImageButton ibNewFolder;
    private MainAdapter mainAdapter;

    private String TAG = "MainActivity";

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadViews();
        loadGroups();
    }

    private void loadViews() {
        //* Initialize Views *//
        rvItemList = findViewById(R.id.rvItemList);
        rvItemList.setLayoutManager(new LinearLayoutManager(this));

        srItemList = findViewById(R.id.srItemList);
        srItemList.setOnRefreshListener(this::loadGroups);

        ibNewFolder = findViewById(R.id.ibNewFolder);
        ibNewFolder.setOnClickListener(v -> showAddGroupDialog());
    }

    private void loadGroups() {
        //* Load Data in Recycler View *//
        executor.execute(() -> {
            List<TableGroups> groups = AppClient
                    .getInstance(getApplicationContext())
                    .getAppDatabase()
                    .daoGroups()
                    .getAllGroups();

            runOnUiThread(() -> {
                mainAdapter = new MainAdapter(groups);
                rvItemList.setAdapter(mainAdapter);
                srItemList.setRefreshing(false);
            });
        });
    }

    private void showAddGroupDialog() {
        //* Show Add Group Dialog *//
        AddGroupDialogFragment dialog = new AddGroupDialogFragment((groupId, groupName) -> {
            Toast.makeText(this, "Group Created: " + groupId + " - " + groupName, Toast.LENGTH_SHORT).show();
        });

        dialog.show(getSupportFragmentManager(), "AddGroupDialog");
    }

    private void addNewGroup(TableGroups newGroup) {
        //* Add New Group *//
        executor.execute(() -> {
            AppClient.getInstance(getApplicationContext())
                    .getAppDatabase()
                    .daoGroups()
                    .insertGroup(newGroup);
            runOnUiThread(() -> {
                Log.d(TAG, "New Group Added");
                loadGroups();
            });
        });
    }
}