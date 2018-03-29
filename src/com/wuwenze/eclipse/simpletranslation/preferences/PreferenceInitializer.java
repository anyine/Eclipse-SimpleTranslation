package com.wuwenze.eclipse.simpletranslation.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.wuwenze.eclipse.simpletranslation.SimpleTranslationActivator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = SimpleTranslationActivator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.YOUDAO_APP_KEY, "1612bf5e8ada8acfc");
        store.setDefault(PreferenceConstants.YOUDAO_APP_SECRET, "dfXDndLOWBXwHIlzi6lQMtbpWnuS7CRZc");
        store.setDefault(PreferenceConstants.VIEW_LAYOUT_TYPE, 1);
    }
}