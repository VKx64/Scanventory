package vkx64.android.scanventory.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vkx64.android.scanventory.R;
import vkx64.android.scanventory.database.AppClient;
import vkx64.android.scanventory.database.DaoItems;
import vkx64.android.scanventory.database.DaoMarkets;
import vkx64.android.scanventory.database.TableItems;
import vkx64.android.scanventory.database.TableMarkets;
import vkx64.android.scanventory.utilities.FileHelper;

public class ScannedItemsAdapter extends RecyclerView.Adapter<ScannedItemsAdapter.ViewHolder> {

    private final Context context;
    private final List<String> itemIds;
    private final Map<String, Integer> scannedItems;
    private String selectedMarketName;

    public ScannedItemsAdapter(Context context, Map<String, Integer> scannedItems, String selectedMarketName) {
        this.context = context;
        this.scannedItems = scannedItems;
        this.itemIds = new ArrayList<>(scannedItems.keySet());
        this.selectedMarketName = selectedMarketName; // Initialize the field
    }

    public void setSelectedMarketName(String selectedMarketName) {
        this.selectedMarketName = selectedMarketName;
        notifyDataSetChanged(); // Refresh the adapter
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemId = itemIds.get(position);
        int scannedQuantity = scannedItems.get(itemId);

        Log.d("ScannedItemsAdapter", "onBindViewHolder called for itemId: " + itemId + " with quantity: " + scannedQuantity);

        new Thread(() -> {
            DaoItems daoItems = AppClient.getInstance(context).getAppDatabase().daoItems();
            DaoMarkets daoMarkets = AppClient.getInstance(context).getAppDatabase().daoMarkets();

            // Fetch the item details
            TableItems item = daoItems.getItemById(itemId);

            if (item == null) {
                Log.e("ScannedItemsAdapter", "Item not found in database for itemId: " + itemId);
            }

            // Fetch the selling quantity for the selected marketplace
            TableMarkets marketEntry = null;
            if (selectedMarketName != null) {
                marketEntry = daoMarkets.getMarketByItemIdAndMarketName(itemId, selectedMarketName);
            }

            int sellingQuantity = marketEntry != null ? marketEntry.getMarket_quantity() : 0;

            // Calculate remaining quantity dynamically
            int remainingQuantity = sellingQuantity - scannedQuantity;

            // Update the UI on the main thread
            holder.itemView.post(() -> {
                if (item != null) {
                    holder.tvProductName.setText(item.getItem_name());
                    holder.tvQuantity.setText("x" + scannedQuantity);

                    // Update Selling Quantity
                    holder.tvSellingCount.setText("Selling Quantity: " + Math.max(remainingQuantity, 0)); // Ensure non-negative values

                    Log.d("ScannedItemsAdapter", "Setting item details for itemId: " + itemId);
                    loadItemImage(holder.ivProductImage, itemId);
                } else {
                    holder.tvProductName.setText("Unknown Item");
                    holder.tvSellingCount.setText("Selling Quantity: 0");
                    holder.tvQuantity.setText("x" + scannedQuantity);

                    Log.w("ScannedItemsAdapter", "Setting placeholder for unknown item with itemId: " + itemId);
                    holder.ivProductImage.setImageResource(R.drawable.im_placeholder);
                }
            });
        }).start();
    }

    @Override
    public int getItemCount() {
        return itemIds.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvSellingCount, tvQuantity;
        ImageView ivProductImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSellingCount = itemView.findViewById(R.id.tvSellingCount);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }

    private void loadItemImage(ImageView imageView, String itemId) {
        // Log the initial input
        Log.d("ScannedItemsAdapter", "loadItemImage called with itemId: " + itemId);

        if (imageView == null) {
            Log.e("ScannedItemsAdapter", "ImageView is null. Cannot load image.");
            return;
        }

        if (itemId == null || itemId.isEmpty()) {
            Log.e("ScannedItemsAdapter", "Item ID is null or empty. Setting placeholder.");
            imageView.setImageResource(R.drawable.im_placeholder);
            return;
        }

        // Fetch image paths using FileHelper
        List<String> imagePaths = FileHelper.getImagesForItem(context, itemId);
        if (imagePaths == null || imagePaths.isEmpty()) {
            Log.w("ScannedItemsAdapter", "No images found for itemId: " + itemId);
            imageView.setImageResource(R.drawable.im_placeholder);
            return;
        }

        File imageFile = new File(imagePaths.get(0));
        Log.d("ScannedItemsAdapter", "Attempting to load image: " + imageFile.getAbsolutePath());

        if (imageFile.exists()) {
            Glide.with(context)
                    .load(imageFile)
                    .placeholder(R.drawable.im_placeholder)
                    .error(R.drawable.im_placeholder)
                    .into(imageView);
            Log.d("ScannedItemsAdapter", "Image loaded successfully: " + imageFile.getAbsolutePath());
        } else {
            Log.e("ScannedItemsAdapter", "Image file does not exist: " + imageFile.getAbsolutePath());
            imageView.setImageResource(R.drawable.im_placeholder);
        }
    }

    public void refreshData() {
        itemIds.clear();
        itemIds.addAll(scannedItems.keySet());
        notifyDataSetChanged();
    }
}
