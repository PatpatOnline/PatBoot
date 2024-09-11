/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.models.entities;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.problem.exceptions.ProblemInitializeException;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ProblemDescriptor implements Serializable {
    /**
     * Whether the problem has init files.
     */
    private boolean init;

    /**
     * The mode of the problem.
     * <ul>
     *     <li>basic: only compare result as same or different.</li>
     *     <li>advanced: compare result with diff tool.</li>
     * </ul>
     */
    private String mode;

    /**
     * The name of the problem.
     */
    private String name;

    /**
     * The main class of the submitted code.
     * No .java suffix.
     */
    private String mainClass;

    /**
     * Time limit in milliseconds.
     */
    private long timeLimit;

    private List<CaseDescriptor> cases;

    public static ProblemDescriptor loadFromYamlFile(String path, Mappers mappers) throws IOException {
        return mappers.fromYamlFile(path, ProblemDescriptor.class);
    }

    public void validate() throws ProblemInitializeException {
        if (!("basic".equalsIgnoreCase(mode) || "advanced".equalsIgnoreCase(mode))) {
            throw new ProblemInitializeException("Problem mode must be basic or advanced");
        }
        if (Strings.isNullOrBlank(name)) {
            throw new ProblemInitializeException("Problem name can not be empty");
        }
        if (Strings.isNullOrBlank(mainClass)) {
            throw new ProblemInitializeException("Main class can not be empty");
        }
        if (cases == null || cases.isEmpty()) {
            throw new ProblemInitializeException("Problem cases can not be empty");
        }
        if (timeLimit <= 0) {
            throw new ProblemInitializeException("Time limit must be positive");
        }
        if (getTotalScore() != Globals.FULL_SCORE) {
            throw new ProblemInitializeException("Total score of cases must be 100");
        }
    }

    private int getTotalScore() throws ProblemInitializeException {
        int totalScore = 0;
        Set<Integer> caseIds = new HashSet<>();
        for (CaseDescriptor caseDescriptor : cases) {
            if (caseDescriptor.getId() < 0) {
                throw new ProblemInitializeException("Case id must be non-negative");
            }
            if (caseIds.contains(caseDescriptor.getId())) {
                throw new ProblemInitializeException("Case id must be unique");
            }
            caseIds.add(caseDescriptor.getId());
            if (caseDescriptor.getDescription() == null || caseDescriptor.getDescription().isEmpty()) {
                throw new ProblemInitializeException("Case description can not be empty");
            }
            if (caseDescriptor.getScore() <= 0) {
                throw new ProblemInitializeException("Case score must be positive");
            }
            totalScore += caseDescriptor.getScore();
        }
        return totalScore;
    }
}

