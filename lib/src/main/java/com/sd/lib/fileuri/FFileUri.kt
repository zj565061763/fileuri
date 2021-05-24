package com.sd.lib.fileuri;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class FFileUri
{
    private FFileUri()
    {
    }

    public static Uri get(Context context, File file)
    {
        if (file == null)
            return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return FileProvider.getUriForFile(context,
                    context.getPackageName() + "." + FFileProvider.class.getSimpleName().toLowerCase(),
                    file);
        } else
        {
            return Uri.fromFile(file);
        }
    }
}
