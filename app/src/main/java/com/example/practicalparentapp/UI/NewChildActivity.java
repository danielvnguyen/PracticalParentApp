package com.example.practicalparentapp.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.practicalparentapp.Model.Child;
import com.example.practicalparentapp.Model.ChildrenManager;
import com.example.practicalparentapp.Model.ChildListViewAdapter;
import com.example.practicalparentapp.R;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

/**
 * This class manages the parent's children.
 * Supports adding, deleting, and editing.
 * Also supports adding/editing a child's image.
 */
public class NewChildActivity extends AppCompatActivity {

    private ChildrenManager childrenManager;
    private boolean isEditingChild = false;
    private Integer editChildIndex;
    private Child editedChild;
    private EditText childNameInput;
    private Button deleteBtn;
    private ImageView childImageInput;
    private byte[] imageToSave;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_child);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        childrenManager = ChildrenManager.getInstance(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultimage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageToSave = stream.toByteArray();

        setInterface();
        setUpSaveBtn();
        setUpDeleteBtn();
        setUpChooseImage();
        setUpSaveImage();
    }

    private void setInterface() {
        Button saveImgBtn = findViewById(R.id.save_img_btn);
        saveImgBtn.setEnabled(false);
        childNameInput = findViewById(R.id.child_name_input);
        childImageInput = findViewById(R.id.choose_img_btn);
        deleteBtn = findViewById(R.id.delete_btn);

        //check if child being edited
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEditingChild = (Boolean) extras.get(ChildListViewAdapter.STRING_EXTRA);
            editChildIndex = (Integer) extras.get(ChildListViewAdapter.POSITION_EXTRA);
        }

        if (isEditingChild) {
            setTitle("Editing a child");
            deleteBtn.setVisibility(View.VISIBLE);
            editedChild = childrenManager.getChildList().get(editChildIndex);
            Bitmap bitmap = editedChild.getChildImage();
            childImageInput.setImageBitmap(bitmap);
            childNameInput.setText(editedChild.getName());
        }
        else {
            setTitle("Adding a new child");
        }
    }

    private void setUpSaveBtn() {
        Button btn = findViewById(R.id.save_btn);
        btn.setOnClickListener(view -> {
            if (isEditingChild) {
                editedChild.editChild(childNameInput.getText().toString(), imageToSave);
                ChildrenManager.saveChildList(this, childrenManager.getChildList());
            }
            else {
                Child newChild = new Child(childNameInput.getText().toString(), imageToSave);
                childrenManager.addChildToList(this, newChild);
            }
            Toast.makeText(getApplicationContext(),"Child has been saved!",
                    Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setUpSaveImage() {
        Button saveImgBtn = findViewById(R.id.save_img_btn);
        saveImgBtn.setOnClickListener((v) -> {
            imageToSave = convertImageViewToByteArray(childImageInput);
            Toast.makeText(NewChildActivity.this, "Image saved!",Toast.LENGTH_SHORT).show();
        });
    }

    private void setUpChooseImage() {
        ImageView chooseImage = findViewById(R.id.choose_img_btn);
        Button saveImgBtn = findViewById(R.id.save_img_btn);
        chooseImage.setOnClickListener((v) -> {
            chooseProfilePicture();
            saveImgBtn.setEnabled(true);
        });
    }

    private byte[] convertImageViewToByteArray(ImageView imageView){
        Bitmap bitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewChildActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);

        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView imageViewADPPCamera = dialogView.findViewById(R.id.imageviewADPPCamera);
        ImageView imageViewADPPGallery = dialogView.findViewById(R.id.imageviewADPPGallery);

        AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        imageViewADPPCamera.setOnClickListener(view -> {
            if (checkAndRequestPermissions())
            {
                takePictureFromCamera();
                alertDialogProfilePicture.cancel();
            }
        });

        imageViewADPPGallery.setOnClickListener(view -> {
            takePictureFromGallery();
            alertDialogProfilePicture.cancel();
        });
    }

    @SuppressWarnings("deprecation")
    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    Uri selectedImageUri = data.getData();
                    childImageInput.setImageURI(selectedImageUri);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    childImageInput.setImageBitmap(bitmapImage);
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("QueryPermissionsNeeded")
    private void takePictureFromCamera() {
        Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(takePicture, 2);
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 27) {
            int cameraPermission = ActivityCompat.checkSelfPermission(
                    NewChildActivity.this, Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(NewChildActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePictureFromCamera();
        } else
            Toast.makeText(NewChildActivity.this, "Permission not Granted",
                    Toast.LENGTH_SHORT).show();
    }

    private void setUpDeleteBtn() {
        deleteBtn.setOnClickListener(view -> {
            childrenManager.removeChild(this, editChildIndex);
            ChildrenManager.saveChildList(this, childrenManager.getChildList());
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        if (itemID == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, NewChildActivity.class);
    }
}