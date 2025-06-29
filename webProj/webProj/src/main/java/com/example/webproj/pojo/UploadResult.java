package com.example.webproj.pojo;

public class UploadResult {
    private int status;
    private String msg;
    private UploadData data;

    public UploadResult(int i, Object o) {
    }

    public UploadResult() {

    }

    public static UploadResult error(String message) {
        UploadResult result = new UploadResult();
        result.setStatus(1);
        result.setMsg(message);
        return result;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UploadData getData() {
        return data;
    }

    public void setData(UploadData data) {
        this.data = data;
    }

    public static class UploadData {
        private String url;

        public UploadData(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}