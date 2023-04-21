package com.vmorg.virtualmachine;

import com.vmorg.exceptions.InvalidHostnameException;

public class Desktop extends Machine{
    private String microsoftWindowsVersion;
    private String buildNumber;

    public Desktop(String hostname, String requestorName, int numberOfCPUs, int RAMSizeInGB, int hardDistSizeInGB, String microsoftWindowsVersion, String buildNumber) throws InvalidHostnameException {
        super(hostname, requestorName, numberOfCPUs, RAMSizeInGB, hardDistSizeInGB, "Desktop");
        this.buildNumber = buildNumber;
        this.microsoftWindowsVersion = microsoftWindowsVersion;
    }
}
