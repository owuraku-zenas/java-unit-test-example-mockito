package com.vmorg.virtualmachine;

import com.vmorg.exceptions.InvalidHostnameException;

public abstract class Machine {
    private static final int HOST_NAME_LENGTH = 15;
    private final String hostname;
    private final String requestorName;

    private int numberOfCPUs;

    private int RAMSizeInGB;
    private int hardDistSizeInGB;
    private String machineType;

    public Machine(String hostname, String requestorName, int numberOfCPUs, int RAMSizeInGB, int hardDistSizeInGB, String machineType) throws InvalidHostnameException {
        validateHostName(hostname);
        this.hostname = hostname;
        this.requestorName = requestorName;
        this.numberOfCPUs = numberOfCPUs;
        this.RAMSizeInGB = RAMSizeInGB;
        this.hardDistSizeInGB = hardDistSizeInGB;
    }

    void validateHostName(String hostname) throws InvalidHostnameException {
        int prefixStartIndex = 0;
        int prefixEndIndex = 4;
        int monthStartIndex = 8;
        int monthEndIndex = 10;
        int dayStartIndex = 10;
        int dayEndIndex = 12;

        // CHeck length
        if (hostname.length() != HOST_NAME_LENGTH) throw new InvalidHostnameException("Hostname length is invalid");

        String hostnamePrefix = hostname.substring(prefixStartIndex, prefixEndIndex);
        if (!hostnamePrefix.equals("host")) throw new InvalidHostnameException("Hostname prefix is invalid");

        int monthString = Integer.parseInt(hostname.substring(monthStartIndex, monthEndIndex));
        int dayString = Integer.parseInt(hostname.substring(dayStartIndex, dayEndIndex));
        if (monthString > 12 || dayString > 31) throw new InvalidHostnameException("Hostname date is invalid");
    }

    public String getRequestorName() {
        return requestorName;
    }
    public String getMachineType() {
        return machineType;
    }
}