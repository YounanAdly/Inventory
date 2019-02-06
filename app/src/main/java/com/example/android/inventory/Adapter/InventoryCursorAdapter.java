package com.example.android.inventory.Adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.CatalogActivity;
import com.example.android.inventory.R;
import com.example.android.inventory.data.InventoryContract;
import com.example.android.inventory.data.InventoryContract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    public final CatalogActivity mActivity;

    public InventoryCursorAdapter(Context context, Cursor c, CatalogActivity mActivity) {
        super(context, c, 0 /* flags */);
        this.mActivity = mActivity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final int id = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));


        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView quantityText = (TextView) view.findViewById(R.id.quantity);
        TextView price = (TextView) view.findViewById(R.id.price_view);
        ImageView sale = (ImageView) view.findViewById(R.id.image_sale);
        ImageView image = (ImageView) view.findViewById(R.id.image);

        int nameIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
        int quantityIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int priceIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        int imageIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_IMAGE);


        String name = cursor.getString(nameIndex);
        final int quantity = cursor.getInt(quantityIndex);
        String priceitem = cursor.getString(priceIndex);
        String imagev = cursor.getString(imageIndex);

        final Uri imageUri = Uri.parse(imagev);


        productName.setText(name);
        quantityText.setText(String.valueOf(quantity));
        price.setText(priceitem);
        image.setImageURI(imageUri);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.clickOnViewItem(id);
            }
        });


        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI, id);
                editProductQuantity(context, uri, quantity);
            }
        });
    }

    private void editProductQuantity(Context context, Uri productUri, int currentQuantityInStock) {

        int newQuantityValue = (currentQuantityInStock >= 1) ? currentQuantityInStock - 1 : 0;

        if (currentQuantityInStock == 0) {
            Toast.makeText(context.getApplicationContext(), R.string.quantity_not_found, Toast.LENGTH_SHORT).show();
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY, newQuantityValue);
        int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);

    }
}