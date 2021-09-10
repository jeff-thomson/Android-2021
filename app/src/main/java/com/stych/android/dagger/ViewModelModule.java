package com.stych.android.dagger;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stych.android.home.MainViewModel;
import com.stych.android.home.lifefilelist.LifeFileListViewModel;
import com.stych.android.home.transferlifefile.TransferLifeFileViewModel;
import com.stych.android.profile.ProfileViewModel;
import com.stych.android.start.forgotpassword.ForgotViewModel;
import com.stych.android.start.login.LoginViewModel;
import com.stych.android.start.signup.RegisterViewModel;
import com.stych.android.start.thankyouforsignup.ThankYouViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    /*

     */
    /*
     * This method basically says
     * inject this object into a Map using the @IntoMap annotation,
     * with the  LoginViewModel.class as key,
     * and a Provider that will build a LoginViewModel
     * object.
     *
     * */

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    protected abstract ViewModel loginViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    protected abstract ViewModel mainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    protected abstract ViewModel profileViewModel(ProfileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    protected abstract ViewModel registerViewModel(RegisterViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ForgotViewModel.class)
    protected abstract ViewModel forgotViewModel(ForgotViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LifeFileListViewModel.class)
    protected abstract ViewModel lifeFileListViewModel(LifeFileListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TransferLifeFileViewModel.class)
    protected abstract ViewModel transferLifeFileViewModel(TransferLifeFileViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ThankYouViewModel.class)
    protected abstract ViewModel thankYouViewModel(ThankYouViewModel viewModel);
}

