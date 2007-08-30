package com.example.web;

import org.seasar.ymir.Note;
import org.seasar.ymir.Notes;
import org.seasar.ymir.Request;
import org.seasar.ymir.annotation.SuppressConstraints;
import org.seasar.ymir.annotation.Validator;
import org.seasar.ymir.constraint.ConstraintType;
import org.seasar.ymir.constraint.ValidationFailedException;
import org.seasar.ymir.constraint.annotation.Fufu;
import org.seasar.ymir.constraint.annotation.Fuga;
import org.seasar.ymir.constraint.annotation.Fugas;

@Fuga("saru")
@Fufu("tora")
public class ConstraintInterceptorTestPage {
    private Request request_;

    private String fuga_;

    public void setRequest(Request request) {
        request_ = request;
    }

    @Fuga("fugar")
    public String getFuga() {
        return fuga_;
    }

    @Fuga("fuga")
    public void setFuga(String fuga) {
        fuga_ = fuga;
    }

    @Validator("_get_button1")
    public Notes validate1() {
        return new Notes().add(new Note("validator1"));
    }

    @Validator("_get_button1")
    public void validate2() throws ValidationFailedException {
        throw new ValidationFailedException(new Notes().add(new Note(
                "validator2")));
    }

    @Validator
    public Notes validate3() {
        request_.setAttribute("validator3", "validator3");
        return null;
    }

    @Validator
    public void validate4() throws ValidationFailedException {
        request_.setAttribute("validator4", "validator4");
    }

    @Validator("_get_button6")
    public void validate5(int param1, String param2) {
        request_.setAttribute("param1", Integer.valueOf(param1));
        request_.setAttribute("param2", param2);
    }

    @Fuga("button1")
    public void _get_button1() {
    }

    @SuppressConstraints
    @Fuga("button2")
    public void _get_button2() {
    }

    @SuppressConstraints(ConstraintType.VALIDATION)
    @Fuga("button3")
    public void _get_button3() {
    }

    @SuppressConstraints
    @Fugas( { @Fuga("button4_1"), @Fuga("button4_2") })
    public void _get_button4() {
    }

    public void _get_button5() {
    }

    public void _get_button6(int param1, String param2) {
    }

    @Fuga("render")
    public void _render() {
    }
}