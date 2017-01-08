package com.mingo_blanch.pr_idi.bookshelf_app.BooksByAuthor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mingo_blanch.pr_idi.bookshelf_app.BookDatabase.Book;
import com.mingo_blanch.pr_idi.bookshelf_app.OptionsAlertDialog.OptionsAlertDialog;
import com.mingo_blanch.pr_idi.bookshelf_app.R;

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
            text = (TextView) itemView.findViewById(R.id.category_header);
        }

        public void setText(String text) { this.text.setText(text); }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
                                        implements View.OnLongClickListener {

        private TextView title, category, year;
        private Long id;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            title = (TextView) itemView.findViewById(R.id.book_title);
            category = (TextView) itemView.findViewById(R.id.book_subtitle);
            year = (TextView) itemView.findViewById(R.id.book_year);
        }

        public void setTitle(String text) { this.title.setText(text); }
        public void setCategory(String text) { this.category.setText(text); }
        public void setYear(String text) { this.year.setText(text); }
        public void setId(Long id) { this.id = id; }

        @Override
        public boolean onLongClick(View view) {
            OptionsAlertDialog dialog = new OptionsAlertDialog(view.getContext(), id);
            dialog.showBuilder();
            return true;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ItemList.TYPE_HEADER) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category, parent, false);
            return new HeaderViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_rv_item, parent, false);
        }
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == ItemList.TYPE_HEADER) {
            ItemHeader header = (ItemHeader) mItems.get(position);
            ((HeaderViewHolder)holder).setText(header.getText());
        } else {
            ItemBook item = (ItemBook) mItems.get(position);
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
