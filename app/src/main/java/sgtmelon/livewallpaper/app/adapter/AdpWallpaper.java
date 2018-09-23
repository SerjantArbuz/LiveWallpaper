package sgtmelon.livewallpaper.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.livewallpaper.R;
import sgtmelon.livewallpaper.app.model.ItemWallpaper;
import sgtmelon.livewallpaper.office.intf.IntfItem;
import sgtmelon.livewallpaper.office.st.StMode;

public class AdpWallpaper extends RecyclerView.Adapter<AdpWallpaper.HolderWallpaper> {

    private final List<ItemWallpaper> listWallpaper = new ArrayList<>();
    private StMode stMode;

    public StMode getStMode() {
        return stMode;
    }

    public void setStMode(StMode stMode) {
        this.stMode = stMode;

        int size = getItemCount();
        if (stMode.getSelect().length != size) {
            boolean[] select = new boolean[size];
            Arrays.fill(select, false);
            stMode.setSelect(select);
        }
    }

    public void update(List<ItemWallpaper> listWallpaper) {
        this.listWallpaper.clear();
        this.listWallpaper.addAll(listWallpaper);
    }

    private IntfItem.Click click;
    private IntfItem.LongClick longClick;

    public void setClick(IntfItem.Click click) {
        this.click = click;
    }

    public void setLongClick(IntfItem.LongClick longClick) {
        this.longClick = longClick;
    }

    @NonNull
    @Override
    public HolderWallpaper onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_wallpaper, parent, false);

        return new HolderWallpaper(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderWallpaper holder, int position) {
        ItemWallpaper itemWallpaper = listWallpaper.get(position);

        Picasso.get()
                .load(itemWallpaper.getUri())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_place_holder)
                .fit()
                .into(holder.image);

        holder.name.setText(itemWallpaper.getName());

        if (stMode.isActive()) {
            holder.check.setChecked(stMode.getSelect(position));
            holder.check.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listWallpaper.size();
    }

    class HolderWallpaper extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final View view;

        private final ImageView image;
        private final TextView name;
        private final CheckBox check;

        HolderWallpaper(@NonNull View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.itemWallpaper_click);

            image = itemView.findViewById(R.id.itemWallpaper_image);
            name = itemView.findViewById(R.id.itemWallpaper_name);
            check = itemView.findViewById(R.id.itemWallpaper_check);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            click.onItemClick(position);

            if (stMode.isActive()) {
                check.setChecked(!check.isChecked());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            longClick.onItemLongClick(position);
            return true;
        }

    }
}
