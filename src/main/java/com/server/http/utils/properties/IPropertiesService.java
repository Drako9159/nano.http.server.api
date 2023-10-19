package com.server.http.utils.properties;

public interface IPropertiesService {
    public boolean existProperty(PropertyName propertyName);

    public void saveProperty(Property property);

    public Property getPropertyByName(PropertyName propertyName);

    public void deleteProperty(PropertyName propertyName);

}
