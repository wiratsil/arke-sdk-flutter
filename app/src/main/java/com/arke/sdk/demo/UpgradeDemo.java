package com.arke.sdk.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.arke.sdk.R;
import com.arke.sdk.api.TMS;
import com.usdk.apiservice.aidl.tms.OnResultListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Upgrade demo.
 */

public class UpgradeDemo extends ApiDemo {

    private static final String TAG = "UpgradeDemo";
    private static final String SDCARD_PATH = "/sdcard/";
    private static final String DLL_SUB_PATH = "Download/";
    private static final String APK_SUB_PATH = "Podcasts/";
    private static final String UNS_FILE_NAME = "TestApplication-v2.0.uns";
    private static final String PKG_FILE_NAME = "APOSOVS_ShellApk-1.0.0.pkg";
    private static final String APK_FILE_NAME = "TestApplication-v1.0.apk";
    private static final String APK_PACKAGE_NAME = "com.example.test.testapplication";

    /**
     * Constructor.
     */
    private UpgradeDemo(Context context, Toast toast, AlertDialog dialog) {
        super(context, toast, dialog);
    }

    /**
     * Get upgrade demo instance.
     */
    public static UpgradeDemo getInstance(Context context, Toast toast, AlertDialog dialog) {
        return new UpgradeDemo(context, toast, dialog);
    }

    /**
     * Do upgrade functions.
     */
    public void execute(String value) throws RemoteException {
        if (value.equals(getContext().getString(R.string.mock_download))) {
            mockDownload();

        } else if (value.equals(getContext().getString(R.string.offline_dll_upgrade))) {
            upgradeDll();

        } else if (value.equals(getContext().getString(R.string.install_apk))) {
            installApk();

        } else if (value.equals(getContext().getString(R.string.uninstall_apk))) {
            uninstallApk();
        }
    }

    /**
     * Uninstall APK.
     */
    private void uninstallApk() throws RemoteException {
        ArrayList<String> packageNames = new ArrayList<>();
        packageNames.add(APK_PACKAGE_NAME);
        showDialog(R.string.uninstalling, false);
        TMS.getInstance().uninstall(packageNames, new OnResultListener.Stub() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess");
                hideDialog();
                showToast(R.string.succeed);
            }

            @Override
            public void onError(List<Bundle> list) {
                StringBuilder errorMessage = new StringBuilder();
                for (Bundle bundle : list) {
                    errorMessage.append(bundle.getString("errorMessage"));
                }
                Log.d(TAG, "onError: " + errorMessage.toString());
                hideDialog();
                showToast(R.string.failed);
            }
        });
    }

    /**
     * Install APK.
     */
    private void installApk() throws RemoteException {
        showDialog(R.string.installing, false);
        String path = SDCARD_PATH + APK_SUB_PATH;
        TMS.getInstance().install(path, new OnResultListener.Stub() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess");
                hideDialog();
                showToast(R.string.succeed);
            }

            @Override
            public void onError(List<Bundle> list) {
                StringBuilder errorMessage = new StringBuilder();
                for (Bundle bundle : list) {
                    errorMessage.append(bundle.getString("errorMessage"));
                }
                Log.d(TAG, "onError: " + errorMessage.toString());
                hideDialog();
                showToast(R.string.failed);
            }
        });
    }

    /**
     * Mock download.
     */
    private void mockDownload() throws RemoteException {
        copyAssetsFileToSdcard(DLL_SUB_PATH, UNS_FILE_NAME);
        copyAssetsFileToSdcard(DLL_SUB_PATH, PKG_FILE_NAME);
        copyAssetsFileToSdcard(APK_SUB_PATH, APK_FILE_NAME);
        showToast(R.string.succeed);
    }

    /**
     * Upgrade DLL.
     */
    private void upgradeDll() throws RemoteException {
        showDialog(R.string.upgrading, false);
        String path = SDCARD_PATH + DLL_SUB_PATH;
        TMS.getInstance().install(path, new OnResultListener.Stub() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess");
                hideDialog();
                showToast(R.string.succeed);
            }

            @Override
            public void onError(List<Bundle> list) {
                StringBuilder errorMessage = new StringBuilder();
                for (Bundle bundle : list) {
                    errorMessage.append(bundle.getString("errorMessage"));
                }
                Log.d(TAG, "onError: " + errorMessage.toString());
                hideDialog();
                showToast(R.string.failed);
            }
        });
    }

    /**
     * Copy assets file.
     */
    private void copyAssetsFileToSdcard(String subPath, String fileName) throws RemoteException {
        int byteRead;
        InputStream input = null;
        try {
            input = getContext().getAssets().open(fileName);
            FileOutputStream fs = new FileOutputStream(SDCARD_PATH + subPath + fileName);
            byte[] buffer = new byte[input.available()];
            while ((byteRead = input.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            input.close();
        } catch (IOException e) {
            throw new RemoteException(e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    showToast(e.getLocalizedMessage());
                }
            }
        }
    }
}
