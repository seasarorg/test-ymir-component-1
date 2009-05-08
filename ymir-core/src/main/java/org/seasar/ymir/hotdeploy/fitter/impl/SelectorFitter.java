package org.seasar.ymir.hotdeploy.fitter.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.ymir.render.Candidate;
import org.seasar.ymir.render.Selector;

@SuppressWarnings("unchecked")
public class SelectorFitter extends AbstractFitter<Selector> {
    public Class<Selector> getTargetClass() {
        return Selector.class;
    }

    public void fitContent(Selector value) {
        Candidate[] candidates = value.getCandidates();
        if (candidates != null) {
            List<Candidate> list = new ArrayList<Candidate>();
            for (Candidate candidate : candidates) {
                list.add((Candidate) getHotdeployManager().fit(candidate));
            }
            value.setCandidateList(list);
        }
    }
}
