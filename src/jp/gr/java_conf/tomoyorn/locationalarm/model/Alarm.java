package jp.gr.java_conf.tomoyorn.locationalarm.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

/**
 * ひとつのアラーム設定を表すクラスです。
 *
 * @author tomoyorn
 */
@Table(name = "Alarms")
public class Alarm extends Model {

    @Column(name = "Lavel")
    private String lavel;

    @Column(name = "LatitudeE6")
    private int latitudeE6;

    @Column(name = "LongitudeE6")
    private int longitudeE6;

    @Column(name = "Distance")
    private int distance;

    @Column(name = "Duration")
    private int duration;

    public String getLavel() {
        return lavel;
    }

    /**
     * 指定されたIDに一致するアラーム設定を返します。
     *
     * @param id ID
     * @return 指定されたIDに一致するアラーム設定
     */
    public static Alarm find(long id) {
        return Alarm.load(Alarm.class, id);
    }

    /**
     * すべてのアラーム設定を返します。
     *
     * @return すべてのアラーム設定
     */
    public static ArrayList<Alarm> findAll() {
        return Alarm.all(Alarm.class);
    }

    /**
     * すべてのアラーム設定を削除します。
     */
    public static void deleteAll() {
        new Delete().from(Alarm.class).execute();
    }

    /**
     * ラベルを設定します。
     *
     * @param lavel ラベル
     */
    public void setLavel(String lavel) {
        this.lavel = lavel;
    }

    /**
     * 目的地の緯度（マイクロ度）を設定します。
     *
     * @param latitudeE6 目的地の緯度
     */
    public void setLatitudeE6(int latitudeE6) {
        this.latitudeE6 = latitudeE6;
    }

    /**
     * 目的地の緯度（マイクロ度）を返します。
     *
     * @return 目的地の緯度
     */
    public int getLatitudeE6() {
        return latitudeE6;
    }

    /**
     * 目的地の経度（マイクロ度）を設定します。
     *
     * @param longitudeE6 目的地の経度
     */
    public void setLongitudeE6(int longitudeE6) {
        this.longitudeE6 = longitudeE6;
    }

    /**
     * 目的地の経度（マイクロ度）を返します。
     *
     * @return 目的地の経度
     */
    public int getLongitudeE6() {
        return longitudeE6;
    }

    /**
     * 目的地の緯度を返します。
     *
     * @return 目的地の緯度
     */
    public double getLatitude() {
        return latitudeE6 / 1E6;
    }

    /**
     * 目的地の経度を返します。
     *
     * @return 目的地の経度
     */
    public double getLongitude() {
        return longitudeE6 / 1E6;
    }

    /**
     * 近接アラートを通知する距離を設定します（km）。
     *
     * @param distance 近接アラートを通知する距離
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * 近接アラートを通知する距離を返します（km）。
     *
     * @return 近接アラートを通知する距離
     */
    public int getDistance() {
        return distance;
    }

    /**
     *
     * 近接アラートを鳴らし続ける時間を設定します（分）。
     *
     * @param duration 近接アラートを鳴らし続ける時間
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     *
     * 近接アラートを鳴らし続ける時間を返します（分）。
     *
     * @return 近接アラートを鳴らし続ける時間
     */
    public int getDuration() {
        return duration;
    }

    /**
     * 自動でラベルを設定します。
     */
    public void autoLavel() {
        lavel = (lavel != null) ? lavel
                : (getLatitude() + ", " + getLongitude());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) { return false; }
        Alarm rhs = (Alarm) obj;
        return new EqualsBuilder()
                      .appendSuper(super.equals(obj))
                      .append(getTableName(), rhs.getTableName())
                      .append(getId(), rhs.getId())
                      .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(getId()).
                append(getTableName()).
                toHashCode();
    }

    @Override
    public String toString() {
        return lavel;
    }

    public String dump() {
        return ToStringBuilder.reflectionToString(this);
    }
}
