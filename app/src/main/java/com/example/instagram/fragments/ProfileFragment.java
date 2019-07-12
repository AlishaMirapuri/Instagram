package com.example.instagram.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class ProfileFragment extends Fragment {

    private final String TAG = "ProfileFragment";

    private Button btnSetPic;
    private ImageView ivProfilePic;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSetPic = view.findViewById(R.id.btnSetPic);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);


        //queryPosts();

        ParseUser user = ParseUser.getCurrentUser();

        if (user.get("proImage") != null) {
            ParseFile img = (ParseFile) user.get("proImage");
            Glide.with(getContext()).load(img.getUrl()).into(ivProfilePic);
        }

        btnSetPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
                if (photoFile == null) {
                    Log.e(TAG, "No photo to submit");
                    Toast.makeText(getContext(), "There is no photo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Glide.with(getContext()).load(photoFile).into(ivProfilePic);
                saveUser(photoFile);
            }
        });
    }

    public void saveUser (File photoFile) {
        ParseFile photo = new ParseFile(photoFile);
        ParseUser user = ParseUser.getCurrentUser();
        user.put("proImage", photo);
        user.saveInBackground();
    }

    public void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

//    @Override
//    protected void queryPosts() {
//        final Post.Query postParseQuery = new Post.Query();
//        postParseQuery.getTop().withUser();
//        postParseQuery.setLimit(20);
//        postParseQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
//        postParseQuery.addDescendingOrder(Post.KEY_CREATED_AT);
//        postParseQuery.findInBackground(new FindCallback<Post>() {
//
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Error with query");
//                    e.printStackTrace();
//                    return;
//                }
//                mPosts.addAll(posts);
//                adapter.notifyDataSetChanged();
//
//                for (int i = 0; i < posts.size(); i++) {
//                    Post post = posts.get(i);
//                    Log.d(TAG, "Post: " + posts.get(i).getDescription() + " username: " + post.getUser().getUsername());
//
//                }
//            }
//        });
//    }
}
