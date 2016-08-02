package com.example.android.inventoryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lakshmivineeth on 7/6/16.
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    private ProductHelper mProductHelper;

    public ProductAdapter(Context context, ArrayList<Product> objects) {
        super(context, 0, objects);
        mProductHelper = ProductHelper.getInstance(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Product product = getItem(position);

        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.product_list_item, parent, false);
        }
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.mProduct = product;
                ((MainActivity) getContext()).goToDetails();
            }
        });

        TextView tvName = (TextView) listView.findViewById(R.id.tvName);
        TextView tvPrice = (TextView) listView.findViewById(R.id.tvPrice);
        final TextView tvQuantity = (TextView) listView.findViewById(R.id.tvQuantity);
        Button bTrackSale = (Button) listView.findViewById(R.id.bTrackSale);

        tvName.setText(product.getName());
        tvPrice.setText(String.valueOf(product.getPrice()));
        tvQuantity.setText(String.valueOf(product.getQuantity()));

        Bitmap selectedImage = null;
        String temp = product.getImageResourceUri();
        ImageView productImage = (ImageView) listView.findViewById(R.id.productImage);

        if (temp != null) {
            Uri photoUri = Uri.parse(temp);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            selectedImage = BitmapFactory.decodeFile(CommonHelper.getRealPathFromUri(getContext(), photoUri), options);
            productImage.setImageBitmap(selectedImage);
        } else {
            productImage.setImageResource(R.drawable.info);
        }

        bTrackSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = product.getQuantity();
                if (quantity > 1) {
                    quantity--;
                } else {
                    quantity = 0;
                }
                product.setQuantity(quantity);
                tvQuantity.setText(String.valueOf(product.getQuantity()));
                mProductHelper.updateProduct(product);
            }
        });

        return listView;
    }
}
