package com.example.android.inventoryapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final static int PERMISSION_CODE = 123;
    private final static int REQUEST_PERMISSION_SETTING = 456;
    public static boolean permissionGranted;
    private ArrayList<Product> products;
    private ProductHelper productHelper;
    private ProductAdapter itemsAdapter;
    private ListView listview;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvListEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productHelper = ProductHelper.getInstance(this);

        tvListEmpty = (TextView) findViewById(R.id.tvListEmpty);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListView();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        products = productHelper.getAllProducts();
        itemsAdapter = new ProductAdapter(this, products);
        listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(itemsAdapter);

        isListEmptyShowMessage();
        invalidateOptionsMenu();

        CommonHelper.checkPermission(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem saveProduct = menu.findItem(R.id.save_product);
        saveProduct.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_product:
                startActivity(new Intent(MainActivity.this, AddNewProductActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    private void refreshListView() {
        products.clear();
        products.addAll(productHelper.getAllProducts());
        itemsAdapter.notifyDataSetChanged();
        isListEmptyShowMessage();
    }

    public void goToDetails() {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class));
    }

    private void isListEmptyShowMessage() {
        if (listview.getCount() == 0) {
            tvListEmpty.setVisibility(View.VISIBLE);
        } else {
            tvListEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!permissionGranted) {
                    CommonHelper.takeToSettings(this);
                }
                break;
        }
    }
}
