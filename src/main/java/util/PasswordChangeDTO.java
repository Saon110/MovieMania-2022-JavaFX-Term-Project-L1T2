package util;

import java.io.Serializable;

public class PasswordChangeDTO implements Serializable {

    private String companyName;
    private String currentPass;
    private String newPass;
    private boolean status;

    public PasswordChangeDTO(String companyName, String currentPass, String newPass) {
        this.companyName = companyName;
        this.currentPass = currentPass;
        this.newPass = newPass;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
