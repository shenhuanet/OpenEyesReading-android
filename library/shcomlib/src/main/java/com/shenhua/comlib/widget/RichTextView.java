package com.shenhua.comlib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本框
 * Created by Shenhua on 8/21/2016.
 */
public class RichTextView extends TextView {

    private static Pattern IMG_TAG_PATTERN = Pattern.compile("\\<img(.*?)\\>");
    private static Pattern IMAGE_WIDTH_PATTERN = Pattern.compile("width=\"(.*?)\"");
    private static Pattern IMAGE_HEIGHT_PATTERN = Pattern.compile("height=\"(.*?)\"");
    private static Pattern IMAGE_SRC_PATTERN = Pattern.compile("src=\"(.*?)\"");
    private Drawable placeImage, errorImage;//占位图，错误图
    private HashSet<ImageTarget> targets;
    private HashMap<String, ImageHolder> mImages;
    private OnImageClickListener onImageClickListener;
    private OnURLClickListener onUrlClickListener;
    private OnImageFixListener onImageFixListener;

    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        targets = new HashSet<>();
//        placeHolder = ...;
//        errorImage = ...;
//        dw = ...;
//        dh = ...;
        if (placeImage == null) placeImage = new ColorDrawable(Color.GRAY);
        if (errorImage == null) errorImage = new ColorDrawable(Color.GRAY);
        int dw = 20;
        int dh = 20;
        placeImage.setBounds(0, 0, dw, dh);
        errorImage.setBounds(0, 0, dw, dh);
    }

    private void matchImages(String text) {
        mImages = new HashMap<>();
        ImageHolder holder;
        Matcher imgMatcher, srcMatcher, widthMatcher, heightMatcher;
        int position = 0;
        imgMatcher = IMG_TAG_PATTERN.matcher(text);
        while (imgMatcher.find()) {
            String img = imgMatcher.group().trim();
            srcMatcher = IMAGE_SRC_PATTERN.matcher(img);
            String src = null;
            if (srcMatcher.find()) {
                src = getTextBetweenQuotation(srcMatcher.group().trim().substring(4));
            }
            if (TextUtils.isEmpty("")) {
                continue;
            }
            holder = new ImageHolder(src, position);
            widthMatcher = IMAGE_WIDTH_PATTERN.matcher(img);
            if (widthMatcher.find()) {
                holder.width = parseStringToInteger(getTextBetweenQuotation(widthMatcher.group().trim().substring(6)));
            }
            heightMatcher = IMAGE_HEIGHT_PATTERN.matcher(img);
            if (heightMatcher.find()) {
                holder.height = parseStringToInteger(getTextBetweenQuotation(heightMatcher.group().trim().substring(6)));
            }
            mImages.put(holder.src, holder);
            position++;
        }
    }

    private void addTarget(ImageTarget target) {
        targets.add(target);
    }

    private int parseStringToInteger(String integerStr) {
        int result = -1;
        if (!TextUtils.isEmpty(integerStr)) {
            try {
                result = Integer.parseInt(integerStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Nullable
    private String getTextBetweenQuotation(String text) {
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private class CallableURLSpan extends URLSpan {
        private OnURLClickListener onURLClickListener;

        public CallableURLSpan(String url, OnURLClickListener onURLClickListener) {
            super(url);
            this.onURLClickListener = onURLClickListener;
        }

        public void onClick(View widget) {
            if (onURLClickListener != null && (onURLClickListener.urlClicked(getURL()))) return;
            super.onClick(widget);
        }
    }

    private class ImageTarget implements com.squareup.picasso.Target {

        private final URLDrawable urlDrawable;

        private ImageTarget(URLDrawable urlDrawable) {
            this.urlDrawable = urlDrawable;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            urlDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            urlDrawable.setDrawable(drawable);
            RichTextView.this.setText(getText());
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            urlDrawable.setBounds(errorDrawable.getBounds());
            urlDrawable.setDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            urlDrawable.setBounds(placeHolderDrawable.getBounds());
            urlDrawable.setDrawable(placeHolderDrawable);
        }
    }

    private static class ImageHolder {
        public static final int DEFAULT = 0;
        public static final int CENTER_CORP = 1;
        public static final int CENTER_INSIDE = 2;
        private final String src;
        private final int position;
        private int width = -1, height = -1;
        private int scaleType = DEFAULT;

//        @IntDef({DEFAULT, CENTER_CORP, CENTER_INSIDE})
//        public @interface ScaleType {
//
//        }

        public ImageHolder(String src, int position) {
            this.src = src;
            this.position = position;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getScaleType() {
            return scaleType;
        }

        public void setScaleType(int scaleType) {
            this.scaleType = scaleType;
        }
    }

    private static final class URLDrawable extends BitmapDrawable {
        private Drawable drawable;

        @SuppressWarnings("deprecation")
        public URLDrawable() {
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) drawable.draw(canvas);
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }

    private Html.ImageGetter asyncImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            RichTextView.URLDrawable urlDrawable = new RichTextView.URLDrawable();
            RichTextView.ImageTarget target = new RichTextView.ImageTarget(urlDrawable);
            RichTextView.this.addTarget(target);
            RichTextView.ImageHolder holder = mImages.get(source);
            RequestCreator load = Picasso.with(getContext()).load(source);
            if ((onImageFixListener != null) && (holder != null)) {
                onImageFixListener.onFix(holder);
                if ((holder.width != -1) && (holder != null))
                    load.resize(holder.width, holder.height);
                if (holder.scaleType == 1) load.centerCrop();
                else load.centerInside();
            }
            load.placeholder(placeImage).error(errorImage).into(target);
            return urlDrawable;
        }
    };

    public interface OnImageFixListener {
        void onFix(ImageHolder paramImageHolder);
    }

    public interface OnImageClickListener {
        void imageClicked(List<String> paramList, int paramInt);
    }

    public interface OnURLClickListener {
        boolean urlClicked(String paramString);
    }

    public ImageHolder getImageHolder(String url) {
        return mImages.get(url);
    }

    public void setRichText(String text) {
        targets.clear();
        matchImages(text);
        Spanned spanned = Html.fromHtml(text, asyncImageGetter, null);
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder)
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        else spannableStringBuilder = new SpannableStringBuilder(spanned);
        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), ImageSpan.class);
        final List imageUrls = new ArrayList();
        int i = 0;
        for (int size = imageSpans.length; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);
            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onImageClickListener != null)
                        onImageClickListener.imageClicked(imageUrls, finalI);
                }
            };
            ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(start, end, ClickableSpan.class);
            if ((clickableSpans != null) && (clickableSpans.length != 0)) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, start, end, 33);
        }
        URLSpan[] urlSpans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpan.class);
        i = 0;
        for (int size = urlSpans == null ? 0 : urlSpans.length; i < size; i++) {
            URLSpan urlSpan = urlSpans[i];
            int start = spannableStringBuilder.getSpanStart(urlSpan);
            int end = spannableStringBuilder.getSpanEnd(urlSpan);
            spannableStringBuilder.removeSpan(urlSpan);
            spannableStringBuilder.setSpan(new CallableURLSpan(urlSpan.getURL(), onUrlClickListener), start, end, 33);
        }
        super.setText(spanned);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setPlaceImage(Drawable placeImage, int dw, int dh) {
        this.placeImage = placeImage;
        this.placeImage.setBounds(0, 0, dw, dh);
    }

    public void setErrorImage(Drawable errorImage, int dw, int dh) {
        this.errorImage = errorImage;
        this.errorImage.setBounds(0, 0, dw, dh);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setOnImageFixListener(OnImageFixListener onImageFixListener) {
        this.onImageFixListener = onImageFixListener;
    }

    public void setOnUrlClickListener(OnURLClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
    }
}
