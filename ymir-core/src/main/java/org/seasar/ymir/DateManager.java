package org.seasar.ymir;

import java.util.Calendar;
import java.util.Date;

public interface DateManager {
    /**
     * 現在の時刻を表すDateオブジェクトを生成して返します。
     * 
     * @return 現在の時刻を表すDateオブジェクト。
     */
    Date getDate();

    /**
     * 現在の時刻を表すlong値を生成して返します。
     * 
     * @return 現在の時刻を表すlong値。
     */
    long getTime();

    /**
     * 現在の日付を表すCalendarオブジェクトを生成して返します。
     * 
     * @return 現在の日付を表すCalendarオブジェクト。
     */
    Calendar getCalendar();

    /**
     * テストのために、現在の時刻として返すべき値を設定します。
     * <p>このメソッドは時刻関連の操作のための単体テストを実現するために用意されています。
     * 通常は時刻を設定しないようにして下さい。
     * </p>
     * 
     * @param date 現在の時刻を表すDateオブジェクト。
     * nullでない値を設定すると、常に設定した値が時刻として返されるようになります。
     * nullを設定すると、実際の時刻が返されるようになります。
     */
    void setDate(Date date);
}
