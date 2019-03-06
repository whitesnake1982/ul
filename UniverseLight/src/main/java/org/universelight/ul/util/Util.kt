package org.universelight.ul.util

import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi

import org.universelight.ul.R

import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

/**
 * Created by whitesnake on 2016/7/12.
 */
object Util {
    private var keyStore: KeyStore? = null
    @JvmField
    var cipher: Cipher? = null
    private val KEY_NAME = "ul_key"

    fun showLog(c: Context, msg: String) {
        val dialog = AlertDialog.Builder(c)
        dialog.setTitle(c.getString(R.string.common_dialog_title))
        dialog.setCancelable(false)
        dialog.create().setCanceledOnTouchOutside(false)
        dialog.setMessage(msg)
        dialog.setPositiveButton(c.getString(R.string.common_dialog_confirm)) { _, _ -> }
        dialog.show()
    }

    fun showLog(c: Context, msg: String, listener: DialogInterface.OnClickListener) {
        val dialog = AlertDialog.Builder(c)
        dialog.setTitle(c.getString(R.string.common_dialog_title))
        dialog.setCancelable(false)
        dialog.create().setCanceledOnTouchOutside(false)
        dialog.setMessage(msg)
        dialog.setPositiveButton(c.getString(R.string.common_dialog_confirm), listener)
        dialog.show()
    }

    fun showConfirm(c: Context, msg: String, listener: DialogInterface.OnClickListener) {
        val dialog = AlertDialog.Builder(c)
        dialog.setTitle(c.getString(R.string.dialog_delete_msg))
        dialog.setCancelable(false)
        dialog.create().setCanceledOnTouchOutside(false)
        dialog.setMessage(msg)
        dialog.setPositiveButton(c.getString(R.string.common_dialog_yes), listener)
        dialog.setNegativeButton(c.getString(R.string.common_dialog_no)) { dialog, _ -> dialog.cancel() }

        dialog.show()
    }

    fun checkFingerPrintService(c: Context, km: KeyguardManager, fm: FingerprintManager): Boolean {

        /**
         * KeyManager.isKeyguardSecure()：是否有設定screen lock
         * FingerprintManager.isHardwareDetected()：硬體是否有指紋辨識
         * FingerprintManager.hasEnrolledFingerprints()：是否有設定最少一枚指紋
         */

        return km.isKeyguardSecure && fm.isHardwareDetected && fm.hasEnrolledFingerprints()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun cipherInit(): Boolean {
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
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore!!.load(null)
            val key = keyStore!!.getKey(KEY_NAME, null) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val keyGenerator: KeyGenerator
        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(
                    "Failed to get KeyGenerator instance", e)
        } catch (e: NoSuchProviderException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }

        try {
            keyStore!!.load(null)
            keyGenerator.init(KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build())
            keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }
}
