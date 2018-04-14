package com.wuwenze.eclipse.simpletranslation.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.wuwenze.eclipse.simpletranslation.SimpleTranslationActivator;
import com.wuwenze.eclipse.simpletranslation.pojo.Constants;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer implements Constants {

    /*
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = SimpleTranslationActivator.getDefault().getPreferenceStore();
        store.setDefault(KEY_YOUDAO_APP_KEY, "");
        store.setDefault(KEY_YOUDAO_APP_SECRET, "");
    }
}