package com.example.android.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static Product mProduct;
    private TextView tvPrice;
    private TextView tvQuantity;
    private EditText etQuantity;
    private EditText etPrice;
    private ImageView mProductImage;
    private ProductHelper mProductHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activty);

        mProductHelper = ProductHelper.getInstance(this);

        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvQuantity = (TextView) findViewById(R.id.tvQuantity);
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        etPrice = (EditText) findViewById(R.id.etPrice);
        Button bAddQuantity = (Button) findViewById(R.id.bAddQuantity);
        Button bReduceQuantity = (Button) findViewById(R.id.bReduceQuantity);
        Button bUpdatePrice = (Button) findViewById(R.id.bUpdatePrice);
        Button bOrderMore = (Button) findViewById(R.id.bOrderMore);
        Button bTrackSale = (Button) findViewById(R.id.bTrackSale);
        Button bDeleteProduct = (Button) findViewById(R.id.bDeleteProduct);
        mProductImage = (ImageView) findViewById(R.id.productImage);

        tvName.setText(mProduct.getName());
        tvPrice.setText(String.valueOf(mProduct.getPrice()));
        tvQuantity.setText(String.valueOf(mProduct.getQuantity()));
        etQuantity.setText(String.valueOf(mProduct.getQuantity()));
        etPrice.setText(String.valueOf(mProduct.getPrice()));

        loadImage();

        bDeleteProduct.setOnClickListener(this);
        bAddQuantity.setOnClickListener(this);
        bReduceQuantity.setOnClickListener(this);
        bUpdatePrice.setOnClickListener(this);
        bOrderMore.setOnClickListener(this);
        bTrackSale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bDeleteProduct:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.confirm_deletion);
                builder.setMessage(getString(R.string.delete_confirmation) + mProduct.getName() + getString(R.string.question_mark));
                builder.setNegativeButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.delete_product, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mProductHelper.deleteProduct(mProduct) != 0) {
                            Toast.makeText(DetailsActivity.this, R.string.delete_success, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(DetailsActivity.this, R.string.delete_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.bAddQuantity:
                updateQuantity(Integer.parseInt(etQuantity.getText().toString()));
                break;
            case R.id.bReduceQuantity:
                if (mProduct.getQuantity() == 0) {
                    Toast.makeText(DetailsActivity.this, R.string.no_stock, Toast.LENGTH_SHORT).show();
                } else {
                    updateQuantity(-Integer.parseInt(etQuantity.getText().toString()));
                }
                break;
            case R.id.bTrackSale:
                updateQuantity(-1);
                break;
            case R.id.bUpdatePrice:
                int price = Integer.parseInt(etPrice.getText().toString());
                if (price <= 0) {
                    etPrice.setError(getString(R.string.validation_number));
                } else {
                    mProduct.setPrice(price);
                    mProductHelper.updateProduct(mProduct);
                    tvPrice.setText(String.valueOf(mProduct.getPrice()));
                }
                break;
            case R.id.bOrderMore:
                sendEmail();
                break;
        }
    }

    private void updateQuantity(int change) {
        int value = mProduct.getQuantity();
        value = value + change;
        if (value <= 0) {
            value = 0;
        }
        mProduct.setQuantity(value);
        mProductHelper.updateProduct(mProduct);
        tvQuantity.setText(String.valueOf(mProduct.getQuantity()));
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: " + mProduct.getSupplierEmail()));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.purchase_order) + mProduct.getName());
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shipment_request) + mProduct.getName());

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.purchase_order)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DetailsActivity.this, R.string.no_apps_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage() {
        Bitmap selectedImage = null;
        String temp = mProduct.getImageResourceUri();

        if (temp != null) {
            Uri photoUri = Uri.parse(temp);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            selectedImage = BitmapFactory.decodeFile(temp, options);
            if (selectedImage == null) {
                selectedImage = BitmapFactory.decodeFile(CommonHelper.getRealPathFromUri(this, photoUri), options);
            }
            mProductImage.setImageBitmap(selectedImage);
        } else {
            mProductImage.setImageResource(R.drawable.info);
        }
    }
}
