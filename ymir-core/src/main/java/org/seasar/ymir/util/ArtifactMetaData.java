package org.seasar.ymir.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.seasar.kvasir.util.io.IOUtils;

public class ArtifactMetaData {
    private static final String KEY_GROUPID = "groupId";

    private static final String KEY_ARTIFACTID = "artifactId";

    private static final String KEY_VERSION = "version";

    private static final String UNKNOWN = "unknown";

    private String groupId_;

    private String artifactId_;

    private String version_;

    private String timestamp_;

    private static Log log_ = LogFactory.getLog(ArtifactMetaData.class);

    private ArtifactMetaData(String groupId, String artifactId, String version,
            String timestamp) {
        groupId_ = groupId;
        artifactId_ = artifactId;
        version_ = version;
        timestamp_ = timestamp;
    }

    public static ArtifactMetaData newInstance(URL resource) {
        if (resource == null) {
            return null;
        }

        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = resource.openStream();
            prop.load(is);
        } catch (IOException ex) {
            log_.warn("Can't get version information from '"
                    + resource.toExternalForm() + "'", ex);
        } finally {
            IOUtils.closeQuietly(is);
            is = null;
        }

        String timestamp = null;
        try {
            is = resource.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    "ISO-8859-1"));
            String line = br.readLine();
            line = br.readLine();
            if (line != null && line.startsWith("#")) {
                timestamp = line.substring(1/*= "#".length() */).trim();
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Can't happen!", ex);
        } catch (IOException ex) {
            log_.warn("Can't read '" + resource.toExternalForm() + "'", ex);
        } finally {
            IOUtils.closeQuietly(is);
        }

        return new ArtifactMetaData(prop.getProperty(KEY_GROUPID), prop
                .getProperty(KEY_ARTIFACTID), prop.getProperty(KEY_VERSION),
                timestamp);
    }

    public String getGroupId() {
        return groupId_;
    }

    public String getArtifactId() {
        return artifactId_;
    }

    public String getVersion() {
        return version_;
    }

    public String getTimestamp() {
        return timestamp_;
    }

    public String getSignature() {
        return (groupId_ != null ? groupId_ : UNKNOWN) + ":"
                + (artifactId_ != null ? artifactId_ : UNKNOWN) + "-"
                + (version_ != null ? version_ : UNKNOWN) + " ("
                + (timestamp_ != null ? timestamp_ : UNKNOWN) + ")";
    }
}
