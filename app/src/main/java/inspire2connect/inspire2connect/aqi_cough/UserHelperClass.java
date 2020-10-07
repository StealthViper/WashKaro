package inspire2connect.inspire2connect.aqi_cough;

import java.util.Date;
import java.util.Calendar;

public class UserHelperClass {
    String aqiPredictString;
    String aqiActualString;
    String userLatitude;
    String userLongitude;
    String dataLocationString;
    String bmiString;
    String ageString;
    boolean bronchitisVal;
    boolean asthmaVal;
    boolean pneumoniaVal;
    boolean cancerVal;
    boolean tbVal;
    boolean otherRespVal;
    boolean femaleVal;
    boolean maleVal;
    boolean otherGenderVal;
    Date currentTime;

    public UserHelperClass() {

    }

    public UserHelperClass(String aqiPredictString, String aqiActualString, String userLatitude, String userLongitude, String dataLocationString, String bmiString, String ageString, boolean bronchitisVal, boolean asthmaVal, boolean pneumoniaVal, boolean cancerVal, boolean tbVal, boolean otherRespVal, boolean femaleVal, boolean maleVal, boolean otherGenderVal, Date currentTime) {
        this.aqiPredictString = aqiPredictString;
        this.aqiActualString = aqiActualString;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
        this.dataLocationString = dataLocationString;
        this.bmiString = bmiString;
        this.ageString = ageString;
        this.bronchitisVal = bronchitisVal;
        this.asthmaVal = asthmaVal;
        this.pneumoniaVal = pneumoniaVal;
        this.cancerVal = cancerVal;
        this.tbVal = tbVal;
        this.otherRespVal = otherRespVal;
        this.femaleVal = femaleVal;
        this.maleVal = maleVal;
        this.otherGenderVal = otherGenderVal;
        this.currentTime = currentTime;
    }

    public String getAqiPredictString() {
        return aqiPredictString;
    }

    public void setAqiPredictString(String aqiPredictString) {
        this.aqiPredictString = aqiPredictString;
    }

    public String getAqiActualString() {
        return aqiActualString;
    }

    public void setAqiActualString(String aqiActualString) {
        this.aqiActualString = aqiActualString;
    }

    public String getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(String userLatitude) {
        this.userLatitude = userLatitude;
    }

    public String getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(String userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getDataLocationString() {
        return dataLocationString;
    }

    public void setDataLocationString(String dataLocationString) {
        this.dataLocationString = dataLocationString;
    }

    public String getBmiString() {
        return bmiString;
    }

    public void setBmiString(String bmiString) {
        this.bmiString = bmiString;
    }

    public String getAgeString() {
        return ageString;
    }

    public void setAgeString(String ageString) {
        this.ageString = ageString;
    }

    public boolean isBronchitisVal() {
        return bronchitisVal;
    }

    public void setBronchitisVal(boolean bronchitisVal) {
        this.bronchitisVal = bronchitisVal;
    }

    public boolean isAsthmaVal() {
        return asthmaVal;
    }

    public void setAsthmaVal(boolean asthmaVal) {
        this.asthmaVal = asthmaVal;
    }

    public boolean isPneumoniaVal() {
        return pneumoniaVal;
    }

    public void setPneumoniaVal(boolean pneumoniaVal) {
        this.pneumoniaVal = pneumoniaVal;
    }

    public boolean isCancerVal() {
        return cancerVal;
    }

    public void setCancerVal(boolean cancerVal) {
        this.cancerVal = cancerVal;
    }

    public boolean isTbVal() {
        return tbVal;
    }

    public void setTbVal(boolean tbVal) {
        this.tbVal = tbVal;
    }

    public boolean isOtherRespVal() {
        return otherRespVal;
    }

    public void setOtherRespVal(boolean otherRespVal) {
        this.otherRespVal = otherRespVal;
    }

    public boolean isFemaleVal() {
        return femaleVal;
    }

    public void setFemaleVal(boolean femaleVal) {
        this.femaleVal = femaleVal;
    }

    public boolean isMaleVal() {
        return maleVal;
    }

    public void setMaleVal(boolean maleVal) {
        this.maleVal = maleVal;
    }

    public boolean isOtherGenderVal() {
        return otherGenderVal;
    }

    public void setOtherGenderVal(boolean otherGenderVal) {
        this.otherGenderVal = otherGenderVal;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
