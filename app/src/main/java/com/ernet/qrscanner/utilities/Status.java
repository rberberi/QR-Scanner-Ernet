package com.ernet.qrscanner.utilities;

public class Status {
    private static String name;
    private static String id;
    private static String status;
    private static String lastStatus;
    private static String lastStatusChangeTime;

    public Status() { }

    public Status(String id, String status, String name, String lastStatus, String lastStatusChangeTime) {
        Status.id = id;
        Status.status = status;
        Status.name = name;
        Status.lastStatus = lastStatus;
        Status.lastStatusChangeTime = lastStatusChangeTime;
    }

    public void destroyStatusContext() {
        id = null;
        name = null;
        status = null;
        lastStatus = null;
        lastStatusChangeTime = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Status.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        Status.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        Status.status = status;
    }

    public static String getLastStatus() {
        return lastStatus;
    }

    public static void setLastStatus(String lastStatus) {
        Status.lastStatus = lastStatus;
    }

    public static String getLastStatusChangeTime() {
        return lastStatusChangeTime;
    }

    public static void setLastStatusChangeTime(String lastStatusChangeTime) {
        Status.lastStatusChangeTime = lastStatusChangeTime;
    }
}
