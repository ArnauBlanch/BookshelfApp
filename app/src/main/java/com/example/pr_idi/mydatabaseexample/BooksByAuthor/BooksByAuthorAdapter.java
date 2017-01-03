package com.example.pr_idi.mydatabaseexample.BooksByAuthor;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pr_idi.mydatabaseexample.Book;
import com.example.pr_idi.mydatabaseexample.EditPersonalEvaluation.PersEvalDialogFragment;
import com.example.pr_idi.mydatabaseexample.R;

import java.util.List;

/**
 * Created by ivan on 02/01/2017.
 */

public class BooksByAuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemList> mItems;

    public BooksByAuthorAdapter(List<ItemList> mItems) {
        this.mItems = mItems;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_header);
        }

        public void setText(String text) { this.text.setText(text); }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
                                        implements View.OnClickListener {

        private TextView title, category, year;
        private Long id;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.card_book_title);
            category = (TextView) itemView.findViewById(R.id.card_book_category);
            year = (TextView) itemView.findViewById(R.id.card_book_year);
        }

        public void setTitle(String text) { this.title.setText(text); }
        public void setCategory(String text) { this.category.setText(text); }
        public void setYear(String text) { this.year.setText(text); }
        public void setId(Long id) { this.id = id; }

        @Override
        public void onClick(View view) {
            ((AppCompatActivity)view.getContext()).getSupportFragmentManager();

            FragmentTransaction transaction = ((AppCompatActivity)view.getContext()).
                    getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = PersEvalDialogFragment.newInstance(id);
            newFragment.show(transaction, "dialog");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ItemList.TYPE_HEADER) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_item_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == ItemList.TYPE_TOP) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_item_top, parent, false);
        } else if (viewType == ItemList.TYPE_MIDDLE) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_item_middle, parent, false);
        } else if (viewType == ItemList.TYPE_BOTTOM){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_item_bottom, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_item_alone, parent, false);
        }
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ItemList.TYPE_HEADER) {
            ItemHeader header = (ItemHeader) mItems.get(position);
            ((HeaderViewHolder)holder).setText(header.getText());
        } else if (type == ItemList.TYPE_TOP) {
            ItemTop item = (ItemTop) mItems.get(position);
            setViewHolderAttr((ItemViewHolder)holder, item.getBook());
        } else if (type == ItemList.TYPE_MIDDLE) {
            ItemMiddle item = (ItemMiddle) mItems.get(position);
            setViewHolderAttr((ItemViewHolder)holder, item.getBook());
        } else if (type == ItemList.TYPE_BOTTOM){
            ItemBottom item = (ItemBottom) mItems.get(position);
            setViewHolderAttr((ItemViewHolder)holder, item.getBook());
        } else {
            ItemAlone item = (ItemAlone) mItems.get(position);
            setViewHolderAttr((ItemViewHolder)holder, item.getBook());
        }
    }

    private void setViewHolderAttr(ItemViewHolder holder, Book b) {
        holder.setTitle(b.getTitle());
        holder.setCategory(b.getCategory());
        holder.setYear(String.valueOf(b.getYear()));
        holder.setId(b.getId());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }
}
