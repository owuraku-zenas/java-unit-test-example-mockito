package com.vmorg.requestengine;

import com.vmorg.auth.AuthorisingService;
import com.vmorg.build.SystemBuildService;
import com.vmorg.buildrequest.VirtualMachineRequestor;
import com.vmorg.exceptions.MachineNotCreatedException;
import com.vmorg.exceptions.UserNotEntitledException;
import com.vmorg.virtualmachine.Machine;

import java.util.HashMap;
import java.util.Map;

public class RequestEngine implements VirtualMachineRequestor {

    private AuthorisingService authorisingService;
    private SystemBuildService systemBuildService;
    private final int maxNumberOfBuildsForDay = 999;
    private Map<String, Map<String, Integer>> buildByUserForDay;
    private int totalNumberOfFailedBuildsForDay = 0;
    private int totalSuccessfulBuildsForDay = 0;

    public RequestEngine(AuthorisingService authorisingService, SystemBuildService systemBuildService) {
        this.authorisingService = authorisingService;
        this.systemBuildService = systemBuildService;
        this.buildByUserForDay = new HashMap<>();
    }

    @Override
    public void createNewRequest(Machine machine) throws UserNotEntitledException, MachineNotCreatedException {

        if (getTotalSuccessfulBuildsForDay() >= maxNumberOfBuildsForDay) {
            totalNumberOfFailedBuildsForDay++;
            throw new MachineNotCreatedException("Build limit exceeded for the day. Try again tomorrow");
        }

        if (!authorisingService.isAuthorised(machine.getRequestorName())) throw new UserNotEntitledException("User is not entitled to request for a machine");


        String result = systemBuildService.createNewMachine(machine);
        if (result.isEmpty()) {
            totalNumberOfFailedBuildsForDay++;
            throw new MachineNotCreatedException("Build was not successful");
        }


        updateUserBuildData(machine);
    }

    private void updateUserBuildData(Machine machine) {
        if (buildByUserForDay.containsKey(machine.getRequestorName())) {
            Map<String, Integer> buildType = buildByUserForDay.get(machine.getRequestorName());
            if (buildType.containsKey(machine.getMachineType())) {

                int numberOfBuilds = buildType.get(machine.getMachineType());
                buildType.put(machine.getMachineType(), ++numberOfBuilds);
            } else {
                buildType.put(machine.getMachineType(), 1);
            }

        } else {
            Map<String, Integer> buildData = new HashMap<>();
            buildData.put(machine.getMachineType(), 1);
            buildByUserForDay.put(machine.getRequestorName(), buildData);
        }
        totalSuccessfulBuildsForDay++;
    }

    @Override
    public Map<String, Map<String, Integer>> totalBuildsByUserForDay() {
        return buildByUserForDay;
    }

    public int getTotalSuccessfulBuildsForDay() {
        return totalSuccessfulBuildsForDay;
    }

    public void setTotalSuccessfulBuildsForDay(int totalSuccessfulBuildsForDay) {
        this.totalSuccessfulBuildsForDay = totalSuccessfulBuildsForDay;
    }

    @Override
    public int totalFailedBuildsForDay() {
        return totalNumberOfFailedBuildsForDay;
    }
}
