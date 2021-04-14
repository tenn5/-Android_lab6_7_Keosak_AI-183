package ua.opu.contactlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.DeleteItemListener{

    @BindView(R.id.list)
    RecyclerView mContactRV;

    private AppDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();

    private List<Contact> contacts;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWindow();
        ButterKnife.bind(this);

        db = AppDatabase.getInstance(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        updateData();
        verifyStoragePermissions();
    }

    @Override
    public void onDeleteItem(int id) {
        executor.execute(() -> {

            db.contactDAO().deleteContactById(id);
            updateData();
        });
    }

    private void updateData() {
        executor.execute(() -> {
            // С помощью метода getAll() получаем все заметки из БД
            contacts = new ArrayList<>(db.contactDAO().getAll());
            runOnUiThread(() -> {

                mContactRV.setLayoutManager(new LinearLayoutManager(this));
                adapter = new ContactsAdapter(this, contacts);
                mContactRV.setAdapter(adapter);
            });
        });

    }

    @OnClick(R.id.fab)
    public void fabClick() {
        Intent i = new Intent(this, AddContactActivity.class);
        startActivity(i);
    }

    private void setWindow() {
        // Метод устанавливает StatusBar в цвет фона
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.activity_background));

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void verifyStoragePermissions() {
        // Проверяем наличие разрешения на запись во внешнее хранилище
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем разрешение у пользователя
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}