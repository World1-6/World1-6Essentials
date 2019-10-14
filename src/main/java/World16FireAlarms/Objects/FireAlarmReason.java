package World16FireAlarms.Objects;

import java.util.Optional;

public class FireAlarmReason {

    private TroubleReason troubleReason;

    private Optional<Zone> optionalZone = Optional.empty();
    private Optional<String> optionalPullStationName = Optional.empty();

    public FireAlarmReason(TroubleReason troubleReason) {
        this.troubleReason = troubleReason;
    }

    //Getter's and Setters
    public TroubleReason getTroubleReason() {
        return troubleReason;
    }

    public void setTroubleReason(TroubleReason troubleReason) {
        this.troubleReason = troubleReason;
    }

    public Optional<Zone> getOptionalZone() {
        return optionalZone;
    }

    public void setOptionalZone(Zone optionalZone) {
        this.optionalZone = Optional.ofNullable(optionalZone);
    }

    public Optional<String> getOptionalPullStationName() {
        return optionalPullStationName;
    }

    public void setOptionalPullStationName(Optional<String> optionalPullStationName) {
        this.optionalPullStationName = optionalPullStationName;
    }
}
