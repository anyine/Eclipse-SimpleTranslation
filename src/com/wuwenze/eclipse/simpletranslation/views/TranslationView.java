package com.wuwenze.eclipse.simpletranslation.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.wuwenze.eclipse.simpletranslation.SimpleTranslationActivator;
import com.wuwenze.eclipse.simpletranslation.pojo.Constants;
import com.wuwenze.eclipse.simpletranslation.util.StringUtil;
import com.wuwenze.eclipse.simpletranslation.util.YoudaoUtil;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

@SuppressWarnings("serial")
public class TranslationView extends ViewPart implements Constants, ModifyListener {

    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "com.wuwenze.eclipse.simpletranslation.views.TranslationView";
    private Button btnCheckAuto;
    private Text textQuery, textResult;
    private Combo cboTranslationModelFrom, cboTranslationModelTo;
    private IPreferenceStore preferenceStore = SimpleTranslationActivator.getDefault().getPreferenceStore();

    private final static List<Map<String, String>> mTranslationModelDataSource = new ArrayList<>();
    static {
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("auto", "自动检测");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("zh-CHS", "中文");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("ja", "日文");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("EN", "英文");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("ko", "韩文");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("fr", "法文");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("ru", "俄文");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("pt", "葡萄牙文");
            }
        });
        mTranslationModelDataSource.add(new HashMap() {
            {
                put("es", "西班牙文");
            }
        });
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(4, true));

        cboTranslationModelFrom = new Combo(parent, SWT.READ_ONLY);
        cboTranslationModelFrom.addModifyListener(this);
        cboTranslationModelFrom.setToolTipText(TOOLTIP_cboTranslationModelFrom);
        cboTranslationModelFrom.setItems(getTranslationModelItems());
        cboTranslationModelFrom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cboTranslationModelFrom.select(0);

        cboTranslationModelTo = new Combo(parent, SWT.READ_ONLY);
        cboTranslationModelTo.addModifyListener(this);
        cboTranslationModelTo.setToolTipText(TOOLTIP_cboTranslationModelTo);
        cboTranslationModelTo.setItems(getTranslationModelItems());
        cboTranslationModelTo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        cboTranslationModelTo.select(0);

        Button btnTranslation = new Button(parent, SWT.FLAT);
        btnTranslation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                btnTranslation.getDisplay().asyncExec(() -> doTranslationAction());
            }
        });
        btnTranslation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
        btnTranslation.setText(TEXT_btnTranslation);

        btnCheckAuto = new Button(parent, SWT.CHECK);
        btnCheckAuto.setSelection(true);
        btnCheckAuto.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                preferenceStore.setValue(KEY_OPEN_AUTO_TRANSLATION, btnCheckAuto.getSelection());
            }
        });
        btnCheckAuto.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, true, false, 1, 1));
        btnCheckAuto.setText(TEXT_btnCheckAuto);

        textQuery = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        textQuery.addModifyListener(this);
        GridData gd_textQuery = new GridData(GridData.FILL_BOTH);
        gd_textQuery.horizontalSpan = 2;
        gd_textQuery.widthHint = 464;
        textQuery.setLayoutData(gd_textQuery);

        textResult = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        textResult.setEditable(false);
        GridData gd_textResult = new GridData(GridData.FILL_BOTH);
        gd_textResult.grabExcessHorizontalSpace = false;
        gd_textResult.horizontalSpan = 2;
        textResult.setLayoutData(gd_textResult);
    }

    private void doTranslationAction() {
        textResult.setText("");
        String query = textQuery.getText().trim();
        if (!StringUtil.isEmpty(query)) {
            String appKey = preferenceStore.getString(KEY_YOUDAO_APP_KEY);
            String appSecret = preferenceStore.getString(KEY_YOUDAO_APP_SECRET);
            String from = getTranslationMode(cboTranslationModelFrom.getText());
            String to = getTranslationMode(cboTranslationModelTo.getText());
            textResult.setText(YoudaoUtil.translate(appKey, appSecret, from, to, query));
        }
    }

    private static String[] mTranslationModelItems = null;

    private String[] getTranslationModelItems() {
        if (null == mTranslationModelItems) {
            List<String> textList = new ArrayList<>();
            mTranslationModelDataSource.forEach((map) -> {
                map.keySet().forEach((key) -> {
                    textList.add(map.get(key));
                });
            });
            mTranslationModelItems = textList.toArray(new String[textList.size()]);
        }
        return mTranslationModelItems;
    }

    private String getTranslationMode(String text) {
        for (Map<String, String> map : mTranslationModelDataSource) {
            for (String key : map.keySet()) {
                if (map.get(key).equals(text)) {
                    return key;
                }
            }
        }
        return "auto";
    }

    @Override
    public void modifyText(ModifyEvent arg0) {
        if (null != btnCheckAuto && btnCheckAuto.getSelection()) {
            btnCheckAuto.getDisplay().asyncExec(() -> doTranslationAction());
        }
    }

    @Override
    public void setFocus() {}
}
