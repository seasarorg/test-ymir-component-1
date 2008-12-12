package ${rootPackageName};

import java.util.HashMap;
import java.util.Map;

import org.seasar.ymir.vili.Configurator;
import org.seasar.ymir.vili.ViliBehavior;
import org.seasar.ymir.vili.ViliProjectPreferences;

public class ConfiguratorImpl implements Configurator {
    public Map<String, Object> createAdditionalParameters(
            ViliBehavior behavior, ViliProjectPreferences preferences) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        // TODO 追加のパラメータをparametersにputする処理を書いて下さい。
        return parameters;
    }
}
