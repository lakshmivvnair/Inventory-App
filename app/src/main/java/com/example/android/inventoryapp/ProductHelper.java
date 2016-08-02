package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by lakshmivineeth on 7/5/16.
 */
public class ProductHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productsManager";
    private static final String TABLE_PRODUCTS = "products";
    // Products Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_SUPP_NAME = "supplierName";
    private static final String KEY_SUPP_EMAIL = "supplierEmail";
    private static final String KEY_IMAGE = "imageResourceID";

    private static ProductHelper sInstance;

    public ProductHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ProductHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ProductHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PRICE + " INTEGER, "
                + KEY_QUANTITY + " INTEGER, "
                + KEY_SUPP_NAME + " TEXT, "
                + KEY_SUPP_EMAIL + " TEXT, "
                + KEY_IMAGE + " TEXT " + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_QUANTITY, product.getQuantity());
        values.put(KEY_SUPP_NAME, product.getSupplierName());
        values.put(KEY_SUPP_EMAIL, product.getSupplierEmail());
        values.put(KEY_IMAGE, product.getImageResourceUri());

        // Inserting Row
        db.insert(TABLE_PRODUCTS, null, values);
        db.close(); // Closing database connection
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6));
                // Adding product to list
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_QUANTITY, product.getQuantity());
        values.put(KEY_SUPP_NAME, product.getSupplierName());
        values.put(KEY_SUPP_EMAIL, product.getSupplierEmail());
        values.put(KEY_IMAGE, product.getImageResourceUri());

        db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public int deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
        return result;
    }
}
