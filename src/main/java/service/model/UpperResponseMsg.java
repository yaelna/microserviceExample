package service.model;


import java.time.Instant;
import java.util.Objects;

public class UpperResponseMsg {

    private Instant instant;
    private String incomingMsg;
    private String responseMsg;

    public UpperResponseMsg(Instant instant, String incomingMsg, String responseMsg) {
        this.instant = instant;
        this.incomingMsg = incomingMsg;
        this.responseMsg = responseMsg;
    }

    public Instant getInstance() {
        return instant;
    }

    public String getIncomingMsg() {
        return incomingMsg;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpperResponseMsg that = (UpperResponseMsg) o;
        return getInstance().equals(that.getInstance()) &&
                getIncomingMsg().equals(that.getIncomingMsg()) &&
                getResponseMsg().equals(that.getResponseMsg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInstance(), getIncomingMsg(), getResponseMsg());
    }


}
