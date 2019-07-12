package com.example.instagram;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    TextView tvUsername;
    ImageView ivImage;
    TextView tvCaption;
    TextView tvTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvCaption = findViewById(R.id.tvCaption);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        post = Parcels.unwrap(getIntent().getParcelableExtra("Detailed"));
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvTimestamp.setText(post.getCreatedAt().toString());
        Glide.with(this).load(post.getImage().getUrl()).into(ivImage);

    }
}
