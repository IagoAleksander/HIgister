package com.iaz.HIgister.util;

import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.iaz.HIgister.ui.main.MainActivity;

/**
 * Created by alksander on 24/05/2018.
 */

public final class DynamicLinkUtil {

    public static Uri createDynamicLinkToList(String id) {
        String deepLink = "http://higister.com/page?listId=" +id;

        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDynamicLinkDomain("ra892.app.goo.gl")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .setFallbackUrl(Uri.parse("http://play.google.com/store/apps/details?id=com.iaz.Higister"))
                        .build())
                .setLink(Uri.parse(deepLink));

        // Build the dynamic link
        DynamicLink link = builder.buildDynamicLink();
        return link.getUri();

        // Or create a shortened dynamic link
//        builder.buildShortDynamicLink()
//                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
//                    @Override
//                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
//                        ShareDialog shareDialog;
//                        shareDialog = new ShareDialog(MainActivity.this);
//                        ShareLinkContent content = new ShareLinkContent.Builder()
//                                .setContentUrl(shortDynamicLink.getShortLink())
//                                .build();
//                        shareDialog.show(content);
//                    }
//                });
    }
}
