package com.example.android.inventoryapp;

/**
 * Created by lakshmivineeth on 7/5/16.
 */
public class Product {

    private int mID;
    private String mName;
    private int mQuantity;
    private int mPrice;
    private String mSupplierName;
    private String mSupplierEmail;
    private String mImageResourceUri;

    public Product() {

    }

    public Product(int id, String name, int price, int quantity, String suppName, String suppContact, String imageResourceID) {
        setId(id);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setSupplierName(suppName);
        setSupplierEmail(suppContact);
        setImageResourceUri(imageResourceID);
    }

    public int getId() {
        return mID;
    }

    private void setId(int id) {
        this.mID = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

    public String getSupplierName() {
        return mSupplierName;
    }

    public void setSupplierName(String supplierName) {
        this.mSupplierName = supplierName;
    }

    public String getSupplierEmail() {
        return mSupplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.mSupplierEmail = supplierEmail;
    }

    public String getImageResourceUri() {
        return mImageResourceUri;
    }

    public void setImageResourceUri(String imageResourceID) {
        this.mImageResourceUri = imageResourceID;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "mID=" + mID +
                ", mName='" + mName + '\'' +
                ", mQuantity=" + mQuantity +
                ", mPrice=" + mPrice +
                ", mSupplierName='" + mSupplierName + '\'' +
                ", mSupplierEmail='" + mSupplierEmail + '\'' +
                ", mImageResourceUri=" + mImageResourceUri +
                '}';
    }
}
