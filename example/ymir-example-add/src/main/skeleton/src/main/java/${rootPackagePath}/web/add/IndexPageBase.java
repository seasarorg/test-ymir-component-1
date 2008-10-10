package ${rootPackageName}.web.add;

import org.seasar.ymir.scope.annotation.RequestParameter;

public class IndexPageBase
{
    protected Integer left_;

    protected Integer result_;

    protected Integer right_;


    public Integer getLeft()
    {
        return left_;
    }

    @RequestParameter
    public void setLeft(Integer left)
    {
        left_ = left;
    }

    public Integer getResult()
    {
        return result_;
    }

    public Integer getRight()
    {
        return right_;
    }

    @RequestParameter
    public void setRight(Integer right)
    {
        right_ = right;
    }

    public void _get()
    {

    }

    public void _post()
    {

    }

    public void _prerender()
    {

    }
}
