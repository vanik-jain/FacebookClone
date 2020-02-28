package com.example.frenzbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frenzbook.DTO.BaseResponse;
import com.example.frenzbook.DTO.Content;
import com.example.frenzbook.DTO.PostDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private EditText postEditText;
    private ImageView uploadImage;
    private Button postButton;

    private StorageReference mStorageRef;
    private Uri imageUri;
    private PostDTO postDTO;
    private Content content;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        uploadImage = findViewById(R.id.upload_image_icon);
        postButton = findViewById(R.id.post_button);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        postDTO = new PostDTO();
        content = new Content();

        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id",null);
        postDTO.setUserId("114");
        postDTO.setCategory("personal");

        uploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        postButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                postEditText = findViewById(R.id.addPostEditText);
                content.setText(postEditText.getText().toString());
                postDTO.setContent(content);
                Api api = App.getRetrofit(Api.BASE_URL_PROXY).create(Api.class);
                Call<BaseResponse<String>> call = api.addPost(postDTO);
                call.enqueue(new Callback<BaseResponse<String>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response)
                    {
                     if (response.body()!= null)
                     {
                         if (response.body().isSuccess())
                         {
                             Toast.makeText(PostActivity.this,"Post Added Successfully",Toast.LENGTH_SHORT).show();
                         }


                     }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<String>> call, Throwable t)
                    {

                        Toast.makeText(PostActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
         // uploadImage.setImageURI(imageUri);

            final StorageReference ref = mStorageRef.child(System.currentTimeMillis() + getExtension(imageUri));
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    content.setImage(uri.toString());
                                }
                            });
                            Toast.makeText(PostActivity.this, "Image uploaded successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            Toast.makeText(PostActivity.this, "Upload is " + progress + "% done", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
