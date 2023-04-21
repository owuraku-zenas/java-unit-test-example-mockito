package com.vmorg.virtualmachine;

import com.vmorg.exceptions.InvalidHostnameException;

public class Server extends Machine{
    private String distroName;
    private int majorNumberOfDistribution;
    private String kernelVersion;
    private String administrativeTeam;

    public Server(String hostname, String requestorName, int numberOfCPUs, int RAMSizeInGB, int hardDistSizeInGB,String distroName, int majorNumberOfDistribution, String kernelVersion, String administrativeTeam) throws InvalidHostnameException {
        super(hostname, requestorName, numberOfCPUs, RAMSizeInGB, hardDistSizeInGB, "Server");
        this.distroName = distroName;
        this.majorNumberOfDistribution = majorNumberOfDistribution;
        this.kernelVersion = kernelVersion;
        this.administrativeTeam = administrativeTeam;
    }


}
