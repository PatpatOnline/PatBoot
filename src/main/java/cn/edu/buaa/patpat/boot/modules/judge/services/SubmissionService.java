/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.judge.services;

import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionAdminDto;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.judge.models.entities.Submission;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.SubmissionFilter;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.SubmissionFilterMapper;
import cn.edu.buaa.patpat.boot.modules.judge.models.mappers.SubmissionMapper;
import cn.edu.buaa.patpat.boot.modules.judge.models.views.SubmissionListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService extends BaseService {
    private final SubmissionMapper submissionMapper;
    private final SubmissionFilterMapper submissionFilterMapper;

    /**
     * If no submission is found, return null.
     */
    public SubmissionDto getLastSubmission(int problemId, int accountId) {
        Submission submission = submissionFilterMapper.findLast(problemId, accountId);
        if (submission == null) {
            return null;
        }

        // check if submission is timed out
        if (submission.isTimedOut()) {
            submissionMapper.delete(submission.getId());
            // calling self recursively to find at least one submission, or throw NotFoundException
            return getLastSubmission(problemId, accountId);
        }

        return SubmissionDto.of(submission, mappers);
    }

    public PageListDto<SubmissionListView> query(int courseId, int page, int pageSize, SubmissionFilter filter) {
        int count;
        List<SubmissionListView> submissions;
        if (filter.getId() != null) {
            var view = submissionFilterMapper.queryById(filter.getId(), courseId);
            if (view == null) {
                count = 0;
                submissions = List.of();
            } else {
                count = 1;
                submissions = List.of(view);
            }
        } else {
            count = submissionFilterMapper.count(courseId, filter);
            submissions = count == 0
                    ? List.of()
                    : submissionFilterMapper.query(courseId, page, pageSize, filter);
        }
        return PageListDto.of(submissions, count, page, pageSize);
    }

    public SubmissionAdminDto query(int id, int courseId) {
        Submission submission = submissionFilterMapper.findById(id, courseId);
        if (submission == null) {
            throw new NotFoundException(M("submission.exists.not"));
        }
        return SubmissionAdminDto.of(submission, mappers);
    }
}