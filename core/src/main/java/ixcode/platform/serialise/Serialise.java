package ixcode.platform.serialise;

public class Serialise {

    private final XmlSerialiser xmlSerialiser;
    private final JsonSerialiser jsonSerialiser;

    public Serialise(XmlSerialiser xmlSerialiser,
                     JsonSerialiser jsonSerialiser) {

        this.xmlSerialiser = xmlSerialiser;
        this.jsonSerialiser = jsonSerialiser;
    }

    public <T> String toXml(T object) {
        return xmlSerialiser.toXml(object);
    }

    public <T> String toJson(T object) {
        return jsonSerialiser.toJson(object);
    }
}