package org.universelight.ul.page;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.universelight.ul.R;
import org.universelight.ul.objects.MobileGlobalVariable;
import org.universelight.ul.ui.ULUIDefine;
import org.universelight.ul.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static org.universelight.ul.ui.ULUIDefine.FontSize_5u;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor>, View
        .OnTouchListener
{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText             mPasswordView;
    private View                 mProgressView;
    private View                 mLoginFormView;
    FirebaseAuth                   auth;
    FirebaseAuth.AuthStateListener authStateListener;

    // FingerPrint
    private KeyguardManager    km;
    private FingerprintManager fm;
    private CancellationSignal cancellationSignal;
    private boolean canUseFingerPrint = false;
    private Dialog                          dialog;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager.AuthenticationCallback mAuthenticationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //改由manifest設定
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initVariables(LoginActivity.this);

        final SharedPreferences LoginStatus = getSharedPreferences("USER", MODE_PRIVATE);

        String fingerPrintStatus = LoginStatus.getString("FingerPrint", "0");

        Log.e("fingerPrintStatus:", fingerPrintStatus);

        // Set up the login form.
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                showProgress(false);


                if (user != null)
                {
                    Log.d("onAuthStateChanged", "登入");
                    LoginStatus.edit().putString("UID", user.getUid()).commit();
                }
                else
                {
                    Log.d("onAuthStateChanged", "已登出");
                    LoginStatus.edit().putString("UID", "").commit();
                }
            }
        };

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginFormView.setOnTouchListener(this);

        mProgressView = findViewById(R.id.login_progress);

        try
        {
            km = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
            fm = getSystemService(FingerprintManager.class);
            canUseFingerPrint = Util.checkFingerPrintService(this, km, fm);
        }
        catch (NoClassDefFoundError e)
        {
            Log.e("NoClassDefFoundError", e.toString());
        }

        if (canUseFingerPrint && fingerPrintStatus.equals("1"))
        {
            mAuthenticationCallback
                    = new FingerprintManager.AuthenticationCallback()
            {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString)
                {
                    Log.e("finger print", "error " + errorCode + " " + errString);
                    dialog.dismiss();
                }

                @Override
                public void onAuthenticationFailed()
                {
                    Log.e("finger print", "onAuthenticationFailed");
                    dialog.dismiss();
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result)
                {
                    Log.i("finger print", "onAuthenticationSucceeded");

                    dialog.dismiss();

                    SharedPreferences LoginStatus = getSharedPreferences("USER", MODE_PRIVATE);
                    showProgress(true);
                    fireBaseLogin(LoginStatus.getString("AuthMail", ""), LoginStatus.getString("AuthPW",
                            ""));
                }
            };

            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_fingerprint_login);
            Button btnClose = (Button) dialog.findViewById(R.id.btn_finger_cancel);

            btnClose.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

            dialog.show();

            startFingerprintListening();
        }


        ImageView img = (ImageView) findViewById(R.id.imageView);

        img.getLayoutParams().width = mUIDefine.getLayoutWidth(45);
        img.getLayoutParams().height = mUIDefine.getLayoutWidth(45);

        mEmailView.getLayoutParams().width = mUIDefine.getLayoutWidth(100);
        mEmailView.getLayoutParams().height = mUIDefine.getLayoutWidth(10);
        mUIDefine.setTextSize(FontSize_5u, mEmailView);

        mPasswordView.getLayoutParams().width = mUIDefine.getLayoutWidth(100);
        mPasswordView.getLayoutParams().height = mUIDefine.getLayoutWidth(10);
        mUIDefine.setTextSize(FontSize_5u, mEmailView);

        mProgressView.getLayoutParams().width = mUIDefine.getLayoutWidth(25);
        mProgressView.getLayoutParams().height = mUIDefine.getLayoutWidth(25);
    }

    private void startFingerprintListening()
    {
        cancellationSignal = new CancellationSignal();

        if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) == PackageManager
                .PERMISSION_GRANTED)
        /** In SDK 23, we need to check the permission before we call FingerprintManager API functionality.*/

        {

            Util.generateKey();

            if (Util.cipherInit())
            {
                cryptoObject =
                        new FingerprintManager.CryptoObject(Util.getCipher());

                fm.authenticate(cryptoObject,
                        /** crypto objects 的 wrapper class，可以透過它讓驗證過程更為安全，但也可以不使用。*/
                        cancellationSignal,
                        /** 用來取消 authenticate 的 object*/
                        0,
                        /** optional flags; should be 0*/
                        mAuthenticationCallback,
                        /** callback 用來接收 authenticate 成功與否，有三個 callback method*/
                        null
                        /** optional 的參數，如果有使用，FingerprintManager 會透過它來傳遞訊息*/
                );
            }


        }
    }

    private void populateAutoComplete()
    {
        if (!mayRequestContacts())
        {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS))
        {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener()
                    {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v)
                        {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        }
        else
        {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_READ_CONTACTS)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email    = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel    = false;
        View    focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        else if (!isEmailValid(email))
        {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            showProgress(true);
            fireBaseLogin(email, password);
        }
    }

    private void fireBaseLogin(String email, String password)
    {
        Log.d("AUTH", email + "/" + password);
        SharedPreferences LoginStatus = getSharedPreferences("USER", MODE_PRIVATE);
        LoginStatus.edit().putString("AuthMail", email).apply();
        LoginStatus.edit().putString("AuthPW", password).apply();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()

        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (!task.isSuccessful())
                {
                    showProgress(false);
                    Util.showLog(mPage, "登入失敗");
                }
                else
                {
                    mEmailView.setText("");
                    mPasswordView.setText("");

                    Intent intent = new Intent();
                    intent.setClass(mPage, MainPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean isEmailValid(String email)
    {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password)
    {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
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
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection)
    {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(mPage,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (v.getId())
        {
            case R.id.login_form:
                packUpKeyboard(v);
                break;
        }
        return false;
    }

    private void packUpKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        Log.d("Close", "keyboard");
    }

    private interface ProfileQuery
    {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
                };

        int ADDRESS    = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (cancellationSignal != null)
        {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        auth.removeAuthStateListener(authStateListener);
    }

}

