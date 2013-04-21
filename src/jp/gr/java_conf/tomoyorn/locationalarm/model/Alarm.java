package jp.gr.java_conf.tomoyorn.locationalarm.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * ひとつのアラーム設定を表すクラスです。
 *
 * @author tomoyorn
 */
@Table(name = "Alarms")
public class Alarm extends Model {

    @Column(name = "Lavel")
    private String lavel;

//    @Column(name = "Address")
//    private String address;

    @Column(name = "LatitudeE6") // TODO doubleで保持していても良いような
    private int latitudeE6;

    @Column(name = "LongitudeE6")
    private int longitudeE6;

    @Column(name = "Enabled")
    private boolean enabled;

    public String getLavel() {
        return lavel;
    }

    public void setLavel(String lavel) {
        this.lavel = lavel;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }

    public int getLatitudeE6() {
        return latitudeE6;
    }

    public void setLatitudeE6(int latitudeE6) {
        this.latitudeE6 = latitudeE6;
    }

    public int getLongitudeE6() {
        return longitudeE6;
    }

    public void setLongitudeE6(int longitudeE6) {
        this.longitudeE6 = longitudeE6;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


//    public String getLavel() {
//        if (address != null) {
//            return address;
//        }
//        return getLatitude() + ", " + getLongitude();
//    }

    public double getLatitude() {
        return latitudeE6 / 1E6;
    }

    public double getLongitude() {
        return longitudeE6 / 1E6;
    }

    public void autoLavel() {
        lavel = (lavel != null) ? lavel
                : (getLatitude() + ", " + getLongitude());
    }
//    public void autoLavel() {
//        lavel = (lavel != null) ? lavel
//                : (address != null) ? address
//                : (getLatitude() + ", " + getLongitude());
//    }

    @Override
    public String toString() {
//        return "toStringじゃなくて？";
        return lavel;
//        return "Alarm [address=" + address + ", latitudeE6=" + latitudeE6
//                + ", longitudeE6=" + longitudeE6 + "]";
    }
}

//public class Alarm extends Model {
//
//    public Alarm() {
//        // TODO Auto-generated constructor stub
//    }
//
//    public String getLavel() {
//        // TODO 仮実装
//        return "ラベル";
//    }
//
//}
