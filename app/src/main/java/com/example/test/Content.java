package com.example.test;

public class Content {
    private String province;
    private String city;
    private String adcode;
    private String weather;
    private String temperature;
    private String winddirection;
    private String windpower;
    private String humidity;
    private String reporttime;

    public Content() {
    }

    public Content(String province, String city, String adcode, String weather, String temperature, String winddirection, String windpower, String humidity, String reporttime) {
        this.province = province;
        this.city = city;
        this.adcode = adcode;
        this.weather = weather;
        this.temperature = temperature;
        this.winddirection = winddirection;
        this.windpower = windpower;
        this.humidity = humidity;
        this.reporttime = reporttime;
    }

    @Override
    public String toString() {
        return "实时天气" +'\n' +
                "省份：" + province + '\n' +
                "城市：" + city + '\n' +
                "城市编码：" + adcode + '\n' +
                "天气：" + weather + '\n' +
                "温度：" + temperature + '\n' +
                "风向：" + winddirection + '\n' +
                "风力：" + windpower + '\n' +
                "湿度：" + humidity + '\n' +
                "更新时间：" + reporttime + '\n' ;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(String winddirection) {
        this.winddirection = winddirection;
    }

    public String getWindpower() {
        return windpower;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getReporttimeime() {
        return reporttime;
    }

    public void setReporttime(String reporttime) {
        this.reporttime = reporttime;
    }
}
