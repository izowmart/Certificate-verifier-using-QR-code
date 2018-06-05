package com.marttech.certverifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class AnimatedSplash extends Activity{


    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animated_spalsh);

        relativeLayout = findViewById(R.id.relativeLayout);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                   Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
                   relativeLayout.setAnimation(animation);
                   sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(AnimatedSplash.this, MainActivity.class));
                }
            }
        };
        timer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
