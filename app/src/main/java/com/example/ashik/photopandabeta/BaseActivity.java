package com.example.ashik.photopandabeta;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ashik.photopandabeta.databinding.ActivityBaseBinding;


public class BaseActivity extends AppCompatActivity {

    protected StateMaintainer mStateMaintainer;
    protected SharedPreferenceManager mSharedPreferenceManager;
    private ActivityBaseBinding mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSharedPreferenceManager();
    }

    private void initSharedPreferenceManager() {
        mSharedPreferenceManager = new SharedPreferenceManager(this, "Panda");
    }

//    private void initToolBar(){
//
//        if(showToolBar()) {
//            setSupportActionBar(mBinding.toolBarLayout.toolbar);
//        } else {
//            mBinding.toolBarLayout.toolbar.setVisibility(View.GONE);
//        }
//
//        if(showBackButton()){
//            if(getSupportActionBar() != null){
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            }
//        }
//    }

    protected void init(String classTag){
        mStateMaintainer = new StateMaintainer(getSupportFragmentManager(), classTag);
    }

    protected void initialize(String viewTag, Object presenter){
        mStateMaintainer.put(viewTag, presenter);
    }

    protected <T> T isPresenterPresent(String viewTag){
        return mStateMaintainer.get(viewTag);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mStateMaintainer = null;
    }

//    protected void showOrdinarySnackBar(View view, String message) {
//
//        final Snackbar snackbar = AdaniUtility.getSnackBar(view, message);
//        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackbar.dismiss();
//            }
//        });
//        snackbar.show();
//    }

//    protected String getUserId(){
//
//        if(mSharedPreferenceManager != null){
//            return mSharedPreferenceManager.getValue(AdaniConstants.USER_ID, "");
//        } else {
//            return "";
//        }
//    }

//    protected void showSnackBar(String message) {
//        showSnackBar(mBinding.getRoot(), message);
//    }
//
//    protected void showSnackBar(View view, String message) {
//        showOrdinarySnackBar(view, message);
//    }
//
//    private void showForbiddenSnackBar(View view){
//        AdaniUtility.showForbiddenSnackBar(this,view, getString(R.string.forbidden));
//    }
//
//    private void showForbiddenSnackBar() {
//        AdaniUtility.showForbiddenSnackBar(this, mBinding.getRoot(), getString(R.string.forbidden));
//    }
//
//    protected boolean showToolBar(){
//        return true;
//    }
//
//    protected boolean showBackButton(){
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        // Inflated activity base.
        View rootView = getRootView();
        // get reference to root view in base activity and added activity view to it.
        LinearLayout activityContainer = (LinearLayout) rootView.findViewById(R.id.baseRootLinearLayout);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        initView(rootView);
    }

    @Override
    public void setContentView(View view) {

        // Inflated activity base.
        View rootView = getRootView();
        // To make the view(Child activities layout) fill the entire screen.
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(layoutParams);
        // get reference to root view in base activity and added activity view to it.
        LinearLayout activityContainer = (LinearLayout) rootView.findViewById(R.id.baseRootLinearLayout);
        activityContainer.addView(view);
        initView(rootView);
    }

    /**
     * Function to init binding, set content view and init toolbar
     * @param  - Root view of base activity.
     */
    private void initView(View rootView){

        mBinding = DataBindingUtil.bind(rootView);
        super.setContentView(rootView);
//        initToolBar();
    }
//
//    /**
//     * Function to obtain root view.
//     * @return Root View
//     */
    public View getRootView(){
        return getLayoutInflater().inflate(
                R.layout.activity_base,
                null
        );
    }
//
//    public String getAccessToken(){
//        if(mSharedPreferenceManager != null){
//            return mSharedPreferenceManager.getValue(AdaniConstants.ACCESS_TOKEN, "");
//        }
//        return "";
//    }
//
//    protected void setToolbarBackground(int resId) {
//        mBinding.toolBarLayout.toolbar.setBackgroundColor(ContextCompat.getColor(this, resId));
//    }
//
//    protected Toolbar getToolBar(){
//        return mBinding.toolBarLayout.toolbar;
//    }
//
//    public String getmConversationId() {
//        return mConversationId;
//    }
//
//    public void setmConversationId(String mConversationId) {
//        this.mConversationId = mConversationId;
//    }
}
