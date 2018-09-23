package sgtmelon.livewallpaper.app.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.model.ItemInfo;
import sgtmelon.livewallpaper.databinding.ItemInfoBinding;

public class AdpInfo extends RecyclerView.Adapter<AdpInfo.HolderInfo> {

    private final List<ItemInfo> listInfo = new ArrayList<>();

    public void update(List<ItemInfo> listInfo) {
        this.listInfo.clear();
        this.listInfo.addAll(listInfo);
    }

    @NonNull
    @Override
    public HolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_info, parent, false);
        return new HolderInfo(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderInfo holder, int position) {
        ItemInfo itemInfo = listInfo.get(position);
        holder.bind(itemInfo);
    }

    @Override
    public int getItemCount() {
        return listInfo.size();
    }

    class HolderInfo extends RecyclerView.ViewHolder {

        private final ItemInfoBinding binding;

        HolderInfo(ItemInfoBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(ItemInfo itemInfo) {
            binding.setItemInfo(itemInfo);
            binding.executePendingBindings();
        }

    }
}
