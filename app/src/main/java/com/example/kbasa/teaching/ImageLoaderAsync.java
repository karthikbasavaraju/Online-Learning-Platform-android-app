package com.example.kbasa.teaching;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ImageLoaderAsync extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoaderAsync(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {

            if (url != null) {
                final DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Teacher").child(FirebaseAuth.getInstance().getUid());
                InputStream picStream = null;
                try {
                    picStream = new FileInputStream(new File(url));
                } catch (Exception e) {
                    Log.i("louda upload", "path : " + picStream);
                }

                StorageReference mountainsRef = FirebaseStorage.getInstance().getReference();

                UploadTask picUploadTask = mountainsRef.child("pic").putStream(picStream);
                picUploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri profileUri = taskSnapshot.getDownloadUrl();
                        courseDB.updateChildren(new HashMap<String, Object>() {{
                            put("profilePic", profileUri);
                        }});
                    }
                });
            }

            Bitmap myBitmap = BitmapFactory.decodeFile(url);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }

}
