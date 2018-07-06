package quotes.pro.sau.quotes.model;

import java.util.List;

public class InsertTokenModel {

    /**
     * status : 0
     * msg : Insert Successfully
     * data : [{"id":2,"device_id":"deb705b76497b7c","reg_token":"abc"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * device_id : deb705b76497b7c
         * reg_token : abc
         */

        private int id;
        private String device_id;
        private String reg_token;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getReg_token() {
            return reg_token;
        }

        public void setReg_token(String reg_token) {
            this.reg_token = reg_token;
        }
    }
}
