package com.example.userlocationalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;
    protected void setup(Bundle savedInstanceState){

        // before set layout
        beforeInit(savedInstanceState);

        // set content view
        setContentView(getLayout());

        // call init
        init(savedInstanceState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setup(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void beforeInit(Bundle savedInstanceState){}

    protected void init(Bundle savedInstanceState){}

    public final <T extends View> T getView(final int resId){
        return getView(resId, false);
    }

    public final <T extends View> T getView(int resId, final boolean attachClickListener){
        final View v = findViewById(resId);
        if(attachClickListener) v.setOnClickListener(this);
        return (T)v;
    }

    public final <T extends View> T getView(int resId, final View.OnClickListener clickListener){
        final View v = findViewById(resId);
        v.setOnClickListener(clickListener);
        return (T)v;
    }

    public final <T> T getSystemsService(String name){
        return (T) super.getSystemService(name);
    }

    public final void bindClickListener(final int resId, final View.OnClickListener clickListener){
        View v = getView(resId);
        bindClickListener(v, clickListener);
    }

    public final void bindClickListener(final View v, final View.OnClickListener clickListener){
        if(v != null) v.setOnClickListener(clickListener);
    }


    public final void attachClickListener(final int... views){
        for(int i = 0; i < views.length; i++){
            findViewById(views[i]).setOnClickListener(this);
        }
    }


    public final void gotoActivity(final Class<?> activity){
        this.gotoActivity(activity, false, new Bundle());
    }

    public final void gotoActivity(final Class<?> activity, final boolean finish){
        this.gotoActivity(activity, finish, new Bundle());
    }

    public final void gotoActivity(final Class<?> activity, final Bundle bundle){
        this.gotoActivity(activity, false, bundle);
    }

    public final void gotoActivity(final Class<?> activity, final boolean finish, final Bundle bundle){

        intent = new Intent(this, activity);
        intent.putExtras(bundle);
        startActivity(intent);

        if(finish) finish();
    }

    public final void resetToMainActivity(final Class<?> activity){
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //////////////////////////////////////////
    // ABSTRACT FUNCTIONS
    //////////////////////////////////////////

    public abstract int getLayout();

    protected void showToast(String mesaage){
        Toast.makeText(this,mesaage,Toast.LENGTH_SHORT).show();
    }
    public static void showAlertMessageForRadius(final Activity mActivity,
                                                 String message, final String content, String positiveLabel, String negativeLabel,
                                                 DialogInterface.OnClickListener clickListener) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setTitle(message)
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(positiveLabel, clickListener)
                .setNegativeButton(negativeLabel, clickListener)
                ;



        dialog = builder.create();
        dialog.show();

    }
}