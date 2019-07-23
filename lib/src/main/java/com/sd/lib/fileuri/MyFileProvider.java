package com.sd.lib.fileuri;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class MyFileProvider extends FileProvider
{
    public static Uri getUri(Context context, File file)
    {
        if (file == null)
            return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return getUriForFile(context,
                    context.getPackageName() + "." + MyFileProvider.class.getSimpleName().toLowerCase(),
                    file);
        } else
        {
            return Uri.fromFile(file);
        }
    }
}
