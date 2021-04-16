package com.ernet.qrscanner.utilities;

public class Auftrag {
    // fields
    private String auftragId;
    private String beschreibung;
    private String vorname;
    private String nachname;
    private String auftragStatus;
    private String lastStatus;
    private String lastStatusChangeTime;

    // constructors
    public Auftrag() {}

    public Auftrag(String auftragId, String vorname, String nachname, String beschreibung, String auftragStatus, String lastStatus, String lastStatusChangeTime) {
        this.auftragId = auftragId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.beschreibung = beschreibung;
        this.auftragStatus = auftragStatus;
        this.lastStatus = lastStatus;
        this.lastStatusChangeTime = lastStatusChangeTime;
    }

    public String getAuftragId() {
        return auftragId;
    }

    public void setAuftragId(String auftragId) {
        this.auftragId = auftragId;
    }

    public String getDescription() {
        return beschreibung;
    }

    public void setDescription(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String KName) {
        this.vorname = KName;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getAuftragStatus() {
        return auftragStatus;
    }

    public void setAuftragStatus(String auftragStatus) {
        this.auftragStatus = auftragStatus;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getLastStatusChangeTime() {
        return lastStatusChangeTime;
    }

    public void setLastStatusChangeTime(String lastStatusChangeTime) {
        this.lastStatusChangeTime = lastStatusChangeTime;
    }
}
