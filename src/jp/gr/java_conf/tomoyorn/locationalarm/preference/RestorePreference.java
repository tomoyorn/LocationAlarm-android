package jp.gr.java_conf.tomoyorn.locationalarm.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.util.AttributeSet;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;

import java.util.ArrayList;

import jp.gr.java_conf.tomoyorn.locationalarm.R;
import jp.gr.java_conf.tomoyorn.locationalarm.model.Alarm;

// TODO SettingsActivityのネストクラスにできないかを検討
public class RestorePreference extends Preference {

    public RestorePreference(Context context) {
        super(context);
    }

    public RestorePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RestorePreference(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onClick() {
        super.onClick();
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(R.string.message_confirm_restoration);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearDatabase();
                        clearPreferences();
                        callChangeListener(null);
                    }
                });
        alert.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    private void clearDatabase() {
        new Delete().from(Alarm.class).execute();
    }

    private void clearPreferences() {
        getSharedPreferences().edit().clear().commit();
    }
}
//    protected void onClick() {
//        super.onClick();
//        // TODO 確認ダイアログを表示する
//        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//        alert.setMessage(R.string.message_confirm_restoration);
//        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO データベスをクリア
//                // TODO SharedPreferenceをクリア
//                getSharedPreferences().edit().clear().commit();
//
//                // TODO data/data/app_nameをすべてさくじょすればいいんじゃないか？
//                System.out.println("#### " + getContext().getPackageResourcePath());
//                System.out.println("#### " + getContext().getPackageName());
//                System.out.println("#### " + getContext().getPackageCodePath());
//                getContext().getPackageResourcePath();
//                getContext().getFilesDir();
//                getContext().getCacheDir();
//                getContext().deleteFile("");
//                try {
//                    FileUtils.cleanDirectory(new File("data/data/" + getContext().getPackageName()));
//                } catch (IOException e) {
//                    Log.e("Failed while restoring application.: cause=" + e.getMessage());
//                }
//
////                getContext().getPackageManager().
////                getContext().getDir(name, mode);
////                getContext().getFilesDir()
////                getContext().deleteDatabase(name)
////                getContext().deleteFile(name)
////                Toast.makeText(getContext(), "アプリケーションを初期化しました", Toast.LENGTH_LONG).show();
//                callChangeListener(null);
////                notifyChanged();
//            }});
//        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
//            public void onClick(DialogInterface dialog, int which) {
//            }});
//        alert.show();
//    }
//
//}
