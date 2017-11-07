package com.shenhua.comlib.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.shenhua.comlib.annotation.ActivityFragmentInject;

/**
 * Fragment基类
 * Created by Shenhua on 8/21/2016.
 */
public abstract class BaseFragment extends Fragment {

    NetworkReceiver netReceiver;
    private View rootView;
    private boolean hasOptionsMenu;
    private int mToolbarId;
    private int mToolbarTitle;
    private int mMenuId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
            ActivityFragmentInject annotation = getClass().getAnnotation(ActivityFragmentInject.class);
            hasOptionsMenu = annotation.hasOptionsMenu();
        }
        setHasOptionsMenu(hasOptionsMenu);
        netReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(netReceiver, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == rootView) {
            int mContentViewId;
            if (getClass().isAnnotationPresent(ActivityFragmentInject.class)) {
                ActivityFragmentInject annotation = getClass().getAnnotation(ActivityFragmentInject.class);
                mContentViewId = annotation.contentViewId();
                mToolbarId = annotation.toolbarId();
                mToolbarTitle = annotation.toolbarTitle();
                mMenuId = annotation.menuId();
            } else {
                throw new RuntimeException("BaseFragment:Class must add annotations of ActivityFragmentInitParams.class");
            }
            rootView = inflater.inflate(mContentViewId, container, false);
            initView(rootView);
            initToolbar();
        }
        return rootView;
    }

    public abstract void initView(View rootView);

    private void initToolbar() {
        if (mToolbarId == -1) return;
        Toolbar toolbar = (Toolbar) rootView.findViewById(mToolbarId);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = getToolbar();
        assert ab != null;
        ab.setTitle("");
    }

    public void setToolbarTitle(@IdRes int titleId) {
        TextView textView = (TextView) rootView.findViewById(titleId);
        if (textView != null) textView.setText(mToolbarTitle);
    }

    public void setToolbarTitle(String str, @IdRes int titleId) {
        TextView textView = (TextView) rootView.findViewById(titleId);
        if (textView != null) textView.setText(str);
    }

    protected ActionBar getToolbar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    protected void showSnackBar(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackBar(int id) {
        Snackbar.make(rootView, id, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mMenuId != -1)
            inflater.inflate(mMenuId, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (null != parent) {
            parent.removeView(rootView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(netReceiver);
    }

    public class NetworkReceiver extends BroadcastReceiver {
        public NetworkReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {// 有网
                onNetWorkIsOk();
            } else {
                onNetWorkIsError();
            }
        }
    }

    protected void onNetWorkIsOk() {

    }

    protected void onNetWorkIsError() {

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
