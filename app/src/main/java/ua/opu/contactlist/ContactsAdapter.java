package ua.opu.contactlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {

    public interface DeleteItemListener {
        void onDeleteItem(int position);
    }

    private final LayoutInflater inflater;
    private final List<Contact> list;
    private final DeleteItemListener listener;

    public ContactsAdapter(Context context, List<Contact> users) {
        this.inflater = LayoutInflater.from(context);
        this.list = users;
        this.listener = (DeleteItemListener) context;
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView email;
        TextView phone;

        ImageButton deleteButton;

        public ContactHolder (@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.contact_image);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);

            deleteButton = itemView.findViewById(R.id.clearButton);
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (list.size() == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_no_items, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact, parent, false);
        }

        return new ContactHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        if (list.size() == 0){
            return;
        }

        Contact contact = list.get(position);

        holder.image.setImageURI(Uri.parse(contact.getUri()));
        holder.name.setText(contact.getName());
        holder.email.setText(contact.getEmail());
        holder.phone.setText(contact.getPhone());

        holder.deleteButton.setOnClickListener(view -> {
            listener.onDeleteItem(contact.getId());
        });
    }

    @Override
    public int getItemCount() {
        return Math.max(list.size(), 1);
    }
}
