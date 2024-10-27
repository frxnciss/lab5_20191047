package com.example.lab5_20191047.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_20191047.Dto.Plato;
import com.example.lab5_20191047.R;

import java.util.List;

public class PlatoAdapter extends RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder> {

    private List<Plato> platoList;
    private Context context;

    public PlatoAdapter(Context context, List<Plato> platoList) {
        this.platoList = platoList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de item_food.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plato, parent, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        Plato plato = platoList.get(position);
        holder.tvNombreplato.setText(plato.getNombre());
        holder.tvCalorias.setText(plato.getKcal());
    }


    @Override
    public int getItemCount() {
        return platoList.size();
    }

    // Clase interna para el ViewHolder
    public static class PlatoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreplato;
        TextView tvCalorias;

        public PlatoViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNombreplato = itemView.findViewById(R.id.tvName);
            tvCalorias = itemView.findViewById(R.id.tvKcalCon);
        }
    }

}
