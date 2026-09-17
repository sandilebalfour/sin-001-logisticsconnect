package co.wethinkcode.logisticsconnect;

public class WeatherAlert {

    private int level;
    private Code code;

    public WeatherAlert() {
    }

    public WeatherAlert(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        this.code = newCode(level);
    }

    public static Code newCode(int level){
        switch (level) {
            case 1 -> {
                return Code.GREEN;
            }
            case 2, 3, 4 -> {
                return Code.YELLOW;
            }
            case 5, 6, 7, 8 -> {
                return Code.ORANGE;
            }
            case 9, 10 -> {
                return Code.RED;
            }
            default -> throw new IllegalArgumentException("Unknown Level");
        }
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "WeatherAlert{" +
                "level=" + level +
                ", code=" + code +
                '}';
    }
}
