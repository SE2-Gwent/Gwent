package at.aau.se2.gwent.views.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import at.aau.se2.gwent.BuildConfig;

public class SettingsViewModel extends ViewModel {
  public String getVersionName() {
    return versionName;
  }

  public String getVersionCode() {
    return versionCode;
  }

  public MutableLiveData<ViewState> getCurrentState() {
    return currentState;
  }

  enum ViewState {
    INITIAL,
    LOADED
  }

  private MutableLiveData<ViewState> currentState = new MutableLiveData<>(ViewState.INITIAL);
  private final String versionName;
  private final String versionCode;

  public SettingsViewModel() {
    versionName = BuildConfig.VERSION_NAME;
    versionCode = String.valueOf(BuildConfig.VERSION_CODE);

    currentState.setValue(ViewState.LOADED);
  }
}
