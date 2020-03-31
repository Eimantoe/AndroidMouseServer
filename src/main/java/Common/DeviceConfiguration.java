package Common;

public class DeviceConfiguration {

    @PrimaryKey
    public String address;

    public int sensitivity;
    public int linesToScroll;

    public DeviceConfiguration(String address) {
        this.address = address;
    }

    public DeviceConfiguration setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
        return this;
    }

    public DeviceConfiguration setLinesToScroll(int linesToScroll) {
        this.linesToScroll = linesToScroll;
        return this;
    }

    public int getSensitivity() {
        return sensitivity;
    }

    public int getLinesToScroll() {
        return linesToScroll;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DeviceConfiguration) {
            DeviceConfiguration conf = (DeviceConfiguration) obj;

            if (conf.address.equalsIgnoreCase(this.address))
            {
                return true;
            }
        }
        return false;
    }
}
