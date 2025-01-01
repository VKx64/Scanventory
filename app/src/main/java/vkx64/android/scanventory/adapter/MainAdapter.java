package vkx64.android.scanventory.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vkx64.android.scanventory.MainActivity;
import vkx64.android.scanventory.R;
import vkx64.android.scanventory.database.TableGroups;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{
    private final List<TableGroups> groups;

    public MainAdapter(List<TableGroups> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout for Each Item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new MainViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        // Bind the data for current item
        TableGroups group = groups.get(position);
        holder.tvGroupName.setText(group.getGroup_name());
        holder.ivGroupImage.setImageResource(R.drawable.ic_folder);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        ImageView ivGroupImage;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            ivGroupImage = itemView.findViewById(R.id.ivGroupImage);
        }
    }
}
