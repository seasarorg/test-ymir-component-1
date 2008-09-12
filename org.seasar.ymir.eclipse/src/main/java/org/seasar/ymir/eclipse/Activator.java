package org.seasar.ymir.eclipse;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.seasar.ymir.eclipse.util.StreamUtils;

import werkzeugkasten.mvnhack.repository.Artifact;
import werkzeugkasten.mvnhack.repository.Configuration;
import werkzeugkasten.mvnhack.repository.Context;
import werkzeugkasten.mvnhack.repository.Repository;
import werkzeugkasten.mvnhack.repository.impl.DefaultConfiguration;
import werkzeugkasten.mvnhack.repository.impl.DefaultContext;
import freemarker.cache.TemplateLoader;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.seasar.ymir.eclipse";

    // The shared instance
    private static Activator plugin;

    private Configuration mvnHackConfig;

    private Context mvnHackContext;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        mvnHackConfig = new DefaultConfiguration(new Properties());
        mvnHackContext = new DefaultContext(mvnHackConfig);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        mvnHackConfig = null;
        mvnHackContext = null;
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public Artifact resolveArtifact(String groupId, String artifactId, String version, IProgressMonitor monitor)
            throws ArtifactNotFoundException {
        monitor.beginTask("Resolve project skeleton artifact", 1);
        try {
            return getArtifact(groupId, artifactId, version);
        } finally {
            monitor.done();
        }
    }

    public void expandSkeleton(IProject project, Artifact artifact, Map<String, String> parameterMap,
            IProgressMonitor monitor) throws IOException, CoreException {
        monitor.beginTask("Expand project skeleton", 1);

        URL artifactURL = getURL(artifact, "jar");
        if (artifactURL == null) {
            return;
        }

        File artifactFile = File.createTempFile("ymir", "ymir");
        artifactFile.deleteOnExit();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = artifactURL.openStream();
            os = new FileOutputStream(artifactFile);
            StreamUtils.copyStream(is, os);
        } finally {
            StreamUtils.close(is);
            StreamUtils.close(os);
        }
        final JarFile jarFile = new JarFile(artifactFile);
        try {
            freemarker.template.Configuration cfg = new freemarker.template.Configuration();
            cfg.setEncoding(Locale.getDefault(), "UTF-8");
            cfg.setTemplateLoader(new TemplateLoader() {

                public void closeTemplateSource(Object name) throws IOException {
                }

                public Object findTemplateSource(String name) throws IOException {
                    return jarFile.getEntry(name);
                }

                public long getLastModified(Object name) {
                    return 0;
                }

                public Reader getReader(Object templateSource, String encoding) throws IOException {
                    return new InputStreamReader(jarFile.getInputStream((JarEntry) templateSource), encoding);
                }
            });
            cfg.setObjectWrapper(new DefaultObjectWrapper());

            for (Enumeration<JarEntry> enm = jarFile.entries(); enm.hasMoreElements();) {
                JarEntry entry = enm.nextElement();
                String name = entry.getName();
                expand(project, name, cfg, parameterMap, jarFile, new SubProgressMonitor(monitor, 1));
            }
        } finally {
            jarFile.close();
        }

        monitor.done();
    }

    URL getURL(Artifact artifact, String type) {
        String suffix = "." + type;
        for (Repository repo : mvnHackConfig.getRepositories()) {
            for (URL url : repo.getLocation(artifact)) {
                if (url.toExternalForm().endsWith(suffix)) {
                    return url;
                }
            }
        }
        return null;
    }

    void expand(IProject project, String path, freemarker.template.Configuration cfg, Map<String, String> parameterMap,
            JarFile jarFile, IProgressMonitor monitor) throws IOException, CoreException {
        if (shouldIgnore(path)) {
            return;
        } else if (path.endsWith("/")) {
            mkdirs(project.getFolder(path), new SubProgressMonitor(monitor, 1));
        } else {
            InputStream in;
            if (shouldEvaluateAsTemplate(path)) {
                byte[] evaluated;
                try {
                    StringWriter sw = new StringWriter();
                    cfg.getTemplate(path).process(parameterMap, sw);
                    evaluated = sw.toString().getBytes("UTF-8");
                } catch (TemplateException ex) {
                    IOException ioex = new IOException();
                    ioex.initCause(ex);
                    throw ioex;
                }
                in = new ByteArrayInputStream(evaluated);
            } else {
                in = jarFile.getInputStream(jarFile.getEntry(path));
            }
            try {
                IFile outputFile = project.getFile(path);
                if (outputFile.exists()) {
                    outputFile.setContents(in, false, false, new SubProgressMonitor(monitor, 1));
                } else {
                    mkdirs(outputFile.getParent(), new SubProgressMonitor(monitor, 1));
                    outputFile.create(in, false, new SubProgressMonitor(monitor, 1));
                }
            } finally {
                StreamUtils.close(in);
            }
        }
    }

    private boolean shouldIgnore(String path) {
        return path.startsWith("META-INF/");
    }

    boolean shouldEvaluateAsTemplate(String path) {
        String name;
        int slash = path.lastIndexOf('/');
        if (slash < 0) {
            name = path;
        } else {
            name = path.substring(slash + 1);
        }
        return name.startsWith(".") || name.endsWith(".xml") || name.endsWith(".properties");
    }

    Artifact getArtifact(String groupId, String artifactId, String version) throws ArtifactNotFoundException {
        Artifact artifact = mvnHackContext.resolve(groupId, artifactId, version);
        if (artifact == null) {
            throw new ArtifactNotFoundException();
        }
        return artifact;
    }

    private void mkdirs(IResource container, IProgressMonitor monitor) throws CoreException {
        if (container.getType() != IResource.FOLDER) {
            return;
        }
        IFolder folder = (IFolder) container;
        if (!folder.exists()) {
            mkdirs(folder.getParent(), monitor);
            folder.create(false, true, new SubProgressMonitor(monitor, 1));
        }
    }
}
