package org.seasar.ymir.extension.creator;

import org.seasar.ymir.extension.creator.impl.ClassDescImpl;

public class EntityMetaData {
    private DescPool pool_;

    private String entityName_;

    private String dtoClassName_;

    private String beanClassName_;

    private String daoClassName_;

    private String dxoClassName_;

    private String converterClassName_;

    public EntityMetaData(DescPool pool, String className) {
        pool_ = pool;
        analyze(className);
    }

    void analyze(String className) {
        entityName_ = new ClassDescImpl(null, className).getNameBase();
        String subPackageName = getSubPackageName(className);
        dtoClassName_ = pool_.getSourceCreator().getDtoPackageName() + "."
                + subPackageName + entityName_ + ClassType.DTO.getSuffix();
        beanClassName_ = pool_.getSourceCreator().getDaoPackageName() + "."
                + entityName_;
        daoClassName_ = pool_.getSourceCreator().getDaoPackageName() + "."
                + entityName_ + ClassType.DAO.getSuffix();
        dxoClassName_ = pool_.getSourceCreator().getDxoPackageName() + "."
                + subPackageName + entityName_ + ClassType.DXO.getSuffix();
        converterClassName_ = pool_.getSourceCreator()
                .getConverterPackageName()
                + "."
                + subPackageName
                + entityName_
                + ClassType.CONVERTER.getSuffix();
    }

    String getSubPackageName(String className) {
        String name = className.substring(pool_.getSourceCreator()
                .getFirstRootPackageName().length() + 1);
        int start = name.indexOf('.') + 1;
        int end = name.lastIndexOf('.') + 1;
        return name.substring(start, end);
    }

    public String getEntityName() {
        return entityName_;
    }

    public ClassDesc newDtoClassDesc() {
        return pool_.getClassDesc(dtoClassName_);
    }

    public ClassDesc newBeanClassDesc() {
        return pool_.getClassDesc(beanClassName_);
    }

    public ClassDesc newDaoClassDesc() {
        return pool_.getClassDesc(daoClassName_);
    }

    public ClassDesc newDxoClassDesc() {
        return pool_.getClassDesc(dxoClassName_);
    }

    public ClassDesc newConverterClassDesc() {
        return pool_.getClassDesc(converterClassName_);
    }
}
