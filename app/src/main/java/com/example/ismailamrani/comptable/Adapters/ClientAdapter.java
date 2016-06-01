package com.example.ismailamrani.comptable.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ismailamrani.comptable.R;
import com.example.ismailamrani.comptable.models.Client;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mohammed Aouf ZOUAG on 01/06/2016.
 */
public class ClientAdapter extends BaseSearchAdapter<ClientAdapter.ViewHolder, Client> {

    private ClientListener listener;

    public ClientAdapter(Context context, List<Client> clients) {
        super(context, clients);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.supplier_client_grid_item, viewGroup, false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends BinderViewHolder<Client> {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.thumbnail)
        ImageView thumbnail;
        @Bind(R.id.overflow)
        ImageView overflow;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            View.OnClickListener clickListener = view -> {
                if (listener != null)
                    listener.onClientSelected(mItems.get(getAdapterPosition()));
            };

            overflow.setOnClickListener(this::showPopupMenu);
            v.setOnClickListener(clickListener);
            thumbnail.setOnClickListener(clickListener);
        }

        @Override
        public void bind(Client client) {
            title.setText(client.getNomPrenom());
            address.setText(client.getAdresse());
            thumbnail.setImageResource(R.mipmap.ic_client);
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(mContext, view);
            popup.inflate(R.menu.grid_popup_menu);
            popup.setOnMenuItemClickListener(
                    new MyMenuItemClickListener(mItems.get(getAdapterPosition())));
            popup.show();
        }

        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            private Client client;

            public MyMenuItemClickListener(Client client) {
                this.client = client;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_edit_supplier:
                        if (listener != null)
                            listener.onEditClient(client);
                        return true;
                    case R.id.action_delete_supplier:
                        if (listener != null)
                            listener.onDeleteClient(client.getId());
                        return true;
                    default:
                }
                return false;
            }
        }
    }

    public void setClientListener(ClientListener listener) {
        this.listener = listener;
    }

    public interface ClientListener {
        void onClientSelected(Client client);
        void onEditClient(Client client);
        void onDeleteClient(String clientID);
    }
}
