package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.inventory.Adapter.InventoryCursorAdapter;
import com.example.android.inventory.data.InventoryContract.InventoryEntry;


public class CatalogActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;

    InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView inventoryListView = (ListView) findViewById(R.id.list_item_view);
        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        mCursorAdapter = new InventoryCursorAdapter(this, null, this);
        inventoryListView.setAdapter(mCursorAdapter);
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    private void insertInventory() {
        ContentValues headphones = new ContentValues();
        headphones.put(InventoryEntry.COLUMN_NAME, "Headphone");
        headphones.put(InventoryEntry.COLUMN_QUANTITY, "10");
        headphones.put(InventoryEntry.COLUMN_PRICE,30);
        headphones.put(InventoryEntry.COLUMN_IMAGE,"android.resource://com.example.android.inventory/drawable/headphone");
        headphones.put(InventoryEntry.COLUMN_SUPPLIER_NAME,"Younan Adly");
        headphones.put(InventoryEntry.COLUMN_SUPPLIER_PHONE,"01010070000");
        headphones.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL,"Younan@gmail.com");
        headphones.put(InventoryEntry.COLUMN_GRADE, InventoryEntry.GRADE_NEW);
        headphones.put(InventoryEntry.COLUMN_WEIGHT, 7);

        // new Mouse
        ContentValues mouse = new ContentValues();
        mouse.put(InventoryEntry.COLUMN_NAME, "Mouse");
        mouse.put(InventoryEntry.COLUMN_QUANTITY, "50");
        mouse.put(InventoryEntry.COLUMN_PRICE, 15);
        mouse.put(InventoryEntry.COLUMN_IMAGE,"android.resource://com.example.android.inventory/drawable/mouse");
        mouse.put(InventoryEntry.COLUMN_SUPPLIER_NAME,"Younan Adly");
        mouse.put(InventoryEntry.COLUMN_SUPPLIER_PHONE,"01010070000");
        mouse.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL,"Younan@gmail.com");
        mouse.put(InventoryEntry.COLUMN_GRADE, InventoryEntry.GRADE_NEW);
        mouse.put(InventoryEntry.COLUMN_WEIGHT, 88);

        //New Keyboard
        ContentValues keyboard = new ContentValues();
        keyboard.put(InventoryEntry.COLUMN_NAME, "Keyboard");
        keyboard.put(InventoryEntry.COLUMN_QUANTITY, "30");
        keyboard.put(InventoryEntry.COLUMN_PRICE, 30);
        keyboard.put(InventoryEntry.COLUMN_IMAGE,"android.resource://com.example.android.inventory/drawable/keyboard");
        keyboard.put(InventoryEntry.COLUMN_SUPPLIER_NAME,"Younan Adly");
        keyboard.put(InventoryEntry.COLUMN_SUPPLIER_PHONE,"01010070000");
        keyboard.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL,"Younan@gmail.com");
        keyboard.put(InventoryEntry.COLUMN_GRADE, InventoryEntry.GRADE_NEW);
        keyboard.put(InventoryEntry.COLUMN_WEIGHT, 50);

        // New Game Controller
        ContentValues controller = new ContentValues();
        controller.put(InventoryEntry.COLUMN_NAME, "Game Controller");
        controller.put(InventoryEntry.COLUMN_QUANTITY, "20");
        controller.put(InventoryEntry.COLUMN_PRICE, 100);
        controller.put(InventoryEntry.COLUMN_IMAGE,"android.resource://com.example.android.inventory/drawable/game_controller");
        controller.put(InventoryEntry.COLUMN_SUPPLIER_NAME,"Younan Adly");
        controller.put(InventoryEntry.COLUMN_SUPPLIER_PHONE,"01010070000");
        controller.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL,"Younan@gmail.com");
        controller.put(InventoryEntry.COLUMN_GRADE, InventoryEntry.GRADE_NEW);
        controller.put(InventoryEntry.COLUMN_WEIGHT, 50);

        Uri headphoneUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, headphones);
        Uri mouseUri = getContentResolver().insert(InventoryEntry.CONTENT_URI,mouse);
        Uri keyboardUri = getContentResolver().insert(InventoryEntry.CONTENT_URI,keyboard);
        Uri gameControllerUri = getContentResolver().insert(InventoryEntry.CONTENT_URI,controller);
    }


    public void clickOnViewItem(long id) {
        Intent intent = new Intent(this, EditorActivity.class);
        Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
        intent.setData(currentInventoryUri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertInventory();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllInventory();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllInventory() {
        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from Inventory database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_NAME,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_IMAGE,
                InventoryEntry.COLUMN_PRICE};

        return new CursorLoader(
                this,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}