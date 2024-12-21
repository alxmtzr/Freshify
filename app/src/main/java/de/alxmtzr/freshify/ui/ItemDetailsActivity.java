package de.alxmtzr.freshify.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import de.alxmtzr.freshify.R;

public class ItemDetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView itemQuantity;
    private TextView itemCategory;
    private TextView itemExpirationDate;
    private TextView itemComment;
    private ImageButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        initUIComponents();
        setDeleteButtonListener();
    }

    private void setDeleteButtonListener() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
    }

    private void initUIComponents() {
        itemName = findViewById(R.id.textViewItemName);
        itemQuantity = findViewById(R.id.textViewItemQuantity);
        itemCategory = findViewById(R.id.textViewItemCategory);
        itemExpirationDate = findViewById(R.id.textViewItemExpiryDate);
        itemComment = findViewById(R.id.textViewItemComment);
        deleteButton = findViewById(R.id.deleteItemBtn);
    }
}