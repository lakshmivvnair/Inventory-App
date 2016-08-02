package com.example.android.inventoryapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AddNewProductActivity extends AppCompatActivity {

    // PICK_PHOTO_CODE is a constant integer
    private final static int PICK_PHOTO_CODE = 1046;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mAddImage;
    Activity mContext;
    private EditText mEtName;
    private EditText mEtPrice;
    private EditText mEtQuantity;
    private EditText mEtSupplierName;
    private EditText mEtSupplierContact;
    private ProductHelper mProductHelper;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        mProductHelper = ProductHelper.getInstance(this);
        mContext = AddNewProductActivity.this;

        mEtName = (EditText) findViewById(R.id.etName);
        mEtPrice = (EditText) findViewById(R.id.etPrice);
        mEtQuantity = (EditText) findViewById(R.id.etQuantity);
        mEtSupplierName = (EditText) findViewById(R.id.etSupplierName);
        mEtSupplierContact = (EditText) findViewById(R.id.etSupplierContact);

        TextView tvTakePhoto = (TextView) findViewById(R.id.takePhoto);
        TextView tvOpenGallery = (TextView) findViewById(R.id.openGallery);
        tvOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.permissionGranted) {
                    onPickPhoto();
                } else {
                    CommonHelper.checkPermission(mContext);
                }
            }
        });
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.permissionGranted) {
                    takePhoto();
                } else {
                    CommonHelper.checkPermission(mContext);
                }
            }
        });

        mAddImage = (ImageView) findViewById(R.id.addImage);
        invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem addProduct = menu.findItem(R.id.add_new_product);
        addProduct.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_product:
                addProductToDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri photoUri = data.getData();
            imageUri = photoUri.toString();
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView productImage = (ImageView) findViewById(R.id.addImage);
            productImage.setImageBitmap(selectedImage);
        }
    }

    private void addProductToDB() {
        if (validate()) {
            Product product = new Product();
            product.setName(mEtName.getText().toString());
            product.setPrice(Integer.parseInt(mEtPrice.getText().toString()));
            product.setQuantity(Integer.parseInt(mEtQuantity.getText().toString()));
            product.setSupplierName(mEtSupplierName.getText().toString());
            product.setSupplierEmail(mEtSupplierContact.getText().toString());
            product.setImageResourceUri(imageUri);

            mProductHelper.addProduct(product);
            finish();
        }
    }

    private boolean validate() {
        boolean isError = true;
        if (TextUtils.isEmpty(mEtName.getText())) {
            mEtName.setError(getString(R.string.validation));
            isError = false;
        }
        if (TextUtils.isEmpty(mEtPrice.getText())) {
            mEtPrice.setError(getString(R.string.validation));
            isError = false;
        } else if (notZero(mEtPrice.getText().toString())) {
            mEtPrice.setError(getString(R.string.validation_number));
            isError = false;
        }
        if (TextUtils.isEmpty(mEtQuantity.getText())) {
            mEtQuantity.setError(getString(R.string.validation));
            isError = false;
        } else if (notZero(mEtQuantity.getText().toString())) {
            mEtQuantity.setError(getString(R.string.validation_number));
            isError = false;
        }
        if (TextUtils.isEmpty(mEtSupplierName.getText().toString())) {
            mEtSupplierName.setError(getString(R.string.validation));
            isError = false;
        }
        if (TextUtils.isEmpty(mEtSupplierContact.getText().toString())) {
            mEtSupplierContact.setError(getString(R.string.validation));
            isError = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEtSupplierContact.getText().toString()).matches()) {
            mEtSupplierContact.setError(getString(R.string.valid_email_message));
            isError = false;
        }
        return isError;
    }

    private boolean notZero(String number) {
        boolean check;
        int temp = Integer.parseInt(number);
        check = temp == 0 ? true : false;
        return check;
    }

    private void onPickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        } else {
            Toast.makeText(this, R.string.no_apps_found, Toast.LENGTH_LONG).show();
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, R.string.no_apps_found, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CommonHelper.PERMISSION_CODE:
                MainActivity.permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!MainActivity.permissionGranted) {
                    CommonHelper.takeToSettings(this);
                }
        }
    }
}
