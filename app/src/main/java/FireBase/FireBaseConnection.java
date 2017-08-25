package FireBase;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.cdk.food.foodreviews.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;


/**
 * Created by martint on 7/17/17.
 */

public class FireBaseConnection {
    private Context context;

    public FireBaseConnection(Context context) {
        this.context = context;
    }
    public void connectToFirebase() {
        System.out.println("-------test test test test test------");
        FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = dataBase.getReference("Angel");
        System.out.println("size value ");
        myRef.setValue("good friend");

    }



    public void uploadImage(String dir, String imgName) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        StorageReference imgRef = storageRef.child(dir + "/" + imgName);

        String imgNameWithoutExt = imgName.split("\\.")[0];

        InputStream ins = context.getResources().openRawResource(context.getResources().getIdentifier(imgNameWithoutExt, "raw", context.getPackageName()));
        UploadTask uploadTask = imgRef.putStream(ins);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }



}