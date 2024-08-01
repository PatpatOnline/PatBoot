package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateAnnouncementRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateAnnouncementRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Announcement;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.AnnouncementMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.views.AnnouncementView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class AnnouncementService extends BaseService {
    private final AnnouncementMapper announcementMapper;

    public Announcement create(int courseId, int accountId, CreateAnnouncementRequest request) {
        Announcement announcement = mappers.map(request, Announcement.class);
        announcement.setCourseId(courseId);
        announcement.setAccountId(accountId);
        announcementMapper.save(announcement);
        return announcement;
    }

    public Announcement update(int id, UpdateAnnouncementRequest request) {
        Announcement announcement = announcementMapper.findUpdate(id);
        if (announcement == null) {
            throw new NotFoundException(M("announcement.exists.not"));
        }
        mappers.map(request, announcement);
        announcementMapper.update(announcement);
        return announcement;
    }

    public void delete(int id) {
        int updated = announcementMapper.delete(id);
        if (updated == 0) {
            throw new NotFoundException(M("announcement.exists.not"));
        }
    }

    public AnnouncementView find(int id) {
        AnnouncementView announcement = announcementMapper.find(id);
        if (announcement == null) {
            throw new NotFoundException(M("announcement.exists.not"));
        }
        return announcement;
    }

    public List<AnnouncementView> getAll(int courseId) {
        return announcementMapper.getAll(courseId);
    }
}
