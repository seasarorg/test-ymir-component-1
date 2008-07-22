package org.seasar.ymir.extension.creator;

import org.seasar.ymir.extension.creator.impl.SimpleClassDesc;

public class EntityMetaData {
    private SourceCreator creator_;

    private String entityName_;

    private String dtoClassName_;

    private String beanClassName_;

    private String daoClassName_;

    private String dxoClassName_;

    private String converterClassName_;

    public EntityMetaData(SourceCreator creator, String className) {
        creator_ = creator;
        analyze(className);
    }

    void analyze(String className) {
        entityName_ = new SimpleClassDesc(className).getNameBase();
        String subPackageName = getSubPackageName(className);
        dtoClassName_ = creator_.getDtoPackageName() + "." + subPackageName
                + entityName_ + ClassType.DTO.getSuffix();
        beanClassName_ = creator_.getDaoPackageName() + "." + entityName_;
        daoClassName_ = creator_.getDaoPackageName() + "." + entityName_
                + ClassType.DAO.getSuffix();
        dxoClassName_ = creator_.getDxoPackageName() + "." + subPackageName
                + entityName_ + ClassType.DXO.getSuffix();
        converterClassName_ = creator_.getConverterPackageName() + "."
                + subPackageName + entityName_
                + ClassType.CONVERTER.getSuffix();
    }

    String getSubPackageName(String className) {
        String name = className.substring(creator_.getRootPackageName()
                .length() + 1);
        int start = name.indexOf('.') + 1;
        int end = name.lastIndexOf('.') + 1;
        return name.substring(start, end);
    }

    public String getEntityName() {
        return entityName_;
    }

    public ClassDesc getDtoClassDesc() {
        return creator_.newClassDesc(dtoClassName_);
    }

    public ClassDesc getBeanClassDesc() {
        return creator_.newClassDesc(beanClassName_);
    }

    public ClassDesc getDaoClassDesc() {
        return creator_.newClassDesc(daoClassName_);
    }

    public ClassDesc getDxoClassDesc() {
        return creator_.newClassDesc(dxoClassName_);
    }

    public ClassDesc getConverterClassDesc() {
        return creator_.newClassDesc(converterClassName_);
    }
}
