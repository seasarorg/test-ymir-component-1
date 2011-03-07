package org.seasar.ymir.scaffold.dbflute.allcommon;

import javax.sql.DataSource;

import org.seasar.dbflute.DBDef;
import org.seasar.dbflute.cbean.ConditionBeanContext;
import org.seasar.dbflute.jdbc.DataSourceHandler;
import org.seasar.dbflute.s2dao.extension.TnSqlLogRegistry;
import org.seasar.dbflute.util.DfSystemUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author DBFlute(AutoGenerator)
 */
public class DBFluteInitializer {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** Log instance. */
    private static final Log _log = LogFactory.getLog(DBFluteInitializer.class);

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor. And initialize various components.
     */
    public DBFluteInitializer() {
        _log.info("...Initializing DBFlute components");
        handleSqlLogRegistry();
        loadCoolClasses();
        lockConfig();
    }

    protected void handleSqlLogRegistry() { // for S2Container
        if (DBFluteConfig.getInstance().isUseSqlLogRegistry()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("{SqlLog Information}").append(ln());
            sb.append("  [SqlLogRegistry]").append(ln());
            if (TnSqlLogRegistry.setupSqlLogRegistry()) {
                sb.append("    ...Setting up sqlLogRegistry(org.seasar.extension.jdbc)").append(ln());
                sb.append("    because the property 'useSqlLogRegistry' of the config of DBFlute is true");
            } else {
                sb.append("    The sqlLogRegistry(org.seasar.extension.jdbc) is not supported at the version");
            }
           _log.info(sb);
        } else {
            final Object sqlLogRegistry = TnSqlLogRegistry.findContainerSqlLogRegistry();
            if (sqlLogRegistry != null) {
                TnSqlLogRegistry.closeRegistration();
            }
        }
    }

    protected void loadCoolClasses() { // for S2Container 
        ConditionBeanContext.loadCoolClasses(); // against the ClassLoader Headache!
    }

    /**
     * Set up the handler of data source to the configuration of DBFlute. <br />
     * If it uses commons-DBCP, it needs to arrange some for transaction.
     * <ul>
     *     <li>A. To use DataSourceUtils which is Spring Framework class.</li>
     *     <li>B. To use TransactionConnection that is original class and doesn't close really.</li>
     * </ul>
     * If you use a transaction library which has a data source which supports transaction,
     * It doesn't need these arrangement. (For example, the framework 'Atomikos') <br />
     * This method should be executed when application is initialized.
     * @param dataSource The instance of data source. (NotNull)
     */
    protected void setupDataSourceHandler(DataSource dataSource) { // for Spring
        if (dataSource == null) {
            String msg = "The argument 'dataSource' should not be null!";
            throw new IllegalArgumentException(msg);
        }
        final DBFluteConfig config = DBFluteConfig.getInstance();
        final DataSourceHandler dataSourceHandler = config.getDataSourceHandler();
        if (dataSourceHandler != null) {
            return;
        }
        final String dataSourceFqcn = dataSource.getClass().getName();
        if (dataSourceFqcn.startsWith("org.apache.commons.dbcp.")) {
            config.unlock();
            config.setDataSourceHandler(new DBFluteConfig.SpringDBCPDataSourceHandler());
        }
    }

    protected void lockConfig() {
        if (!DBFluteConfig.getInstance().isLocked()) {
            DBFluteConfig.getInstance().lock();
        }
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    protected boolean isCurrentDBDef(DBDef currentDBDef) {
        return DBCurrent.getInstance().isCurrentDBDef(currentDBDef);
    }

    // ===================================================================================
    //                                                                      General Helper
    //                                                                      ==============
    protected String ln() {
        return DfSystemUtil.getLineSeparator();
    }
}
