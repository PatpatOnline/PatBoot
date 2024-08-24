package cn.edu.buaa.patpat.boot.modules.account.services;

import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.account.dto.AccountDto;
import cn.edu.buaa.patpat.boot.modules.account.dto.LoginRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.RegisterRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.UpdateAccountRequest;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Gender;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountFilter;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountFilterMapper;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountMapper;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountDetailView;
import cn.edu.buaa.patpat.boot.modules.account.models.views.AccountListView;
import cn.edu.buaa.patpat.boot.modules.account.models.views.TeacherIndexView;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService extends BaseService {
    private final AccountMapper accountMapper;
    private final BucketApi bucketApi;
    private final AccountFilterMapper accountFilterMapper;

    public Account register(RegisterRequest request) {
        if (accountFilterMapper.exists(request.getBuaaId())) {
            throw new BadRequestException(M("account.exists"));
        }

        Account account = mappers.map(request, Account.class);
        account.setAvatar(Gender.toAvatar(account.getGender()));
        accountMapper.save(account);

        return account;
    }

    public AccountDto login(LoginRequest request) {
        Account account = accountFilterMapper.findByBuaaId(request.getBuaaId());
        if (account == null) {
            throw new BadRequestException(M("account.exists.not"));
        }

        if (!account.getPassword().equals(request.getPassword())) {
            throw new BadRequestException(M("account.password.incorrect"));
        }

        AccountDto accountDto = mappers.map(account, AccountDto.class);
        accountDto.setAvatar(bucketApi.recordToUrl(accountDto.getAvatar()));

        return accountDto;
    }

    public void updatePassword(int accountId, String oldPassword, String newPassword) {
        Account account = accountMapper.findUpdatePassword(accountId);
        if (account == null) {
            throw new NotFoundException(M("account.exists.not"));
        }
        if (!account.getPassword().equals(oldPassword)) {
            throw new BadRequestException(M("account.password.incorrect"));
        }
        account.setPassword(newPassword);
        accountMapper.updatePassword(account);
    }

    public void resetPassword(int accountId) {
        Account account = accountMapper.findUpdatePassword(accountId);
        if (account == null) {
            throw new NotFoundException(M("account.exists.not"));
        }
        account.setPassword(account.getBuaaId());
        accountMapper.updatePassword(account);
    }

    public AccountDto update(int accountId, UpdateAccountRequest request) {
        Account account = accountFilterMapper.findById(accountId);
        if (account == null) {
            throw new NotFoundException(M("account.exists.not"));
        }
        mappers.map(request, account);
        if (request.getRole() != null) {
            switch (request.getRole()) {
                case 0:
                    account.setTa(false);
                    account.setTeacher(false);
                    break;
                case 1:
                    account.setTa(true);
                    account.setTeacher(false);
                    break;
                case 2:
                    account.setTa(true);
                    account.setTeacher(true);
                    break;
            }
        }
        accountMapper.update(account);

        AccountDto accountDto = mappers.map(account, AccountDto.class);
        accountDto.setAvatar(bucketApi.recordToUrl(accountDto.getAvatar()));

        return accountDto;
    }

    public AccountDetailView findDetailView(int id) {
        var view = accountFilterMapper.findDetailView(id);
        if (view == null) {
            throw new NotFoundException(M("account.exists.not"));
        }
        view.setAvatar(bucketApi.recordToUrl(view.getAvatar()));
        return view;
    }

    public PageListDto<AccountListView> query(int page, int pageSize, AccountFilter filter) {
        if (filter.getId() != null) {
            var account = accountFilterMapper.findById(filter.getId());
            if (account == null) {
                return PageListDto.of(List.of(), 0, page, pageSize);
            } else {
                return PageListDto.of(List.of(mappers.map(account, AccountListView.class)), 1, page, pageSize);
            }
        }

        int count = accountFilterMapper.count(filter);
        List<AccountListView> accounts = count == 0
                ? List.of()
                : accountFilterMapper.query(page, pageSize, filter);
        return PageListDto.of(accounts, count, page, pageSize);
    }

    public List<TeacherIndexView> queryTeachers() {
        return accountFilterMapper.queryTeachers();
    }

    public TeacherIndexView queryTeacher(int id) {
        return accountFilterMapper.queryTeacher(id);
    }
}
