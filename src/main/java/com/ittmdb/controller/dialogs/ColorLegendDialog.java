package com.ittmdb.controller.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ittmdb.R;

public class ColorLegendDialog extends Dialog {


    public ColorLegendDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {

        setTitle("Color Legend");
        setContentView(R.layout.dialog_color_legend);

        LinearLayout root = (LinearLayout) findViewById(R.id.root_color_legend);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        super.show();
    }
}
