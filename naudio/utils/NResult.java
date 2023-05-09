package naudio.utils;

public class NResult {
    private final NResultEnum type;
    private final String error;

    NResult(NResultEnum type, String error) {
        this.type = type;
        this.error = error;
    }

    public static NResult ok() {
        return new NResult(NResultEnum.Ok, "");
    }

    public static NResult error(String error) {
        return new NResult(NResultEnum.Error, error);
    }

    public NResultEnum getType() {
        return type;
    }

    public String getError() {
        return error;
    }

    public boolean isError() {
        return type == NResultEnum.Error;
    }

    public boolean isOk() {
        return type == NResultEnum.Ok;
    }
}
