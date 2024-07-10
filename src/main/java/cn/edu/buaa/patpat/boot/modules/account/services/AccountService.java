package cn.edu.buaa.patpat.boot.modules.account.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.modules.account.dto.AccountDto;
import cn.edu.buaa.patpat.boot.modules.account.dto.LoginRequest;
import cn.edu.buaa.patpat.boot.modules.account.dto.RegisterRequest;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Gender;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountMapper;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService extends BaseService {
    private final AccountMapper accountMapper;
    private final BucketApi bucketApi;

    public Account register(RegisterRequest request) {
        if (accountMapper.existsByBuaaId(request.getBuaaId())) {
            throw new BadRequestException("BUAA ID already exists");
        }

        Account account = objects.map(request, Account.class);
        account.setAvatar(Gender.toAvatar(account.getGender()));
        accountMapper.save(account);

        return account;
    }

    public AccountDto login(LoginRequest request) {
        Account account = accountMapper.findByBuaaId(request.getBuaaId());
        if (account == null) {
            throw new BadRequestException("Account not found");
        }

        if (!account.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Password incorrect");
        }

        AccountDto accountDto = objects.map(account, AccountDto.class);
        accountDto.setAvatar(bucketApi.recordToUrl(accountDto.getAvatar()));

        return accountDto;
    }
}
