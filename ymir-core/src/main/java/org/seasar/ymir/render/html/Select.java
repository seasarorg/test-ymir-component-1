package org.seasar.ymir.render.html;

import static org.seasar.ymir.util.StringUtils.asString;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.ymir.util.HTMLUtils;
import org.seasar.ymir.util.StringUtils;

/**
 * HTMLのselectタグを扱うためのクラスです。
 * 
 * @author YOKOTA Takehiko
 * @since 1.0.3
 */
public class Select extends Tag {
    private static final long serialVersionUID = 1L;

    private static final String LS = System.getProperty("line.separator");

    private String name_;

    private Integer size_;

    private boolean multiple_;

    private boolean disabled_;

    private Optgroup[] optgroups_;

    private Option[] options_;

    private Map<String, Option> optionMap_ = new LinkedHashMap<String, Option>();

    private String[] values_;

    /**
     * このクラスのインスタンスを構築します。
     */
    public Select() {
    }

    /**
     * このクラスのインスタンスを構築します。
     * 
     * @param optgroupsAndOptions optionタグに対応するOptionオブジェクト
     * またはoptgroupタグに対応するOptgroupオブジェクトのリスト。
     * nullを指定してはいけません。
     */
    public Select(List<?> optgroupsAndOptions) {
        setOptgroupsAndOptions(optgroupsAndOptions);
    }

    /**
     * このクラスのインスタンスを構築します。
     * 
     * @param optgroups optgroupタグに対応するOptgroupオブジェクトのリスト。
     * nullを指定してはいけません。
     * @param options optionタグに対応するOptionオブジェクトのリスト。
     * nullを指定してはいけません。
     */
    public Select(List<Optgroup> optgroups, List<Option> options) {
        setOptgroupsAndOptions(optgroups, options);
    }

    /**
     * このクラスのインスタンスを構築します。
     * 
     * @param optgroups optgroupタグに対応するOptgroupオブジェクトの配列。
     * nullを指定してはいけません。
     */
    public Select(Optgroup[] optgroups) {
        setOptgroups(optgroups);
    }

    /**
     * このクラスのインスタンスを構築します。
     * 
     * @param options optionタグに対応するOptionオブジェクトの配列。
     * nullを指定してはいけません。
     */
    public Select(Option[] options) {
        setOptions(options);
    }

    /**
     * このクラスのインスタンスを構築します。
     * 
     * @param optgroups optgroupタグに対応するOptgroupオブジェクトの配列。
     * nullを指定してはいけません。
     * @param options optionタグに対応するOptionオブジェクトの配列。
     * nullを指定してはいけません。
     */
    public Select(Optgroup[] optgroups, Option[] options) {
        setOptgroupsAndOptions(optgroups, options);
    }

    protected void writeName(StringBuilder sb) {
        sb.append("select");
    }

    protected void writeAttributes(StringBuilder sb) {
        super.writeAttributes(sb);
        if (name_ != null) {
            sb.append(" name=\"").append(HTMLUtils.filter(name_)).append("\"");
        }
        if (size_ != null) {
            sb.append(" size=\"").append(size_).append("\"");
        }
        if (multiple_) {
            sb.append(" multiple=\"multiple\"");
        }
    }

    protected void writeContent(StringBuilder sb) {
        if (optgroups_ != null && optgroups_.length > 0 || options_ != null
                && options_.length > 0) {
            sb.append(">").append(LS);
            if (optgroups_ != null && optgroups_.length > 0) {
                for (int i = 0; i < optgroups_.length; i++) {
                    sb.append("  ").append(addIndent(optgroups_[i])).append(LS);
                }
            }
            if (options_ != null && options_.length > 0) {
                for (int i = 0; i < options_.length; i++) {
                    sb.append("  ").append(options_[i]).append(LS);
                }
            }
            sb.append("</");
            writeName(sb);
        } else {
            sb.append(" /");
        }
    }

    protected void updateState() {
        if (optgroups_ == null) {
            return;
        }

        for (Iterator<Option> itr = optionMap_.values().iterator(); itr
                .hasNext();) {
            itr.next().setSelected(false);
        }
        if (values_ != null) {
            for (int i = 0; i < values_.length; i++) {
                Option optionTag = optionMap_.get(values_[i]);
                if (optionTag != null) {
                    optionTag.setSelected(true);
                }
            }
        }
    }

    /**
     * このselectのname属性の値を返します。
     * 
     * @return name属性の値。
     */
    public String getName() {
        return name_;
    }

    /**
     * このselectのname属性の値を設定します。
     * 
     * @param name name属性の値。
     * @return このオブジェクト。
     */
    public Select setName(String name) {
        name_ = name;
        return this;
    }

    /**
     * このselectのsize属性の値を返します。
     * 
     * @return size。
     */
    public Integer getSize() {
        return size_;
    }

    /**
     * このselectのsize属性の値を設定します。
     * 
     * @param size size属性の値。
     * @return このオブジェクト。
     */
    public Select setSize(Integer size) {
        size_ = size;
        return this;
    }

    /**
     * このselectのmultiple属性の値を設定します。
     * 
     * @param multiple multiple属性の値。
     * @return このオブジェクト。
     */
    public boolean isMultiple() {
        return multiple_;
    }

    /**
     * このselectのmultiple属性の値を設定します。
     * 
     * @param multiple multiple属性の値。
     * @return このオブジェクト。
     */

    public Select setMultiple(boolean multiple) {
        multiple_ = multiple;
        return this;
    }

    /**
     * このselectのdisabled属性の値を設定します。
     * 
     * @param disabled disabled属性の値。
     * @return このオブジェクト。
     */
    public boolean isDisabled() {
        return disabled_;
    }

    /**
     * このselectのdisabled属性の値を設定します。
     * 
     * @param disabled disabled属性の値。
     * @return このオブジェクト。
     */

    public Select setDisabled(boolean disabled) {
        disabled_ = disabled;
        return this;
    }

    /**
     * 値を返します。
     * <p>このメソッドはこのオブジェクトが持つ値に対してJavaBeansプロパティとしてアクセスできるように用意されています。
     * 例えば画面テンプレートで値を表示する場合はこのメソッドがテンプレートエンジン等によって間接的に利用されます。
     * </p>
     * <p>このメソッドは、内部に持っているどのOptionも持たない値を返すことがあることに注意して下さい。
     * どのOptionも持たない値を除外したい場合は{@link #getSelectedValues()}を使用してください。
     * </p>
     * 
     * @return 値。nullを返すこともあります。
     */
    public String[] getValue() {
        return values_;
    }

    /**
     * 値を設定します。
     * <p>このメソッドは{@link #setSelectedValues(String[])}と同じです。
     * </p>
     * <p>このメソッドはこのオブジェクトが持つ値に対してJavaBeansプロパティとしてアクセスできるように用意されています。
     * 例えばリクエストパラメータを直接受ける場合はこのメソッドがフレームワーク等によって間接的に利用されます。
     * </p>
     * <p>内部に持っているどのOptionも持たない値が指定された場合でも除外されません。
     * </p>
     * 
     * @param values 値。nullを設定することもできます。
     * @see #setSelectedValues(String[])
     */
    public void setValue(String[] values) {
        setSelectedValues(values);
    }

    /**
     * 選択されたOptionの値を返します。
     * <p>複数選択されている場合は1つだけを返します。
     * </p>
     * 
     * @return 選択されたOptionの値。
     * 選択されたOptionが存在しない場合はnullを返します。
     */
    public String getSelectedValue() {
        for (Iterator<Option> itr = optionMap_.values().iterator(); itr
                .hasNext();) {
            Option option = itr.next();
            if (option.isSelected()) {
                return option.getValue();
            }
        }
        return null;
    }

    /**
     * 選択されたOptionの値を数値として返します。
     * <p>複数選択されている場合は1つだけを返します。
     * </p>
     * <p>数値に変換できない場合は{@link NumberFormatException}をスローします。
     * </p>
     * 
     * @return 選択されたOptionの値。
     * 選択されたOptionが存在しない場合はnullを返します。
     * @throws IllegalStateException このオブジェクトが内部にOptionを持っていない場合。
     */
    public Integer getSelectedValueAsInteger() {
        String value = getSelectedValue();
        if (value == null) {
            return null;
        } else {
            return Integer.valueOf(value);
        }
    }

    /**
     * 選択されたOptionの値を返します。
     * 
     * @return 選択されたOptionの値。
     * 選択されたOptionが存在しない場合は空の配列を返します。
     */
    public String[] getSelectedValues() {
        List<String> list = new ArrayList<String>();
        for (Iterator<Option> itr = optionMap_.values().iterator(); itr
                .hasNext();) {
            Option option = itr.next();
            if (option.isSelected()) {
                list.add(option.getValue());
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * 選択されたOptionの値を数値として返します。
     * <p>数値に変換できない場合は{@link NumberFormatException}をスローします。
     * </p>
     * 
     * @return 選択されたOptionの値。
     * 選択されたOptionが存在しない場合は空の配列を返します。
     * @throws IllegalStateException このオブジェクトが内部にOptionを持っていない場合。
     */
    public Integer[] getSelectedValuesAsInteger() {
        String[] values = getSelectedValues();
        Integer[] integers = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            integers[i] = Integer.valueOf(values[i]);
        }
        return integers;
    }

    /**
     * 選択された値を設定します。
     * <p>このオブジェクトが内部にOptionを持っていない場合は値だけを内部で保持します。
     * </p>
     * <p>内部に持っているどのOptionも持たない値が指定された場合でも除外されません。
     * </p>
     * 
     * @param values 選択された値。nullを指定することもできます。
     * 値が配列型またはCollection型の場合、複数の値が選択されたものとして解釈されます。
     * また文字列でないオブジェクトが値として指定された場合は、オブジェクトの文字列表現を値として設定します。
     * @return このオブジェクト。
     */
    @SuppressWarnings("unchecked")
    public Select setSelectedValues(Object values) {
        if (values == null) {
            return setSelectedValues(new String[0]);
        } else if (values.getClass().isArray()) {
            List<String> list = new ArrayList<String>();
            int length = Array.getLength(values);
            for (int i = 0; i < length; i++) {
                Object value = Array.get(values, i);
                if (value != null) {
                    list.add(value.toString());
                }
            }
            return setSelectedValues(list.toArray(new String[0]));
        } else if (values instanceof Collection) {
            List<String> list = new ArrayList<String>();
            for (Iterator<String> itr = ((Collection) values).iterator(); itr
                    .hasNext();) {
                Object value = itr.next();
                if (value != null) {
                    list.add(value.toString());
                }
            }
            return setSelectedValues(list.toArray(new String[0]));
        } else {
            return setSelectedValues(new String[] { values.toString() });
        }
    }

    /**
     * 選択された値を設定します。
     * <p>このメソッドは<code>setSelectedValue(value)と同じです。
     * </p>
     * 
     * @param values 選択された値。nullを指定することもできます。
     * @return このオブジェクト。
     * @see #setSelectedValues(String...)
     */
    public Select setSelectedValue(String value) {
        return setSelectedValues(value);
    }

    /**
     * 選択された値を設定します。
     * <p>指定された値の文字列表現を選択された値として設定します。
     * </p>
     * 
     * @param values 値。nullを指定することもできます。
     * @return このオブジェクト。
     * @see #setSelectedValue(String)
     */
    public Select setSelectedValueObject(Object value) {
        return setSelectedValue(asString(value));
    }

    /**
     * 選択された値を設定します。
     * <p>このオブジェクトが内部にOptionを持っていない場合は値だけを内部で保持します。
     * </p>
     * <p>内部に持っているどのOptionも持たない値が指定された場合でも除外されません。
     * </p>
     * 
     * @param values 選択された値。nullを指定することもできます。
     * @return このオブジェクト。
     */
    public Select setSelectedValues(String... values) {
        values_ = values;

        updateState();

        return this;
    }

    /**
     * 選択された値を設定します。
     * <p>指定された値の文字列表現を選択された値として設定します。
     * </p>
     * <p>このオブジェクトが内部にOptionを持っていない場合は値だけを内部で保持します。
     * </p>
     * <p>内部に持っているどのOptionも持たない値が指定された場合でも除外されません。
     * </p>
     * 
     * @param values 値。nullを指定することもできます。
     * @return このオブジェクト。
     */
    public Select setSelectedValueObjects(Object... values) {
        String[] valueStrings;
        if (values == null) {
            valueStrings = new String[0];
        } else {
            valueStrings = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                valueStrings[i] = asString(values[i]);
            }
        }
        setSelectedValues(valueStrings);
        return this;
    }

    /**
     * このオブジェクトが内部に持っている全てのOptgroupの配列を返します。
     * 
     * @return Optgroupの配列。nullが返されることもあります。
     */
    public Optgroup[] getOptgroups() {
        return optgroups_;
    }

    /**
     * このオブジェクトにOptgroupを設定します。
     * <p>このオブジェクトに値が設定されている場合、
     * 値に従って設定されたOptgroupの状態も更新されます。
     * </p>
     * <p>Optgroupに関連付けられておらず直接このオブジェクトが持つOptionは削除されます。
     * </p>
     * 
     * @param inputs Optionのリスト。nullを指定してはいけません。
     * @return このオブジェクト。
     */
    public Select setOptgroups(List<Optgroup> optgroups) {
        return setOptgroups(optgroups.toArray(new Optgroup[0]));
    }

    /**
     * このオブジェクトにOptgroupを設定します。
     * <p>このオブジェクトに値が設定されている場合、
     * 値に従って設定されたOptgroupの状態も更新されます。
     * </p>
     * <p>Optgroupに関連付けられておらず直接このオブジェクトが持つOptionは削除されます。
     * </p>
     * 
     * @param inputs Optionの配列。nullを指定してはいけません。
     * @return このオブジェクト。
     */
    public Select setOptgroups(Optgroup[] optgroups) {
        return setOptgroupsAndOptions(optgroups, new Option[0]);
    }

    /**
     * このオブジェクトが内部に持っている全てのOptionの配列を返します。
     * 
     * @return Optionの配列。nullが返されることもあります。
     */
    public Option[] getOptions() {
        return options_;
    }

    /**
     * このオブジェクトにOptionを設定します。
     * <p>このオブジェクトに値が設定されている場合、
     * 値に従って設定されたOptionの状態も更新されます。
     * </p>
     * <p>このオブジェクトが持つOptgroupは削除されます。
     * </p>
     * 
     * @param inputs Optionのリスト。nullを指定してはいけません。
     * @return このオブジェクト。
     */
    public Select setOptions(List<Option> options) {
        return setOptions(options.toArray(new Option[0]));
    }

    /**
     * このオブジェクトにOptionを設定します。
     * <p>このオブジェクトに値が設定されている場合、
     * 値に従って設定されたOptionの状態も更新されます。
     * </p>
     * <p>このオブジェクトが持つOptgroupは削除されます。
     * </p>
     * 
     * @param inputs Optionの配列。nullを指定してはいけません。
     * @return このオブジェクト。
     */
    public Select setOptions(Option[] options) {
        return setOptgroupsAndOptions(new Optgroup[0], options);
    }

    /**
     * このオブジェクトにOptgroupとOptionを設定します。
     * <p>このオブジェクトに値が設定されている場合、
     * 値に従って設定されたOptgroupとOptionの状態も更新されます。
     * </p>
     * 
     * @param optgroupsOrOptions OptgroupまたはOptionのリスト。nullを指定してはいけません。
     * @return このオブジェクト。
     */
    public Select setOptgroupsAndOptions(List<?> optgroupsOrOptions) {
        List<Optgroup> optgroups = new ArrayList<Optgroup>();
        List<Option> options = new ArrayList<Option>();
        for (Iterator<?> itr = optgroupsOrOptions.iterator(); itr.hasNext();) {
            Object tag = itr.next();
            if (tag instanceof Optgroup) {
                optgroups.add((Optgroup) tag);
            } else if (tag instanceof Option) {
                options.add((Option) tag);
            } else {
                throw new IllegalArgumentException(
                        "Option or Optgroup must be specified: " + tag);
            }
        }
        return setOptgroupsAndOptions(optgroups.toArray(new Optgroup[0]),
                options.toArray(new Option[0]));
    }

    /**
     * このオブジェクトにOptgroupとOptionを設定します。
     * <p>このオブジェクトに値が設定されている場合、
     * 値に従って設定されたOptgroupとOptionの状態も更新されます。
     * </p>
     * 
     * @param optgroups Optgroupのリスト。nullを指定してはいけません。
     * @param options Optionのリスト。nullを指定してはいけません。
     * @return このオブジェクト。
     */
    public Select setOptgroupsAndOptions(List<Optgroup> optgroups,
            List<Option> options) {
        return setOptgroupsAndOptions(optgroups.toArray(new Optgroup[0]),
                options.toArray(new Option[0]));
    }

    /**
     * このオブジェクトにOptgroupとOptionを設定します。
     * <p>このオブジェクトに値が設定されている場合、
     * 値に従って設定されたOptgroupとOptionの状態も更新されます。
     * </p>
     * 
     * @param optgroups Optgroupの配列。nullを指定してはいけません。
     * @param options Optionの配列。nullを指定してはいけません。
     * @return このオブジェクト。
     */
    public Select setOptgroupsAndOptions(Optgroup[] optgroups, Option[] options) {
        optgroups_ = optgroups;
        options_ = options;
        optionMap_.clear();

        List<String> valueList = new ArrayList<String>();
        for (int i = 0; i < optgroups_.length; i++) {
            Option[] os = optgroups_[i].getOptions();
            for (int j = 0; j < os.length; j++) {
                if (values_ == null) {
                    if (os[j].isSelected()) {
                        valueList.add(os[j].getValue());
                    }
                }

                Object old = optionMap_.put(os[j].getValue(), os[j]);
                if (old != null) {
                    throw new IllegalArgumentException(
                            "Values of options are duplicated: " + old + ", "
                                    + os[j]);
                }
            }
        }

        for (int i = 0; i < options_.length; i++) {
            if (values_ == null) {
                if (options_[i].isSelected()) {
                    valueList.add(options_[i].getValue());
                }
            }

            Object old = optionMap_.put(options_[i].getValue(), options_[i]);
            if (old != null) {
                throw new IllegalArgumentException(
                        "Values of options are duplicated: " + old + ", "
                                + options_[i]);
            }
        }

        if (values_ == null) {
            values_ = valueList.toArray(new String[0]);
        }

        updateState();

        return this;
    }

    /**
     * 選択されたOptionのボディを返します。
     * <p>複数選択されている場合は1つだけを返します。
     * </p>
     * 
     * @return 選択されたOptionのボディ。
     * 選択されたOptionが存在しない場合はnullを返します。
     */
    public String getSelectedContent() {
        for (Iterator<Option> itr = optionMap_.values().iterator(); itr
                .hasNext();) {
            Option option = itr.next();
            if (option.isSelected()) {
                return option.getContentAsString();
            }
        }
        return null;
    }

    /**
     * 選択されたOptionのボディを返します。
     * 
     * @return 選択されたOptionのボディの配列。
     * 選択されたOptionが存在しない場合は空の配列を返します。
     */
    public String[] getSelectedContents() {
        List<String> list = new ArrayList<String>();
        for (Iterator<Option> itr = optionMap_.values().iterator(); itr
                .hasNext();) {
            Option option = itr.next();
            if (option.isSelected()) {
                list.add(option.getContentAsString());
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * 選択されたOptionを返します。
     * <p>複数選択されている場合は1つだけを返します。
     * </p>
     * 
     * @return 選択されたOption。
     * 選択されたOptionが存在しない場合はnullを返します。
     */
    public Option getSelectedOption() {
        for (Iterator<Option> itr = optionMap_.values().iterator(); itr
                .hasNext();) {
            Option option = itr.next();
            if (option.isSelected()) {
                return option;
            }
        }
        return null;
    }

    /**
     * 選択されたOptionを返します。
     * 
     * @return 選択されたOptionの配列。
     * 選択されたOptionが存在しない場合は空の配列を返します。
     */
    public Option[] getSelectedOptions() {
        List<Option> list = new ArrayList<Option>();
        for (Iterator<Option> itr = optionMap_.values().iterator(); itr
                .hasNext();) {
            Option option = itr.next();
            if (option.isSelected()) {
                list.add(option);
            }
        }
        return list.toArray(new Option[0]);
    }

    /**
     * 指定されたラベルを持つOptgroupを返します。
     * 
     * @param label ラベル。
     * @return 指定されたラベルを持つOption。
     * 見つからなかった場合はnullを返します。
     */
    public Optgroup getOptgroup(String label) {
        for (int i = 0; i < optgroups_.length; i++) {
            if (label.equals(optgroups_[i].getLabel())) {
                return optgroups_[i];
            }
        }
        return null;
    }

    /**
     * 指定された値を持つOptionを返します。
     * 
     * @param value 値。
     * @return 指定された値を持つOption。
     * 見つからなかった場合はnullを返します。
     */
    public Option getOption(String value) {
        return optionMap_.get(value);
    }

    /**
     * 指定された値を持つOptionのボディを返します。
     * 
     * @param value 値。
     * @return 指定された値を持つOptionのボディ。
     * 見つからなかった場合はnullを返します。
     */
    public String getOptionContent(String value) {
        Option option = getOption(value);
        if (option != null) {
            return option.getContentAsString();
        } else {
            return null;
        }
    }
}
