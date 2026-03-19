package com.arke.sdk.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.arke.sdk.view.SaleActivity;
import com.arke.sdk.R;

/**
 * Simple pay demo.
 */

public class SimplePayDemo extends ApiDemo {

    /**
     * Constructor.
     */
    private SimplePayDemo(Context context, Toast toast, AlertDialog dialog) {
        super(context, toast, dialog);
    }

    /**
     * Get simple pay demo instance.
     */
    public static SimplePayDemo getInstance(Context context, Toast toast, AlertDialog dialog) {
        return new SimplePayDemo(context, toast, dialog);
    }

    /**
     * Do simple pay functions.
     */
    public void execute(String value) {
        if (value.equals(getContext().getString(R.string.sale_search_card_first))) {
            Intent intent = new Intent(getContext(), SaleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(SaleActivity.SEARCH_CARD_FIRST, true);
            getContext().startActivity(intent);

        } else if (value.equals(getContext().getString(R.string.sale_start_emv_process_first))) {
            Intent intent = new Intent(getContext(), SaleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(SaleActivity.SEARCH_CARD_FIRST, false);
            getContext().startActivity(intent);
        }
    }
}
