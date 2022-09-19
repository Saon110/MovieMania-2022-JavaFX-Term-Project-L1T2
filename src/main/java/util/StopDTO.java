package util;

import java.io.Serializable;

public class StopDTO implements Serializable {
    private boolean status;

    public StopDTO(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
