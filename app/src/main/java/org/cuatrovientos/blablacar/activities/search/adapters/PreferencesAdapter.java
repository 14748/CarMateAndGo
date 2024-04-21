package org.cuatrovientos.blablacar.activities.search.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;

import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.ViewHolder> {

    private List<String> preferencesList;

    public PreferencesAdapter(List<String> preferencesList) {
        this.preferencesList = preferencesList;
    }

    @NonNull
    @Override
    public PreferencesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preference, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferencesAdapter.ViewHolder holder, int position) {
        String preference = preferencesList.get(position);
        holder.preferenceTextView.setText("⭐ " + preference);
    }

    @Override
    public int getItemCount() {
        return preferencesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView preferenceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            preferenceTextView = itemView.findViewById(R.id.preference_text_view);
        }
    }
}
