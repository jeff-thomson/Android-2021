package com.stych.android.dagger;


import com.stych.android.home.ExoPlayerActivity;
import com.stych.android.home.MainActivity;
import com.stych.android.home.lifefilelist.LifeFileListActivity;
import com.stych.android.home.transferlifefile.TransferALifeFileActivity;
import com.stych.android.profile.ProfileActivity;
import com.stych.android.profile.changeemail.ChangeEmailActivity;
import com.stych.android.profile.changepassword.ChangePasswordActivity;
import com.stych.android.profile.subscription.SubscriptionActivity;
import com.stych.android.start.forgotpassword.ForgotPasswordActivity;
import com.stych.android.start.login.LoginActivity;
import com.stych.android.start.resetpassword.ResetPasswordActivity;
import com.stych.android.start.signup.SignUpActivity;
import com.stych.android.start.splash.SplashActivity;
import com.stych.android.start.thankyouforsignup.ThankYouActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector()
    abstract SignUpActivity contributeSignUpActivity();

    @ContributesAndroidInjector()
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector()
    abstract ForgotPasswordActivity contributeForgotPasswordActivity();

    @ContributesAndroidInjector()
    abstract ThankYouActivity contributeThankYouActivity();

    @ContributesAndroidInjector()
    abstract ChangeEmailActivity contributeChangeEmailActivity();

    @ContributesAndroidInjector()
    abstract ChangePasswordActivity contributeChangePasswordActivity();

    @ContributesAndroidInjector()
    abstract SubscriptionActivity contributeSubscriptionActivity();

    @ContributesAndroidInjector()
    abstract ProfileActivity contributeProfileActivity();

    @ContributesAndroidInjector()
    abstract ExoPlayerActivity contributeExoPlayerActivity();

    @ContributesAndroidInjector()
    abstract LifeFileListActivity contributeLifeFileListActivity();

    @ContributesAndroidInjector()
    abstract TransferALifeFileActivity contributeTransferALifeFileActivity();

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector()
    abstract ResetPasswordActivity contributeResetPasswordActivity();
}
