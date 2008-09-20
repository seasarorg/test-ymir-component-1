package ${rootPackageName}.web.add;

import org.seasar.ymir.constraint.annotation.Required;

public class IndexPage extends IndexPageBase {
    @Override
    @Required( { "left", "right" })
    public void _post() {
        result_ = left_ + right_;
    }
}
