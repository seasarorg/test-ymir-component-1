package org.seasar.ymir.render;

import static org.seasar.ymir.util.StringUtils.asString;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.ymir.util.StringUtils;

/**
 * 複数の候補からいくつかのものを選択するようなフォームのモデルとなるクラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class Selector implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String[] EMPTY_STRINGS = new String[0];

    private static final Candidate[] EMPTY_CANDIDATES = new Candidate[0];

    private String[] selectedValues_;

    private Candidate[] candidates_;

    private Map<String, Candidate> candidateMap_ = new HashMap<String, Candidate>();

    /**
     * 選択されている値を返します。
     * <p>複数選択されている場合は最初の1つだけを返します。
     * 1つも選択されていない場合はnullを返します。
     * </p>
     * 
     * @return 選択されている値。
     */
    public String getSelectedValue() {
        String[] values = getSelectedValues();
        if (values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }

    /**
     * 選択されている値を返します。
     * <p>1つも選択されていない場合は空の配列を返します。
     * </p>
     * 
     * @return 選択されている値。
     */
    public String[] getSelectedValues() {
        if (selectedValues_ == null) {
            if (candidates_ == null) {
                return EMPTY_STRINGS;
            } else {
                List<String> list = new ArrayList<String>();
                for (Candidate candidate : candidates_) {
                    if (candidate.isSelected()) {
                        list.add(candidate.getValue());
                    }
                }
                return list.toArray(EMPTY_STRINGS);
            }
        } else {
            if (candidates_ == null) {
                return selectedValues_;
            } else {
                List<String> list = new ArrayList<String>();
                for (String value : selectedValues_) {
                    if (candidateMap_.containsKey(value)) {
                        list.add(value);
                    }
                }
                return list.toArray(EMPTY_STRINGS);
            }
        }
    }

    /**
     * 選択されている値を設定します。
     * <p>{@link #setCandidates(Candidate...)}などによって
     * 候補オブジェクトが設定済みである場合、
     * 候補オブジェクトの選択状態がリセットされて更新されます。
     * </p>
     * 
     * @param values 値。nullを指定してはいけません。
     * @see #setSelectedValueObjects(Object...)
     */
    public void setSelectedValues(String... values) {
        selectedValues_ = values;

        updateCandidates();
    }

    /**
     * 選択されている値を設定します。
     * <p>このメソッドは{@link #setSelectedValues(value)と同じです。
     * </p>
     * 
     * @param values 値。nullを指定してはいけません。
     * @see #setSelectedValues(String...)
     */
    public void setSelectedValue(String value) {
        setSelectedValues(value);
    }

    /**
     * 選択されている値を設定します。
     * <p>{@link #setCandidates(Candidate...)}などによって
     * 候補オブジェクトが設定済みである場合、
     * 候補オブジェクトの選択状態がリセットされて更新されます。
     * </p>
     * <p>値は文字列に変換されて設定されます。
     * </p>
     * 
     * @param values 値。nullを指定してはいけません。
     * @see #setSelectedValues(String...)
     */
    public void setSelectedValueObjects(Object... valueObjects) {
        String[] values = new String[valueObjects.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = asString(valueObjects[i]);
        }
        setSelectedValues(values);
    }

    /**
     * 選択されている値を設定します。
     * <p>このメソッドは{@link #setSelectedValueObjects(value)と同じです。
     * </p>
     * 
     * @param values 値。nullを指定してはいけません。
     * @see #setSelectedValueObjects(Object...)
     */
    public void setSelectedValueObject(Object value) {
        setSelectedValueObjects(value);
    }

    /**
     * 選択されている候補オブジェクトを返します。
     * <p>複数選択されている場合は最初の1つだけを返します。
     * 1つも選択されていない場合はnullを返します。
     * </p>
     * 
     * @return 選択されている候補オブジェクト。
     */
    public <C extends Candidate> C getSelectedCandidate() {
        List<C> candidateList = getSelectedCandidateList();
        if (candidateList.isEmpty()) {
            return null;
        } else {
            return candidateList.get(0);
        }
    }

    /**
     * 選択されている候補オブジェクトを返します。
     * <p>1つも選択されていない場合は空の配列を返します。
     * </p>
     * 
     * @return 選択されている候補オブジェクト。
     */
    @SuppressWarnings("unchecked")
    public Candidate[] getSelectedCandidates() {
        // 本当は<C extends Candidate>[]を返すようにしたいが、1つもCandidateを持っていない場合に
        // 空のCの配列を生成できないため断念。
        return getSelectedCandidateList().toArray(EMPTY_CANDIDATES);
    }

    /**
     * 選択されている候補オブジェクトのList返します。
     * <p>1つも選択されていない場合は空のListを返します。
     * </p>
     * 
     * @return 選択されている候補オブジェクトのList。
     */
    @SuppressWarnings("unchecked")
    public <C extends Candidate> List<C> getSelectedCandidateList() {
        if (selectedValues_ == null) {
            if (candidates_ == null) {
                return Collections.emptyList();
            } else {
                List<C> list = new ArrayList<C>();
                for (Candidate candidate : candidates_) {
                    if (candidate.isSelected()) {
                        list.add((C) candidate);
                    }
                }
                return list;
            }
        } else {
            if (candidates_ == null) {
                return Collections.emptyList();
            } else {
                List<C> list = new ArrayList<C>();
                for (String value : selectedValues_) {
                    Candidate candidate = candidateMap_.get(value);
                    if (candidate != null) {
                        list.add((C) candidate);
                    }
                }
                return list;
            }
        }
    }

    /**
     * 全ての候補オブジェクトを返します。
     * <p>候補オブジェクトが設定されていない場合はnullを返します。
     * </p>
     * 
     * @return 全ての候補オブジェクト。
     */
    @SuppressWarnings("unchecked")
    public <C extends Candidate> C[] getCandidates() {
        return (C[]) candidates_;
    }

    /**
     * 候補オブジェクトを設定します。
     * <p>{@link #setSelectedValues(String...)}などによって
     * 選択された値が設定済みである場合、
     * 候補オブジェクトの選択状態がリセットされて更新されます。
     * 設定済みでない場合は候補オブジェクトの選択状態がそのまま残ります。
     * </p>
     * 
     * @param candidates 候補オブジェクト。nullを指定してはいけません。
     * @return このオブジェクト自身。
     */
    public Selector setCandidates(Candidate... candidates) {
        candidates_ = candidates;

        candidateMap_.clear();
        for (Candidate candidate : candidates_) {
            candidateMap_.put(candidate.getValue(), candidate);
        }

        updateCandidates();

        return this;
    }

    /**
     * 全ての候補オブジェクトのListを返します。
     * <p>候補オブジェクトが設定されていない場合はnullを返します。
     * </p>
     * 
     * @return 全ての候補オブジェクトのList。
     */
    @SuppressWarnings("unchecked")
    public <C extends Candidate> List<C> getCandidateList() {
        if (candidates_ == null) {
            return null;
        } else {
            return new ArrayList(Arrays.asList(candidates_));
        }
    }

    /**
     * 候補オブジェクトを設定します。
     * <p>{@link #setSelectedValues(String...)}などによって
     * 選択された値が設定済みである場合、
     * 候補オブジェクトの選択状態がリセットされて更新されます。
     * 設定済みでない場合は候補オブジェクトの選択状態がそのまま残ります。
     * </p>
     * 
     * @param candidateList 候補オブジェクトのList。nullを指定してはいけません。
     * @return このオブジェクト自身。
     */
    public <E extends Candidate> Selector setCandidateList(List<E> candidateList) {
        return setCandidates(candidateList.toArray(EMPTY_CANDIDATES));
    }

    private void updateCandidates() {
        if (selectedValues_ == null || candidates_ == null) {
            return;
        }

        Set<String> valueSet = new HashSet<String>(Arrays
                .asList(selectedValues_));
        for (Candidate candidate : candidates_) {
            candidate.setSelected(valueSet.contains(candidate.getValue()));
        }
    }
}
