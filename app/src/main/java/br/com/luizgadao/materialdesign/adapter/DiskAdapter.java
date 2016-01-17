package br.com.luizgadao.materialdesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.luizgadao.materialdesign.LoadDisk;
import br.com.luizgadao.materialdesign.R;
import br.com.luizgadao.materialdesign.model.Disk;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luiz on 05/01/16.
 */
public class DiskAdapter extends RecyclerView.Adapter<DiskAdapter.ViewHolder> {

    private Disk[] mDisks;
    private OnClickDiskListener mListener;

    public DiskAdapter(Disk[] mDisks) {
        this.mDisks = mDisks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disk, parent, false);
        ViewHolder viewHolder = new ViewHolder(item);

        item.setTag(viewHolder);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    ViewHolder vh = (ViewHolder) v.getTag();
                    int position = vh.getAdapterPosition();
                    mListener.onClick(v, position, mDisks[position]);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Disk disk = mDisks[position];
        Picasso.with(holder.itemView
                .getContext())
                .load(LoadDisk.URL + disk.capa)
                .into(holder.imgCapa);
        holder.txtTitle.setText(disk.titulo);
        holder.txtYear.setText(disk.ano + "");
    }

    @Override
    public int getItemCount() {
        return mDisks == null? 0 : mDisks.length;
    }

    public void setOnItemClickListener(OnClickDiskListener mListener) {
        this.mListener = mListener;
    }

    public interface OnClickDiskListener{
        void onClick(View view, int position, Disk disk);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.imgDisk)
        ImageView imgCapa;
        @Bind(R.id.txtTitle)
        TextView txtTitle;
        @Bind(R.id.txtYear)
        TextView txtYear;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
