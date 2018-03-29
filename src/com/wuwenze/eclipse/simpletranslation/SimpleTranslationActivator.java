package com.wuwenze.eclipse.simpletranslation;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SimpleTranslationActivator extends AbstractUIPlugin implements BundleActivator {

    private static BundleContext context;
    
    private static SimpleTranslationActivator activator;
    
    static BundleContext getContext() {
        return context;
    }

    /*
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        SimpleTranslationActivator.context = bundleContext;
        activator = this;
    }

    /*
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        SimpleTranslationActivator.context = null;
        activator = null;
    }

    public static SimpleTranslationActivator getDefault() {
        return activator;
    }
}
