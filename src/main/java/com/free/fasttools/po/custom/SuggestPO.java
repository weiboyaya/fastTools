package com.free.fasttools.po.custom;

import com.free.fasttools.dto.BaseDTO;

import java.io.Serializable;

/**
 * @Auther: zhengwei
 * @Date: 2018/12/6 15:19
 * @Description:
 */
public class SuggestPO extends BaseDTO implements Serializable {
    /**
     * 编号
     */
    private String id;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态：0-未处理，1-已回复，2-已解决，3-已作废
     */
    private String status;
    /**
     * 创建时间
     */
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "[id="+this.id+",email="+this.email+",remark="+this.remark+",status="+this.status+",created="+this.created+"]";
    }
}
