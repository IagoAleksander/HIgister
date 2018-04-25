package com.iaz.higister.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;

import io.reactivex.Single;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * Created by estevao on 06/10/17.
 */

public class CompressorUtil {
    public static void compress(String uri, final CompressListener compressListener) {
            Luban.compress(ApplicationUtil.getContext(), new File(uri.replace("file://", "")))
                    .putGear(Luban.CUSTOM_GEAR)
                    .setMaxSize(1024)
                    .launch(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            compressListener.onCompressSuccess(file);

                        }

                        @Override
                        public void onError(Throwable e) {
                            compressListener.onCompressError();
                            e.printStackTrace();
                        }
                    });
    }


    public interface CompressListener {
        void onCompressSuccess(File file);

        void onCompressError();
    }
}
