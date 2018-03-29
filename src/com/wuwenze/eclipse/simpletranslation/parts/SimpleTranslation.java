package com.wuwenze.eclipse.simpletranslation.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.wuwenze.eclipse.simpletranslation.SimpleTranslationActivator;
import com.wuwenze.eclipse.simpletranslation.preferences.PreferenceConstants;
import com.wuwenze.eclipse.simpletranslation.util.StringUtil;
import com.wuwenze.eclipse.simpletranslation.util.YoudaoUtil;

public class SimpleTranslation {
    private Text sourceText = null, resultText = null;
    private IPreferenceStore preferenceStore = SimpleTranslationActivator.getDefault().getPreferenceStore();

    @PostConstruct
    public void createPartControl(Composite parent) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = preferenceStore.getInt(PreferenceConstants.VIEW_LAYOUT_TYPE);
        gridLayout.makeColumnsEqualWidth = true;
        parent.setLayout(gridLayout);

        GridData gd = new GridData(GridData.FILL_BOTH);
        sourceText = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        sourceText.setLayoutData(gd);
        sourceText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent event) {
                resultText.setText("");
                sourceText.getDisplay().asyncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        String source = sourceText.getText().trim();
                        if (!StringUtil.isEmpty(source)) {
                            String appKey = preferenceStore.getString(PreferenceConstants.YOUDAO_APP_KEY);
                            String appSecret = preferenceStore.getString(PreferenceConstants.YOUDAO_APP_SECRET);
                            if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(appSecret)) {
                                resultText.setText("Please setup YOUDAO_APP_KEY / YOUDAO_APP_SECRET\n[Window -> Preference -> SimpleTranslation]");
                                return;
                            }
                            String translate = YoudaoUtil.translate(appKey,appSecret,source);
                            if (!StringUtil.isEmpty(translate)) {
                                resultText.setText(YoudaoUtil.analysis(translate).trim());
                            }
                        }
                        
                    }
                });
            }
        });

        gd = new GridData(GridData.FILL_BOTH);
        resultText = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        resultText.setEditable(false);
        resultText.setLayoutData(gd);
    }
}
