package org.seasar.ymir.scaffold.dbflute.allcommon;

import org.seasar.dbflute.Entity;

/**
 * The interface of entity defined common columns.
 * @author DBFlute(AutoGenerator)
 */
public interface EntityDefinedCommonColumn extends Entity {

    /**
	 * Enable common column auto set up. <br />
	 * It's only for after disable because the default is enabled.
	 */
    void enableCommonColumnAutoSetup();

    /**
	 * Disable common column auto set up.
	 */
    void disableCommonColumnAutoSetup();

    /**
	 * Can the entity set up common column by auto? (basically for Framework)
	 * @return Determination.
	 */
	boolean canCommonColumnAutoSetup();
}
