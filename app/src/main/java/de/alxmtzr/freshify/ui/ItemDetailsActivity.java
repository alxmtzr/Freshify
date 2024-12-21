package de.alxmtzr.freshify.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.concurrency.DeleteItemRunnable;
import de.alxmtzr.freshify.data.concurrency.GetItemByIdRunnable;
import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;

public class ItemDetailsActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView itemQuantity;
    private TextView itemCategory;
    private TextView itemExpirationDate;
    private TextView itemComment;
    private ImageView deleteButton;
    private long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        initUIComponents();
        setDeleteButtonListener();

        itemId = getIntent().getLongExtra("itemId", -1);
        retrieveItemDetails(itemId);
    }

    private void retrieveItemDetails(long itemId) {
        FreshifyDBHelper dbHelper = new FreshifyDBHelper(this);
        GetItemByIdRunnable runnable = new GetItemByIdRunnable(
                new FreshifyRepositoryImpl(dbHelper),
                itemId,
                itemName,
                itemQuantity,
                itemCategory,
                itemExpirationDate,
                itemComment
        );
        new Thread(runnable).start();
    }

    private void setDeleteButtonListener() {
        deleteButton.setOnClickListener(view -> new AlertDialog.Builder(view.getContext())
                .setTitle(R.string.delete_item_uppercase)
                .setMessage(R.string.are_you_sure_you_want_to_delete_this_item)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    FreshifyDBHelper dbHelper = new FreshifyDBHelper(view.getContext());
                    FreshifyRepository repository = new FreshifyRepositoryImpl(dbHelper);

                    DeleteItemRunnable runnable = new DeleteItemRunnable(
                            repository,
                            itemId,
                            view,
                            () -> {
                                // Set result and finish activity
                                setResult(RESULT_OK);
                                finish();
                            }
                    );
                    new Thread(runnable).start();
                })
                .setNegativeButton("Cancel", null)
                .show());
    }


    private void initUIComponents() {
        initToolbar();
        itemName = findViewById(R.id.textViewItemName);
        itemQuantity = findViewById(R.id.textViewItemQuantity);
        itemCategory = findViewById(R.id.textViewItemCategory);
        itemExpirationDate = findViewById(R.id.textViewItemExpiryDate);
        itemComment = findViewById(R.id.textViewItemComment);
        deleteButton = findViewById(R.id.deleteItemBtn);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarItemDetails);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Drawable backArrow = AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back);
            if (backArrow != null) {
                backArrow.setTint(ContextCompat.getColor(this, R.color.md_theme_onPrimaryContainer)); // change color
                getSupportActionBar().setHomeAsUpIndicator(backArrow);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!getOnBackPressedDispatcher().hasEnabledCallbacks()) {
            // No custom back-pressed logic present, execute default behavior
            finish(); // cancel current activity
        } else {
            // active custom logic
            getOnBackPressedDispatcher().onBackPressed();
        }
        return true;
    }
}