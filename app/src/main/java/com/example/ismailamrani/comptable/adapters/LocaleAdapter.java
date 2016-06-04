package com.example.ismailamrani.comptable.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Local;

import java.util.List;

/**
 * Created by Mohammed Aouf ZOUAG on 29/05/2016.
 */
public class LocaleAdapter extends ArrayAdapter<Local> {

    private Context context;
    private LocaleClickListener listener;
    private List<Local> locales;

    public LocaleAdapter(Context context, List<Local> locales) {
        super(context, -1, locales);
        this.context = context;
        this.locales = locales;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.local_row, parent, false);
            // Setup the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.localeText = (TextView) convertView.findViewById(R.id.localText);
            // store the holder with the view.
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Local local = locales.get(position);
        viewHolder.localeText.setText(local.getFullAddress());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onLocaleSelected(local.getId());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView localeText;
    }

    public void setListener(LocaleClickListener listener) {
        this.listener = listener;
    }

    public interface LocaleClickListener {
        void onLocaleSelected(int localeID);
    }
}