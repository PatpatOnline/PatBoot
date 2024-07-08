package cn.edu.buaa.patpat.boot.common.requets;

import cn.edu.buaa.patpat.boot.common.utils.Objects;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.modules.auth.api.AuthApi;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

public class BaseController {
    @Autowired
    protected AuthApi authApi;

    @Autowired
    protected Objects objects;

    protected void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
    }

    protected void taOnly(HttpServletRequest request) {
        var auth = authApi.getAuth(request);
        if ((auth == null) || !auth.isTa()) {
            throw new BadRequestException("Only TA can access this API");
        }
    }

    protected void teacherOnly(HttpServletRequest request) {
        var auth = authApi.getAuth(request);
        if ((auth == null) || !auth.isTeacher()) {
            throw new BadRequestException("Only teacher can access this API");
        }
    }
}
