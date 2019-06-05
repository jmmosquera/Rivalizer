package iezv.jmm.rivalizer.POJO;

public class Message {

    String id_msg;
    String id_sender;
    String date_msg;
    String text_msg;
    int sended;
    int status;

    public Message(){}

    public Message(String id_msg, String id_sender, String date_msg, String text_msg, int sended, int status) {
        this.id_msg = id_msg;
        this.id_sender = id_sender;
        this.date_msg = date_msg;
        this.text_msg = text_msg;
        this.sended = sended;
        this.status = status;
    }

    public String getId_msg() {
        return id_msg;
    }

    public void setId_msg(String id_msg) {
        this.id_msg = id_msg;
    }

    public String getDate_msg() {
        return date_msg;
    }

    public void setDate_msg(String date_msg) {
        this.date_msg = date_msg;
    }

    public String getText_msg() {
        return text_msg;
    }

    public void setText_msg(String text_msg) {
        this.text_msg = text_msg;
    }

    public int getSended() {
        return sended;
    }

    public void setSended(int sended) {
        this.sended = sended;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId_sender() {
        return id_sender;
    }

    public void setId_sender(String id_sender) {
        this.id_sender = id_sender;
    }
}
