package org.universelight.ul.util;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import org.universelight.ul.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by whitesnake on 2016/7/12.
 */
public class Util
{
    private static KeyStore     keyStore;
    private static KeyGenerator keyGenerator;
    private static Cipher       cipher;
    private static final String KEY_NAME = "ul_key";

    public static void showLog(Context c, String msg)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(c.getString(R.string.common_dialog_title));
        dialog.setCancelable(false);
        dialog.create().setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton(c.getString(R.string.common_dialog_confirm), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        dialog.show();
    }

    public static void showLog(Context c, String msg, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(c.getString(R.string.common_dialog_title));
        dialog.setCancelable(false);
        dialog.create().setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton(c.getString(R.string.common_dialog_confirm), listener);
        dialog.show();
    }

    public static void showConfirm(Context c, String msg, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setTitle(c.getString(R.string.dialog_delete_msg));
        dialog.setCancelable(false);
        dialog.create().setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.setPositiveButton(c.getString(R.string.common_dialog_yes), listener);
        dialog.setNegativeButton(c.getString(R.string.common_dialog_no), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public static boolean checkFingerPrintService(Context c, KeyguardManager km, FingerprintManager fm) {

        /**
         * KeyManager.isKeyguardSecure()：是否有設定screen lock
         * FingerprintManager.isHardwareDetected()：硬體是否有指紋辨識
         * FingerprintManager.hasEnrolledFingerprints()：是否有設定最少一枚指紋
         * */

        return km.isKeyguardSecure() && fm.isHardwareDetected() && fm.hasEnrolledFingerprints();
    }

    public static boolean cipherInit() {
        try {

            /**
             * KeyProperties.KEY_ALGORITHM_AES
             * KeyProperties.BLOCK_MODE_CBC
             * KeyProperties.ENCRYPTION_PADDING_PKCS7)
             * SecretKey:javax.crypto
             * requires API Level 23
             */

            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public static void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException e) {
            throw new RuntimeException(
                    "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Cipher getCipher()
    {
        return cipher;
    }

}
