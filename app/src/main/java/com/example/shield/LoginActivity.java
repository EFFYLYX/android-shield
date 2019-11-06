package com.example.shield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Handler;
import android.widget.Toast;

import static android.Manifest.permission.READ_CONTACTS;
//import com.sun.corba.se.impl.orbutil.closure.Constant;

//import org.json.JSONException;
//import org.json.JSONObject;

import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;
import es.dmoral.toasty.Toasty;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final int TYPE_TAKE_PHOTO = 1;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button sendCodeButton;
    private TimeCount time;



    final Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    break;
                case 1:

                    Boolean success = (Boolean) msg.obj;
                    if (success){

                        Intent intent = new Intent(getApplication(), TabActivity.class);
                        String[] arr = new String[4];
                        arr[0]= phone;
                        arr[1] = name1;
                        arr[2] = name2;
                        arr[3] = secure;


                        intent.putExtra("thisUser",arr);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        Bundle bundle = new Bundle();



                        startActivity(intent);


                    }else {
                        Toasty.error(getApplicationContext(), "Register fails. ", Toast.LENGTH_SHORT, true).show();

                    }
                    break;
                case 2:

                    User u = (User) msg.obj;



                    Intent intent = new Intent(LoginActivity.this,TabActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    String[] arr = new String[4];
                    arr[0]= u.phoneNo;
                    arr[1] = u.firstName;
                    arr[2] = u.secondName;
                    arr[3] = u.secureCode;
                    intent.putExtra("thisUser",arr);
                    startActivity(intent);
                    break;



            }


        }

    };

    private ImageView mHeader_iv;

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;

    //调用照相机返回图片文件
    private File tempFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SMSSDK.getInstance().setIntervalTime(60000);
        initView();





    }

    EditText first_name;
    EditText second_name;
    EditText secure_code;

    ImageView iv_back;

    Button next;

    private void initView(){

        SMSSDK.getInstance().initSdk(this);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        sendCodeButton = findViewById(R.id.verification_code_button);

        sendCodeButton.setEnabled(false);
//        if(mEmailView.getText().toString().length() ==11){
//            sendCodeButton.setEnabled(true);
//        }
        time = new TimeCount(60000, 1000);

        sendCodeButton.setAlpha(0.5f);



        sendCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendCodeButton.setAlpha(1f);

                attemptSend();



            }
        });


//        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (mEmailView.getText().toString().length() == 11) {
//
//                    sendCodeButton.setAlpha(1f);
//                    sendCodeButton.setEnabled(true);
//                }else{
//                    sendCodeButton.setAlpha(0.5f);
//                    sendCodeButton.setEnabled(false);
//                }
//            }
//        });


//
//        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (mEmailView.getText().toString().length() == 11) {
//
//                    sendCodeButton.setAlpha(1f);
//                    sendCodeButton.setEnabled(true);
//                }else{
//                    sendCodeButton.setAlpha(0.5f);
//                    sendCodeButton.setEnabled(false);
//                }
//                return false;
//            }
//        });





        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focusP = hasFocus;
                if (hasFocus && !hasStart){
                    sendCodeButton.setAlpha(1f);
                    sendCodeButton.setEnabled(true);
                }else {

                    sendCodeButton.setAlpha(0.5f);
                    sendCodeButton.setEnabled(false);
                }

            }
        });

//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
////                    attemptLogin();
//                    return true;
//                }
//
//                return false;
//            }
//        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                SMSSDK.getInstance().checkSmsCodeAsyn(mEmailView.getText().toString(), mPasswordView.getText().toString(), new SmscheckListener() {
                    @Override
                    public void checkCodeSuccess(final String code) {


                        attemptLogin();
                        // 验证码验证成功，code 为验证码信息。
                    }

                    @Override
                    public void checkCodeFail(int errCode, final String errMsg) {
                        // 验证码验证失败, errCode 为错误码，详情请见文档后面的错误码表；errMsg 为错误描述。

                        mPasswordView.setError("Invalid!");
                        attemptLogin();
                    }
                });







//                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    boolean focusP = false;


    String name1;
    String name2;
    String secure;

    private void initView2() {
        setContentView(R.layout.login_first_time);



        first_name =  findViewById(R.id.first_name);
        second_name = findViewById(R.id.second_name);
        secure_code = findViewById(R.id.secure_password);



        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                setContentView(R.layout.activity_login);
                initView();

            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        name1 = first_name.getText().toString();
                         name2 = second_name.getText().toString();
                        secure = secure_code.getText().toString();

                        Boolean success = request.CompleteInfo(name1,name2,phone,secure);
                        Message msg = new Message();
                        msg.obj = success;
                        msg.what=1;

                        handle.sendMessage(msg);





//                        User u = request.Login()
                    }
                }).start();
            }
        });




    }




    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    private void attemptSend(){

        // Reset errors.
        mEmailView.setError(null);
//        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }






        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            SMSSDK.getInstance().getSmsCodeAsyn(mEmailView.getText().toString(), "1", new SmscodeListener() {
                @Override
                public void getCodeSuccess(final String uuid) {
//                        time.onFinish();
//                    sendCodeButton.setEnabled(false);
                    sendCodeButton.setAlpha(0.5f);
                    sendCodeButton.setEnabled(false);
                    time.start();

                    // 获取验证码成功，uuid 为此次获取的唯一标识码。
                }

                @Override
                public void getCodeFail(int errCode, final String errMsg) {
//                    time.onFinish();

                    Toasty.error(getApplicationContext(), "Send fails. "+errMsg, Toast.LENGTH_SHORT, true).show();
                    // 获取验证码失败 errCode 为错误码，详情请见文档后面的错误码表；errMsg 为错误描述。
                }
            });






        }
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }




        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();



        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            mAuthTask = new UserLoginTask(email,password);
            mAuthTask.execute();

        }
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return email.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() == 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.NUMBER));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    PhotoPopupWindow mPhotoPopupWindow;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.photo_btn:
//                mPhotoPopupWindow = new PhotoPopupWindow(LoginActivity.this, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 进入相册选择
//
////                        getPicFromAlbm();
//
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        getPicFromCamera();
//                        // 拍照
//                    }
//                });
//                View rootView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_first_time, null);
//                mPhotoPopupWindow.showAtLocation(rootView,
//                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//
//
//                break;


            default:
                break;
        }

    }






    private interface ProfileQuery {

        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int NUMBER = 0;

        int ADDRESS = 1;
        int IS_PRIMARY = 2;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }



    String phone;


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            return request.Exist(mEmail);

        }

        @Override
        protected void onPostExecute(final Boolean user) {
            mAuthTask = null;
            showProgress(false);

            if (user == null){
                Toasty.error(getApplicationContext(), "Error ", Toast.LENGTH_SHORT, true).show();

            }else{
                phone = mEmail;
                if (!user) {
                    phone = mEmail;
                    initView2();

                    mAuthTask = null;
//                setContentView(R.layout.login_first_time);
                }else{

                    toTab();

                    mAuthTask = null;
                }
            }




        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    public void toTab(){

        new Thread(new Runnable() {
            @Override
            public void run() {


                User u = request.Login(phone);

                Message msg = new Message();
                msg.obj = u;
                msg.what=2;
                handle.sendMessage(msg);

            }
        }).start();



    }
    boolean hasStart = false;

    private class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            sendCodeButton.setBackgroundColor(Color.parseColor("#B6B6D8"));
//            sendCodeButton.setClickable(false);

            hasStart = true;
            sendCodeButton.setAlpha(0.5f);
            sendCodeButton.setEnabled(false);


            sendCodeButton.setText(millisUntilFinished / 1000 + " s ");

        }

        @Override
        public void onFinish() {
            hasStart = false;

            if (focusP){
                sendCodeButton.setEnabled(true);
                sendCodeButton.setAlpha(1f);
            }

            sendCodeButton.setText("Send");

//            sendCodeButton.setClickable(true);
//            sendCodeButton.setBackgroundColor(Color.parseColor("#4EB84A"));
        }
    }
}




