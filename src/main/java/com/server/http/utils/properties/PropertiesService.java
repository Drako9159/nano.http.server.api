package com.server.http.utils.properties;

public class PropertiesService implements IPropertiesService {

    private final PropertiesRW propertiesRW = new PropertiesRW();

    @Override
    public boolean existProperty(PropertyName propertyName) {
        return propertiesRW.checkPropertyByName(propertyName);
    }

    @Override
    public void saveProperty(Property property) {
        propertiesRW.addProperty(property);
    }

    @Override
    public Property getPropertyByName(PropertyName propertyName) {
        return propertiesRW.findPropertyByName(propertyName);
    }

    @Override
    public void deleteProperty(PropertyName propertyName) {
        propertiesRW.deleteProperty(propertyName);
    }
}
