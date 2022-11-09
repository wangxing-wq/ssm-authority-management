package com.wx.domain.dto;

import com.wx.domain.entity.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * @author 22343
 */
@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

    private boolean checked = false;

    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }
}
