package dev.pmelnik;

import java.util.List;

public class GroupAnalysisResult {

    private final List<List<List<String>>> groups;

    public GroupAnalysisResult(List<List<List<String>>> groups) {
        this.groups = groups;
    }

    public int getMultiElementGroupsCount() {
        return (int) groups.stream().filter(g -> g.size() > 1).count();
    }

    public void printStatistics() {
        System.out.println("Groups with more than one element: " + getMultiElementGroupsCount());
    }
}
