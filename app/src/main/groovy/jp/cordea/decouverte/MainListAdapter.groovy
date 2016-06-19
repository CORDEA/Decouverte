package jp.cordea.decouverte

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnUIThread
import com.squareup.picasso.Picasso
import jp.cordea.decouverte.response.Image

class MainListAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context
    private List<Image> items = []
    private List<HolderType> types = []

    @OnUIThread
    def insertItems(List<Image> images) {
        def bef = items.size()
        items.addAll(images)
        def standardTypes = 0
        for (image in images) {
            def isFullSpan = new Random().nextInt(5) == 0
            if (standardTypes != 2 && standardTypes != 0) {
                isFullSpan = false
            }
            standardTypes = isFullSpan ? 0 : standardTypes >= 2 ? 1 : standardTypes + 1
            types.add(isFullSpan ? HolderType.FULL_SPAN : standardTypes == 1 ? HolderType.LEFT : HolderType.RIGHT)
        }
        notifyItemRangeInserted(bef - 1, images.size())
    }

    MainListAdapter(context) {
        this.context = context
    }

    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_main, parent, false), HolderType.values()[viewType])
    }

    @Override
    void onBindViewHolder(ViewHolder holder, int position) {
        def item = items[position]

        def lp = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        lp.fullSpan = holder.type == HolderType.FULL_SPAN
        def d = context.resources.getDimension(R.dimen.list_item_main_margin)
        if (holder.type == HolderType.RIGHT) {
            lp.marginStart = d / 2
        }
        if (holder.type == HolderType.LEFT) {
            lp.marginEnd = d / 2
        }
        if (position == items.size() - 1) {
            lp.bottomMargin = d
        }
        holder.imageView.aspect = holder.type == HolderType.FULL_SPAN ? 9f / 16f : 1f
        Picasso.with(context).load(item.thumbnail.mediaUrl).into(holder.imageView)
        holder.openButton.setOnClickListener({
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.sourceUrl)))
        })

    }

    @Override
    int getItemViewType(int position) {
        return types[position].ordinal()
    }

    @Override
    int getItemCount() {
        return items.size
    }

    private enum HolderType {
        FULL_SPAN,
        RIGHT,
        LEFT
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.image_view)
        AspectFitImageView imageView

        @InjectView(R.id.favorite_button)
        ImageButton favoriteButton

        @InjectView(R.id.open_button)
        ImageButton openButton

        HolderType type

        ViewHolder(view, type) {
            super(view)
            SwissKnife.inject(this, view)
            this.type = type
        }
    }

}