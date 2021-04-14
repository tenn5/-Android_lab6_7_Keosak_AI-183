package ua.opu.contactlist;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends AppCompatActivity {

    private Uri mContactImageUri;

    @BindView(R.id.profile_image)
    ImageView mContactImage;

    @BindView(R.id.name_et)
    EditText nameEditText;

    @BindView(R.id.email_et)
    EditText emailEditText;

    @BindView(R.id.phone_et)
    EditText phoneEditText;

    private AppDatabase db;


    private static final int IMAGE_CAPTURE_REQUEST_CODE = 7777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        setWindow();

        ButterKnife.bind(this);
        db = AppDatabase.getInstance(this);

        Button takePhotoButton = findViewById(R.id.button_camera);
        takePhotoButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
            } catch (ActivityNotFoundException e){
                Toast.makeText(this, "Error while trying to open camera app", Toast.LENGTH_SHORT).show();
            }
        });

        Button cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(v -> onBackPressed());
    }

    @OnClick(R.id.button_add)
    public void createClick() {

        if(mContactImageUri == null){
            Toast.makeText(this, "Contact image not set!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String image = mContactImageUri.toString();

        Contact contact = new Contact(name, email, phone, image);

        Executors.newSingleThreadExecutor().execute(
                () -> db.contactDAO().insertContact(contact));

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            mContactImage.setImageBitmap(imageBitmap);

            String filename = "contact_" + System.currentTimeMillis() + ".png";

            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(outputFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                mContactImageUri = Uri.fromFile(outputFile);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        setResult(RESULT_CANCELED);
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
}
